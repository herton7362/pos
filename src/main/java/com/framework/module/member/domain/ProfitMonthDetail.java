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
    @ApiModelProperty(value = "秒到/云闪付/理财收益")
    private double profitMiaoDao;
    @ApiModelProperty(value = "扫码收益")
    private double profitSaoMa;
    @ApiModelProperty(value = "快捷支付收益")
    private double profitKuaiJie;
    @ApiModelProperty(value = "代还收益")
    private double profitDaiHuan;
    @ApiModelProperty(value = "秒到/云闪付/理财交易金额")
    private double transactionAmountMiaoDao;
    @ApiModelProperty(value = "扫码交易金额")
    private double transactionAmountSaoMa;
    @ApiModelProperty(value = "快捷支付交易金额")
    private double transactionAmountKuaiJie;
    @ApiModelProperty(value = "代还交易金额")
    private double transactionAmountDaiHuan;

    public double getProfitKuaiJie() {
        return profitKuaiJie;
    }

    public void setProfitKuaiJie(double profitKuaiJie) {
        this.profitKuaiJie = profitKuaiJie;
    }

    public double getTransactionAmountKuaiJie() {
        return transactionAmountKuaiJie;
    }

    public void setTransactionAmountKuaiJie(double transactionAmountKuaiJie) {
        this.transactionAmountKuaiJie = transactionAmountKuaiJie;
    }

    public double getProfitMiaoDao() {
        return profitMiaoDao;
    }

    public void setProfitMiaoDao(double profitMiaoDao) {
        this.profitMiaoDao = profitMiaoDao;
    }

    public double getProfitSaoMa() {
        return profitSaoMa;
    }

    public void setProfitSaoMa(double profitSaoMa) {
        this.profitSaoMa = profitSaoMa;
    }

    public double getProfitDaiHuan() {
        return profitDaiHuan;
    }

    public void setProfitDaiHuan(double profitDaiHuan) {
        this.profitDaiHuan = profitDaiHuan;
    }

    public double getTransactionAmountMiaoDao() {
        return transactionAmountMiaoDao;
    }

    public void setTransactionAmountMiaoDao(double transactionAmountMiaoDao) {
        this.transactionAmountMiaoDao = transactionAmountMiaoDao;
    }

    public double getTransactionAmountSaoMa() {
        return transactionAmountSaoMa;
    }

    public void setTransactionAmountSaoMa(double transactionAmountSaoMa) {
        this.transactionAmountSaoMa = transactionAmountSaoMa;
    }

    public double getTransactionAmountDaiHuan() {
        return transactionAmountDaiHuan;
    }

    public void setTransactionAmountDaiHuan(double transactionAmountDaiHuan) {
        this.transactionAmountDaiHuan = transactionAmountDaiHuan;
    }

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
