package com.ejubc.commons.yilou.utils;

import java.util.HashMap;

/**
 * @author xy
 */
public class MapBuilder<K,V> {

    private HashMap<K,V> map;

    public MapBuilder() {
        map = new HashMap<>(16);
    }
    public MapBuilder(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    public MapBuilder put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public HashMap<K,V> build() {
        return map;
    }

}
