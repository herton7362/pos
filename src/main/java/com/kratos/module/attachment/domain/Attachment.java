package com.kratos.module.attachment.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("附件")
public class Attachment extends BaseEntity {
    @ApiModelProperty(value = "附件存储路径")
    @Column(length = 500)
    private String path;
    @ApiModelProperty(value = "附件名称")
    @Column(length = 200)
    private String name;
    @ApiModelProperty(value = "附件格式")
    @Column(length = 50)
    private String format;
    @ApiModelProperty(value = "附件大小")
    private Long size;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
