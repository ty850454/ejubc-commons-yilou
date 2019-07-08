package com.ejubc.commons.yilou.utils;

import com.ejubc.commons.base.GetBeanUtil;
import org.springframework.core.env.Environment;

/**
 * 配置工具类
 *
 * @author xy
 */
public class EnvUtil {

    private static final Environment ENVIRONMENT = GetBeanUtil.getBean(Environment.class);
    private static final String ENV = ENVIRONMENT.getProperty("spring.profiles.active");
    private static final boolean IS_ENV = !"prod".equals(ENV);

    @SuppressWarnings("unused")
    public static String getProperty(String key) {
        return ENVIRONMENT.getProperty(key);
    }

    @SuppressWarnings("unused")
    public static <T> T getProperty(String key, Class<T> targetType) {
        return ENVIRONMENT.getProperty(key, targetType);
    }

    @SuppressWarnings("unused")
    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return ENVIRONMENT.getProperty(key, targetType, defaultValue);
    }

    @SuppressWarnings("unused")
    public static boolean isDev() {
        return IS_ENV;
    }

    @SuppressWarnings("unused")
    public static String getEnv() {
        return ENV;
    }


}
