package com.framework.module.member.service;

import com.framework.module.common.Constant;
import com.framework.module.member.domain.*;
import com.framework.module.rule.domain.ActiveRule;
import com.framework.module.rule.domain.GroupBuildDrawRule;
import com.framework.module.rule.service.ActiveRuleService;
import com.framework.module.rule.service.GroupBuildDrawRuleService;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.domain.ShopRepository;
import com.kratos.common.AbstractCrudService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Transactional
public class MemberProfitRecordsServiceImpl extends AbstractCrudService<MemberProfitRecords> implements MemberProfitRecordsService {
    private final MemberService memberService;
    private final MemberRepository repository;
    private final MemberLevelParamService memberLevelParamService;
    private final ShopRepository shopRepository;
    private final GroupBuildDrawRuleService groupBuildDrawRuleService;
    private final ActiveRuleService activeRuleService;
    private final MemberProfitRecordsRepository memberProfitRecordsRepository;

    @Override
    public void setTeamBuildProfit(String fatherId) throws Exception {
        Long activeCount = repository.count(
                (Root<Member> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
                    List<Predicate> predicate = new ArrayList<>();
                    predicate.add(criteriaBuilder.equal(root.get("fatherId"), fatherId));
                    predicate.add(criteriaBuilder.equal(root.get("status"), Member.Status.ACTIVE));
                    return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
                });
        List<GroupBuildDrawRule> groupBuildDrawRules = groupBuildDrawRuleService.findAll(new HashMap<>());
        if (CollectionUtils.isEmpty(groupBuildDrawRules) || groupBuildDrawRules.get(0).getMemberCount() == null || groupBuildDrawRules.get(0).getReward() == null) {
            throw new BusinessException("团建奖励设置不合法");
        }
        Integer memberCount = groupBuildDrawRules.get(0).getMemberCount();
        Double reward = groupBuildDrawRules.get(0).getReward();
        if (activeCount > 0 && activeCount % memberCount == 0) {
            MemberProfitRecords teamBuilderProfit = new MemberProfitRecords();
            teamBuilderProfit.setProfitType(Constant.PROFIT_TYPE_TUANJIAN);
            teamBuilderProfit.setProfit(activeCount / memberCount * reward);
            teamBuilderProfit.setMemberId(fatherId);
            teamBuilderProfit.setTransactionDate(new Date());
            save(teamBuilderProfit);
        }
    }

