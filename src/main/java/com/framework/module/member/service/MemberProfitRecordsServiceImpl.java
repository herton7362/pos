package com.framework.module.member.service;

import com.framework.module.common.Constant;
import com.framework.module.member.domain.*;
import com.framework.module.rule.domain.ActiveRule;
import com.framework.module.rule.domain.GroupBuildDrawRule;
import com.framework.module.rule.service.ActiveRuleService;
import com.framework.module.rule.service.GroupBuildDrawRuleService;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.domain.ShopRepository;
import com.framework.module.sn.domain.SnInfo;
import com.framework.module.sn.domain.SnInfoHistory;
import com.framework.module.sn.domain.SnInfoRepository;
import com.framework.module.sn.service.SnInfoService;
import com.kratos.common.AbstractCrudService;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.dictionary.domain.Dictionary;
import com.kratos.module.dictionary.domain.DictionaryCategory;
import com.kratos.module.dictionary.service.DictionaryCategoryService;
import com.kratos.module.dictionary.service.DictionaryService;
import com.kratos.module.dictionary.web.DictionaryController;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    private final DictionaryService dictionaryService;
    private final DictionaryCategoryService dictionaryCategoryService;
    private final MemberCashInRecordsService memberCashInRecordsService;
    private final MemberProfitTmpRecordsRepository memberProfitTmpRecordsRepository;
    private final SnInfoRepository snInfoRepository;

    private final Logger logger = Logger.getLogger(MemberProfitRecordsServiceImpl.class);

    @Override
    public void setTeamBuildProfit(String fatherId) throws Exception {
        Long activeCount = repository.count(
                (Root<Member> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
                    List<Predicate> predicate = new ArrayList<>();
                    predicate.add(criteriaBuilder.equal(root.get("fatherId"), fatherId));
                    predicate.add(criteriaBuilder.equal(root.get("status"), Member.Status.ACTIVE));
                    return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
                });
        Map<String, String[]> param = new HashMap<>();
        String[] nameArray = {String.valueOf(activeCount)};
        param.put("memberCount", nameArray);
        List<GroupBuildDrawRule> groupBuildDrawRules = groupBuildDrawRuleService.findAll(param);
        if (CollectionUtils.isEmpty(groupBuildDrawRules) || groupBuildDrawRules.get(0).getMemberCount() == null || groupBuildDrawRules.get(0).getReward() == null) {
            return;
        }
        MemberProfitRecords teamBuilderProfit = new MemberProfitRecords();
        teamBuilderProfit.setProfitType(Constant.PROFIT_TYPE_TUANJIAN);
        teamBuilderProfit.setProfit(groupBuildDrawRules.get(0).getReward());
        teamBuilderProfit.setMemberId(fatherId);
        teamBuilderProfit.setTransactionDate(new Date().getTime());
        save(teamBuilderProfit);
    }

    @Override
    public List<ProfitMonthDetail> getProfitByMonth(String memberId, String startMonth, int size) throws Exception {
        Member member = memberService.findOne(memberId);
        if (member == null) {
            throw new BusinessException(String.format("会员id:[%s]不存在", memberId));
        }
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
            if (member.getCreatedDate() > end) {
                break;
            }
            Map<String, Double> resultMap = memberProfitRecordsRepository.staticProfitsByMonth(memberId, start, end);
            ProfitMonthDetail profitMonthDetail = new ProfitMonthDetail();
            profitMonthDetail.setTotalProfit(setDouleScale(resultMap.get("totalProfit") == null ? 0 : resultMap.get("totalProfit")));
            profitMonthDetail.setActiveAward(setDouleScale(resultMap.get("activeAward") == null ? 0 : resultMap.get("activeAward")));
            profitMonthDetail.setDirectlyAward(setDouleScale(resultMap.get("directlyAward") == null ? 0 : resultMap.get("directlyAward")));
            profitMonthDetail.setManagerAward(setDouleScale(resultMap.get("managerAward") == null ? 0 : resultMap.get("managerAward")));
            profitMonthDetail.setTeamBuildAward(setDouleScale(resultMap.get("teamBuildAward") == null ? 0 : resultMap.get("teamBuildAward")));
            profitMonthDetail.setTotalTransactionAmount(setDouleScale(resultMap.get("totalTransactionAmount") == null ? 0 : resultMap.get("totalTransactionAmount")));
            profitMonthDetail.setMonth(sdf.format(calendar.getTime()));

            resultMap = memberProfitRecordsRepository.staticDirectAwardByMonth(memberId, start, end);
            profitMonthDetail.setProfitMiaoDao(setDouleScale(resultMap.get("profit1") == null ? 0 : resultMap.get("profit1")));
            profitMonthDetail.setProfitKuaiJie(setDouleScale(resultMap.get("profit2") == null ? 0 : resultMap.get("profit2")));
            profitMonthDetail.setProfitSaoMa(setDouleScale(resultMap.get("profit3") == null ? 0 : resultMap.get("profit3")));
            profitMonthDetail.setProfitDaiHuan(setDouleScale(resultMap.get("profit4") == null ? 0 : resultMap.get("profit4")));
            profitMonthDetail.setTransactionAmountMiaoDao(setDouleScale(resultMap.get("transaction1") == null ? 0 : resultMap.get("transaction1")));
            profitMonthDetail.setTransactionAmountKuaiJie(setDouleScale(resultMap.get("transaction2") == null ? 0 : resultMap.get("transaction2")));
            profitMonthDetail.setTransactionAmountSaoMa(setDouleScale(resultMap.get("transaction3") == null ? 0 : resultMap.get("transaction3")));
            profitMonthDetail.setTransactionAmountDaiHuan(setDouleScale(resultMap.get("transaction4") == null ? 0 : resultMap.get("transaction4")));
            result.add(profitMonthDetail);
        }
        return result;
    }

    @Override
    public List<AchievementDetail> getAchievementByMonth(String memberId, String startMonth, int size) throws Exception {
        Member member = memberService.findOne(memberId);
        if (member == null) {
            throw new BusinessException(String.format("会员id:[%s]不存在", memberId));
        }

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
            if (member.getCreatedDate() > end) {
                break;
            }
//            List<Member> sonList = repository.findMembersByFatherId(memberId, end);
            List<String> sonList = new ArrayList<>();
            AllyMembers allyMembers = memberService.getAlliesByMemberId(memberId, end);
            if (allyMembers != null) {
                sonList.addAll(allyMembers.getSonList());
                sonList.addAll(allyMembers.getGrandSonList());
            }
            getAchievementDetail(achievementDetail, start, end, sonList);
            achievementDetail.setStaticDate(sdf.format(calendar.getTime()));
            result.add(achievementDetail);
        }
        return result;
    }

    @Override
    public List<AchievementDetail> getAchievementByDate(String memberId, String date, int size) throws Exception {
        Member member = memberService.findOne(memberId);
        if (member == null) {
            throw new BusinessException(String.format("会员id:[%s]不存在", memberId));
        }
        List<AchievementDetail> result = new ArrayList<>(size);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        for (int i = 0; i < size; i++) {
//            Long startOperater = System.currentTimeMillis();
//            logger.info("start--------------"+i);
            AchievementDetail achievementDetail = new AchievementDetail();
            calendar.setTime(sdf.parse(date));
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            LocalDate staticDate = ZonedDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalDate();
            if (!staticDate.isBefore(currentDate)) {
                continue;
            }
            String startTime = sdf.format(calendar.getTime()) + " 00:00:00";
            String endTime = sdf.format(calendar.getTime()) + " 23:59:59";
            long start = sdf2.parse(startTime).getTime();
            long end = sdf2.parse(endTime).getTime();
            if (member.getCreatedDate() > end) {
                break;
            }
//            Long endTimeOperate = System.currentTimeMillis();
//            logger.info("end caculate time" + (endTimeOperate - startOperater));
            List<String> sonList = new ArrayList<>();
            AllyMembers allyMembers = memberService.getAlliesByMemberId(memberId, end);
            if (allyMembers != null) {
                sonList.addAll(allyMembers.getSonList());
                sonList.addAll(allyMembers.getGrandSonList());
            }
//            Long endMemberOperate = System.currentTimeMillis();
//            logger.info("end get member time" + (endMemberOperate - endTimeOperate));

            getAchievementDetail(achievementDetail, start, end, sonList);
//            Long endStaticOperate = System.currentTimeMillis();
//            logger.info("end get static time" + (endStaticOperate - endMemberOperate));
            achievementDetail.setStaticDate(sdf.format(calendar.getTime()));
            result.add(achievementDetail);
//            logger.info("end--------------"+i);
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
            List<String> sonList = new ArrayList<>();
            AllyMembers allyMembers = memberService.getAlliesByMemberId(memberId, end);
            if (allyMembers != null) {
                sonList.addAll(allyMembers.getSonList());
                sonList.addAll(allyMembers.getGrandSonList());
            }
            int newSonShopNum = 0;
            Double totalTransactionAmount = 0d;
            for (String m : sonList) {
                List<Shop> shops = shopRepository.findAllByMemberId(m, start, end);
                newSonShopNum += shops == null ? 0 : shops.size();
                Map<String, Double> resultMap = memberProfitRecordsRepository.staticProfitsByMonth(m, start, end);
                totalTransactionAmount += resultMap.get("totalTransactionAmount") == null ? 0d : resultMap.get("totalTransactionAmount");
            }
            totalTransactionAmount = new BigDecimal(totalTransactionAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            achievement.setAllyNewShopNum(newSonShopNum);
            achievement.setAllyTransactionAmount(new BigDecimal(totalTransactionAmount.toString()).toString());

            List<Shop> shops1 = shopRepository.findAllByMemberId(memberId, start, end);
            Map<String, Double> resultMap1 = memberProfitRecordsRepository.staticProfitsByMonth(memberId, start, end);
            Double totalTransactionAmount1 = resultMap1.get("totalTransactionAmount") == null ? 0d : resultMap1.get("totalTransactionAmount");
            achievement.setNewShopNum(shops1.size());
            achievement.setTransactionAmount(new BigDecimal(totalTransactionAmount1).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
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
        List<String> sonList = new ArrayList<>();
        AllyMembers allyMembers = memberService.getAlliesByMemberId(memberId, end);
        if (allyMembers != null) {
            sonList.addAll(allyMembers.getSonList());
            sonList.addAll(allyMembers.getGrandSonList());
        }
        int newSonShopNum = 0;
        for (String m : sonList) {
            List<Shop> shops = shopRepository.findAllByMemberId(m, start, end);
            newSonShopNum += shops == null ? 0 : shops.size();
        }
        return newSonShopNum;
    }

    @Override
    public String getTotalProfit(String memberId) {
        Map<String, Double> resultMap = memberProfitRecordsRepository.staticTotalProfit(memberId);
        return setDouleScale(resultMap.get("totalProfit") == null ? 0d : resultMap.get("totalProfit"));
    }

    private void getAchievementDetail(AchievementDetail achievementDetail, long start, long end, List<String> sonList) {
        if (sonList != null) {
            achievementDetail.setTotalAllyNum(sonList.size());
            int sonShopNum = 0;
            int newSonShopNum = 0;
            double totalTransactionAmount = 0;
            for (String m : sonList) {
                Integer shops = shopRepository.countAllByMemberId(m, 0, end);
                sonShopNum += shops == null ? 0 : shops;
                shops = shopRepository.countAllByMemberId(m, start, end);
                newSonShopNum += shops == null ? 0 : shops;
                Map<String, Double> resultMap = memberProfitRecordsRepository.staticProfitsByTime(m, start, end);
                totalTransactionAmount += resultMap.get("totalTransactionAmount") == null ? 0d : resultMap.get("totalTransactionAmount");
            }
            achievementDetail.setTransactionAmount(new BigDecimal(totalTransactionAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            achievementDetail.setTotalAllyShopNum(sonShopNum);
            achievementDetail.setNewAllyShopNum(newSonShopNum);
        }
    }

    @Override
    public Integer batchImport(String fileName, MultipartFile file) throws Exception {
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new BusinessException("输入文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        List<MemberProfitTmpRecords> memberProfitTmpRecordsList = new ArrayList<>();
        Integer importSize = sheet.getLastRowNum();
        String timestamp = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
        String operateTransactionId = timestamp + RandomStringUtils.randomNumeric(8);
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            String relateId = RandomStringUtils.randomNumeric(8);
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            if (isCellBlack(row.getCell(0)) && isCellBlack(row.getCell(1)) && isCellBlack(row.getCell(2)) && isCellBlack(row.getCell(3)) && isCellBlack(row.getCell(4)) && isCellBlack(row.getCell(5)) && isCellBlack(row.getCell(6))) {
                importSize--;
                continue;
            }

            MemberProfitTmpRecords importProfit = getAllParamFromExcel(row, r);
            importProfit.setOperateTransactionId(operateTransactionId);
            importProfit.setRelateId(relateId);
            Shop shop = shopRepository.findOneBySn(importProfit.getSn());
            if (shop == null) {
                SnInfo snInfo = snInfoRepository.findFirstBySn(importProfit.getSn());
                if (snInfo != null && StringUtils.isNotBlank(snInfo.getMemberId())) {
                    shop = new Shop();
                    shop.setMemberId(snInfo.getMemberId());
                    shop.setSn(importProfit.getSn());
                    shop.setName(importProfit.getUserName());
                    shopRepository.save(shop);
                }else {
                    throw new BusinessException(String.format("第" + r + "行数据不合法,SN对应商户不存在。SN为:[%s]", importProfit.getSn()));
                }
            }
            if (StringUtils.isBlank(shop.getMemberId())) {
                throw new BusinessException(String.format("第" + r + "行数据不合法,SN对应商户会员信息不存在。SN为:[%s]", importProfit.getSn()));
            }
            Member member = memberService.findOne(shop.getMemberId());
            if (member == null) {
                throw new BusinessException(String.format("第" + r + "行数据不合法,用户编号不存在:[%s]", importProfit.getUserNo()));
            }
            importProfit.setMemberId(member.getId());
            if (member.getMemberLevel() == null) {
                throw new BusinessException(String.format("第" + r + "行数据不合法,用户等级信息不存在:[%s]", importProfit.getUserNo()));
            }
            MemberLevelParam param = memberLevelParamService.getParamByLevel(String.valueOf(member.getMemberLevel()));
            if (param == null) {
                throw new BusinessException(String.format("第" + r + "行数据不合法,用户等级信息不合法:[%s]", importProfit.getUserNo()));
            }
            double profitRate = param.getmPosProfit() / 10000;
            importProfit.setProfit(new BigDecimal(profitRate * importProfit.getTransactionAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            memberProfitTmpRecordsList.add(importProfit);
            // 如果父节点不为空，设置父节点的收益
            if (StringUtils.isNotBlank(member.getFatherId())) {
                setParamProfitRecords(memberProfitTmpRecordsList, importProfit, member, profitRate, operateTransactionId, relateId);
            }
        }
        // 将数据都插入到数据库中
        memberProfitTmpRecordsRepository.save(memberProfitTmpRecordsList);
        return importSize;
    }

    /**
     * 设置激活奖励
     *
     * @param activeRules         激活规则
     * @param shop                商户实体
     * @param operatTransactionId 操作流水号
     * @param transactionDate     操作日期
     * @throws Exception 异常
     */
    private void setActiveRewardProfit(List<ActiveRule> activeRules, Shop shop, String operatTransactionId, Long transactionDate) throws Exception {
        if ((null == shop.getStatus() || shop.getStatus().equals(Shop.Status.UN_ACTIVE)) && shop.getTransactionAmount() >= activeRules.get(0).getConditionValue()) {
            shop.setStatus(Shop.Status.ACTIVE);
            Long rewardCount = shopRepository.count(
                    (Root<Shop> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
                        List<Predicate> predicate = new ArrayList<>();
                        predicate.add(criteriaBuilder.equal(root.get("mobile"), shop.getMobile()));
                        predicate.add(criteriaBuilder.equal(root.get("activationReward"), Constant.ACTIVATION_REWARD_YES));
                        return criteriaBuilder.and(predicate.toArray(new Predicate[]{}));
                    });
            if (rewardCount == 0) {
                shop.setActivationReward(Constant.ACTIVATION_REWARD_YES);
                MemberProfitRecords activationRewardProfit = new MemberProfitRecords();
                activationRewardProfit.setOperateTransactionId(operatTransactionId);
                activationRewardProfit.setProfitType(Constant.PROFIT_TYPE_FANXIAN);
                activationRewardProfit.setProfit(activeRules.get(0).getAwardMoney());
                activationRewardProfit.setMemberId(shop.getMemberId());
                activationRewardProfit.setTransactionDate(transactionDate);
                save(activationRewardProfit);
            }
        }
    }

    /**
     * 设置上级的收益情况
     *
     * @param memberProfitTmpRecordsList 入库的list
     * @param importProfit               当前用户的收益情况
     * @param member                     当前用户
     * @param profitRate                 当前用户的收益
     * @param operateTransactionId       操作流水号
     * @param relateId                   关联号码
     * @throws Exception 异常
     */
    private void setParamProfitRecords(List<MemberProfitTmpRecords> memberProfitTmpRecordsList, MemberProfitTmpRecords importProfit, Member member, double profitRate, String operateTransactionId, String relateId) throws Exception {
        Member fatherMember = repository.findOne(member.getFatherId());
        while (fatherMember != null) {
            MemberProfitTmpRecords fatherProfit = new MemberProfitTmpRecords();
            fatherProfit.setOperateTransactionId(operateTransactionId);
            fatherProfit.setRelateId(relateId);
            fatherProfit.setTransactionDate(importProfit.getTransactionDate());
            fatherProfit.setProfitType(Constant.PROFIT_TYPE_GUANLI);
            fatherProfit.setMemberId(fatherMember.getId());
            MemberLevelParam fatherMemberParam = memberLevelParamService.getParamByLevel(String.valueOf(fatherMember.getMemberLevel()));
            double fatherProfitRate = fatherMemberParam.getmPosProfit() / 10000 - profitRate;
            fatherProfitRate = fatherProfitRate < 0 ? 0 : fatherProfitRate;
            fatherProfit.setProfit(new BigDecimal(fatherProfitRate * importProfit.getTransactionAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            memberProfitTmpRecordsList.add(fatherProfit);
            if (StringUtils.isBlank(fatherMember.getFatherId())) {
                fatherMember = null;
            } else {
                fatherMember = repository.findOne(fatherMember.getFatherId());
            }
        }
    }

    @Override
    public void membersIncreaseLevel() throws Exception {
        Iterable<Member> allMembers = repository.findAllOrderByCreatedDate();
        int s = 0;
        while (s + 1 != ((List<Member>) allMembers).size()) {
            Member member = ((List<Member>) allMembers).get(s);
            Integer memberLevel = member.getMemberLevel() == null ? 1 : member.getMemberLevel();
            // 获得下一级别的升级要求
            MemberLevelParam memberLevelParam = memberLevelParamService.getParamByLevel(String.valueOf(memberLevel + 1));
            List<Member> sons = repository.findMemberInfosByFatherId(member.getId(), new Date().getTime());
            Map<String, Double> transactionAmount = shopRepository.staticTotalTransaction(member.getId());
            double totalTransactionAmount = transactionAmount.get("totalTransactionAmount") == null ? 0 : transactionAmount.get("totalTransactionAmount");
            double transAmountYuan = memberLevelParam.getTotalTransactionVolume() * 10000;
            if (totalTransactionAmount < transAmountYuan) {
                s++;
                continue;
            }
            Map<String, Integer> sonsLevelNum = new HashMap<>();
            for (Member m : sons) {
                Integer mLevel = m.getMemberLevel() == null ? 1 : m.getMemberLevel();
                sonsLevelNum.putIfAbsent(String.valueOf(mLevel), 0);
                sonsLevelNum.put(String.valueOf(mLevel), (sonsLevelNum.get(String.valueOf(mLevel)) + 1));
            }
            String[] scale = memberLevelParam.getTeamScale().split("\\|");
            boolean scaleRequired = true;
            for (int i = 0; i < scale.length; i++) {
                // 升级需要的级别人数。
                int scaleNum = Integer.valueOf(scale[i]);
                // 当前已经有的人数
                int currentNum = 0;
                if (scaleNum == 0) {
                    continue;
                }
                for (int j = i + 1; j <= 12; j++) {
                    currentNum += (sonsLevelNum.get(String.valueOf(j)) == null ? 0 : sonsLevelNum.get(String.valueOf(j)));
                }
                if (currentNum < scaleNum) {
                    scaleRequired = false;
                    break;
                }
            }

            if (scaleRequired) {
                // 如果用户升级，看一下是否符合下一级别的要求
                member.setMemberLevel(memberLevel + 1);
                repository.save(member);
            } else {
                s++;
            }
        }
    }

    @Override
    public double cashOnAmount(String memberId) throws Exception {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = sdf1.format(calendar.getTime()) + " 23:59:59";
        Date lastDate = sdf2.parse(lastDay);
        Map<String, Double> resultMap = memberProfitRecordsRepository.staticTotalProfitByDate(memberId, lastDate.getTime());
        double tilLastMonthProfit = resultMap.get("totalProfit") == null ? 0d : resultMap.get("totalProfit");
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String thisMonthStart = sdf1.format(calendar.getTime()) + " 00:00:00";
        Date thisMonthStartDate = sdf2.parse(thisMonthStart);

        Map<String, String[]> params = new HashMap<>();
        params.put("code", new String[]{"DailyProfitType"});
        List<DictionaryCategory> dictionaryCategories = dictionaryCategoryService.findAll(params);
        List<Dictionary> dictionaries = new ArrayList<>();
        dictionaries = DictionaryController.getDictionaries(params, dictionaryCategories, dictionaries, dictionaryService);

        List<Integer> profitTypeParam = new ArrayList<>();
        for (Dictionary d : dictionaries) {
            if (!d.getLogicallyDeleted()) {
                profitTypeParam.add(Integer.valueOf(d.getCode()));
            }
        }
        if (CollectionUtils.isEmpty(profitTypeParam)) {
            throw new BusinessException("未配置日结算类型，请联系管理员。");
        }

        resultMap = memberProfitRecordsRepository.staticThisProfit(memberId, thisMonthStartDate.getTime(), profitTypeParam);
        double currentMonthProfit = resultMap.get("totalProfit") == null ? 0d : resultMap.get("totalProfit");

        double cashInAmount = memberCashInRecordsService.getCashInAmount(memberId);

        return new BigDecimal(tilLastMonthProfit + currentMonthProfit - cashInAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public void examineImportProfit(String operateTransactionId, boolean examineResult) throws Exception {
        List<MemberProfitTmpRecords> records = memberProfitTmpRecordsRepository.findAllByOperateTransactionId(operateTransactionId);
        if (CollectionUtils.isEmpty(records)) {
            throw new BusinessException("未找到对应数据的操作流水号" + operateTransactionId);
        }
        if (!examineResult) {
            memberProfitTmpRecordsRepository.delete(records);
            return;
        }
        List<ActiveRule> activeRules = activeRuleService.findAll(new HashMap<>());
        if (CollectionUtils.isEmpty(activeRules) || activeRules.get(0).getConditionValue() == null) {
            throw new BusinessException("设备激活奖励未设置");
        }
        List<MemberProfitRecords> memberProfitRecordsList = new ArrayList<>();
        for (MemberProfitTmpRecords record : records) {
            MemberProfitRecords memberProfitRecord = new MemberProfitRecords();
            BeanUtils.copyProperties(record, memberProfitRecord);
            memberProfitRecord.setId(null);
            memberProfitRecordsList.add(memberProfitRecord);
            if (record.getProfitType() == Constant.PROFIT_TYPE_ZHIYING) {
                // 设置激活奖励
                Shop shop = shopRepository.findOneBySn(record.getSn());
                double transactionAmountInDb = shop.getTransactionAmount() == null ? 0 : shop.getTransactionAmount();
                double addedTransactionAmount = transactionAmountInDb + record.getTransactionAmount();
                shop.setTransactionAmount(new BigDecimal(addedTransactionAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                // 设置激活奖励
                setActiveRewardProfit(activeRules, shop, operateTransactionId, record.getTransactionDate());
                shopRepository.save(shop);
            }
        }
        memberProfitRecordsRepository.save(memberProfitRecordsList);
        memberProfitTmpRecordsRepository.delete(records);
    }

    /**
     * 再Excel中获取数据并校验，校验通过后加入到实体中
     *
     * @param row 行
     * @return 收益数据
     */
    private MemberProfitTmpRecords getAllParamFromExcel(Row row, int r) throws BusinessException {
        if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null || row.getCell(3) == null || row.getCell(4) == null || row.getCell(5) == null || row.getCell(6) == null) {
            throw new BusinessException("导入文档不符合模板要求");
        }
        row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
        String organizationNo = row.getCell(0).getStringCellValue();
        if (StringUtils.isBlank(organizationNo)) {
            throw new BusinessException(String.format("第" + r + "行数据不合法,[%s]为空", "机构编码"));
        }
        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
        String organizationName = row.getCell(1).getStringCellValue();
        if (StringUtils.isBlank(organizationName)) {
            throw new BusinessException(String.format("第" + r + "行数据不合法,[%s]为空", "机构名称"));
        }

        row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
        String userNo = row.getCell(2).getStringCellValue();
        if (StringUtils.isBlank(userNo)) {
            throw new BusinessException(String.format("第" + r + "行数据不合法,[%s]为空", "用户编号"));
        }

        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
        String userName = row.getCell(3).getStringCellValue();
        if (StringUtils.isBlank(userName)) {
            throw new BusinessException(String.format("第" + r + "行数据不合法,[%s]为空", "用户姓名"));
        }
        Double transactionAmount = 0d;
        try {
            if (row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                transactionAmount = row.getCell(4).getNumericCellValue();
            }
            if (row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
                String stringCellValue = row.getCell(4).getStringCellValue();
                if (stringCellValue.indexOf(",") > 0) {
                    stringCellValue = stringCellValue.replace(",", "");
                }
                transactionAmount = Double.valueOf(stringCellValue);
            }
        } catch (Exception e) {
            throw new BusinessException("第" + r + "行数据不合法,文件中交易金额单元格 格式需要为【数值型】并且不能为空");
        }
        if (transactionAmount == 0) {
            throw new BusinessException(String.format("第" + r + "行数据不合法,[%s]数据不合法", "交易金额"));
        }
        row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
        String sn = row.getCell(5).getStringCellValue();
        if (StringUtils.isBlank(sn)) {
            throw new BusinessException(String.format("第" + r + "行数据不合法,[%s]为空", "SN"));
        }
        row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
        String transactionType = row.getCell(6).getStringCellValue();
        if (StringUtils.isBlank(transactionType)) {
            throw new BusinessException(String.format("第" + r + "行数据不合法,[%s]为空", "交易类型"));
        }
        Date transactionDate;
        try {
            transactionDate = row.getCell(7).getDateCellValue();
        } catch (Exception e) {
            throw new BusinessException("第" + r + "行数据不合法,文件中交易日期单元格 格式需要为【日期】并且不能为空");
        }
        if (transactionDate == null) {
            throw new BusinessException(String.format("第" + r + "行数据不合法,[%s]为空", "交易日期"));
        }
        MemberProfitTmpRecords importProfit = new MemberProfitTmpRecords();
        importProfit.setOrganizationNo(organizationNo);
        importProfit.setOrganizationName(organizationName);
        importProfit.setUserNo(userNo);
        importProfit.setUserName(userName);
        importProfit.setTransactionAmount(transactionAmount);
        importProfit.setTransactionType(transactionType);
        importProfit.setSn(sn);
        importProfit.setTransactionDate(transactionDate.getTime());
        importProfit.setProfitType(Constant.PROFIT_TYPE_ZHIYING);
        return importProfit;
    }

    private boolean isCellBlack(Cell cell) {
        return cell == null || StringUtils.isBlank(cell.getStringCellValue());
    }

    private String setDouleScale(double inputDouble) {
        return new BigDecimal(inputDouble).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Lazy
    @Autowired
    public MemberProfitRecordsServiceImpl(
            MemberRepository repository,
            MemberService memberService,
            MemberLevelParamService memberLevelParamService,
            ShopRepository shopRepository, GroupBuildDrawRuleService groupBuildDrawRuleService, ActiveRuleService activeRuleService, MemberProfitRecordsRepository memberProfitRecordsRepository, DictionaryService dictionaryService, DictionaryCategoryService dictionaryCategoryService, MemberCashInRecordsService memberCashInRecordsService, MemberProfitTmpRecordsRepository memberProfitTmpRecordsRepository, SnInfoRepository snInfoRepository) {
        this.repository = repository;
        this.memberService = memberService;
        this.memberLevelParamService = memberLevelParamService;
        this.shopRepository = shopRepository;
        this.groupBuildDrawRuleService = groupBuildDrawRuleService;
        this.activeRuleService = activeRuleService;
        this.memberProfitRecordsRepository = memberProfitRecordsRepository;
        this.dictionaryService = dictionaryService;
        this.dictionaryCategoryService = dictionaryCategoryService;
        this.memberCashInRecordsService = memberCashInRecordsService;
        this.memberProfitTmpRecordsRepository = memberProfitTmpRecordsRepository;
        this.snInfoRepository = snInfoRepository;
    }
}
