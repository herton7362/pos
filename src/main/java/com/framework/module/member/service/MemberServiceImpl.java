package com.framework.module.member.service;

import com.framework.module.auth.MemberThread;
import com.framework.module.member.domain.*;
import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.record.domain.OperationRecord;
import com.framework.module.record.service.OperationRecordService;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.PageResult;
import com.kratos.exceptions.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

@Component("memberService")
@Transactional
public class MemberServiceImpl extends AbstractCrudService<Member> implements MemberService {
    private final MemberRepository repository;
    private final MemberCardService memberCardService;
    private final OperationRecordService operationRecordService;

    @Override
    public Member save(Member member) throws Exception {
        if (StringUtils.isNotBlank(member.getId())) {
            Member old = repository.findOne(member.getId());
            member.setPassword(old.getPassword());
            if (member.getMemberLevel() != null && !member.getMemberLevel().equals(old.getMemberLevel())) {
                member.setManualLevel("1");
            }
        }
        return super.save(member);
    }

    @Override
    protected PageRepository<Member> getRepository() {
        return repository;
    }

    @Override
    public PageResult<Member> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        return new PageResult<>(repository.findAll(new MySpecification(param, true), pageRequest));
    }

    @Override
    public List<Member> findAll(Map<String, String[]> param) throws Exception {
        return repository.findAll(new MySpecification(param, true));
    }

    private class MySpecification extends SimpleSpecification {
        MySpecification(Map<String, String[]> params, Boolean allEntities) {
            super(params, allEntities);
        }

        @Override
        public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            Predicate predicate = super.toPredicate(root, criteriaQuery, criteriaBuilder);
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("quickSearch") && StringUtils.isNotBlank(params.get("quickSearch")[0])) {
                String[] value = params.get("quickSearch");
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + value[0] + "%"));
                predicates.add(criteriaBuilder.like(root.get("loginName"), "%" + value[0] + "%"));
                predicates.add(criteriaBuilder.like(root.get("mobile"), "%" + value[0] + "%"));
                predicates.add(criteriaBuilder.like(root.get("idCard"), "%" + value[0] + "%"));
                Predicate predicateTemp = criteriaBuilder.or(predicates.toArray(new Predicate[]{}));
                predicates.clear();
                predicates.add(predicateTemp);
            }

            List<Predicate> predicatesStatus = new ArrayList<>();
            predicatesStatus.add(criteriaBuilder.equal(root.get("logicallyDeleted"), false));
            Predicate predicateTemp = criteriaBuilder.and(predicatesStatus.toArray(new Predicate[]{}));
            predicates.add(predicateTemp);

            predicates.add(predicate);
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        }
    }

    @Override
    public Member findOneByLoginName(String loginName) {
        return repository.findOneByLoginName(loginName);
    }

    @Override
    public Member findOneByCardNo(String cardNo) throws Exception {
        Map<String, String[]> param = new HashMap<>();
        param.put("cardNumber", new String[]{cardNo});
        List<MemberCard> memberCards = memberCardService.findAll(param);
        if (memberCards != null && !memberCards.isEmpty()) {
            return memberCards.get(0).getMember();
        }
        return null;
    }

    @Override
    public void fastIncreasePoint(String id, Integer point) throws Exception {
        Member member = findOne(id);
        if (member == null) {
            throw new BusinessException(String.format("会员id:[%s]不存在", id));
        }
        if (point == null) {
            return;
        }
        member.setPoint(increaseNumber(member.getPoint(), point));
        member.setSalePoint(increaseNumber(member.getSalePoint(), point));
        save(member);

        record(member, String.format("充值积分 %s 分", point), OperationRecord.BusinessType.FAST_INCREASE_POINT);
    }

    /**
     * 记录充值记录
     *
     * @param member  会员
     * @param content 记录内容
     */
    private void record(Member member, String content, OperationRecord.BusinessType businessType) throws Exception {
        OperationRecord rechargeRecord = new OperationRecord();
        rechargeRecord.setMember(member);
        rechargeRecord.setBusinessType(businessType.name());
        rechargeRecord.setClientId(MemberThread.getInstance().getClientId());
        rechargeRecord.setIpAddress(MemberThread.getInstance().getIpAddress());
        rechargeRecord.setContent(content);
        operationRecordService.save(rechargeRecord);
    }

    @Override
    public void increaseBalance(String id, Double balance) throws Exception {
        Member member = findOne(id);
        if (member == null) {
            throw new BusinessException(String.format("会员id:[%s]不存在", id));
        }
        if (balance == null) {
            return;
        }
        member.setBalance(increaseMoney(member.getBalance(), balance));
        save(member);
    }

    @Override
    public void deductBalance(String memberId, Double amount) throws Exception {
        Member member = findOne(memberId);
        if (member == null) {
            throw new BusinessException(String.format("会员id:[%s]不存在", memberId));
        }
        if (amount == null) {
            return;
        }
        member.setBalance(subtractMoney(member.getBalance(), amount));
        save(member);

        record(member, String.format("储值消费 %s 元", amount), OperationRecord.BusinessType.DEDUCT_BALANCE);
    }

    @Override
    public Long count() {
        return repository.count(
                (Root<Member> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
                    List<Predicate> predicate = new ArrayList<>();
                    predicate.add(criteriaBuilder.equal(root.get("logicallyDeleted"), false));
                    return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
                }
        );
    }

    @Override
    public AllyMembers getAlliesByMemberId(String memberId) {
        return getAlliesByMemberId(memberId, new Date().getTime());
    }

    @Override
    public AllyMembers getAlliesByMemberId(String memberId, long endDate) {
        List<String> allSons = repository.findMembersByFatherId(memberId, endDate);
        if (allSons == null) {
            allSons = new ArrayList<>();
        }
        List<String> allGrandson = new ArrayList<>();
        for (String m : allSons) {
            allGrandson.addAll(repository.findMembersByFatherId(m, endDate));
        }
        Integer totalSize = allGrandson.size();
        Integer startPos = 0;
        do {
            for (int i = startPos; i < totalSize; i++) {
                allGrandson.addAll(repository.findMembersByFatherId(allGrandson.get(i), endDate));
            }
            startPos = totalSize;
            totalSize = allGrandson.size();
        } while (!totalSize.equals(startPos));
        AllyMembers allyMembers = new AllyMembers();
        allyMembers.setGrandSonList(allGrandson);
        allyMembers.setSonList(allSons);
        allyMembers.setTotalNum(allSons.size() + allGrandson.size());
        return allyMembers;
    }

    @Override
    public AllyMemberInfos getAlliesInfosByMemberId(String memberId, Long endDate) {
        List<Member> allSons = repository.findMemberInfosByFatherId(memberId, endDate);
        if (allSons == null) {
            allSons = new ArrayList<>();
        }
        List<Member> allGrandson = new ArrayList<>();
        for (Member m : allSons) {
            allGrandson.addAll(repository.findMemberInfosByFatherId(m.getId(), endDate));
        }
        Integer totalSize = allGrandson.size();
        Integer startPos = 0;
        do {
            for (int i = startPos; i < totalSize; i++) {
                allGrandson.addAll(repository.findMemberInfosByFatherId(allGrandson.get(i).getId(), endDate));
            }
            startPos = totalSize;
            totalSize = allGrandson.size();
        } while (!totalSize.equals(startPos));
        AllyMemberInfos allyMembers = new AllyMemberInfos();
        allyMembers.setGrandSonList(allGrandson);
        allyMembers.setSonList(allSons);
        allyMembers.setTotalNum(allSons.size() + allGrandson.size());
        return allyMembers;
    }

    private Integer increaseNumber(Integer sourcePoint, Integer point) {
        if (sourcePoint == null) {
            sourcePoint = 0;
        }
        return sourcePoint + point;
    }

    private Double increaseMoney(Double sourceMoney, Double money) {
        if (sourceMoney == null) {
            sourceMoney = 0D;
        }
        return new BigDecimal(sourceMoney).add(new BigDecimal(money)).doubleValue();
    }

    private Double subtractMoney(Double sourceMoney, Double money) {
        if (sourceMoney == null) {
            sourceMoney = 0D;
        }
        return new BigDecimal(sourceMoney).subtract(new BigDecimal(money)).doubleValue();
    }

    @Override
    public void editPwd(Member member) throws Exception {
        if (StringUtils.isBlank(member.getId())) {
            return;
        }
        Member old = repository.findOne(member.getId());
        old.setPassword(new BCryptPasswordEncoder().encode(member.getPassword()));
        super.save(old);
    }

    @Override
    public Map<String, Integer> getActiveSonNum() {
        Map<String, Integer> result = new HashMap<>();
        Member member = MemberThread.getInstance().get();
        AllyMemberInfos allyMembers = getAlliesInfosByMemberId(member.getId(), new Date().getTime());
        int activeSonNum = 0;
        int activeGrandSonNum = 0;
        if (allyMembers != null) {
            result.put("allySonNumber", allyMembers.getSonList().size());
            result.put("allyAllNumber", allyMembers.getTotalNum());
            for (Member m : allyMembers.getSonList()) {
                if (Member.Status.ACTIVE.equals(m.getStatus())) {
                    activeSonNum++;
                }
            }
            for (Member m : allyMembers.getGrandSonList()) {
                if (Member.Status.ACTIVE.equals(m.getStatus())) {
                    activeGrandSonNum++;
                }
            }
        }
        result.put("allyAllActiveNumber", activeGrandSonNum + activeSonNum);
        result.put("allySonActiveNumber", activeSonNum);
        return result;
    }

    @Lazy
    @Autowired
    public MemberServiceImpl(
            MemberRepository repository,
            MemberCardService memberCardService,
            OperationRecordService operationRecordService) {
        this.repository = repository;
        this.memberCardService = memberCardService;
        this.operationRecordService = operationRecordService;
    }
}
