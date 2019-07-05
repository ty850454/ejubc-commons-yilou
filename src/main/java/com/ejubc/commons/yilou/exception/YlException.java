package com.ejubc.commons.yilou.exception;

import com.ejubc.commons.yilou.utils.ErrorCodeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * mgt项目异常
 * <p>示例：throw new MgtException(SysErrorCode.MARKET01);</p>
 *
 * @author xy
 */
@Getter
@NoArgsConstructor
public class YlException extends RuntimeException {

    private IErrorCode codeEnum;
    private Object[] params;

    /**
     * 创建mgt项目异常
     *
     * @param codeEnum 异常枚举
     * @param params 可选的参数
     */
    public YlException(IErrorCode codeEnum, Object... params) {
        super(ErrorCodeUtil.getMsgWithParams(codeEnum, params));
        this.codeEnum = codeEnum;
        this.params = params;
    }

    protected YlException(String msg) {
        super(msg);
    }

}
