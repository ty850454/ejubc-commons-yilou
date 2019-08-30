package com.ejubc.commons.yilou.utils;

import java.math.BigDecimal;
import java.util.Optional;

public class NumberUtil {


    public static Optional<Long> parseToLong(String str) {
        if (StringUtil.isNotEmpty(str)) {
            try {
                return Optional.of(Long.valueOf(str));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public static String toFixed(BigDecimal bigDecimal, int scale) {
        if (bigDecimal == null) {
            return null;
        }
        return bigDecimal.setScale(scale, BigDecimal.ROUND_DOWN).toString();
    }

    public static String toFixed2(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        }
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).toString();
    }


}
