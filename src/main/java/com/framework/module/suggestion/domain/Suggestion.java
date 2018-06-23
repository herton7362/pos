package com.framework.module.suggestion.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("问题建议")
public class Suggestion extends BaseEntity {
    @ApiModelProperty(value = "会员id")
    @Column(length = 36)
    private String memberId;
    @ApiModelProperty(value = "手机号")
    @Column(length = 20)
    private String mobile;
    @ApiModelProperty(value = "内容")
    @Column(length = 2000)
    private String content;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
