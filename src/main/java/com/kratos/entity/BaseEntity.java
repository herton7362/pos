package com.kratos.entity;

import com.kratos.common.utils.IteratorUtils;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@MappedSuperclass
public abstract class BaseEntity implements Cloneable, Serializable {
    @Id
    @Column(length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @ApiModelProperty(value = "主键", notes = "uuid自动生成，系统默认字段")
    private String id;
    @ApiModelProperty(value = "数据创建时间", notes = "自动生成，系统默认字段")
    private Long createdDate;
    @ApiModelProperty(value = "数据修改时间", notes = "自动生成，系统默认字段")
    private Long updatedDate;
    @ApiModelProperty(value = "是否逻辑删除", notes = "自动生成，系统默认字段")
    private Boolean logicallyDeleted = false;
    @ApiModelProperty(value = "排序号", notes = "自动生成，系统默认字段")
    private Integer sortNumber;
    @ApiModelProperty(required = true, value = "oauth client id")
    @Column(length = 128)
    private String clientId;
    @ApiModelProperty(required = true, value = "创建人id")
    @Column(length = 36)
    private String createUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getLogicallyDeleted() {
        return logicallyDeleted;
    }

    public void setLogicallyDeleted(Boolean logicallyDeleted) {
        this.logicallyDeleted = logicallyDeleted;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false ;
        else{
            if (obj instanceof BaseEntity){
                BaseEntity e = (BaseEntity) obj;
                return e.id.equals(this.id) ;
            }
        }
        return false ;
    }

    public static <E extends BaseEntity> boolean compare(List<E> l1, List<E> l2) {
        String[] array1 = new String[l1.size()];
        String[] array2 = new String[l2.size()];
        IteratorUtils.forEach(l1, (index, l) ->array1[index] = l.getId());
        IteratorUtils.forEach(l2, (index, l) ->array2[index] = l.getId());
        Arrays.sort(array1);
        Arrays.sort(array2);
        return Arrays.equals(array1, array2);
    }
}
