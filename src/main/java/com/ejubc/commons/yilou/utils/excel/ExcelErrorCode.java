package com.ejubc.commons.yilou.utils.excel;

import com.ejubc.commons.yilou.exception.ErrorScope;
import com.ejubc.commons.yilou.exception.IErrorCode;
import lombok.Getter;

import static com.ejubc.commons.yilou.exception.ErrorScope.SYSTEM;

/**
 * <p>易楼项目异常码枚举</p>
 * <p>如果在配置中心xx-bc-xx.properties文件中配置了对应的code，则将配置中心里的msg返回给用户</p>
 * <p>日志输出不会取配置中心的msg</p>
 *
 * @author xy
 */
@Getter
public enum ExcelErrorCode implements IErrorCode {
    /**  */
    EXCEL01("01", "导出数据为空"),
    EXCEL02("02", "导出失败"),

    ;
    private static final String CODE_PREFIX = "EXCEL";

    ExcelErrorCode(String code, String msg) {
        this.scope = ErrorScope.USER;
        this.code = CODE_PREFIX + code;
        this.msg = msg;
    }

    /** 影响范围 */
    private ErrorScope scope;
    /** 异常码 */
    private String code;
    /** 异常信息 */
    private String msg;
}
