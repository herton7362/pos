package com.framework.module.activity.domain;

import com.kratos.entity.BaseEntity;
import com.kratos.module.attachment.domain.Attachment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("联盟学院")
public class AllianceCollege extends BaseEntity {
    @ApiModelProperty(value = "pdf")
    @ManyToOne
    private Attachment pdf;
    @ApiModelProperty(value = "备注")
    @Column(length = 100)
    private String remark;

    public Attachment getPdf() {
        return pdf;
    }

    public void setPdf(Attachment pdf) {
        this.pdf = pdf;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
