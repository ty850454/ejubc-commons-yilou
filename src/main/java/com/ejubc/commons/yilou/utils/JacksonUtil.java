package com.ejubc.commons.yilou.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JacksonUtil {

    private ObjectMapper mapper;
    JacksonUtil(ObjectMapper mapper) {
        this.mapper = mapper;
    }

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
