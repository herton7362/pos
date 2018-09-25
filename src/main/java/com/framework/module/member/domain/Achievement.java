package com.framework.module.member.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>Description: 业绩</p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/6/27 13:05
 */
public class Achievement {
    @ApiModelProperty(value = "盟友交易总额")
    private String allyTransactionAmount = "0";
    @ApiModelProperty(value = "盟友新增商户总数")
    private int allyNewShopNum = 0;
    @ApiModelProperty(value = "交易总额")
    private String transactionAmount = "0";
    @ApiModelProperty(value = "新增商户总数")
    private int newShopNum = 0;
    @ApiModelProperty(value = "统计月份")
    private String staticMonth;

    public String getAllyTransactionAmount() {
        return allyTransactionAmount;
    }

    public void setAllyTransactionAmount(String allyTransactionAmount) {
        this.allyTransactionAmount = allyTransactionAmount;
    }

    public int getAllyNewShopNum() {
        return allyNewShopNum;
    }

    public void setAllyNewShopNum(int allyNewShopNum) {
        this.allyNewShopNum = allyNewShopNum;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public int getNewShopNum() {
        return newShopNum;
    }

    public void setNewShopNum(int newShopNum) {
        this.newShopNum = newShopNum;
    }

    public String getStaticMonth() {
        return staticMonth;
    }

    public void setStaticMonth(String staticMonth) {
        this.staticMonth = staticMonth;
    }
}
