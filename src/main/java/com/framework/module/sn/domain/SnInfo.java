package com.framework.module.sn.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@ApiModel("SN")
public class SnInfo extends BaseEntity {

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
    @ApiModelProperty(value = "shopId")
    private String shopId;

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

    public enum Status {
        DISTRIBUTION("已分配"),
        UN_DISTRIBUTION("未分配");
        private String displayName;
        Status(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }
    public enum BindStatus {
        BIND("绑定"),
        UN_BIND("未绑定");
        private String displayName;
        BindStatus(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }
}
