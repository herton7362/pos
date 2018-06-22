package com.framework.module.activity.domain;

import com.kratos.entity.BaseEntity;
import com.kratos.module.attachment.domain.Attachment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("活动图片")
public class ActivityImg extends BaseEntity {
    @ApiModelProperty(value = "图片")
    @ManyToOne
    private Attachment image;
    @ApiModelProperty(value = "备注")
    @Column(length = 100)
    private String remark;

    public Attachment getImage() {
        return image;
    }

    public void setImage(Attachment image) {
        this.image = image;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
