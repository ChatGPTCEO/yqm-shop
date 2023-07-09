package com.yqm.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiximei
 * 分销级别
 */
@Getter
@AllArgsConstructor
public enum Brokerage {

    LEVEL_1(1,"一级"),
    LEVEL_2(2,"二级");

    private Integer value;
    private String desc;
}