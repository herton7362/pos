package com.kratos.common;

import com.kratos.common.utils.StringUtils;
import com.kratos.entity.BaseEntity;
import com.kratos.exceptions.BusinessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象service提供基本的增删改查操作
 * @author tang he
 * @since 1.0.0
 * @param <T> 增删改查的实体
 */
public abstract class AbstractCrudClientService<T extends BaseEntity> implements CrudService<T> {
    /**
     * 获取实体跟路径（由此衍生出增删改查）
     * @return 跟路径
     */
    protected abstract String getDomainUri();

    /**
     * 获取OAuth2.0的rest template
     * @return {@link OAuth2RestTemplate}
     */
    protected abstract OAuth2RestTemplate getOAuth2RestTemplate();

    @SuppressWarnings("unchecked")
    private Class<T> getGenericClass() {
        return (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageResult<T> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        Map<String, String> newParam = new HashMap<>();
        newParam.put("currentPage", String.valueOf(pageRequest.getPageNumber()));
        newParam.put("pageSize", String.valueOf(pageRequest.getPageSize()));
        List<String> params = new ArrayList<>();
        List<String> sorts = new ArrayList<>();
        List<String> orders = new ArrayList<>();
        pageRequest.getSort().forEach(sort->{
            sorts.add(sort.getProperty());
            orders.add(sort.getDirection().name().toLowerCase());
        });
        newParam.put("sort", String.join(",", sorts));
        newParam.put("order", String.join(",", orders));
        param.forEach((k, v) -> newParam.put(k, v[0]));
        newParam.forEach((k, v)->params.add(k + "=" + v));
        ResponseEntity<PageResult> responseEntity = getOAuth2RestTemplate().getForEntity(
                getDomainUri() + "?" + String.join("&", params),  PageResult.class);
        return responseEntity.getBody();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(Map<String, String[]> param) throws Exception {
        Map<String, String> newParam = new HashMap<>();
        List<String> params = new ArrayList<>();
        param.forEach((k, v) -> newParam.put(k, v[0]));
        newParam.forEach((k, v)->params.add(k + "=" + v));
        ResponseEntity<List> responseEntity = getOAuth2RestTemplate().getForEntity(
                getDomainUri(),  List.class, newParam);
        return responseEntity.getBody();
    }


    @Override
    public T findOne(String id) throws Exception {
        ResponseEntity<T> responseEntity = getOAuth2RestTemplate().getForEntity(
                URI.create(getDomainUri() + "/" + id), getGenericClass());
        return responseEntity.getBody();
    }

    @Override
    public void delete(String id) throws Exception {
        getOAuth2RestTemplate().delete(
                URI.create(getDomainUri() + "/" + id));
    }

    @Override
    public T save(T t) throws Exception {
        ResponseEntity<T> responseEntity = getOAuth2RestTemplate().postForEntity(
                URI.create(getDomainUri()), t, getGenericClass());
        return responseEntity.getBody();
    }

    public void sort(List<T> ts) throws Exception {
        T t;
        for (int i = 0; i < ts.size(); i++) {
            if(StringUtils.isBlank(ts.get(i).getId())) {
                throw new BusinessException("参数不正确，缺失主键");
            }
            t = findOne(ts.get(i).getId());
            t.setSortNumber(i);
            save(t);
        }
    }
}
