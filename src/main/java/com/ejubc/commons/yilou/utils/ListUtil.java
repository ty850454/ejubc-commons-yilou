package com.ejubc.commons.yilou.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
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


    /**
     * 找到需要删除的数据，需要更新的数据，需要新建的数据
     *
     * 两个集合N、O，返回集合N与集合O的差集(N-O)、集合N与集合O的交集(N∩O)、集合O与集合N的差集(O-N)

     * 这两个集合类型可以不一致，所以需要一个算法计算两个集合中的元素是否一致。
     *
     * @param newCollection 新的集合，集合N(new)，null安全
     * @param oldCollection 老的集合，集合O(old)，null安全
     * @param comparator 计算元素是否一致，不可为null
     * @param <N> 新集合元素类型
     * @param <O> 老集合元素类型
     * @return 结果，不会返回null，如果没有元素，返回空集∅
     */
    public static <N, O> SimilarResult<N, O> similar(Collection<N> newCollection, Collection<O> oldCollection, Compare<N, O> comparator) {

        // 如果新旧有任意一个是空的，则原封不动返回
        if (CollectionUtils.isEmpty(newCollection) || CollectionUtils.isEmpty(oldCollection)) {
            return new SimilarResult<>(
                    CollectionUtils.isEmpty(newCollection) ? Collections.emptyList() : newCollection,
                    Collections.emptyList(),
                    CollectionUtils.isEmpty(oldCollection) ? Collections.emptyList() : oldCollection);
        }

        // 需要更新的集合，是两个集合的交集
        Collection<SimilarNeedUpdateResult<N, O>> needUpdate = new ArrayList<>();
        // 需要创建的集合，是newCollection与oldCollection的差集
        Collection<N> needCreate = new ArrayList<>();
        // 需要创建的集合，是oldCollection与newCollection的差集，oldCollection中去掉需要更新的
        Collection<O> needDelete = new LinkedList<>(oldCollection);


        x:for (N n : newCollection) {
            for (O o : oldCollection) {
                if (comparator.compare(n, o)) {
                    // 需要更新
                    needUpdate.add(new SimilarNeedUpdateResult<>(n, o));
                    needDelete.remove(o);
                    continue x;
                }
            }
            // 需要新建
            needCreate.add(n);
        }

        return new SimilarResult<>(needCreate, needUpdate, needDelete);
    }

    /**
     * 用来比较两个不同类型的元素是否相等
     *
     * @param <N>
     * @param <O>
     */
    @FunctionalInterface
    public interface Compare<N, O> {

        /**
         * 比较是否相等
         *
         * @param newElement 新集合元素
         * @param oldElement 旧集合元素
         * @return 相等返回true
         */
        boolean compare(N newElement, O oldElement);
    }

    /**
     * 返回两个集合的差集与交集
     *
     * @param <N>
     * @param <O>
     */
    @Getter
    @AllArgsConstructor
    @ToString
    public static class SimilarResult<N, O> {
        /** 需要创建的元素，集合N与集合O的差集(N-O) */
        private Collection<N> needCreate;
        /** 需要更新的元素，集合N与集合O的交集(N∩O) */
        private Collection<SimilarNeedUpdateResult<N, O>> needUpdate;
        /** 需要删除的元素，集合O与集合N的差集(O-N) */
        private Collection<O> needDelete;
    }

    /**
     * 交集的元素，因为两个集合类型可以不一样，所以同时返回两个集合中对应的元素
     * @param <N>
     * @param <O>
     */
    @Getter
    @AllArgsConstructor
    @ToString
    public static class SimilarNeedUpdateResult<N, O> {
        /** 需要更新的元素-新元素 */
        private N newElement;
        /** 需要更新的元素-旧元素 */
        private O oldElement;
    }

}
