package com.ejubc.commons.yilou.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;


@SuppressWarnings("WeakerAccess")
public class StringUtilTest {

    @Test
    public void substringWithEnd() {

        System.out.println(StringUtil.substringToEnd("asdadasd.xxs", "."));

    }
    @Test
    public void substringToTopAndEnd() {
        System.out.println(Arrays.toString(StringUtil.substringToTopAndEnd("asdadasd.xxs", ".").orElse(new String[]{})));
    }
}