package com.ejubc.commons.yilou.utils;

import java.util.Optional;

public class ObjUtil {


    public static  <T> T defaultValue(T value, T defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    public static boolean defaultBoolean(Boolean value) {
        return Optional.ofNullable(value).orElse(false);
    }


}
