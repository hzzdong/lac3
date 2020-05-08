package com.linkallcloud.cache.redis.template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class LacRedisTemplate<T> {
    protected Log log = Logs.get();
    protected Mirror<T> mirror;

    protected StringRedisTemplate stringRedisTemplate;

    @SuppressWarnings("unchecked")
	public LacRedisTemplate() {
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

    public LacRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this();
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    /**
     * 读取redis缓存
     *
     * @param key
     * @return
     */
    public T get(final String key) {
        String jsonStr = this.getStr(key);
        if (!Strings.isBlank(jsonStr)) {
            //Castors.me().castTo(jsonStr,mirror.getType());
            return JSON.parseObject(jsonStr, new TypeReference<T>() {
            });
        }
        return null;
    }

    /**
     * 写入redis缓存（设置expire存活时间）
     *
     * @param key
     * @param value
     * @return
     */
    public boolean put(final String key, T value) {
        String jsonStr = JSON.toJSONString(value);
        return this.putStr(key, jsonStr);
    }

    /**
     * 写入redis缓存（设置expire存活时间）
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public boolean put(final String key, T value, long expire) {
        String jsonStr = JSON.toJSONString(value);
        return this.putStr(key, jsonStr, expire);
    }

    /**
     * 判断redis缓存中是否有对应的key
     *
     * @param key
     * @return
     */
    public boolean exist(final String key) {
        return this.existStr(key);
    }

    /**
     * 从redis中移除
     *
     * @param key
     * @return
     */
    public T remove(final String key) {
        String jsonStr = this.removeStr(key);
        if (!Strings.isBlank(jsonStr)) {
            //Castors.me().castTo(jsonStr,mirror.getType());
            return JSON.parseObject(jsonStr, new TypeReference<T>() {
            });
        }
        return null;
    }


    /**
     * 读取redis缓存
     *
     * @param key
     * @return
     */
    public String getStr(final String key) {
        if (!Strings.isBlank(key)) {
            try {
                return getStringRedisTemplate().opsForValue().get(key);
            } catch (Exception e) {
                log.errorf("读取redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
            }
        }
        return null;
    }

    /**
     * 写入redis缓存（设置expire存活时间）
     *
     * @param key
     * @param value
     * @return
     */
    public boolean putStr(final String key, String value) {
        try {
            getStringRedisTemplate().opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.errorf("写入redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
            return false;
        }
    }

    /**
     * 写入redis缓存（设置expire存活时间）
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public boolean putStr(final String key, String value, long expire) {
        if (expire > 0) {
            try {
                getStringRedisTemplate().opsForValue().set(key, value, expire, TimeUnit.SECONDS);
                return true;
            } catch (Exception e) {
                log.errorf("写入redis缓存(%s)失败！错误信息为：%s", key, e.getMessage());
                return false;
            }
        } else {
            return this.putStr(key, value);
        }
    }

    /**
     * 判断redis缓存中是否有对应的key
     *
     * @param key
     * @return
     */
    public boolean existStr(final String key) {
        try {
            return getStringRedisTemplate().hasKey(key);
        } catch (Exception e) {
            log.errorf("判断redis缓存中是否有对应的key(%s)失败！错误信息为：%s", key, e.getMessage());
        }
        return false;
    }

    /**
     * 从redis中移除
     *
     * @param key
     * @return
     */
    public String removeStr(final String key) {
        try {
            if (existStr(key)) {
                String o = this.getStr(key);
                getStringRedisTemplate().delete(key);
                return o;
            }
        } catch (Exception e) {
            log.errorf("redis删除key(%s)失败！错误信息为：%s", key, e.getMessage());
        }
        return null;
    }
}
