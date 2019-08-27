package com.ejubc.commons.yilou.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xy
 */
public class JsonUtils {

    public static final JsonUtils NORMAL = new JsonUtils();
    public static final JsonUtils NON_NULL = new JsonUtils();

    static {
        NORMAL.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        NON_NULL.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        NON_NULL.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    private JsonUtils() {
    }
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 对象转换成JSON字符串
     *
     * @param object
     * @return
     */
    public String toJson(Object object)  {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON字符串转换成对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * JSON字符串转换成List
     *
     * @param json
     * @param typeRef
     * @return
     */
    public List<?> toList(String json, TypeReference<?> typeRef) {
        List list = null;
        try {
            list = mapper.readValue(json, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}