package com.framework.module.aboutus.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("关于我们")
public class AboutUs extends BaseEntity {
    @ApiModelProperty(value = "内容")
    @Column(length = 2000)
    private String content;
    @ApiModelProperty(value = "公众号")
    @Column(length = 200)
    private String wechatSubscription;
    @ApiModelProperty(value = "客服电话")
    @Column(length = 200)
    private String customerServiceNumber;
    @ApiModelProperty(value = "联系电话")
    @Column(length = 200)
    private String contactPhone;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWechatSubscription() {
        return wechatSubscription;
    }

    public void setWechatSubscription(String wechatSubscription) {
        this.wechatSubscription = wechatSubscription;
    }

    public String getCustomerServiceNumber() {
        return customerServiceNumber;
    }

    public void setCustomerServiceNumber(String customerServiceNumber) {
        this.customerServiceNumber = customerServiceNumber;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
