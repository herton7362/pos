package com.framework.module.common;

/**
 * <p>Description: 常量类</p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/6/26 11:03
 */
public abstract class Constant {
    /**
     * 收益类型，返现奖励
     */
    public static final int PROFIT_TYPE_FANXIAN = 1;
    /**
     * 收益类型，直营奖励
     */
    public static final int PROFIT_TYPE_ZHIYING = 2;
    /**
     * 收益类型，管理奖励
     */
    public static final int PROFIT_TYPE_GUANLI = 3;
    /**
     * 收益类型，团建奖励
     */
    public static final int PROFIT_TYPE_TUANJIAN = 4;

    /**
     * 收益类型，1000万大额盟友奖励
     */
    public static final int PROFIT_BIG_PARTNER = 5;

    /**
     * 是否领取激活奖励-未领取
     */
    public static final int ACTIVATION_REWARD_NO = 0;
    /**
     * 是否领取激活奖励-领取
     */
    public static final int ACTIVATION_REWARD_YES = 1;

    /**
     * 盟友排行方式-默认
     */
    public static final int SORT_TYPE_DEFAULT = 1;
    /**
     * 盟友排行方式-级别
     */
    public static final int SORT_TYPE_LEVEL = 2;
    /**
     * 盟友排行方式-收益
     */
    public static final int SORT_TYPE_PROFIT = 3;
    /**
     * 盟友排行方式-盟友规模从多到少
     */
    public static final int SORT_TYPE_ALLY_NUM_HIGH_LOW = 4;
    /**
     * 盟友排行方式-盟友规模从少到多
     */
    public static final int SORT_TYPE_ALLY_NUM_LOW_HIGH = 5;
}
