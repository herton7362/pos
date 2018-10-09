package com.framework.module.sn.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@ApiModel("SNHistory")
public class SnInfoHistory extends BaseEntity {

    @ApiModelProperty(value = "sn")
    @Column(length = 36)
    private String sn;
    @Column()
    @ApiModelProperty(value = "划拨日期")
    private Date transDate;
    @Column(length = 36)
    @ApiModelProperty(value = "memberId")
    private String memberId;
    @Column(length = 36)
    @ApiModelProperty(value = "memberName")
    private String memberName;
    @Column(length = 36)
    @ApiModelProperty(value = "memberMobile")
    private String memberMobile;
    @Column(length = 36)
    @ApiModelProperty(value = "transMemberId")
    private String transMemberId;
    @Column(length = 36)
    @ApiModelProperty(value = "transMemberName")
    private String transMemberName;
    @Column(length = 36)
    @ApiModelProperty(value = "transMemberMobile")
    private String transMemberMobile;
    @Column(length = 36)
    @ApiModelProperty(value = "shopId")
    private String shopId;
    @Column(length = 36)
    @ApiModelProperty(value = "shopName")
    private String shopName;
    @Column(length = 36)
    @ApiModelProperty(value = "shopMobile")
    private String shopMobile;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String getTransMemberId() {
        return transMemberId;
    }

    public void setTransMemberId(String transMemberId) {
        this.transMemberId = transMemberId;
    }

    public String getTransMemberName() {
        return transMemberName;
    }

    public void setTransMemberName(String transMemberName) {
        this.transMemberName = transMemberName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopMobile() {
        return shopMobile;
    }

    public void setShopMobile(String shopMobile) {
        this.shopMobile = shopMobile;
    }

    public String getTransMemberMobile() {
        return transMemberMobile;
    }

    public void setTransMemberMobile(String transMemberMobile) {
        this.transMemberMobile = transMemberMobile;
    }
}
