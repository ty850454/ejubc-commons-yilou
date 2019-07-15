package com.ejubc.commons.yilou.base;

/**
 * 支持构建DTO
 *
 * @author xy
 * @param <T>
 */
@SuppressWarnings("unused")
public interface IMakeDTO<T> {

    /**
     * 构建DTO
     * @return DTO
     */
    T makeDto();
}
