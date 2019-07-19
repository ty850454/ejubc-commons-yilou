package com.ejubc.commons.yilou.exception;

import com.ejubc.commons.base.ApiResponse;
import com.ejubc.commons.yilou.utils.ErrorCodeUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 专供显示异常信息的响应(开发时才有用)
 *
 * @author xy
 */
@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class ErrorResponse<T> extends ApiResponse<T> {
    private String detail;

    private ErrorResponse(String responseCode, String responseMsg, T data, String detail) {
        super(responseCode, responseMsg, data);
        this.detail = detail;
    }

    ErrorResponse(String responseCode, String responseMsg, String detail) {
        this(responseCode, responseMsg, null, detail);
    }

    public static <T> ErrorResponse<T> failure(IErrorCode errorCode, Object... params) {
        return new ErrorResponse<>(errorCode.getCode(), ErrorCodeUtil.getMsgWithParams(errorCode, params), null);
    }

    public static <T> ErrorResponse<T> failure(String detail, IErrorCode errorCode, Object... params) {
        return new ErrorResponse<>(errorCode.getCode(), ErrorCodeUtil.getMsgWithParams(errorCode, params), detail);
    }


}
