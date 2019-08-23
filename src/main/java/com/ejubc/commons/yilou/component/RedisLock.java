package com.ejubc.commons.yilou.component;


import com.ejubc.commons.yilou.IProcess;
import com.ejubc.commons.yilou.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Redis分布式锁
 *
 * @author xuyang
 */
@Component
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class RedisLock {

    /** 默认每隔200ms尝试一次去获取锁 */
    private final static long LOCK_TRY_INTERVAL = 200L;
    /** 默认累计尝试时间达到3秒后，不在尝试 */
    private final static long LOCK_TRY_TIMEOUT = 3 * 1000L;
    /** 默认锁在30秒后自动失效，释放锁 */
    private final static long LOCK_EXPIRE_TIME = 30 * 1000L;

    private StringRedisTemplate template;
    public RedisLock(StringRedisTemplate template) {
        this.template = template;
    }

    /**
     * 获取锁
     *
     * @param key 锁的名称KEY
     * @param timeout 累计获取锁的总时间达到多少后不再尝试，即超时时间（单位毫秒，默认3秒）
     * @param tryInterval 每隔多少时间重新尝试一次获取（单位毫秒，默认200毫秒）
     * @param lockExpireTime 获取锁成功后，锁的过期时间（单位毫秒，默认30秒）
     * @return 成功失败
     */
    public boolean tryLock(String key, Long timeout, Long tryInterval, Long lockExpireTime) {
        log.debug("获取锁,key={}", key);
        if (StringUtil.isEmpty(key)) {
            return false;
        }
        if (timeout == null) {
            timeout = LOCK_TRY_TIMEOUT;
        }
        if (tryInterval == null) {
            tryInterval = LOCK_TRY_INTERVAL;
        }
        if (lockExpireTime == null) {
            lockExpireTime = LOCK_EXPIRE_TIME;
        }
        try {
            long startTime = System.currentTimeMillis();
            for (;!coreLock(key, lockExpireTime);) {
                if (System.currentTimeMillis() - startTime > timeout) {
                    return false;
                }
                Thread.sleep(tryInterval);
            }
        } catch (InterruptedException e) {
            log.error("获取锁失败,key={}", key, e);
        }
        return false;
    }

    /**
     * 获取锁，自动释放锁
     *
     * @param key 锁的名称KEY
     * @param timeout 累计获取锁的总时间达到多少后不再尝试，即超时时间（单位毫秒，默认3秒）
     * @param tryInterval 每隔多少时间重新尝试一次获取（单位毫秒，默认200毫秒）
     * @param lockExpireTime 获取锁成功后，锁的过期时间（单位毫秒，默认30秒）
     * @param iProcess 回调，如果不是null，则执行iProcess.process();后自动释放锁
     * @return 成功失败
     */
    public boolean tryLock(String key, Long timeout, Long tryInterval, Long lockExpireTime, IProcess iProcess) {
        if (iProcess == null) {
            return tryLock(key, timeout, tryInterval, lockExpireTime);
        }
        boolean result = false;
        try {
            result = tryLock(key, timeout, tryInterval, lockExpireTime);
            if (result) {
                iProcess.process();
            }
        } catch (Exception e) {
            log.error("获取锁执行回调失败,key={}",key, e);
        } finally {
            if (result) {
                unLock(key);
            }
        }
        return result;
    }

    /**
     * 获取锁
     *
     * @param key 锁的名称KEY
     * @param lockExpireTime 获取锁成功后，锁的过期时间（单位毫秒，默认30秒）
     * @return 成功失败
     */
    public boolean lock(String key, Long lockExpireTime) {
        if (StringUtil.isEmpty(key)) {
            return false;
        }
        return coreLock(key, lockExpireTime != null ? lockExpireTime : LOCK_EXPIRE_TIME);
    }


    private boolean coreLock(String key, long lockExpireTime) {
        Boolean result = null;
        try {
            result = template.execute((RedisCallback<Boolean>) connection ->
                    connection.set(key.getBytes(StandardCharsets.UTF_8),
                            new byte[]{1},
                            Expiration.seconds(lockExpireTime),
                            RedisStringCommands.SetOption.ifAbsent())
            );
        } catch (Exception e) {
            log.error("redis lock error, key={}", key, e);
        }
        return result != null ? result : false;
    }

    /**
     * 释放锁
     *
     * @param key 锁的名称KEY
     */
    public void unLock(String key) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        if (Optional.ofNullable(template.hasKey(key)).orElse(false)) {
            template.delete(key);
        }
    }

}