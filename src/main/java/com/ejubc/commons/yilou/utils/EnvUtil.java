package com.ejubc.commons.yilou.utils;

import org.springframework.core.env.Environment;

/**
 * 配置工具类
 *
 * @author xy
 */
@SuppressWarnings("unused")
public class EnvUtil {

    private static final Environment ENVIRONMENT = SpringBeanUtil.getBean(Environment.class);
    private static final String ENV = ENVIRONMENT.getProperty("spring.profiles.active");
    private static final boolean IS_ENV = !"prod".equals(ENV);

    public static String getProperty(String key) {
        return ENVIRONMENT.getProperty(key);
    }

    public static <T> T getProperty(String key, Class<T> targetType) {
        return ENVIRONMENT.getProperty(key, targetType);
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return ENVIRONMENT.getProperty(key, targetType, defaultValue);
    }

    public static boolean isDev() {
        return IS_ENV;
    }

    public static String getEnv() {
        return ENV;
    }


}
