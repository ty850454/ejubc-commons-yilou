package com.ejubc.commons.yilou.utils;

import java.math.BigDecimal;

public class DecimalUtil {

    public static String toFixed(BigDecimal bigDecimal, int scale) {
        if (bigDecimal == null) {
            return null;
        }
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).toString();
    }


}
