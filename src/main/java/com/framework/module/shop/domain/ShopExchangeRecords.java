package com.framework.module.shop.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@ApiModel("商户")
public class ShopExchangeRecords extends BaseEntity {
    @ApiModelProperty(value = "激活商户ID")
    private String shopId;
    @ApiModelProperty(value = "合伙人id")
    private String memberId;
    @ApiModelProperty(value = "合伙人姓名")
    private String memberName;
    @ApiModelProperty(value = "合伙人电话")
    private String memberMobile;
    @ApiModelProperty(value = "激活设备SN")
    private String activePosSn;
    @ApiModelProperty(value = "激活设备SN")
    private String shippingAddress;
    @ApiModelProperty(value = "兑换状态")
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status = Status.EXCHANGING;


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getActivePosSn() {
        return activePosSn;
    }

    public void setActivePosSn(String activePosSn) {
        this.activePosSn = activePosSn;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public enum Status {
        EXCHANGING("兑换中"),
        EXCHANGED("兑换完成"),
        EXCHANGE_FAIL("兑换失败");
        private String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
