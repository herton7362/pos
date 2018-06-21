package com.kratos.common;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResult<T> {
    private Boolean first;
    private Boolean last;
    private Integer number;
    private Integer numberOfElements;
    private Integer size;
    private List<T> content;
    private Long totalElements;
    private Integer totalPages;

    public PageResult() {}

    public PageResult(Page<T> page) {
        this.setContent(page.getContent());
        this.setFirst(page.isFirst());
        this.setLast(page.isLast());
        this.setNumber(page.getNumber());
        this.setSize(page.getSize());
        this.setNumberOfElements(page.getNumberOfElements());
        this.setTotalElements(page.getTotalElements());
        this.setTotalPages(page.getTotalPages());
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
