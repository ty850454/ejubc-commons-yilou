package com.ejubc.commons.yilou.component;


import lombok.Getter;

import java.util.UUID;

/**
 * 锁
 * @author xy
 */
@Getter
public class Lock {

    /** 锁的key */
    private String key;
    /** 锁的value，方式其它请求释放锁 */
    private String value;

    public Lock(String key) {
        this.key = key;
        this.value = UUID.randomUUID().toString();
    }
}
