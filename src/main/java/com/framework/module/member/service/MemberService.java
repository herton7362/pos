package com.framework.module.member.service;

import com.kratos.common.CrudService;
import com.framework.module.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService extends CrudService<Member> {
    /**
     * 根据登录名获取会员
     * @param loginName 登录名
     * @return {@link Member}
     */
    Member findOneByLoginName(String loginName);
    /**
     * 根据会员卡获取会员
     * @param cardNo 会员卡号
     * @return {@link Member}
     */
    Member findOneByCardNo(String cardNo) throws Exception;

    /**
     * 快速积分
     * @param id 会员id
     * @param point 增加的积分
     */
    void fastIncreasePoint(String id, Integer point) throws Exception;

    /**
     * 增加余额
     * @param id 会员id
     * @param balance 增加的余额
     */
    void increaseBalance(String id, Double balance) throws Exception;

    /**
     * 储值扣费
     * @param memberId 会员id
     * @param amount 扣除的余额
     */
    void deductBalance(String memberId, Double amount) throws Exception;

    /**
     * 查询总数
     * @return 会员总数
     */
    Long count();

    /**
     * 批量收益导入
     * @param fileName 文件名称
     * @param file 文件
     * @return 导入结果
     * @throws Exception ex
     */
    Integer batchImport(String fileName, MultipartFile file) throws Exception;

    /**
     * 通过POS的用户ID查找用户会员
     * @param memberNumber POS的用户ID
     * @return 会员信息
     */
    Member findOneByMemberNumber(String memberNumber) throws Exception;
}
