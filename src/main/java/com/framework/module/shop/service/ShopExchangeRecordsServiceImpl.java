package com.framework.module.shop.service;

import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberRepository;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.domain.ShopExchangeRecords;
import com.framework.module.shop.domain.ShopExchangeRecordsRepository;
import com.framework.module.shop.domain.ShopRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.exceptions.BusinessException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class ShopExchangeRecordsServiceImpl extends AbstractCrudService<ShopExchangeRecords> implements ShopExchangeRecordsService {

    private final ShopExchangeRecordsRepository shopExchangeRecordsRepository;
    private final ShopRepository shopRepository;
    private final MemberRepository memberRepository;
    private final Logger logger = Logger.getLogger(ShopExchangeRecordsServiceImpl.class);

    public ShopExchangeRecordsServiceImpl(ShopExchangeRecordsRepository shopExchangeRecordsRepository, ShopRepository shopRepository, MemberRepository memberRepository) {
        this.shopExchangeRecordsRepository = shopExchangeRecordsRepository;
        this.shopRepository = shopRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void exchangeMachine(String shopIds, String memberId) throws BusinessException {
        logger.info("shopIds=" + shopIds + ",memberId=" + memberId);
        List<ShopExchangeRecords> saveList = new ArrayList<>();
        List<Shop> shops = new ArrayList<>();
        Member member = memberRepository.findOne(memberId);
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
        shopExchangeRecordsRepository.save(shopExchangeRecords);
    }
}
