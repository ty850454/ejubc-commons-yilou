package com.ejubc.commons.yilou.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonUtil {

    public static final JacksonUtil NORMAL;
    public static final JacksonUtil NON_NULL;

    static {
        ObjectMapper normalMapper = new ObjectMapper();
        normalMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        NORMAL = new JacksonUtil(normalMapper);

        ObjectMapper nonNullMapper = new ObjectMapper();
        nonNullMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        nonNullMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        NON_NULL = new JacksonUtil(nonNullMapper);
    }


    private JsonUtil() {
    }

    /**
     * 对象转换成JSON字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object)  {
        return NORMAL.toJson(object);
    }

    /**
     * JSON字符串转换成对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return NORMAL.toObject(json, clazz);
    }

    /**
     * JSON字符串转换成List
     *
     * @param json
     * @param typeRef
     * @return
     */
    public static List<?> toList(String json, TypeReference<?> typeRef) {
        return NORMAL.toList(json, typeRef);
    }

}