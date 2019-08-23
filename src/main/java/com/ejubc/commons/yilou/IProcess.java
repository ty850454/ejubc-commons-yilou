package com.ejubc.commons.yilou;

@FunctionalInterface
public interface IProcess {

    /**
     * 业务处理逻辑
     * @return T
     */
    <T> T process();
}
