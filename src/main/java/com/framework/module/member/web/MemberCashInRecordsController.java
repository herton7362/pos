package com.framework.module.member.web;

import com.framework.module.member.domain.MemberCashInRecords;
import com.framework.module.payment.domain.PayHistory;
import com.framework.module.payment.domain.PayHistoryRepository;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.PageParam;
import com.kratos.common.PageResult;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("提现记录")
@RestController
@RequestMapping("/api/memberCashInRecords")
public class MemberCashInRecordsController extends AbstractCrudController<MemberCashInRecords> {

    private final PayHistoryRepository payHistoryRepository;

    public MemberCashInRecordsController(PayHistoryRepository payHistoryRepository) {
        this.payHistoryRepository = payHistoryRepository;
    }

    @Override
    public ResponseEntity<?> searchPagedList(@ModelAttribute PageParam pageParam, HttpServletRequest request) throws Exception {
        ResponseEntity<?> result = super.searchPagedList(pageParam, request);
        PageResult<MemberCashInRecords> page = (PageResult<MemberCashInRecords>) result.getBody();
        List<MemberCashInRecords> list = page.getContent();
        for (MemberCashInRecords t : list) {
            PayHistory payHistory = payHistoryRepository.findFirstByCashInId(t.getId());
            if (payHistory != null) {
                t.setPayOrderState(payHistory.getOrderState());
                t.setPayResultCode(payHistory.getResultCode());
                t.setPayResultDes(payHistory.getResultDes());
            }
        }

        return result;
    }
}
