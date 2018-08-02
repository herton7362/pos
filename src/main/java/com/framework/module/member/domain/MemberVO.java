package com.framework.module.member.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kratos.entity.BaseUser;
import com.kratos.module.attachment.domain.Attachment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.util.List;

/**
 * 会员
 *
 * @author tang he
 * @since 1.0.0
 */
@ApiModel("会员")
public class MemberVO extends BaseUser {
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "会员卡")
    private String cardNo;
    @ApiModelProperty(value = "性别 0-女，1-男")
    private String gender;
    @ApiModelProperty(value = "生日")
    private Long birthday;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "微信openid")
    private String openid;
    @ApiModelProperty(value = "余额")
    private Double balance;
    @ApiModelProperty(value = "积分")
    private Integer point = 0;
    @ApiModelProperty(value = "可用积分")
    private Integer salePoint = 0;
    @ApiModelProperty(value = "区域")
    private String address;
    @ApiModelProperty(value = "优惠券")
    @JsonIgnore
    private List<MemberCoupon> coupons;
    @ApiModelProperty(value = "会员卡")
    @JsonIgnore
    private List<MemberCard> memberCards;
    @ApiModelProperty(value = "头像")
    private Attachment headPhoto;
    @ApiModelProperty(value = "父节点ID")
    private String fatherId;
    @ApiModelProperty(value = "盟友级别")
    private String memberLevel;
    @ApiModelProperty(value = "激活状态")
    @Enumerated(EnumType.STRING)
    private Status status;
    @ApiModelProperty(value = "银行卡号")
    private String bankCardNumber;
    @ApiModelProperty(value = "银行卡姓名")
    private String bankCardUserName;
    @ApiModelProperty(value = "开户银行")
    private String openAccountBank;
    @ApiModelProperty(value = "开户地点")
    private String openAccountAddress;
    @ApiModelProperty(value = "支行网点")
    private String openAccountSubbranch;
    @Transient
    @JsonIgnore
    private Integer sortType;
    @Transient
    private Integer allyNumber;
    @Transient
    private Integer realIdentity;
    @Transient
    @ApiModelProperty(value = "所有一级盟友")
    private Integer allySonNumber;
    @Transient
    @ApiModelProperty(value = "所有盟友")
    private Integer allyAllNumber;
    @Transient
    @ApiModelProperty(value = "上级名称")
    private String fatherName;
    @Transient
    @ApiModelProperty(value = "上级手机号")
    private String fatherMobile;
    @Transient
    @ApiModelProperty(value = "购买设备数量")
    private String buyEquipmentNum;

    public String getBuyEquipmentNum() {
        return buyEquipmentNum;
    }

    public void setBuyEquipmentNum(String buyEquipmentNum) {
        this.buyEquipmentNum = buyEquipmentNum;
    }

    public Integer getAllySonNumber() {
        return allySonNumber;
    }

    public void setAllySonNumber(Integer allySonNumber) {
        this.allySonNumber = allySonNumber;
    }

    public Integer getAllyAllNumber() {
        return allyAllNumber;
    }

    public void setAllyAllNumber(Integer allyAllNumber) {
        this.allyAllNumber = allyAllNumber;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherMobile() {
        return fatherMobile;
    }

    public void setFatherMobile(String fatherMobile) {
        this.fatherMobile = fatherMobile;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public void setAllyNumber(Integer allyNumber) {
        this.allyNumber = allyNumber;
    }

    public Integer getAllyNumber() {
        return allyNumber;
    }

    public Integer getRealIdentity() {
        return realIdentity;
    }

    public void setRealIdentity(Integer realIdentity) {
        this.realIdentity = realIdentity;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    public MemberVO() {
        setUserType(UserType.MEMBER.name());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getSalePoint() {
        return salePoint;
    }

    public void setSalePoint(Integer salePoint) {
        this.salePoint = salePoint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<MemberCoupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<MemberCoupon> coupons) {
        this.coupons = coupons;
    }

    public List<MemberCard> getMemberCards() {
        return memberCards;
    }

    public void setMemberCards(List<MemberCard> memberCards) {
        this.memberCards = memberCards;
    }

    public Attachment getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(Attachment headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankCardUserName() {
        return bankCardUserName;
    }

    public void setBankCardUserName(String bankCardUserName) {
        this.bankCardUserName = bankCardUserName;
    }

    public String getOpenAccountBank() {
        return openAccountBank;
    }

    public void setOpenAccountBank(String openAccountBank) {
        this.openAccountBank = openAccountBank;
    }

    public String getOpenAccountAddress() {
        return openAccountAddress;
    }

    public void setOpenAccountAddress(String openAccountAddress) {
        this.openAccountAddress = openAccountAddress;
    }

    public String getOpenAccountSubbranch() {
        return openAccountSubbranch;
    }

    public void setOpenAccountSubbranch(String openAccountSubbranch) {
        this.openAccountSubbranch = openAccountSubbranch;
    }
    public enum Status {
        ACTIVE("已激活"),
        UN_ACTIVE("未激活");
        private String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
