package com.framework.module.shop.service;

import com.framework.module.member.domain.AllyMembers;
import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberRepository;
import com.framework.module.member.service.MemberService;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.domain.ShopExchangeRecords;
import com.framework.module.shop.domain.ShopExchangeRecordsRepository;
import com.framework.module.shop.domain.ShopRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageResult;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.AdminThread;
import com.sun.jndi.toolkit.url.UrlUtil;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class ShopExchangeRecordsServiceImpl extends AbstractCrudService<ShopExchangeRecords> implements ShopExchangeRecordsService {

    private final ShopExchangeRecordsRepository shopExchangeRecordsRepository;
    private final ShopRepository shopRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final Logger logger = Logger.getLogger(ShopExchangeRecordsServiceImpl.class);

    public ShopExchangeRecordsServiceImpl(ShopExchangeRecordsRepository shopExchangeRecordsRepository, ShopRepository shopRepository, MemberRepository memberRepository, MemberService memberService) {
        this.shopExchangeRecordsRepository = shopExchangeRecordsRepository;
        this.shopRepository = shopRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @Override
    public void exchangeMachine(String shopIds, String memberId, String shippingAddress) throws BusinessException {
        logger.info("shopIds=" + shopIds + ",memberId=" + memberId);
        List<ShopExchangeRecords> saveList = new ArrayList<>();
        List<Shop> shops = new ArrayList<>();
        Member member = memberRepository.findOne(memberId);
        try {
            shopIds = UrlUtil.decode(shopIds);
        } catch (MalformedURLException e) {
            throw new BusinessException("商户ID无法解码，shopIds:" + shopIds);
        }
        String[] shopList = shopIds.split("\\|");
        int i = 0;
        while (i < shopList.length) {
            Shop shop = shopRepository.findOne(shopList[i]);
            if (shop == null) {
                throw new BusinessException("商户信息不合法，商户ID：" + shopList[i]);
            }
            if (!memberId.equals(shop.getMemberId())) {
                throw new BusinessException("该设备不属于您，您不能兑换设备,设备SN：" + shop.getSn());
            }
            if (!Shop.Status.ACTIVE.equals(shop.getStatus())) {
                throw new BusinessException("该设备未激活不能领取激活兑换奖励,设备SN：" + shop.getSn());
            }
            if (shop.getExchangePosMachine() != null && shop.getExchangePosMachine() != 0) {
                throw new BusinessException("该设备已经领取过兑换奖励,设备SN：" + shop.getSn());
            }
            ShopExchangeRecords shopExchangeRecords = new ShopExchangeRecords();
            shopExchangeRecords.setMemberId(memberId);
            shopExchangeRecords.setShopId(shopList[i]);
            shopExchangeRecords.setMemberMobile(member.getMobile());
            shopExchangeRecords.setShippingAddress(shippingAddress);
            shopExchangeRecords.setMemberName(member.getName());
            shopExchangeRecords.setActivePosSn(shop.getSn());
            saveList.add(shopExchangeRecords);

            shop.setExchangePosMachine(2);
            shops.add(shop);
            i++;
        }
        shopExchangeRecordsRepository.save(saveList);
        shopRepository.save(shops);
    }

    @Override
    public void examineExchangeMachine(String exchangeId, boolean examineResult) throws BusinessException {
        ShopExchangeRecords shopExchangeRecords = shopExchangeRecordsRepository.findOne(exchangeId);
        if (shopExchangeRecords == null) {
            throw new BusinessException("未找到该条审核记录");
        }
        if (!ShopExchangeRecords.Status.EXCHANGING.equals(shopExchangeRecords.getStatus())) {
            throw new BusinessException("已经审核完成，不能重复审核");
        }
        Shop shop = shopRepository.findOne(shopExchangeRecords.getShopId());
        if (examineResult) {
            shopExchangeRecords.setStatus(ShopExchangeRecords.Status.EXCHANGED);
            shop.setExchangePosMachine(1);
        } else {
            shopExchangeRecords.setStatus(ShopExchangeRecords.Status.EXCHANGE_FAIL);
            shop.setExchangePosMachine(0);
        }
        shopRepository.save(shop);
        shopExchangeRecords.setAuditMemberId(AdminThread.getInstance().get().getId());
        shopExchangeRecordsRepository.save(shopExchangeRecords);
    }

    @Override
    public PageResult<ShopExchangeRecords> getAllExchangeRecords(String memberId, Integer currentPage, Integer pageSize, Long startTime, Long endTime, ShopExchangeRecords.Status status) {
        PageRequest pageRequest = new PageRequest(currentPage, pageSize, Sort.Direction.DESC, "createdDate");
        Map<String, String[]> param = new HashMap<>();
        if (startTime != null && endTime != null) {
            param.put("startTime", new String[]{startTime.toString()});
            param.put("endTime", new String[]{endTime.toString()});
        }
        List<String> allSons = new ArrayList<>();
        AllyMembers allyMembers = memberService.getAlliesByMemberId(memberId);
        allSons.addAll(allyMembers.getGrandSonList());
        allSons.addAll(allyMembers.getSonList());
        if (!CollectionUtils.isEmpty(allSons)) {
            String[] array = new String[allSons.size()];
            param.put("memberIds", allSons.toArray(array));
        }
        if (status != null) {
            param.put("status", new String[]{status.toString()});
        }

        Page<ShopExchangeRecords> all = pageRepository.findAll(new MySpecification(param, true), pageRequest);
        PageResult<ShopExchangeRecords> pageResult = new PageResult<>();
        pageResult.setSize(all.getSize());
        pageResult.setTotalElements(all.getTotalElements());
        pageResult.setContent(all.getContent());
        return pageResult;
    }

    class MySpecification extends SimpleSpecification {
        MySpecification(Map<String, String[]> params, Boolean allEntities) {
            super(params, allEntities);
        }


        @Override
        public Predicate toPredicate(Root<ShopExchangeRecords> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            Predicate predicate = super.toPredicate(root, criteriaQuery, criteriaBuilder);
            List<Predicate> predicates = new ArrayList<>();

            List<Predicate> predicatesAnd = new ArrayList<>();
            if (params.containsKey("startTime") && params.containsKey("endTime")) {
                String[] startTime = params.get("startTime");
                String[] endTime = params.get("endTime");
                predicatesAnd.add(criteriaBuilder.between(root.get("createdDate"), Long.valueOf(startTime[0]), Long.valueOf(endTime[0])));
            }
            if (predicatesAnd.size() != 0) {
                Predicate predicateTemp = criteriaBuilder.and(predicatesAnd.toArray(new Predicate[]{}));
                predicates.add(predicateTemp);
            }

            List<Predicate> predicatesStatus = new ArrayList<>();
            if (params.containsKey("status")) {
                String[] status = params.get("status");

                predicatesStatus.add(criteriaBuilder.equal(root.get("status"), ShopExchangeRecords.Status.valueOf(status[0])));
            }
            if (predicatesStatus.size() != 0) {
                Predicate predicateTemp = criteriaBuilder.and(predicatesStatus.toArray(new Predicate[]{}));
                predicates.add(predicateTemp);
            }

            List<Predicate> predicatesIdOr = new ArrayList<>();
            if (params.containsKey("memberIds")) {
                String[] memberIdList = params.get("memberIds");
                for (int i = 0; i < memberIdList.length; i++) {
                    predicatesIdOr.add(criteriaBuilder.equal(root.get("memberId"), memberIdList[i]));
                }
            }
            if (predicatesIdOr.size() != 0) {
                Predicate predicateTemp = criteriaBuilder.or(predicatesIdOr.toArray(new Predicate[]{}));
                predicates.add(predicateTemp);
            }
            predicates.add(predicate);
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        }
    }
}
