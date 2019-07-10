package com.ejubc.commons.yilou.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期格式化、转换、加减工具
 *
 * @author xy
 */
@SuppressWarnings("unused")
public class DateUtil {
    public static final DateUtil DATE_TIME = new DateUtil(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    public static final DateUtil DATE = new DateUtil(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private DateTimeFormatter dateTimeFormatter;
    private DateUtil(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.systemDefault());
    }

    public String format(Date date) {
        if (date == null) {
            return null;
        }
        return dateTimeFormatter.format(date.toInstant());
    }

    public Date parse(String date) {
        return toDate(LocalDateTime.parse(date, dateTimeFormatter));
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /** 增加指定天 */
    public static Date plusDays(Date date, int days) {
        return toDate(toLocalDateTime(date).plusDays(days));
    }

    /** 增加指定小时*/
    public static Date plusHours(Date date, int hours) {
        return toDate(toLocalDateTime(date).plusHours(hours));
    }

    /** 增加指定分钟 */
    public static Date plusMinutes(Date date, int minutes) {
        return toDate(toLocalDateTime(date).plusMinutes(minutes));
    }

    /** 增加指定月 */
    public static Date plusMonth(Date date, int mouth) {
        return toDate(toLocalDateTime(date).plusMonths(mouth));
    }

    /** 增加指定秒 */
    public static Date plusSeconds(Date date, int seconds) {
        return toDate(toLocalDateTime(date).plusSeconds(seconds));
    }

    /** 增加指定年 */
    public static Date plusYears(Date date, int years) {
        return toDate(toLocalDateTime(date).plusYears(years));
    }

    /** 增加指定周 */
    public static Date plusWeeks(Date date, int weeks) {
        return toDate(toLocalDateTime(date).plusWeeks(weeks));
    }
}
