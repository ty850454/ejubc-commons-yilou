package com.ejubc.commons.yilou.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * list 工具类
 *
 * @author xy
 */
@SuppressWarnings("unused")
public class ListUtil {

    /**
     * 分隔list
     *
     * @param list list
     * @param len 分隔长度
     * @param <T> 类型
     * @return 分隔后的list
     */
    public static <T> List<List<T>> splitList(List<T> list, int len) {
        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList();
        }
        List<List<T>> result = new ArrayList<>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }

    /**
     * 从list中每个元素里取某个值
     *
     * @param list list
     * @param function 取值
     * @param <T> list元素类型
     * @param <V> 值类型
     * @return 值list
     */
    public static <T, V> List<V> getKey(List<T> list, Function<T, V> function) {
        if (list == null || list.isEmpty() || function == null) {
            return Collections.emptyList();
        }
        return list.stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }
}
