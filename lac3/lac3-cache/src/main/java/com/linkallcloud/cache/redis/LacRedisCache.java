package com.linkallcloud.cache.redis;

import java.util.Collection;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class LacRedisCache extends RedisCache {

    protected LacRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }

    @Override
    public void evict(Object key) {
        byte[][] redisKeys = createAndConvertLacCacheKey(key);
        if (redisKeys != null) {
            for (int i = 0; i < redisKeys.length; i++) {
                getNativeCache().remove(getName(), redisKeys[i]);
            }
        }
    }

    protected byte[][] createAndConvertLacCacheKey(Object key) {
        Object[] keys = parseKey2Array(key);
        byte[][] resultKeys = null;
        if (keys != null && keys.length > 0) {
            resultKeys = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++) {
                byte[] resultKey = serializeCacheKey(createCacheKey(keys[i]));
                resultKeys[i] = resultKey;
            }
        }
        return resultKeys;
    }

    private Object[] parseKey2Array(Object key) {
        Object[] keys = null;
        if (key != null) {
            if (key instanceof Object[]) {
                keys = (Object[]) key;
            } else if (key.getClass().isArray()) {
                keys = (Object[]) key;
            } else if (key instanceof Collection) {
                keys = ((Collection<?>) key).toArray();
            } else {
                keys = new Object[1];
                keys[0] = key;
            }
        }
        return keys;
    }

}
