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
    private Integer level;
    private Double totalTransactionVolume;
    private String teamScale;
    private Double mPosProfit;
    private Double bigPosProfit;
    private String allyTitle;
    private String honor;
    private String headquartersSupport;

    public Double getTotalTransactionVolume() {
        return totalTransactionVolume;
    }

    public void setTotalTransactionVolume(Double totalTransactionVolume) {
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getTeamScale() {
        return teamScale;
    }

    public void setTeamScale(String teamScale) {
        this.teamScale = teamScale;
    }

    public Double getmPosProfit() {
        return mPosProfit;
    }

    public void setmPosProfit(Double mPosProfit) {
        this.mPosProfit = mPosProfit;
    }

    public void setBigPosProfit(Double bigPosProfit) {
        this.bigPosProfit = bigPosProfit;
    }
}
