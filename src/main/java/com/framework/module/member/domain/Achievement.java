package com.framework.module.member.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/6/27 13:05
 */
public class Achievement {
    @ApiModelProperty(value = "盟友交易总额")
    private double allyTransactionAmount = 0;
    @ApiModelProperty(value = "盟友新增商户总数")
    private int allyNewShopNum = 0;
    @ApiModelProperty(value = "交易总额")
    private double transactionAmount = 0;
    @ApiModelProperty(value = "新增商户总数")
    private int newShopNum = 0;
    @ApiModelProperty(value = "统计月份")
    private String staticMonth;

    public String getStaticMonth() {
        return staticMonth;
    }

    public void setStaticMonth(String staticMonth) {
        this.staticMonth = staticMonth;
    }

    public double getAllyTransactionAmount() {
        return allyTransactionAmount;
    }

    public void setAllyTransactionAmount(double allyTransactionAmount) {
        this.allyTransactionAmount = allyTransactionAmount;
    }

    public int getAllyNewShopNum() {
        return allyNewShopNum;
    }

    public void setAllyNewShopNum(int allyNewShopNum) {
        this.allyNewShopNum = allyNewShopNum;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public int getNewShopNum() {
        return newShopNum;
    }

    public void setNewShopNum(int newShopNum) {
        this.newShopNum = newShopNum;
    }
}
