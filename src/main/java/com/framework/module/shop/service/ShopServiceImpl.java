package com.framework.module.shop.service;

import com.framework.module.shop.domain.Shop;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class ShopServiceImpl extends AbstractCrudService<Shop> implements ShopService {
    @Override
    public List<Shop> findAll(Map<String, String[]> param) throws Exception {
        return pageRepository.findAll(new MySpecification(param, true));
    }

    @Override
    public PageResult<Shop> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        return new PageResult<>(pageRepository.findAll(new MySpecification(param, true), pageRequest));
    }

    class MySpecification extends SimpleSpecification {
        MySpecification(Map<String, String[]> params, Boolean allEntities) {
            super(params, allEntities);
        }

        @Override
        public Predicate toPredicate(Root<Shop> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            Predicate predicate = super.toPredicate(root, criteriaQuery, criteriaBuilder);
            List<Predicate> predicates = new ArrayList<>();
            if(params.containsKey("quickSearch")) {
                String[] value = params.get("quickSearch");
                predicates.add(criteriaBuilder.like(root.get("name"), "%"+ value[0] +"%"));
                predicates.add(criteriaBuilder.like(root.get("mobile"), "%"+ value[0] +"%"));
                Predicate predicateTemp = criteriaBuilder.or(predicates.toArray(new Predicate[]{}));
                predicates.clear();
                predicates.add(predicateTemp);
            }
            predicates.add(predicate);
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        }
    }
}
