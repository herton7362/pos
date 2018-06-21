package com.kratos.common;

import com.kratos.common.utils.SpringUtils;
import com.kratos.common.utils.StringUtils;
import com.kratos.entity.BaseEntity;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.UserThread;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 抽象service提供基本的增删改查操作
 * @author tang he
 * @since 1.0.0
 * @param <T> 增删改查的实体
 */
public abstract class AbstractCrudService<T extends BaseEntity> implements CrudService<T> {
    @Value("${service.showAllEntities}")
    private Boolean showAllEntities;

    /**
     * 获取实体Repository
     * @return {@link PageRepository} 实现类
     */
    protected abstract PageRepository<T> getRepository();

    @Override
    public PageResult<T> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        Page<T> page = getRepository().findAll(getSpecification(param), pageRequest);
        return new PageResult<>(page);
    }

    @Override
    public List<T> findAll(Map<String, String[]> param) throws Exception {
        return getRepository().findAll(getSpecification(param));
    }

    @Override
    public T findOne(String id) throws Exception {
        return getRepository().findOne(id);
    }

    @Override
    public void delete(String id) throws Exception {
        T t = getRepository().findOne(id);
        t.setLogicallyDeleted(true);
        getRepository().save(t);
    }

    @Override
    public T save(T t) throws Exception {
        return getRepository().save(t);
    }

    /**
     * 提供重写查询的入口
     * @param param 用户传入的查询条件
     * @return {@link Specification}
     */
    protected Specification<T> getSpecification(Map<String, String[]> param) {
        return new SimpleSpecification(param, showAllEntities);
    }

    /**
     * 提供重写查询的入口
     * @param param 用户传入的查询条件
     * @return {@link Specification}
     */
    protected Specification<T> getSpecificationForAllEntities(Map<String, String[]> param) {
        return new SimpleSpecification(param, true);
    }

    /**
     * 查询条件实现，实现思路：
     * 1、获取当前实体的所有属性并且循环
     * 2、判断属性是否与网页传来的参数的key相同
     * 3、当属性类型为字符串时则用like判断
     * 4、当属性类型为数字时则用equal判断
     * 5、当属性类型为数字时并且value为两个是则用between判断
     */
    @SuppressWarnings("unchecked")
    class SimpleSpecification implements Specification<T> {
        Map<String, String[]> params;
        String currentKey;
        Attribute currentAttribute;
        Boolean allEntities;
        SimpleSpecification(Map<String, String[]> params, Boolean allEntities) {
            this.params = params;
            this.allEntities = allEntities;
        }
        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            Set<Attribute<? super T, ?>> attributes =  root.getModel().getAttributes();
            List<Predicate> predicate = new ArrayList<>();
            String key;
            String[] values;
            Attribute attribute;
            Set<Map.Entry<String, String[]>> set = params.entrySet();
            for (Map.Entry<String, String[]> param : set) {

                if(!containsKey(param.getKey(), attributes))  {
                    continue;
                }

                key = this.currentAttribute.getName();
                attribute = this.currentAttribute;
                Assert.isTrue(
                        !attribute.getJavaType().isPrimitive(),
                        "实体[" + root.getModel().getName() + "]:["+attribute.getName()+"]" +
                                "为基本类型，不要将实体属性设置成基本类型"
                );


                values = param.getValue();

                if(values == null || StringUtils.isBlank(values[0])) {
                    continue;
                }

                if("isNull".equals(values[0])) {
                    predicate.add(criteriaBuilder.isNull(root.get(key)));
                } else if(attribute.getJavaType().equals(String.class)) {
                    if(values.length == 1) {
                        predicate.add(criteriaBuilder.like(root.get(key), "%"+ values[0] +"%"));
                    } else {
                        for (String value : values) {
                            if("isNull".equals(value)) {
                                criteriaBuilder.or(criteriaBuilder.isNull(root.get(key)));
                                break;
                            }
                        }

                        predicate.add(criteriaBuilder.in(root.get(key)).value(Arrays.asList(values)));
                    }
                } else if(attribute.getJavaType().equals(Boolean.class)) {
                    predicate.add(criteriaBuilder.equal(root.get(key), Boolean.valueOf(values[0])));
                } else if(attribute.getJavaType().getSuperclass().equals(Enum.class)) {
                    predicate.add(criteriaBuilder.equal(root.get(key), Enum.valueOf(attribute.getJavaType(), values[0])));
                } else if(attribute.getJavaType().getSuperclass().equals(Number.class)) {
                    NumberFormat nf = NumberFormat.getInstance();
                    if(values.length == 1) {
                        try {
                            predicate.add(criteriaBuilder.equal(root.get(key), nf.parse(values[0])));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    } else if(values.length == 2) {
                        try {
                            predicate.add(criteriaBuilder.and(
                                    criteriaBuilder.gt(root.get(key), nf.parse(values[0])),
                                    criteriaBuilder.lt(root.get(key), nf.parse(values[1]))
                            ));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if(attribute.getJavaType().equals(Date.class)) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if(values.length == 1) {
                        try {
                            predicate.add(criteriaBuilder.equal(root.get(key), df.parse(values[0])));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    } else if(values.length == 2) {
                        try {
                            predicate.add(criteriaBuilder.and(
                                    criteriaBuilder.greaterThanOrEqualTo(root.get(key), df.parse(values[0])),
                                    criteriaBuilder.lessThanOrEqualTo(root.get(key), df.parse(values[1]))
                            ));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if(BaseEntity.class.isAssignableFrom(attribute.getJavaType())) {
                    String field = this.getFiled();
                    if(StringUtils.isBlank(field)) {
                        continue;
                    }
                    EntityManager entityManager = SpringUtils.getBean(EntityManager.class);
                    Query query = entityManager.createQuery("select m from "+
                            getEntityName(attribute.getJavaType(), root, key)+" m where m."+field+" like :" + field);
                    query.setParameter(field, "%" + values[0] + "%");
                    List list = query.getResultList();
                    if(!list.isEmpty()) {
                        predicate.add(criteriaBuilder.in(root.get(key)).value(list));
                    } else {
                        predicate.add(criteriaBuilder.isNull(root.get(key)));
                    }
                }
            }
            predicate.add(criteriaBuilder.equal(root.get("logicallyDeleted"), false));
            String clientId = UserThread.getInstance().getClientId();
            if(StringUtils.isNotBlank(clientId) && !this.allEntities) {
                predicate.add(criteriaBuilder.equal(root.get("clientId"), UserThread.getInstance().getClientId()));
            }
            return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
        }

        private String getEntityName(Class<?> clazz, Root<T> root, String key) {
            if("parent".equals(key)) {
                return root.getJavaType().getSimpleName();
            }
            return clazz.getSimpleName();
        }

        private Boolean containsKey(String k, Set<Attribute<? super T, ?>> attributes) {
            String key;
            for(Attribute attribute : attributes) {
                key = attribute.getName();
                if (k.split("\\.")[0].equals(key)
                        || k.split("\\[")[0].equals(key)) {
                    this.currentKey = k;
                    this.currentAttribute = attribute;
                    return true;
                }
            }
            return false;
        }

        private String getFiled() {
            String field = null;
            if(currentKey.contains(".")) {
                field = currentKey.split("\\.")[1];
            } else if (currentKey.contains("[")) {
                field = currentKey.split("\\[")[1];
                if(StringUtils.isNotBlank(field)) {
                    field = field.split("]")[0];
                }
            }
            return field;
        }
    }

    public void sort(List<T> ts) throws Exception {
        T t;
        for (int i = 0; i < ts.size(); i++) {
            if(StringUtils.isBlank(ts.get(i).getId())) {
                throw new BusinessException("参数不正确，缺失主键");
            }
            t = getRepository().findOne(ts.get(i).getId());
            t.setSortNumber(i);
            getRepository().save(t);
        }
    }
}
