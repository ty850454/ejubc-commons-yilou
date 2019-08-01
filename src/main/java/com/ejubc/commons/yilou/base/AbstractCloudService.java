package com.ejubc.commons.yilou.base;

import com.ejubc.commons.base.ApiResponse;
import com.ejubc.commons.utils.JsonUtils;
import com.ejubc.commons.yilou.exception.SysErrorCode;
import com.ejubc.commons.yilou.exception.YlCloudException;
import com.ejubc.commons.yilou.exception.YlException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 微服务基类，提供快速获取接口响应数据的能力
 *
 * 三个方法区别：
 * {@link AbstractCloudService#getData(ApiResponse)}：抛出所有异常，不限制返回值
 * {@link AbstractCloudService#getDataOrElseThrow(ApiResponse)}：抛出所有异常，限制返回值
 * {@link AbstractCloudService#getDataAndNotThrow(ApiResponse)}：不抛出服务异常(只抛出网络异常，可以简单认为不抛出异常)，不限制返回值
 *
 * @author xy
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Slf4j
public abstract class AbstractCloudService {

    /**
     * 获取服务名
     * @return 服务名
     */
    protected abstract String getServiceName();

    /**
     * 获取数据，与{@link AbstractCloudService#getData(ApiResponse)} 相比，不抛出服务返回的异常，但网络问题造成的异常依然抛出，不限制返回值
     *
     * @param apiResponse 响应
     * @param <T> 类型
     * @return 数据而不是
     */
    protected <T> Optional<T> getDataAndNotThrow(ApiResponse<T> apiResponse) {

        if (!apiResponse.isSuccess()) {
            log.warn("调用服务[{}]异常:{}", getServiceName(), JsonUtils.object2Json(apiResponse));
            if (apiResponse.getResponseCode().equals(SysErrorCode.SYS0011.getCode())) {
                throw new YlException(SysErrorCode.SYS0011, getServiceName());
            }
            return Optional.empty();
        }
        String json = JsonUtils.object2Json(apiResponse);
        log.info("调用服务[{}]返回:{}", getServiceName(), json.length() > 500 ? json.substring(0, 500) : json);
        return Optional.ofNullable(apiResponse.getData());
    }

    /**
     * 获取数据，接口调用失败则抛出异常，不限制返回值
     *
     * @param apiResponse 响应
     * @param <T> 类型
     * @return 数据
     */
    protected <T> Optional<T> getData(ApiResponse<T> apiResponse) {

        if (!apiResponse.isSuccess()) {
            throw new YlCloudException(getServiceName(), apiResponse.getResponseCode(), apiResponse.getResponseMsg());
        }
        String json = JsonUtils.object2Json(apiResponse);
        log.info("调用服务[{}]返回:{}", getServiceName(), json.length() > 200 ? json.substring(0, 200) : json);
        return Optional.ofNullable(apiResponse.getData());
    }

    /**
     * 获取数据，与{@link AbstractCloudService#getData(ApiResponse)} 相比，限制返回值必须不是null，否则抛出MgtException异常
     *
     * @param apiResponse 响应
     * @param <T> 类型
     * @return 数据
     */
    protected <T> T getDataOrElseThrow(ApiResponse<T> apiResponse) {
        return getData(apiResponse).orElseThrow(NullPointerException::new);
    }
}
