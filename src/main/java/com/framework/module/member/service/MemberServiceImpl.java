package com.framework.module.member.service;

import com.framework.module.auth.MemberThread;
import com.framework.module.common.Constant;
import com.framework.module.member.domain.*;
import com.framework.module.record.domain.OperationRecord;
import com.framework.module.record.service.OperationRecordService;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.domain.ShopRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.exceptions.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Component("memberService")
@Transactional
public class MemberServiceImpl extends AbstractCrudService<Member> implements MemberService {
    private final MemberRepository repository;
    private final MemberCardService memberCardService;
    private final OperationRecordService operationRecordService;
    private final MemberProfitRecordsService memberProfitRecordsService;
    private final MemberLevelParamService memberLevelParamService;
    private final ShopRepository shopRepository;

    @Override
    public Member save(Member member) throws Exception {
        if (StringUtils.isNotBlank(member.getId())) {
            Member old = repository.findOne(member.getId());
            member.setPassword(old.getPassword());
        }
        return super.save(member);
    }

    @Override
    protected PageRepository<Member> getRepository() {
        return repository;
    }

    @Override
    public Member findOneByLoginName(String loginName) {
        return repository.findOneByLoginName(loginName);
    }

    @Override
    public Member findOneByMemberNumber(String memberNumber) throws Exception {
        Map<String, String[]> param = new HashMap<>();
        param.put("memberNumber", new String[]{memberNumber});
        List<Member> members = findAll(param);
        if (!CollectionUtils.isEmpty(members)) {
            return members.get(0);
        }
        return null;
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
    public Integer batchImport(String fileName, MultipartFile file) throws Exception {
        Integer importSize = 0;
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new BusinessException("输入文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        List<MemberProfitRecords> memberProfitRecordsList = new ArrayList<>();
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            MemberProfitRecords importProfit = getAllParamFromExcel(row, r);
            Shop shop = shopRepository.findOneBySn(importProfit.getSn());
            shop.setTransactionAmount(new BigDecimal(shop.getTransactionAmount() + importProfit.getTransactionAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            if ((null == shop.getStatus() || shop.getStatus().equals(Shop.Status.UN_ACTIVE)) && shop.getTransactionAmount() > 4000) {
                shop.setStatus(Shop.Status.ACTIVE);
                shopRepository.save(shop);
            }

            Member member = findOneByMemberNumber(importProfit.getUserNo());
            if (member == null) {
                throw new BusinessException(String.format("第" + r + "行数据机不合法,用户编号不存在:[%s]", importProfit.getUserNo()));
            }
            importProfit.setMemberId(member.getId());
            if (StringUtils.isBlank(member.getMemberLevel())) {
                throw new BusinessException(String.format("第" + r + "行数据机不合法,用户等级信息不存在:[%s]", importProfit.getUserNo()));
            }
            MemberLevelParam param = memberLevelParamService.getParamByLevel(member.getMemberLevel());
            if (param == null) {
                throw new BusinessException(String.format("第" + r + "行数据机不合法,用户等级信息不合法:[%s]", importProfit.getUserNo()));
            }
            double profitRate = param.getmPosProfit();
            importProfit.setProfit(new BigDecimal(profitRate * importProfit.getTransactionAmount() / 10000d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            memberProfitRecordsList.add(importProfit);
            if (StringUtils.isNotBlank(member.getFatherId())) {
                setParamProfitRecords(memberProfitRecordsList, importProfit, member, profitRate);
            }
        }
        // 将数据都插入到数据库中
        for (MemberProfitRecords t : memberProfitRecordsList) {
            memberProfitRecordsService.save(t);
        }
        importSize = memberProfitRecordsList.size();
        return importSize;
    }

    /**
     * 设置上级的收益情况
     *
     * @param memberProfitRecordsList 入库的list
     * @param importProfit            当前用户的收益情况
     * @param member                  当前用户
     * @param profitRate              当前用户的收益
     * @throws Exception 异常
     */
    private void setParamProfitRecords(List<MemberProfitRecords> memberProfitRecordsList, MemberProfitRecords importProfit, Member member, double profitRate) throws Exception {
        Member fatherMember = repository.findOne(member.getFatherId());
        while (fatherMember != null) {
            MemberProfitRecords fatherProfit = new MemberProfitRecords();
            fatherProfit.setOrganizationNo(importProfit.getOrganizationNo());
            fatherProfit.setOrganizationName(importProfit.getOrganizationName());
            fatherProfit.setUserNo(importProfit.getUserNo());
            fatherProfit.setUserName(importProfit.getUserName());
            fatherProfit.setTransactionAmount(importProfit.getTransactionAmount());
            fatherProfit.setTransactionType(importProfit.getTransactionType());
            fatherProfit.setSn(importProfit.getSn());
            fatherProfit.setTransactionDate(importProfit.getTransactionDate());
            fatherProfit.setStatus(0);
            fatherProfit.setProfitType(Constant.PROFIT_TYPE_GUANLI);
            fatherProfit.setMemberId(fatherMember.getId());
            MemberLevelParam fatherMemberParam = memberLevelParamService.getParamByLevel(fatherMember.getMemberLevel());
            fatherProfit.setProfit(new BigDecimal((fatherMemberParam.getmPosProfit() - profitRate) * importProfit.getTransactionAmount() / 10000d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            memberProfitRecordsList.add(fatherProfit);
            if (StringUtils.isBlank(fatherMember.getFatherId())) {
                fatherMember = null;
            } else {
                fatherMember = repository.findOne(fatherMember.getFatherId());
            }
        }
    }

    /**
     * 再Excel中获取数据并校验，校验通过后加入到实体中
     *
     * @param row
     * @return
     */
    private MemberProfitRecords getAllParamFromExcel(Row row, int r) throws BusinessException {
        row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
        String organizationNo = row.getCell(0).getStringCellValue();
        if (StringUtils.isBlank(organizationNo)) {
            throw new BusinessException(String.format("第" + r + "行数据机不合法,[%s]为空", "机构编码"));
        }
        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
        String organizationName = row.getCell(1).getStringCellValue();
        if (StringUtils.isBlank(organizationName)) {
            throw new BusinessException(String.format("第" + r + "行数据机不合法,[%s]为空", "机构名称"));
        }

        row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
        String userNo = row.getCell(2).getStringCellValue();
        if (StringUtils.isBlank(userNo)) {
            throw new BusinessException(String.format("第" + r + "行数据机不合法,[%s]为空", "用户编号"));
        }

        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
        String userName = row.getCell(3).getStringCellValue();
        if (StringUtils.isBlank(userName)) {
            throw new BusinessException(String.format("第" + r + "行数据机不合法,[%s]为空", "用户姓名"));
        }

        row.getCell(4).setCellType(Cell.CELL_TYPE_NUMERIC);
        Double transactionAmount = row.getCell(4).getNumericCellValue();
        if (transactionAmount == null || transactionAmount.doubleValue() == 0) {
            throw new BusinessException(String.format("第" + r + "行数据机不合法,[%s]数据不合法", "交易金额"));
        }
        row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
        String sn = row.getCell(5).getStringCellValue();
        if (StringUtils.isBlank(sn)) {
            throw new BusinessException(String.format("第" + r + "行数据机不合法,[%s]为空", "SN"));
        }
        row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
        String transactionType = row.getCell(6).getStringCellValue();
        if (StringUtils.isBlank(transactionType)) {
            throw new BusinessException(String.format("第" + r + "行数据机不合法,[%s]为空", "交易类型"));
        }
        Date transactionDate = row.getCell(7).getDateCellValue();
        if (transactionDate == null) {
            transactionDate = new Date();
        }
        MemberProfitRecords importProfit = new MemberProfitRecords();
        importProfit.setOrganizationNo(organizationNo);
        importProfit.setOrganizationName(organizationName);
        importProfit.setUserNo(userNo);
        importProfit.setUserName(userName);
        importProfit.setTransactionAmount(transactionAmount);
        importProfit.setTransactionType(transactionType);
        importProfit.setSn(sn);
        importProfit.setTransactionDate(transactionDate);
        importProfit.setStatus(0);
        importProfit.setProfitType(Constant.PROFIT_TYPE_ZHIYING);
        return importProfit;
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

    @Lazy
    @Autowired
    public MemberServiceImpl(
            MemberRepository repository,
            MemberCardService memberCardService,
            OperationRecordService operationRecordService,
            MemberProfitRecordsService memberProfitRecordsService, MemberLevelParamService memberLevelParamService, ShopRepository shopRepository) {
        this.repository = repository;
        this.memberCardService = memberCardService;
        this.operationRecordService = operationRecordService;
        this.memberProfitRecordsService = memberProfitRecordsService;
        this.memberLevelParamService = memberLevelParamService;
        this.shopRepository = shopRepository;
    }
}
