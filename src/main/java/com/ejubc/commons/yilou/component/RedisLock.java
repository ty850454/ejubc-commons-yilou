package com.ejubc.commons.yilou.component;


import com.ejubc.commons.yilou.IProcess;
import com.ejubc.commons.yilou.enums.SysErrorCode;
import com.ejubc.commons.yilou.exception.YlException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * Redis分布式锁
 *
 * 支持限制同一个lock对象加锁解锁
 * 支持自旋
 * 支持获取锁超时
 * 支持自动解锁
 *
 * @author xuyang
 */
@Component
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class RedisLock {

    /** 默认每隔200ms尝试一次去获取锁 */
    private final static long LOCK_TRY_INTERVAL = 200;
    /** 默认累计尝试时间达到3秒后，不在尝试 */
    private final static long LOCK_TRY_TIMEOUT = 3;
    /** 默认锁在30秒后自动失效，释放锁 */
    private final static long LOCK_EXPIRE_TIME = 30;

    private StringRedisTemplate template;
    public RedisLock(StringRedisTemplate template) {
        this.template = template;
    }


    /**
     * 获取锁
     *
     * @param lock 锁
     * @param timeout 累计获取锁的总时间达到多少后不再尝试，即超时时间（单位毫秒，默认3秒）
     * @param tryInterval 每隔多少时间重新尝试一次获取（单位毫秒，默认200毫秒）
     * @param lockExpireTime 获取锁成功后，锁的过期时间（单位毫秒，默认30秒）
     * @return 成功失败
     */
    public boolean tryLock(Lock lock, Long timeout, Long tryInterval, Long lockExpireTime) {
        log.debug("获取锁,key={}", lock.getKey());
        if (StringUtils.isEmpty(lock.getKey())) {
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
            for (; !coreLock(lock, lockExpireTime);) {
                if (System.currentTimeMillis() - startTime > timeout) {
                    return false;
                }
                Thread.sleep(tryInterval);
            }
            return true;
        } catch (InterruptedException e) {
            log.error("获取锁失败,key={}", lock.getKey(), e);
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
        return tryLock(new Lock(key), timeout, tryInterval, lockExpireTime, iProcess);
    }

    /**
     * 获取锁，自动释放锁
     *
     * @param lock 锁
     * @param timeout 累计获取锁的总时间达到多少后不再尝试，即超时时间（单位毫秒，默认3秒）
     * @param tryInterval 每隔多少时间重新尝试一次获取（单位毫秒，默认200毫秒）
     * @param lockExpireTime 获取锁成功后，锁的过期时间（单位毫秒，默认30秒）
     * @param iProcess 回调，如果不是null，则执行iProcess.process();后自动释放锁
     * @return 成功失败
     */
    public boolean tryLock(Lock lock, Long timeout, Long tryInterval, Long lockExpireTime, IProcess iProcess) {
        if (iProcess == null) {
            return tryLock(lock, timeout, tryInterval, lockExpireTime);
        }
        boolean result = false;
        try {
            result = tryLock(lock, timeout, tryInterval, lockExpireTime);
            if (result) {
                iProcess.process();
            }
        } catch (Exception e) {
            log.error("获取锁执行回调失败,key={}",lock.getKey(), e);
            throw e;
        } finally {
            // 这个锁是不是有可能被别的请求给释放了（没拿到锁也释放锁）
            if (result) {
                unLock(lock);
            }
        }
        return result;
    }

    /**
     * 获取锁，自动释放锁
     *
     * @param key 锁的名称KEY
     * @param iProcess 回调，如果不是null，则执行iProcess.process();后自动释放锁
     * @return 成功失败
     */
    public boolean tryLock(String key, IProcess iProcess) {
        return tryLock(new Lock(key), LOCK_TRY_TIMEOUT, LOCK_TRY_INTERVAL, LOCK_EXPIRE_TIME, iProcess);
    }

    private boolean coreLock(Lock lock, long lockExpireTime) {
        Boolean result = null;
        try {
            result = template.execute((RedisCallback<Boolean>) connection ->
                    connection.set(lock.getKey().getBytes(StandardCharsets.UTF_8),
                            lock.getValue().getBytes(),
                            Expiration.seconds(lockExpireTime),
                            RedisStringCommands.SetOption.ifAbsent())
            );
        } catch (Exception e) {
            log.error("redis lock error, key={}", lock.getKey(), e);
        }
        return result != null ? result : false;
    }

    /**
     * 释放锁
     *
     * @param lock 锁，必须是加锁时使用的lock，否则无法解锁
     */
    public void unLock(Lock lock) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end ";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(Long.class);
        try {
            Long execute = template.execute(redisScript, Collections.singletonList(lock.getKey()), lock.getValue());
            if (execute == null || execute == 0) {
                throw new YlException(SysErrorCode.SYS0002);
            } else {
                log.info("锁释放成功,ip={}", InetAddress.getLocalHost().getHostAddress());
            }
        } catch (UnknownHostException e) {
            log.warn("获取host异常:{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("锁释放错误:{}", e.getMessage(), e);
        }
    }

}