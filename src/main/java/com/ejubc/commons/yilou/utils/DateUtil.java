package com.ejubc.commons.yilou.utils;

import com.ejubc.commons.yilou.enums.SysErrorCode;
import com.ejubc.commons.yilou.exception.YlException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

/**
 * 日期格式化、转换、加减工具
 *
 * @author xy
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DateUtil {
    public static final DateUtil DATE_TIME = new DateUtil(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), DateType.DATE_TIME);
    public static final DateUtil DATE = new DateUtil(DateTimeFormatter.ofPattern("yyyy-MM-dd"), DateType.DATE);
    public static final DateUtil DATE_CHINA = new DateUtil(DateTimeFormatter.ofPattern("yyyy年MM月dd日"), DateType.DATE);
    public static final DateUtil DATE_TIME_CHINA = new DateUtil(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒"), DateType.DATE);
    public static final DateUtil DATE_CHINA_NO_ZERO = new DateUtil(DateTimeFormatter.ofPattern("yyyy年M月d日"), DateType.DATE);

    private DateTimeFormatter dateTimeFormatter;
    /** 1=日期时间，2=日期，3=时间 */
    private DateType type;
    private DateUtil(DateTimeFormatter dateTimeFormatter, DateType type) {
        this.dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.systemDefault());
        this.type = type;
    }

    public String format(Date date) {
        if (date == null) {
            return null;
        }
        return dateTimeFormatter.format(date.toInstant());
    }

    @Deprecated
    public String nullSafeFormat(Date date) {
        if (date == null) {
            return null;
        }
        return format(date);
    }

    public Optional<Date> parse(String date) {
        try {
            switch (type) {
                case DATE:
                    LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
                    return Optional.of(toDate(localDate));
                case DATE_TIME:
                    LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
                    return Optional.of(toDate(localDateTime));
                default:
            }
            return Optional.empty();
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    private enum DateType {/** 日期类型 */ DATE_TIME, DATE, TIME}

    public Date parseOrElseThrow(String date) {
        return parse(date).orElseThrow(() -> new YlException(SysErrorCode.SYS0040));
    }

    private static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
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


    /**
     * 获取今天的结束时间
     */
    public static Date getToDayEndTime() {
        return toDate(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    }
    /**
     * 获取指定日期的结束时间
     */
    public static Date getTheDayEndTime(Date date) {
        return toDate(LocalDateTime.of(toLocalDate(date), LocalTime.MAX));
    }

    /**
     * 获取今天开始时间
     */
    public static Date getToDayStartTime() {
        return toDate(LocalDate.now().atStartOfDay());
    }

    /**
     * 获取指定日期开始时间
     */
    public static Date getTheDayStartTime(Date date) {
        return toDate(toLocalDate(date).atStartOfDay());
    }






}
