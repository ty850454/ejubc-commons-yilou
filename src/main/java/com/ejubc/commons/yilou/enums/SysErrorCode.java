package com.ejubc.commons.yilou.enums;

import com.ejubc.commons.yilou.exception.IErrorCode;
import lombok.Getter;

import static com.ejubc.commons.yilou.enums.ErrorScope.SYSTEM;

/**
 * <p>易楼项目异常码枚举</p>
 * <p>如果在配置中心xx-bc-xx.properties文件中配置了对应的code，则将配置中心里的msg返回给用户</p>
 * <p>日志输出不会取配置中心的msg</p>
 *
 * @author xy
 */
@Getter
public enum SysErrorCode implements IErrorCode {
    /** 0000-0999 系统异常 */
    SYS0000(SYSTEM, "0000", "产生系统错误"),
    SYS0001(SYSTEM, "0001", "产生系统错误，错误码：{0}"),

    SYS0010(SYSTEM, "0010", "远程服务[{0}]异常，code：{1},msg：{2}"),
    SYS0011(SYSTEM, "0011", "抱歉，{0}服务不可用"),

    SYS0020(SYSTEM, "0020", "实体拷贝异常"),

    SYS0030(SYSTEM, "0030", "上传文件过大"),

    SYS0040(SYSTEM, "0040", "日期格式解析失败"),

    /** 1000-1999 参数异常（非业务参数异常） */
    SYS1000("1000","参数[{0}]不能为空"),

    /** 2000-2999 用户异常 */
    SYS2000("2000","用户不存在"),






    SYS9999("SYS9999","{0}"),
    ;
    private static final String CODE_PREFIX = "SYS";

    SysErrorCode(ErrorScope scope, String code, String msg) {
        this.scope = scope;
        this.code = CODE_PREFIX + code;
        this.msg = msg;
    }

    SysErrorCode(String code, String msg) {
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
