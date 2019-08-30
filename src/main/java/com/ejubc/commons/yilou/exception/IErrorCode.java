package com.ejubc.commons.yilou.exception;

import com.ejubc.commons.yilou.enums.ErrorScope;

/**
 * 异常枚举接口
 * @author xy
 */
public interface IErrorCode {
    /**
     * 获取异常影响范围
     * @return 异常影响范围
     */
    ErrorScope getScope();

    /**
     * 获取异常码
     * @return 异常码
     */
    String getCode();

    /**
     * 获取异常信息
     * @return 异常信息
     */
    String getMsg();
}
