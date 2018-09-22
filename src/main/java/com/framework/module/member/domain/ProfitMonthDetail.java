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
    private String totalProfit;
    @ApiModelProperty(value = "激活奖")
    private String activeAward;
    @ApiModelProperty(value = "直营奖")
    private String directlyAward;
    @ApiModelProperty(value = "管理奖")
    private String managerAward;
    @ApiModelProperty(value = "团建奖")
    private String teamBuildAward;
    @ApiModelProperty(value = "总交易额")
    private String totalTransactionAmount;
    @ApiModelProperty(value = "交易月份/yyyyMM")
    private String month;
    @ApiModelProperty(value = "秒到/云闪付/理财收益")
    private String profitMiaoDao;
    @ApiModelProperty(value = "扫码收益")
    private String profitSaoMa;
    @ApiModelProperty(value = "快捷支付收益")
    private String profitKuaiJie;
    @ApiModelProperty(value = "代还收益")
    private String profitDaiHuan;
    @ApiModelProperty(value = "秒到/云闪付/理财交易金额")
    private String transactionAmountMiaoDao;
    @ApiModelProperty(value = "扫码交易金额")
    private String transactionAmountSaoMa;
    @ApiModelProperty(value = "快捷支付交易金额")
    private String transactionAmountKuaiJie;
    @ApiModelProperty(value = "代还交易金额")
    private String transactionAmountDaiHuan;

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String getActiveAward() {
        return activeAward;
    }

    public void setActiveAward(String activeAward) {
        this.activeAward = activeAward;
    }

    public String getDirectlyAward() {
        return directlyAward;
    }

    public void setDirectlyAward(String directlyAward) {
        this.directlyAward = directlyAward;
    }

    public String getManagerAward() {
        return managerAward;
    }

    public void setManagerAward(String managerAward) {
        this.managerAward = managerAward;
    }

    public String getTeamBuildAward() {
        return teamBuildAward;
    }

    public void setTeamBuildAward(String teamBuildAward) {
        this.teamBuildAward = teamBuildAward;
    }

    public String getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setTotalTransactionAmount(String totalTransactionAmount) {
        this.totalTransactionAmount = totalTransactionAmount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getProfitMiaoDao() {
        return profitMiaoDao;
    }

    public void setProfitMiaoDao(String profitMiaoDao) {
        this.profitMiaoDao = profitMiaoDao;
    }

    public String getProfitSaoMa() {
        return profitSaoMa;
    }

    public void setProfitSaoMa(String profitSaoMa) {
        this.profitSaoMa = profitSaoMa;
    }

    public String getProfitKuaiJie() {
        return profitKuaiJie;
    }

    public void setProfitKuaiJie(String profitKuaiJie) {
        this.profitKuaiJie = profitKuaiJie;
    }

    public String getProfitDaiHuan() {
        return profitDaiHuan;
    }

    public void setProfitDaiHuan(String profitDaiHuan) {
        this.profitDaiHuan = profitDaiHuan;
    }

    public String getTransactionAmountMiaoDao() {
        return transactionAmountMiaoDao;
    }

    public void setTransactionAmountMiaoDao(String transactionAmountMiaoDao) {
        this.transactionAmountMiaoDao = transactionAmountMiaoDao;
    }

    public String getTransactionAmountSaoMa() {
        return transactionAmountSaoMa;
    }

    public void setTransactionAmountSaoMa(String transactionAmountSaoMa) {
        this.transactionAmountSaoMa = transactionAmountSaoMa;
    }

    public String getTransactionAmountKuaiJie() {
        return transactionAmountKuaiJie;
    }

    public void setTransactionAmountKuaiJie(String transactionAmountKuaiJie) {
        this.transactionAmountKuaiJie = transactionAmountKuaiJie;
    }

    public String getTransactionAmountDaiHuan() {
        return transactionAmountDaiHuan;
    }

    public void setTransactionAmountDaiHuan(String transactionAmountDaiHuan) {
        this.transactionAmountDaiHuan = transactionAmountDaiHuan;
    }
}
