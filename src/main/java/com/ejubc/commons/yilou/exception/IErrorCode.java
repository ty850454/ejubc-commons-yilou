package com.ejubc.commons.yilou.exception;

public interface IErrorCode {
    ErrorScope getScope();
    String getCode();
    String getMsg();
}
