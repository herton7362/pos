package com.framework.module.information.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("消息中心")
public class Information extends BaseEntity {
    @ApiModelProperty(value = "标题")
    @Column(length = 200)
    private String title;
    @ApiModelProperty(value = "内容")
    @Column(length = 2000)
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