    @Override
    public List<ProfitMonthDetail> getProfitByMonth(String memberId, String startMonth, int size) throws ParseException {
        List<ProfitMonthDetail> result = new ArrayList<>(size);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date searchMonth = sdf.parse(startMonth);
        for (int i = 0; i < size; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(searchMonth);
            calendar.add(Calendar.MONTH, -i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String firstDay = sdf1.format(calendar.getTime()) + " 00:00:00";
            // 获取前一个月最后一天
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            String lastDay = sdf1.format(calendar.getTime()) + " 23:59:59";
            long start = sdf2.parse(firstDay).getTime();
            long end = sdf2.parse(lastDay).getTime();

            Map<String, Double> resultMap = memberProfitRecordsRepository.staticProfitsByMonth(memberId, start, end);
            ProfitMonthDetail profitMonthDetail = new ProfitMonthDetail();
            profitMonthDetail.setTotalProfit(resultMap.get("totalProfit") == null ? 0 : resultMap.get("totalProfit"));
            profitMonthDetail.setActiveAward(resultMap.get("activeAward") == null ? 0 : resultMap.get("activeAward"));
            profitMonthDetail.setDirectlyAward(resultMap.get("directlyAward") == null ? 0 : resultMap.get("directlyAward"));
            profitMonthDetail.setManagerAward(resultMap.get("managerAward") == null ? 0 : resultMap.get("managerAward"));
            profitMonthDetail.setTeamBuildAward(resultMap.get("teamBuildAward") == null ? 0 : resultMap.get("teamBuildAward"));
            profitMonthDetail.setTotalTransactionAmount(resultMap.get("totalTransactionAmount") == null ? 0 : resultMap.get("totalTransactionAmount"));
            profitMonthDetail.setMonth(sdf.format(calendar.getTime()));

            resultMap = memberProfitRecordsRepository.staticDirectAwardByMonth(memberId, start, end);
            profitMonthDetail.setProfitMiaoDao(resultMap.get("profit1") == null ? 0 : resultMap.get("profit1"));
            profitMonthDetail.setProfitSaoMa(resultMap.get("profit2") == null ? 0 : resultMap.get("profit2"));
            profitMonthDetail.setProfitDaiHuan(resultMap.get("profit3") == null ? 0 : resultMap.get("profit3"));
            profitMonthDetail.setTransactionAmountMiaoDao(resultMap.get("transaction1") == null ? 0 : resultMap.get("transaction1"));
            profitMonthDetail.setTransactionAmountSaoMa(resultMap.get("transaction2") == null ? 0 : resultMap.get("transaction2"));
            profitMonthDetail.setTransactionAmountDaiHuan(resultMap.get("transaction3") == null ? 0 : resultMap.get("transaction3"));

            result.add(profitMonthDetail);
        }
        return result;
    }

    @Override
    public List<AchievementDetail> getAchievementByMonth(String memberId, String startMonth, int size) throws ParseException {
        List<AchievementDetail> result = new ArrayList<>(size);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date searchMonth = sdf.parse(startMonth);
        for (int i = 0; i < size; i++) {
            AchievementDetail achievementDetail = new AchievementDetail();
            calendar.setTime(searchMonth);
            calendar.add(Calendar.MONTH, -i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String firstDay = sdf1.format(calendar.getTime()) + " 00:00:00";
            // 获取前一个月最后一天
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            String lastDay = sdf1.format(calendar.getTime()) + " 23:59:59";
            long start = sdf2.parse(firstDay).getTime();
            long end = sdf2.parse(lastDay).getTime();
            List<Member> sonList = repository.findMembersByFatherId(memberId, end);
            getAchievementDetail(achievementDetail, start, end, sonList);
            achievementDetail.setStaticDate(sdf.format(calendar.getTime()));
            result.add(achievementDetail);
        }
        return result;
    }

    @Override
    public List<AchievementDetail> getAchievementByDate(String memberId, String date, int size) throws ParseException {
        List<AchievementDetail> result = new ArrayList<>(size);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        for (int i = 0; i < size; i++) {
            AchievementDetail achievementDetail = new AchievementDetail();
            calendar.setTime(sdf.parse(date));
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            String startTime = sdf.format(calendar.getTime()) + " 00:00:00";
            String endTime = sdf.format(calendar.getTime()) + " 23:59:59";
            long start = sdf2.parse(startTime).getTime();
            long end = sdf2.parse(endTime).getTime();
            List<Member> sonList = repository.findMembersByFatherId(memberId, end);
            getAchievementDetail(achievementDetail, start, end, sonList);
            achievementDetail.setStaticDate(sdf.format(calendar.getTime()));
            result.add(achievementDetail);
        }
        return result;
    }

    @Override
    public List<Achievement> getAchievement(String memberId) throws ParseException {
        List<Achievement> result = new ArrayList<>(2);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 2; i++) {
            Achievement achievement = new Achievement();
            calendar.add(Calendar.MONTH, -i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String firstDay = sdf1.format(calendar.getTime()) + " 00:00:00";
            // 获取前一个月最后一天
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            String lastDay = sdf1.format(calendar.getTime()) + " 23:59:59";
            long start = sdf2.parse(firstDay).getTime();
            long end = sdf2.parse(lastDay).getTime();
            List<Member> sonList = repository.findMembersByFatherId(memberId, end);
            if (sonList != null) {
                int newSonShopNum = 0;
                double totalTransactionAmount = 0;
                for (Member m : sonList) {
                    List<Shop> shops = shopRepository.findAllByMemberId(m.getId(), start, end);
                    newSonShopNum += shops == null ? 0 : shops.size();
                    Map<String, Double> resultMap = memberProfitRecordsRepository.staticProfitsByMonth(m.getId(), start, end);
                    totalTransactionAmount += resultMap.get("totalTransactionAmount") == null ? 0d : resultMap.get("totalTransactionAmount");
                }
                totalTransactionAmount = new BigDecimal(totalTransactionAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                achievement.setAllyNewShopNum(newSonShopNum);
                achievement.setAllyTransactionAmount(totalTransactionAmount);
            }
            List<Shop> shops1 = shopRepository.findAllByMemberId(memberId, start, end);
            Map<String, Double> resultMap1 = memberProfitRecordsRepository.staticProfitsByMonth(memberId, start, end);
            Double totalTransactionAmount1 = resultMap1.get("totalTransactionAmount") == null ? 0d : resultMap1.get("totalTransactionAmount");
            achievement.setNewShopNum(shops1.size());
            achievement.setTransactionAmount(new BigDecimal(totalTransactionAmount1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            achievement.setStaticMonth(firstDay);
            result.add(achievement);
        }

        return result;
    }

    @Override
    public Integer getAllyNewShopToday(String memberId) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String startTime = sdf.format(calendar.getTime()) + " 00:00:00";
        String endTime = sdf.format(calendar.getTime()) + " 23:59:59";
        long start = sdf2.parse(startTime).getTime();
        long end = sdf2.parse(endTime).getTime();
        List<Member> sonList = repository.findMembersByFatherId(memberId, end);
        int newSonShopNum = 0;
        if (sonList != null) {
            for (Member m : sonList) {
                List<Shop> shops = shopRepository.findAllByMemberId(m.getId(), start, end);
                newSonShopNum += shops == null ? 0 : shops.size();
            }
        }
        return newSonShopNum;
    }

    private void getAchievementDetail(AchievementDetail achievementDetail, long start, long end, List<Member> sonList) {
        if (sonList != null) {
            achievementDetail.setTotalAllyNum(sonList.size());
            int sonShopNum = 0;
            int newSonShopNum = 0;
            double totalTransactionAmount = 0;
            for (Member m : sonList) {
                List<Shop> shops = shopRepository.findAllByMemberId(m.getId(), 0, end);
                sonShopNum += shops == null ? 0 : shops.size();
                shops = shopRepository.findAllByMemberId(m.getId(), start, end);
                newSonShopNum += shops == null ? 0 : shops.size();
                Map<String, Double> resultMap = memberProfitRecordsRepository.staticProfitsByMonth(m.getId(), start, end);
                totalTransactionAmount += resultMap.get("totalTransactionAmount") == null ? 0d : resultMap.get("totalTransactionAmount");
            }
            achievementDetail.setTransactionAmount(new BigDecimal(totalTransactionAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            achievementDetail.setTotalAllyShopNum(sonShopNum);
            achievementDetail.setNewAllyShopNum(newSonShopNum);
        }
    }

    @Override
    public Integer batchImport(String fileName, MultipartFile file) throws Exception {
        List<ActiveRule> activeRules = activeRuleService.findAll(new HashMap<>());
        if (CollectionUtils.isEmpty(activeRules) || activeRules.get(0).getConditionValue() == null) {
            throw new BusinessException("设备激活奖励未设置");
        }
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
            if (shop == null) {
                throw new BusinessException(String.format("第" + r + "行数据机不合法,SN对应商户不存在SN为:[%s]", importProfit.getSn()));
            }
            Member member = memberService.findOne(shop.getMemberId());
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
            importProfit.setProfit(new BigDecimal(profitRate * importProfit.getTransactionAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            memberProfitRecordsList.add(importProfit);
            // 如果父节点不为空，设置父节点的收益
            if (StringUtils.isNotBlank(member.getFatherId())) {
                setParamProfitRecords(memberProfitRecordsList, importProfit, member, profitRate);
            }
            // 设置激活奖励
            shop.setTransactionAmount(new BigDecimal(shop.getTransactionAmount() == null ? 0 : shop.getTransactionAmount() + importProfit.getTransactionAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            // 设置激活奖励
            setActiveRewardProfit(activeRules, shop);
        }
        // 将数据都插入到数据库中
        for (MemberProfitRecords t : memberProfitRecordsList) {
            save(t);
        }
        importSize = memberProfitRecordsList.size();
        return importSize;
    }

    private void setActiveRewardProfit(List<ActiveRule> activeRules, Shop shop) throws Exception {
        if ((null == shop.getStatus() || shop.getStatus().equals(Shop.Status.UN_ACTIVE)) && shop.getTransactionAmount() >= activeRules.get(0).getConditionValue()) {
            shop.setStatus(Shop.Status.ACTIVE);
            Long rewardCount = shopRepository.count(
                    (Root<Shop> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
                        List<Predicate> predicate = new ArrayList<>();
                        predicate.add(criteriaBuilder.equal(root.get("memberId"), shop.getMemberId()));
                        predicate.add(criteriaBuilder.equal(root.get("activationReward"), Constant.ACTIVATION_REWARD_YES));
                        return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
                    });
            if (rewardCount == 0) {
                shop.setActivationReward(Constant.ACTIVATION_REWARD_YES);
                MemberProfitRecords activationRewardProfit = new MemberProfitRecords();
                activationRewardProfit.setProfitType(Constant.PROFIT_TYPE_FANXIAN);
                activationRewardProfit.setProfit(activeRules.get(0).getAwardMoney());
                activationRewardProfit.setMemberId(shop.getMemberId());
                activationRewardProfit.setTransactionDate(new Date());
                save(activationRewardProfit);
            }
        }
        shopRepository.save(shop);
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
            fatherProfit.setSn(importProfit.getSn());
            fatherProfit.setTransactionDate(importProfit.getTransactionDate());
            fatherProfit.setProfitType(Constant.PROFIT_TYPE_GUANLI);
            fatherProfit.setMemberId(fatherMember.getId());
            MemberLevelParam fatherMemberParam = memberLevelParamService.getParamByLevel(fatherMember.getMemberLevel());
            fatherProfit.setProfit(new BigDecimal((fatherMemberParam.getmPosProfit() - profitRate) * importProfit.getTransactionAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
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
     * @param row 行
     * @return 收益数据
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
        if (transactionAmount == 0) {
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
        importProfit.setProfitType(Constant.PROFIT_TYPE_ZHIYING);
        return importProfit;
    }

    @Lazy
    @Autowired
    public MemberProfitRecordsServiceImpl(
            MemberRepository repository,
            MemberService memberService,
            MemberLevelParamService memberLevelParamService,
            ShopRepository shopRepository, GroupBuildDrawRuleService groupBuildDrawRuleService, ActiveRuleService activeRuleService, MemberProfitRecordsRepository memberProfitRecordsRepository) {
        this.repository = repository;
        this.memberService = memberService;
        this.memberLevelParamService = memberLevelParamService;
        this.shopRepository = shopRepository;
        this.groupBuildDrawRuleService = groupBuildDrawRuleService;
        this.activeRuleService = activeRuleService;
        this.memberProfitRecordsRepository = memberProfitRecordsRepository;
    }
}
