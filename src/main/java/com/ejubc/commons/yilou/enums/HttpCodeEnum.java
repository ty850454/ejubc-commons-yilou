package com.ejubc.commons.yilou.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * http status
 * @author xuyang
 */

@Getter
@AllArgsConstructor
public enum HttpCodeEnum {
    /** 500 */
    CODE_500("500", 500),

    ;

    private String codeStr;
    private int code;


}
