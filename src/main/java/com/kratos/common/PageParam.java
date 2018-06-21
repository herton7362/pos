package com.kratos.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@ApiModel("分页参数")
public class PageParam {
    @ApiModelProperty(value = "当前页")
    private Integer currentPage;
    @ApiModelProperty(value = "每页页数")
    private Integer pageSize = Integer.MAX_VALUE;
    @ApiModelProperty(value = "排序方向")
    private String order;
    @ApiModelProperty(value = "排序属性")
    private String sort;

    private Sort getPageSort() {
        List<Sort.Order> orders = new ArrayList<>();
        if(isSortAble()) {
            String[] propertyArr = sort.split(",");
            String[] directionArr = order.split(",");
            for (int i = 0; i < propertyArr.length; i++) {
                orders.add(new Sort.Order(Sort.Direction.valueOf(StringUtils.upperCase(directionArr[i])), propertyArr[i]));
            }
        }
        return new Sort(orders);
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isPageAble() {
        return (currentPage != null && currentPage > 0) || isSortAble();
    }

    private boolean isSortAble() {
        return StringUtils.isNotBlank(sort);
    }

    public PageRequest getPageRequest() {
        if(isSortAble()) {
            if(currentPage == null) {
                currentPage = 1;
            }
            return new PageRequest(currentPage - 1, pageSize, getPageSort());
        }
        return new PageRequest(currentPage - 1, pageSize);
    }
}
