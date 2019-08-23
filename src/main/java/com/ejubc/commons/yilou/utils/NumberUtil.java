package com.ejubc.commons.yilou.utils;

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


}
