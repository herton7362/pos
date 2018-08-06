package com.framework.module.shop.service;

import com.framework.module.auth.MemberThread;
import com.framework.module.member.domain.Member;
import com.framework.module.shop.domain.Shop;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageResult;
import com.kratos.common.utils.StringUtils;
import com.kratos.exceptions.BusinessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class ShopServiceImpl extends AbstractCrudService<Shop> implements ShopService {

    @Override
    public Shop save(Shop shop) throws Exception {
        if(StringUtils.isNotBlank(shop.getMobile())) {
            Map<String, String[]> param = new HashMap<>();
            param.put("mobile", new String[]{ shop.getMobile() });
            List<Shop> shops = findAll(param);
            if(shops != null && !shops.isEmpty() && !shops.get(0).getId().equals(shop.getId())) {
                throw new BusinessException("手机号【"+shop.getMobile()+"】不能重复");
            }
        }
        if(StringUtils.isBlank(shop.getId())) {
            Member member = MemberThread.getInstance().get();
            if(member == null) {
                throw new BusinessException("请登录");
            }
            shop.setMemberId(member.getId());
        }
        return super.save(shop);
    }

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
