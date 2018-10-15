package com.framework.module.sn.service;

import com.framework.module.auth.MemberThread;
import com.framework.module.member.domain.Member;
import com.framework.module.member.service.MemberService;
import com.framework.module.sn.domain.SnInfo;
import com.framework.module.sn.domain.SnInfoHistory;
import com.framework.module.sn.domain.SnInfoRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageResult;
import com.kratos.exceptions.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.InputStream;
import java.util.*;

@Component
@Transactional
public class SnInfoServiceImpl extends AbstractCrudService<SnInfo> implements SnInfoService {

    private final SnInfoRepository snInfoRepository;
    private final SnInfoHistoryService snInfoHistoryService;
    private final MemberService memberService;


    public SnInfoServiceImpl(SnInfoRepository snInfoRepository, SnInfoHistoryService snInfoHistoryService, MemberService memberService) {
        this.snInfoRepository = snInfoRepository;
        this.snInfoHistoryService = snInfoHistoryService;
        this.memberService = memberService;
    }


    @Override
    public int batchImport(String fileName, MultipartFile profitFile) throws Exception {
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new BusinessException("输入文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = profitFile.getInputStream();
        Workbook wb;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        int importSize = 0;
        Sheet sheet = wb.getSheetAt(0);
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String sn = row.getCell(0).getStringCellValue();
            if (StringUtils.isBlank(sn)) {
                continue;
            }
            if (snInfoRepository.countAllBySn(sn) > 0) {
                continue;
            }
            SnInfo snInfo = new SnInfo();
            snInfo.setSn(sn);
            save(snInfo);
            importSize++;

            SnInfoHistory snInfoHistory = new SnInfoHistory();
            snInfoHistory.setSn(sn);
            snInfoHistoryService.save(snInfoHistory);
        }
        return importSize;
    }

    @Override
    public void transSnByAdmin(String sns, String memberId) throws Exception {
        String[] snArray = sns.split(",");
        if (snArray.length == 0) {
            throw new BusinessException("sn信息填写的不正确");
        }
//        if (snInfoRepository.countAllByMemberId(memberId) < 5 && snArray.length < 5) {
//            throw new BusinessException("首次划分不得少于5个");
//        }
        Member receiveMember = memberService.findOne(memberId);
        for (int i = 0; i < snArray.length; i++) {
            SnInfo snInfo = snInfoRepository.findFirstBySn(snArray[i]);
            if (StringUtils.isNotBlank(snInfo.getShopId())) {
                throw new BusinessException("【" + snArray[i] + "】已经绑定，不能划拨给其他合伙人");
            }
//            if (StringUtils.isNotBlank(snInfo.getMemberId())) {
//                if (snInfo.getMemberId().equals(memberId)) {
//                    continue;
//                } else {
//                    Member member = memberService.findOne(snInfo.getMemberId());
//                    throw new BusinessException(snArray[i] + "已经划拨给会员【" + member.getName() + "】");
//                }
//            }
            snInfo.setMemberId(memberId);
            snInfo.setMemberName(receiveMember.getName());
            snInfo.setMemberMobile(receiveMember.getMobile());
            snInfo.setTransDate(new Date());
            save(snInfo);

            SnInfoHistory snInfoHistory = new SnInfoHistory();
            snInfoHistory.setSn(snInfo.getSn());
            snInfoHistory.setMemberId(snInfo.getMemberId());
            snInfoHistory.setMemberName(receiveMember.getName());
            snInfoHistory.setMemberMobile(receiveMember.getMobile());
            snInfoHistory.setTransDate(snInfo.getTransDate());
            snInfoHistoryService.save(snInfoHistory);

        }
    }

    @Override
    public void transSnByMember(String sns, String memberId, String currentMemberId) throws Exception {
        String[] snArray = sns.split(",");
        if (snArray.length == 0) {
            throw new BusinessException("sn信息填写的不正确");
        }
//        if (snInfoRepository.countAllByMemberId(memberId) < 5 && snArray.length < 5) {
//            throw new BusinessException("首次划分不得少于5个");
//        }
        Member receiveMember = memberService.findOne(memberId);
        Member transMember = memberService.findOne(currentMemberId);
        for (int i = 0; i < snArray.length; i++) {
            SnInfo snInfo = snInfoRepository.findFirstBySn(snArray[i]);
            if (!currentMemberId.equals(snInfo.getMemberId())) {
                throw new BusinessException(snArray[i] + "不属于您，不能划拨");
            }
            if (StringUtils.isNotBlank(snInfo.getShopId())) {
                throw new BusinessException("【" + snArray[i] + "】已经绑定，不能划拨给其他合伙人");
            }
            snInfo.setMemberId(memberId);
            snInfo.setMemberName(receiveMember.getName());
            snInfo.setMemberMobile(receiveMember.getMobile());
            snInfo.setTransMemberId(transMember.getId());
            snInfo.setTransMemberName(transMember.getName());
            snInfo.setTransMemberMobile(transMember.getMobile());
            snInfo.setTransDate(new Date());
            save(snInfo);

            SnInfoHistory snInfoHistory = new SnInfoHistory();
            snInfoHistory.setSn(snInfo.getSn());
            snInfoHistory.setMemberId(snInfo.getMemberId());
            snInfoHistory.setMemberName(receiveMember.getName());
            snInfoHistory.setMemberMobile(receiveMember.getMobile());
            snInfoHistory.setTransMemberId(transMember.getId());
            snInfoHistory.setTransMemberName(transMember.getName());
            snInfoHistory.setTransMemberMobile(transMember.getMobile());
            snInfoHistory.setTransDate(snInfo.getTransDate());
            snInfoHistoryService.save(snInfoHistory);

        }
    }

