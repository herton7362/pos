package com.framework.module.member.domain;

import com.kratos.entity.BaseEntity;
import com.kratos.module.attachment.domain.Attachment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@ApiModel("实名认证")
public class RealIdentityAudit extends BaseEntity {
    @ApiModelProperty(required = true, value = "姓名")
    @Column(length = 30)
    private String name;
    @ApiModelProperty(required = true, value = "会员id")
    @Column(length = 36)
    private String memberId;
    @ApiModelProperty(required = true, value = "身份证")
    @Column(length = 30)
    private String idCardNumber;
    @ApiModelProperty(required = true, value = "身份证正面")
    @ManyToOne
    private Attachment idCardFront;
    @ApiModelProperty(required = true, value = "身份证背面")
    @ManyToOne
    private Attachment idCardBack;
    @ApiModelProperty(value = "认证状态")
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;
    @ApiModelProperty(required = true, value = "未通过原因")
    @Column(length = 500)
    private String reason;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public Attachment getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(Attachment idCardFront) {
        this.idCardFront = idCardFront;
    }

    public Attachment getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(Attachment idCardBack) {
        this.idCardBack = idCardBack;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public enum Status {
        PENDING("待审核"), PASS("已通过"), UN_PASS("未通过");
        private String displayName;
        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
