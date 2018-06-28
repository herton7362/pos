package com.framework.module.member.service;

import com.framework.module.member.domain.Achievement;
import com.framework.module.member.domain.AchievementDetail;
import com.framework.module.member.domain.MemberProfitRecords;
import com.framework.module.member.domain.ProfitMonthDetail;
import com.kratos.common.CrudService;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

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
    List<ProfitMonthDetail> getProfitByMonth(String memberId, String startMonth, int size) throws ParseException;

    /**
     * 按照月份获取业绩详情
     * @param memberId 会员ID
     * @param startMonth 起始月份
     * @param size 长度
     * @return 业绩详情
     * @throws ParseException 异常
     */
    List<AchievementDetail> getAchievementByMonth(String memberId, String startMonth, int size) throws ParseException;

    /**
     * 按天获取业绩详情
     * @param memberId 会员ID
     * @param date 起始时间
     * @param size 长度
     * @return 业绩详情
     * @throws ParseException 异常
     */
    List<AchievementDetail> getAchievementByDate(String memberId, String date, int size) throws ParseException;

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
    Double getTotalProfit(String memberId);


}
