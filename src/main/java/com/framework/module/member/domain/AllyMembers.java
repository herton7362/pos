package com.framework.module.member.domain;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/6/29 13:24
 */
public class AllyMembers {
    List<Member> sonList;
    List<Member> grandSonList;

    public List<Member> getSonList() {
        return sonList;
    }

    public void setSonList(List<Member> sonList) {
        this.sonList = sonList;
    }

    public List<Member> getGrandSonList() {
        return grandSonList;
    }

    public void setGrandSonList(List<Member> grandSonList) {
        this.grandSonList = grandSonList;
    }
}
