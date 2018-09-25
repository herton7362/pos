package com.framework.module.member.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/6/27 13:05
 */
public class AchievementDetail {
    @ApiModelProperty(value = "盟友总数")
    private int totalAllyNum = 0;
    @ApiModelProperty(value = "盟友商户总数")
    private int totalAllyShopNum = 0;
    @ApiModelProperty(value = "盟友新增商户总数")
    private int newAllyShopNum = 0;
    @ApiModelProperty(value = "盟友交易总额")
    private String transactionAmount = "0";
    @ApiModelProperty(value = "统计时间")
    private String staticDate;

    public int getTotalAllyNum() {
        return totalAllyNum;
    }

    public void setTotalAllyNum(int totalAllyNum) {
        this.totalAllyNum = totalAllyNum;
    }

    public int getTotalAllyShopNum() {
        return totalAllyShopNum;
    }

    public void setTotalAllyShopNum(int totalAllyShopNum) {
        this.totalAllyShopNum = totalAllyShopNum;
    }

    public int getNewAllyShopNum() {
        return newAllyShopNum;
    }

    public void setNewAllyShopNum(int newAllyShopNum) {
        this.newAllyShopNum = newAllyShopNum;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getStaticDate() {
        return staticDate;
    }

    public void setStaticDate(String staticDate) {
        this.staticDate = staticDate;
    }
}
