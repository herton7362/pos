package com.framework.module.member.domain;

import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends PageRepository<Member> {
    @Query("select m from Member m where m.loginName=?1 and m.logicallyDeleted=false")
    Member findOneByLoginName(String account);

    @Query("select m.id from Member m where m.fatherId=?1 and m.createdDate<=?2")
    List<String> findMembersByFatherId(String fatherMemberId, long endDate);

    @Query("select m from Member m where m.fatherId=?1 and m.createdDate<=?2")
    List<Member> findMemberInfosByFatherId(String fatherMemberId, long endDate);

    @Query("select m from Member m where m.id NOT in (select t.fatherId from Member t where t.fatherId is not NULL)")
    List<Member> getAllLeafMembers();

    @Query("select m from Member m order by m.createdDate DESC")
    List<Member> findAllOrderByCreatedDate();
}
