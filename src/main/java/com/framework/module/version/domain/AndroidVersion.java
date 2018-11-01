package com.framework.module.version.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/11/1 16:53
 */
@Entity
@ApiModel("Android版本更新")
public class AndroidVersion extends BaseEntity {

    @ApiModelProperty(value = "versionName")
    @Column(length = 30)
    private String versionName;
    @ApiModelProperty(value = "versionCode")
    @Column()
    private Integer versionCode;
    @ApiModelProperty(value = "forceUpdate")
    @Column()
    private Integer forceUpdate;
    @ApiModelProperty(value = "downloadUrl")
    @Column(length = 100)
    private String downloadUrl;


    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public Integer getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Integer forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
