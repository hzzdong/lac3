package com.linkallcloud.cache.redis;

import java.time.Duration;

import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.Assert;

public interface LacRedisCacheWriter extends RedisCacheWriter {
    
    /**
     * Create new {@link RedisCacheWriter} without locking behavior.
     *
     * @param connectionFactory must not be {@literal null}.
     * @return new instance of {@link LacDefaultRedisCacheWriter}.
     */
    static RedisCacheWriter nonLockingRedisCacheWriter(RedisConnectionFactory connectionFactory) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");

        return new LacDefaultRedisCacheWriter(connectionFactory);
    }

    /**
     * Create new {@link RedisCacheWriter} with locking behavior.
     *
     * @param connectionFactory must not be {@literal null}.
     * @return new instance of {@link LacDefaultRedisCacheWriter}.
     */
    static RedisCacheWriter lockingRedisCacheWriter(RedisConnectionFactory connectionFactory) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");

        return new LacDefaultRedisCacheWriter(connectionFactory, Duration.ofMillis(50));
    }

}
