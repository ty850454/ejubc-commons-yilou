package com.ejubc.commons.yilou.exception;

import lombok.Getter;

/**
 * 使用code与msg保存异常信息
 *
 * @author xy
 */
@Getter
public class YlCloudException extends YlException {
    private String code;
    private String msg;
    private String serviceName;

    /**
     * 创建mgt项目异常
     *
     * @param code 异常code
     * @param msg 异常msg
     */
    public YlCloudException(String serviceName, String code, String msg) {
        super(msg);
        this.serviceName = serviceName;
        this.code = code;
        this.msg = msg;
    }
}
