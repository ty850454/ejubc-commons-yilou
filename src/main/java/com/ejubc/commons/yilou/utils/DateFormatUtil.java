package com.ejubc.commons.yilou.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期格式化工具
 *
 * @author xy
 * @see com.ejubc.commons.yilou.utils.DateUtil
 */
@SuppressWarnings("unused")
@Deprecated
public class DateFormatUtil {


    public static final DateFormatUtil DATE_TIME = new DateFormatUtil(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    public static final DateFormatUtil DATE = new DateFormatUtil(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private DateTimeFormatter dateTimeFormatter;
    private DateFormatUtil(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.systemDefault());
    }

    public String format(Date date) {
        if (date == null) {
            return null;
        }
        return dateTimeFormatter.format(date.toInstant());
    }

    public Date parse(String date) {
        return Date.from(LocalDateTime.parse(date, dateTimeFormatter).atZone(ZoneId.systemDefault()).toInstant());
    }
}
