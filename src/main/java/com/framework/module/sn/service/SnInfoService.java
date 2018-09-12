package com.framework.module.sn.service;

import com.framework.module.sn.domain.SnInfo;
import com.kratos.common.CrudService;
import com.kratos.exceptions.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SnInfoService extends CrudService<SnInfo> {
    /**
     * 批量上传SN
     * @param fileName 文件名称
     * @param profitFile 文件
     * @return 上传文件数量
     */
    int batchImport(String fileName, MultipartFile profitFile) throws Exception;

    /**
     * 管理员划拨SN，使用逗号分隔
     * @param sns
     * @param memberId
     */
    void transSnByAdmin(String sns, String memberId) throws Exception;

    /**
     * 会员划拨SN，使用逗号分隔
     * @param sns
     * @param memberId
     */
    void transSnByMember(String sns, String memberId,String currentMemberId) throws Exception;

    /**
     * 获取可分配的SN列表
     * @return SN列表
     */
    List<String> getAvailableSn();

    /**
     * 查询SN信息
     * @param startSn
     * @param endSn
     * @param status
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<SnInfo> getAllSnInfo(String startSn, String endSn, SnInfo.Status status, Integer pageSize, Integer pageNum) throws Exception;

    List<SnInfo> getUnDistributionList(String memberId);
}
