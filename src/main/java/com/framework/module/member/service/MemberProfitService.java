package com.framework.module.member.service;

import com.framework.module.member.domain.Member;
import com.kratos.common.CrudService;
import org.springframework.web.multipart.MultipartFile;

public interface MemberProfitService extends CrudService<Member> {

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

    /**
     * 用户购买机器激活后，设置上线团建奖励，测试的时候注意刚激活的这个是否算入
     * @param fatherMemberId 当前激活用户的上线MemberId
     */
    void setTeamBuildProfit(String fatherMemberId) throws Exception;
}
