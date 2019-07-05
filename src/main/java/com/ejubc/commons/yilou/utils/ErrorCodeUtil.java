package com.ejubc.commons.yilou.utils;

import com.ejubc.commons.utils.LocalStringUtils;
import com.ejubc.commons.yilou.exception.IErrorCode;

/**
 * 针对error code的一些处理
 *
 * @author xy
 */
public class ErrorCodeUtil {


    /**
     * 通过参数获取error code的msg
     */
    public static String getMsgWithParams(IErrorCode codeEnum, Object... params) {
        if (codeEnum == null) {
            return null;
        }
        String msg = codeEnum.getMsg();
        if (params == null || params.length == 0) {
            return msg;
        }
        try {
            return LocalStringUtils.messageFormat(msg, params);
        } catch (Exception e) {
            return msg;
        }
    }
}