    @Override
    public List<String> getAvailableSn(String searchSn) {
        String memberId = MemberThread.getInstance().get().getId();
        if (StringUtils.isNotBlank(searchSn)) {
            return snInfoRepository.getAvailableSnByMemberIdAndSearchInfo(memberId, searchSn);
        }
        return snInfoRepository.getAvailableSnByMemberId(memberId);
    }

    @Override
    public PageResult<SnInfo> getAllSnInfo(String startSn, String endSn, SnInfo.Status status, SnInfo.BindStatus bindStatus, Integer pageSize, Integer pageNum, String memberId) throws Exception {
        if (startSn == null && endSn != null) {
            throw new BusinessException("请填写起始SN");
        }
        if (startSn != null && endSn == null) {
            throw new BusinessException("请填写终止SN");
        }
        Map<String, String[]> param = new HashMap<>();
        if (StringUtils.isNotBlank(startSn) && StringUtils.isNotBlank(endSn)) {
            param.put("startSn", new String[]{startSn});
            param.put("endSn", new String[]{endSn});
        }
        if (status != null) {
            param.put("status", new String[]{status.toString()});
        }
        if (bindStatus != null) {
            param.put("bindStatus", new String[]{bindStatus.toString()});
        }
        if (StringUtils.isNotBlank(memberId)) {
            param.put("memberId", new String[]{memberId});
        }
        param.put("pageSize", new String[]{pageSize.toString()});
        param.put("pageNum", new String[]{pageNum.toString()});
        PageRequest pageRequest = new PageRequest(Integer.valueOf(param.get("pageNum")[0]) - 1, Integer.valueOf(param.get("pageSize")[0]), Sort.Direction.ASC, "sn");
        return findAll(pageRequest, param);
    }

//    @Override
//    public List<SnInfo> getUnDistributionList(String memberId) {
//        return snInfoRepository.findAllByMemberIdAndShopIdNull(memberId);
//    }

    @Override
    public PageResult<SnInfo> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        Page<SnInfo> all = pageRepository.findAll(new MySpecification(param, true), pageRequest);
        PageResult<SnInfo> pageResult = new PageResult<>();
        pageResult.setSize(all.getSize());
        pageResult.setTotalElements(all.getTotalElements());
        pageResult.setContent(all.getContent());
        return pageResult;
    }

    class MySpecification extends SimpleSpecification {
        MySpecification(Map<String, String[]> params, Boolean allEntities) {
            super(params, allEntities);
        }

        @Override
        public Predicate toPredicate(Root<SnInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            Predicate predicate = super.toPredicate(root, criteriaQuery, criteriaBuilder);
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("startSn") && params.containsKey("endSn")) {
                String[] startSnValue = params.get("startSn");
                String[] endSnValue = params.get("endSn");
                predicates.add(criteriaBuilder.between(root.get("sn"), startSnValue[0], endSnValue[0]));
            }
            if (params.containsKey("status")) {
                String[] statusValue = params.get("status");
                if (SnInfo.Status.DISTRIBUTION.toString().equals(statusValue[0])) {
                    predicates.add(criteriaBuilder.isNotNull(root.get("memberId")));
                } else {
                    predicates.add(criteriaBuilder.isNull(root.get("memberId")));
                }
            }
            if (params.containsKey("bindStatus")) {
                String[] bindStatusValue = params.get("bindStatus");
                if (SnInfo.BindStatus.BIND.toString().equals(bindStatusValue[0])) {
                    predicates.add(criteriaBuilder.isNotNull(root.get("shopId")));
                } else {
                    predicates.add(criteriaBuilder.isNull(root.get("shopId")));
                }
            }
            if (params.containsKey("memberId")) {
                String[] memberId = params.get("memberId");
                predicates.add(criteriaBuilder.equal(root.get("memberId"), memberId[0]));
            }

            if (predicates.size() != 0) {
                Predicate predicateTemp = criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
                predicates.clear();
                predicates.add(predicateTemp);
            }
            predicates.add(predicate);
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        }
    }


}
