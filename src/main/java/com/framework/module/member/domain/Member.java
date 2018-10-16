package com.framework.module.member.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.framework.module.common.Constant;
import com.kratos.entity.BaseUser;
import com.kratos.module.attachment.domain.Attachment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * 会员
 *
 * @author tang he
 * @since 1.0.0
 */
@Entity
@ApiModel("会员")
public class Member extends BaseUser implements Comparable {
    @ApiModelProperty(value = "姓名")
    @Column(length = 50)
    private String name;
    @ApiModelProperty(value = "会员卡")
    @Column(length = 20)
    private String cardNo;
    @ApiModelProperty(value = "性别 0-女，1-男")
    @Column(length = 2)
    private String gender;
    @ApiModelProperty(value = "生日")
    private Long birthday;
    @ApiModelProperty(value = "身份证")
    @Column(length = 20)
    private String idCard;
    @ApiModelProperty(value = "微信openid")
    @Column(length = 36)
    private String openid;
    @ApiModelProperty(value = "余额")
    @Column(length = 11, precision = 2)
    private Double balance;
    @ApiModelProperty(value = "积分")
    private Integer point = 0;
    @ApiModelProperty(value = "可用积分")
    private Integer salePoint = 0;
    @ApiModelProperty(value = "区域")
    @Column(length = 20)
    private String address;
    @ApiModelProperty(value = "优惠券")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    @JsonIgnore
    private List<MemberCoupon> coupons;
    @ApiModelProperty(value = "会员卡")
    @OneToMany(mappedBy = "member")
    @Where(clause = "logically_deleted=0")
    @JsonIgnore
    private List<MemberCard> memberCards;
    @ApiModelProperty(value = "头像")
    @ManyToOne
    private Attachment headPhoto;
    @ApiModelProperty(value = "父节点ID")
    private String fatherId;
    @ApiModelProperty(value = "盟友级别")
    private Integer memberLevel = 1;
    @ApiModelProperty(value = "激活状态")
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;
    @ApiModelProperty(value = "银行卡号")
    @Column(length = 50)
    private String bankCardNumber;
    @ApiModelProperty(value = "银行卡姓名")
    @Column(length = 50)
    private String bankCardUserName;
    @ApiModelProperty(value = "开户银行")
    @Column(length = 200)
    private String openAccountBank;
    @ApiModelProperty(value = "开户地点")
    @Column(length = 200)
    private String openAccountAddress;
    @ApiModelProperty(value = "支行网点")
    @Column(length = 500)
    private String openAccountSubbranch;
    @ApiModelProperty(value = "支持领管理奖")
    @Column()
    private Integer supportManagerAward;
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
    @Transient
    @ApiModelProperty(value = "会员级别名称")
    private String memberLevelName;
    @Transient
    @ApiModelProperty(value = "合伙人数量")
    private Integer partnerNum;
    @Transient
    @ApiModelProperty(value = "合伙人激活数量")
    private Integer activePartnerNum;

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

    public Integer getMemberLevel() {
        if (memberLevel == null) {
            return 1;
        }
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Member() {
        setUserType(BaseUser.UserType.MEMBER.name());
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

    public Integer getPartnerNum() {
        return partnerNum;
    }

    public void setPartnerNum(Integer partnerNum) {
        this.partnerNum = partnerNum;
    }

    public Integer getActivePartnerNum() {
        return activePartnerNum;
    }

    public void setActivePartnerNum(Integer activePartnerNum) {
        this.activePartnerNum = activePartnerNum;
    }

    public String getMemberLevelName() {
        return memberLevelName;
    }

    public void setMemberLevelName(String memberLevelName) {
        this.memberLevelName = memberLevelName;
    }

    public Integer getSupportManagerAward() {
        return supportManagerAward;
    }

    public void setSupportManagerAward(Integer supportManagerAward) {
        this.supportManagerAward = supportManagerAward;
    }

    public Integer getSortType() {
        return sortType;
    }

    @Override
    public int compareTo(Object m) {
        if (sortType == Constant.SORT_TYPE_DEFAULT) {
            return 0;
        }
        if (sortType == Constant.SORT_TYPE_LEVEL) {
            if (memberLevel == null) {
                return 1;
            }
            if (((Member) m).memberLevel == null) {
                return -1;
            }
            return Integer.compare(((Member) m).memberLevel, memberLevel);
        }
        if (sortType == Constant.SORT_TYPE_PROFIT) {
            return Double.compare(((Member) m).balance, balance);
        }
        if (sortType == Constant.SORT_TYPE_ALLY_NUM_HIGH_LOW) {
            return Integer.compare(((Member) m).allyNumber, allyNumber);
        }
        if (sortType == Constant.SORT_TYPE_ALLY_NUM_LOW_HIGH) {
            return Integer.compare(allyNumber, ((Member) m).allyNumber);
        }

        return 0;
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
