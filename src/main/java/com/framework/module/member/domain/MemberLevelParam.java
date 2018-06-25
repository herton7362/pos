package com.framework.module.member.domain;

import com.kratos.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * <p>Description: 各个级别的参数配置</p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/6/23 11:30
 */
@Entity
public class MemberLevelParam extends BaseEntity {
    private int level;
    private double totalTransactionVolume;
    private int teamScale;
    private double mPosProfit;
    private int bigPosProfit;
    private String allyTitle;
    private String honor;
    private String headquartersSupport;

    public double getTotalTransactionVolume() {
        return totalTransactionVolume;
    }

    public void setTotalTransactionVolume(double totalTransactionVolume) {
        this.totalTransactionVolume = totalTransactionVolume;
    }

    public String getAllyTitle() {
        return allyTitle;
    }

    public void setAllyTitle(String allyTitle) {
        this.allyTitle = allyTitle;
    }

    public String getHonor() {
        return honor;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }

    public String getHeadquartersSupport() {
        return headquartersSupport;
    }

    public void setHeadquartersSupport(String headquartersSupport) {
        this.headquartersSupport = headquartersSupport;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTeamScale() {
        return teamScale;
    }

    public void setTeamScale(int teamScale) {
        this.teamScale = teamScale;
    }

    public double getmPosProfit() {
        return mPosProfit;
    }

    public void setmPosProfit(double mPosProfit) {
        this.mPosProfit = mPosProfit;
    }

    public int getBigPosProfit() {
        return bigPosProfit;
    }

    public void setBigPosProfit(int bigPosProfit) {
        this.bigPosProfit = bigPosProfit;
    }
}
