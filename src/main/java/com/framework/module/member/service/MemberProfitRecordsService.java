package com.framework.module.member.service;

import com.framework.module.member.domain.*;
import com.kratos.common.CrudService;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface MemberProfitRecordsService extends CrudService<MemberProfitRecords>  {

    /**
     * 批量收益导入
     * @param fileName 文件名称
     * @param file 文件
     * @return 导入结果
     * @throws Exception ex
     */
    Integer batchImport(String fileName, MultipartFile file) throws Exception;

    /**
     * 用户购买机器激活后，设置上线团建奖励，测试的时候注意刚激活的这个是否算入
     * @param fatherMemberId 当前激活用户的上线MemberId
     */
    void setTeamBuildProfit(String fatherMemberId) throws Exception;

    /**
     * 获取收益详情
     * @param memberId 会员ID
     * @param startMonth 起始月份
     * @param size 长度
     * @return 收益详情
     * @throws ParseException 异常
     */
    List<ProfitMonthDetail> getProfitByMonth(String memberId, String startMonth, int size) throws Exception;

    /**
     * 按照月份获取业绩详情
     * @param memberId 会员ID
     * @param startMonth 起始月份
     * @param size 长度
     * @return 业绩详情
     * @throws ParseException 异常
     */
    List<AchievementDetail> getAchievementByMonth(String memberId, String startMonth, int size) throws Exception;

    /**
     * 按天获取业绩详情
     * @param memberId 会员ID
     * @param date 起始时间
     * @param size 长度
     * @return 业绩详情
     * @throws ParseException 异常
     */
    List<AchievementDetail> getAchievementByDate(String memberId, String date, int size) throws Exception;

    /**
     * 获取业绩
     * @param memberId 会员ID
     * @return 业绩详情
     * * @throws ParseException 异常
     */
    List<Achievement> getAchievement(String memberId) throws ParseException;

    /**
     * 获取联盟今日新增商户
     * @param memberId 会员ID
     * @return 新增商户数量
     * @throws ParseException 异常
     */
    Integer getAllyNewShopToday(String memberId) throws ParseException;

    /**
     * 获取历史总收益
     * @param memberId 会员ID
     * @return 新增商户数量
     * @throws ParseException 异常
     */
    String getTotalProfit(String memberId);

    /**
     * 每月1号凌晨对所有会员进行升级
     */
    void membersIncreaseLevel() throws Exception;

    /**
     *  查询可提现金额
     * @param memberId 会员ID
     * @return 返回数据
     * @throws Exception 异常
     */
    double cashOnAmount(String memberId) throws Exception;

    /**
     * 审核收入信息
     * @param operateTransactionId
     * @param examineResult
     */
    void examineImportProfit(String operateTransactionId, boolean examineResult) throws Exception;

    /**
     * 查询上个月大合伙人
     * @param memberId
     * @return
     */
    Map<String,Object> getBigPartner(String memberId) throws Exception;

    double getSnTransactionAmount(String sn, Long startTime, Long endTime);
}
