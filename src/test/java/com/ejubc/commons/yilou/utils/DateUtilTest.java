package com.ejubc.commons.yilou.utils;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void formatDATE_CHINA() {
        Date parse = DateUtil.DATE_CHINA_NO_ZERO.parseOrElseThrow("2015年7月1日");
        System.out.println(DateUtil.DATE.format(parse));
    }

    @Test
    void formatDATE() {
        Date parse = DateUtil.DATE.parse("2015-07-30").orElseThrow(() -> new RuntimeException("日期转换异常"));
        System.out.println(DateUtil.DATE_CHINA.format(parse));
    }

    @Test
    void getToDayEndTime() {
        System.out.println(DateUtil.DATE_TIME.format(DateUtil.getToDayEndTime()));
    }

    @Test
    void getToDayStartTime() {
        System.out.println(DateUtil.DATE_TIME.format(DateUtil.getToDayStartTime()));
    }

    @Test
    void getTheDayEndTime() {
        System.out.println(DateUtil.DATE_TIME.format(DateUtil.getTheDayEndTime(new Date())));
    }

    @Test
    void getTheDayStartTime() {
        System.out.println(DateUtil.DATE_TIME.format(DateUtil.getTheDayStartTime(new Date())));
    }
}