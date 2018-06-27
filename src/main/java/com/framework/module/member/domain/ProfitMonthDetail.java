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
public class ProfitMonthDetail {
    @ApiModelProperty(value = "总收益")
    private double totalProfit;
    @ApiModelProperty(value = "激活奖")
    private double activeAward;
    @ApiModelProperty(value = "直营奖")
    private double directlyAward;
    @ApiModelProperty(value = "管理奖")
    private double managerAward;
    @ApiModelProperty(value = "团建奖")
    private double teamBuildAward;
    @ApiModelProperty(value = "总交易额")
    private double totalTransactionAmount;
    @ApiModelProperty(value = "交易月份/yyyyMM")
    private String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public double getActiveAward() {
        return activeAward;
    }

    public void setActiveAward(double activeAward) {
        this.activeAward = activeAward;
    }

    public double getDirectlyAward() {
        return directlyAward;
    }

    public void setDirectlyAward(double directlyAward) {
        this.directlyAward = directlyAward;
    }

    public double getManagerAward() {
        return managerAward;
    }

    public void setManagerAward(double managerAward) {
        this.managerAward = managerAward;
    }

    public double getTeamBuildAward() {
        return teamBuildAward;
    }

    public void setTeamBuildAward(double teamBuildAward) {
        this.teamBuildAward = teamBuildAward;
    }

    public double getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setTotalTransactionAmount(double totalTransactionAmount) {
        this.totalTransactionAmount = totalTransactionAmount;
    }
}
