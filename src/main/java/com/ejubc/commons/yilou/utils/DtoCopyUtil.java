package com.ejubc.commons.yilou.utils;

import com.ejubc.commons.yilou.enums.SysErrorCode;
import com.ejubc.commons.yilou.exception.YlException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.objenesis.ObjenesisStd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * dto复制工具
 *
 * @author xy
 */
@SuppressWarnings("unused")
public final class DtoCopyUtil {
    private DtoCopyUtil() {
    }

    private static ThreadLocal<ObjenesisStd> objenesisStdThreadLocal = ThreadLocal.withInitial(ObjenesisStd::new);
    private static ConcurrentHashMap<Class, ConcurrentHashMap<Class, BeanCopier>> cache = new ConcurrentHashMap<>();


    /**
     * Copy properties t.
     *
     * @param <T>    the type parameter
     * @param source the source
     * @param target the target
     * @return the t
     * @author xy
     * @date 2019年04月16日
     */
    public static <T> T copy(Object source, Class<T> target) {
        T newInstance = objenesisStdThreadLocal.get().newInstance(target);
        BeanCopier beanCopier = getCacheBeanCopier(source.getClass(), target);
        beanCopier.copy(source, newInstance, null);
        return newInstance;
    }


    private static <S, T> BeanCopier getCacheBeanCopier(Class<S> source, Class<T> target) {
        ConcurrentHashMap<Class, BeanCopier> copierConcurrentHashMap = cache.computeIfAbsent(source, aClass -> new ConcurrentHashMap<>(16));
        return copierConcurrentHashMap.computeIfAbsent(target, aClass -> BeanCopier.create(source, target, false));
    }

    /**
     * Copy properties list list.
     *
     * @param <T>     the type parameter
     * @param sources the sources
     * @param target  the target
     * @return the list
     * @author xy
     * @date 2019年04月16日
     */
    public static <T> List<T> copyList(List<?> sources, Class<T> target) {
        if (sources.isEmpty()) {
            return Collections.emptyList();
        }

        ArrayList<T> list = new ArrayList<>(sources.size());
        ObjenesisStd objenesisStd = objenesisStdThreadLocal.get();
        for (Object source : sources) {
            if (source == null) {
                throw new YlException(SysErrorCode.SYS0020);
            }
            T newInstance = objenesisStd.newInstance(target);
            BeanCopier beanCopier = getCacheBeanCopier(source.getClass(), target);
            beanCopier.copy(source, newInstance, null);
            list.add(newInstance);
        }
        return list;

    }


}
