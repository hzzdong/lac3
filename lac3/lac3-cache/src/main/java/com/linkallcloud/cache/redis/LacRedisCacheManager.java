package com.linkallcloud.cache.redis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class LacRedisCacheManager extends AbstractTransactionSupportingCacheManager {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;
    private final Map<String, RedisCacheConfiguration> initialCacheConfiguration;
    private final boolean allowInFlightCacheCreation;

    /**
     * Creates new {@link LacRedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter
     *            must not be {@literal null}.
     * @param defaultCacheConfiguration
     *            must not be {@literal null}. Maybe just use {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param allowInFlightCacheCreation
     *            allow create unconfigured caches.
     * @since 2.0.4
     */
    private LacRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
            boolean allowInFlightCacheCreation) {

        Assert.notNull(cacheWriter, "CacheWriter must not be null!");
        Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");

        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
        this.initialCacheConfiguration = new LinkedHashMap<>();
        this.allowInFlightCacheCreation = allowInFlightCacheCreation;
    }

    /**
     * Creates new {@link LacRedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter
     *            must not be {@literal null}.
     * @param defaultCacheConfiguration
     *            must not be {@literal null}. Maybe just use {@link RedisCacheConfiguration#defaultCacheConfig()}.
     */
    public LacRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        this(cacheWriter, defaultCacheConfiguration, true);
    }

    /**
     * Creates new {@link LacRedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter
     *            must not be {@literal null}.
     * @param defaultCacheConfiguration
     *            must not be {@literal null}. Maybe just use {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param initialCacheNames
     *            optional set of known cache names that will be created with given
     *            {@literal defaultCacheConfiguration}.
     */
    public LacRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
            String... initialCacheNames) {

        this(cacheWriter, defaultCacheConfiguration, true, initialCacheNames);
    }

    /**
     * Creates new {@link LacRedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter
     *            must not be {@literal null}.
     * @param defaultCacheConfiguration
     *            must not be {@literal null}. Maybe just use {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param allowInFlightCacheCreation
     *            if set to {@literal true} no new caches can be acquire at runtime but limited to the given list of
     *            initial cache names.
     * @param initialCacheNames
     *            optional set of known cache names that will be created with given
     *            {@literal defaultCacheConfiguration}.
     * @since 2.0.4
     */
    public LacRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
            boolean allowInFlightCacheCreation, String... initialCacheNames) {

        this(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation);

        for (String cacheName : initialCacheNames) {
            this.initialCacheConfiguration.put(cacheName, defaultCacheConfiguration);
        }
    }

    /**
     * Creates new {@link LacRedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter
     *            must not be {@literal null}.
     * @param defaultCacheConfiguration
     *            must not be {@literal null}. Maybe just use {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param initialCacheConfigurations
     *            Map of known cache names along with the configuration to use for those caches. Must not be
     *            {@literal null}.
     */
    public LacRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
            Map<String, RedisCacheConfiguration> initialCacheConfigurations) {

        this(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, true);
    }

    /**
     * Creates new {@link LacRedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter
     *            must not be {@literal null}.
     * @param defaultCacheConfiguration
     *            must not be {@literal null}. Maybe just use {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param initialCacheConfigurations
     *            Map of known cache names along with the configuration to use for those caches. Must not be
     *            {@literal null}.
     * @param allowInFlightCacheCreation
     *            if set to {@literal false} this cache manager is limited to the initial cache configurations and will
     *            not create new caches at runtime.
     * @since 2.0.4
     */
    public LacRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
            Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {

        this(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation);

        Assert.notNull(initialCacheConfigurations, "InitialCacheConfigurations must not be null!");

        this.initialCacheConfiguration.putAll(initialCacheConfigurations);
    }

    /**
     * Create a new {@link LacRedisCacheManager} with defaults applied.
     * <dl>
     * <dt>locking</dt>
     * <dd>disabled</dd>
     * <dt>cache configuration</dt>
     * <dd>{@link RedisCacheConfiguration#defaultCacheConfig()}</dd>
     * <dt>initial caches</dt>
     * <dd>none</dd>
     * <dt>transaction aware</dt>
     * <dd>no</dd>
     * <dt>in-flight cache creation</dt>
     * <dd>enabled</dd>
     * </dl>
     *
     * @param connectionFactory
     *            must not be {@literal null}.
     * @return new instance of {@link LacRedisCacheManager}.
     */
    public static LacRedisCacheManager create(RedisConnectionFactory connectionFactory) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");

        return new LacRedisCacheManager(new LacDefaultRedisCacheWriter(connectionFactory),
                RedisCacheConfiguration.defaultCacheConfig());
    }

    /**
     * Entry point for builder style {@link LacRedisCacheManager} configuration.
     *
     * @param connectionFactory
     *            must not be {@literal null}.
     * @return new {@link LacRedisCacheManagerBuilder}.
     */
    public static LacRedisCacheManagerBuilder builder(RedisConnectionFactory connectionFactory) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");

        return LacRedisCacheManagerBuilder.fromConnectionFactory(connectionFactory);
    }

    /**
     * Entry point for builder style {@link LacRedisCacheManager} configuration.
     *
     * @param cacheWriter
     *            must not be {@literal null}.
     * @return new {@link LacRedisCacheManagerBuilder}.
     */
    public static LacRedisCacheManagerBuilder builder(RedisCacheWriter cacheWriter) {

        Assert.notNull(cacheWriter, "CacheWriter must not be null!");

        return LacRedisCacheManagerBuilder.fromCacheWriter(cacheWriter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.cache.support.AbstractCacheManager#loadCaches()
     */
    @Override
    protected Collection<RedisCache> loadCaches() {

        List<RedisCache> caches = new LinkedList<>();

        for (Map.Entry<String, RedisCacheConfiguration> entry : initialCacheConfiguration.entrySet()) {
            caches.add(createRedisCache(entry.getKey(), entry.getValue()));
        }

        return caches;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.cache.support.AbstractCacheManager#getMissingCache(java.lang.String)
     */
    @Override
    protected RedisCache getMissingCache(String name) {
        return allowInFlightCacheCreation ? createRedisCache(name, defaultCacheConfig) : null;
    }

    /**
     * @return unmodifiable {@link Map} containing cache name / configuration pairs. Never {@literal null}.
     */
    public Map<String, RedisCacheConfiguration> getCacheConfigurations() {

        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(getCacheNames().size());

        getCacheNames().forEach(it -> {

            RedisCache cache = RedisCache.class.cast(lookupCache(it));
            configurationMap.put(it, cache != null ? cache.getCacheConfiguration() : null);
        });

        return Collections.unmodifiableMap(configurationMap);
    }

    /**
     * Configuration hook for creating {@link RedisCache} with given name and {@code cacheConfig}.
     *
     * @param name
     *            must not be {@literal null}.
     * @param cacheConfig
     *            can be {@literal null}.
     * @return never {@literal null}.
     */
    protected LacRedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        return new LacRedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
    }

    /**
     * Configurator for creating {@link LacRedisCacheManager}.
     *
     * @author Christoph Strobl
     * @author Mark Strobl
     * @author Kezhu Wang
     * @since 2.0
     */
    public static class LacRedisCacheManagerBuilder {

        private final RedisCacheWriter cacheWriter;
        private RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        private final Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>();
        private boolean enableTransactions;
        boolean allowInFlightCacheCreation = true;

        private LacRedisCacheManagerBuilder(RedisCacheWriter cacheWriter) {
            this.cacheWriter = cacheWriter;
        }

        /**
         * Entry point for builder style {@link LacRedisCacheManager} configuration.
         *
         * @param connectionFactory
         *            must not be {@literal null}.
         * @return new {@link LacRedisCacheManagerBuilder}.
         */
        public static LacRedisCacheManagerBuilder fromConnectionFactory(RedisConnectionFactory connectionFactory) {

            Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");

            return builder(new LacDefaultRedisCacheWriter(connectionFactory));
        }

        /**
         * Entry point for builder style {@link LacRedisCacheManager} configuration.
         *
         * @param cacheWriter
         *            must not be {@literal null}.
         * @return new {@link LacRedisCacheManagerBuilder}.
         */
        public static LacRedisCacheManagerBuilder fromCacheWriter(RedisCacheWriter cacheWriter) {

            Assert.notNull(cacheWriter, "CacheWriter must not be null!");

            return new LacRedisCacheManagerBuilder(cacheWriter);
        }

        /**
         * Define a default {@link RedisCacheConfiguration} applied to dynamically created {@link RedisCache}s.
         *
         * @param defaultCacheConfiguration
         *            must not be {@literal null}.
         * @return this {@link LacRedisCacheManagerBuilder}.
         */
        public LacRedisCacheManagerBuilder cacheDefaults(RedisCacheConfiguration defaultCacheConfiguration) {

            Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");

            this.defaultCacheConfiguration = defaultCacheConfiguration;

            return this;
        }

        /**
         * Enable {@link RedisCache}s to synchronize cache put/evict operations with ongoing Spring-managed
         * transactions.
         *
         * @return this {@link LacRedisCacheManagerBuilder}.
         */
        public LacRedisCacheManagerBuilder transactionAware() {

            this.enableTransactions = true;

            return this;
        }

        /**
         * Append a {@link Set} of cache names to be pre initialized with current {@link RedisCacheConfiguration}.
         * <strong>NOTE:</strong> This calls depends on {@link #cacheDefaults(RedisCacheConfiguration)} using whatever
         * default {@link RedisCacheConfiguration} is present at the time of invoking this method.
         *
         * @param cacheNames
         *            must not be {@literal null}.
         * @return this {@link LacRedisCacheManagerBuilder}.
         */
        public LacRedisCacheManagerBuilder initialCacheNames(Set<String> cacheNames) {

            Assert.notNull(cacheNames, "CacheNames must not be null!");

            Map<String, RedisCacheConfiguration> cacheConfigMap = new LinkedHashMap<>(cacheNames.size());
            cacheNames.forEach(it -> cacheConfigMap.put(it, defaultCacheConfiguration));

            return withInitialCacheConfigurations(cacheConfigMap);
        }

        /**
         * Append a {@link Map} of cache name/{@link RedisCacheConfiguration} pairs to be pre initialized.
         *
         * @param cacheConfigurations
         *            must not be {@literal null}.
         * @return this {@link LacRedisCacheManagerBuilder}.
         */
        public LacRedisCacheManagerBuilder
                withInitialCacheConfigurations(Map<String, RedisCacheConfiguration> cacheConfigurations) {

            Assert.notNull(cacheConfigurations, "CacheConfigurations must not be null!");
            cacheConfigurations.forEach((cacheName, configuration) -> Assert.notNull(configuration,
                    String.format("RedisCacheConfiguration for cache %s must not be null!", cacheName)));

            this.initialCaches.putAll(cacheConfigurations);

            return this;
        }

        /**
         * Disable in-flight {@link org.springframework.cache.Cache} creation for unconfigured caches.
         * <p />
         * {@link LacRedisCacheManager#getMissingCache(String)} returns {@literal null} for any unconfigured
         * {@link org.springframework.cache.Cache} instead of a new {@link RedisCache} instance. This allows eg.
         * {@link org.springframework.cache.support.CompositeCacheManager} to chime in.
         *
         * @return this {@link LacRedisCacheManagerBuilder}.
         * @since 2.0.4
         */
        public LacRedisCacheManagerBuilder disableCreateOnMissingCache() {

            this.allowInFlightCacheCreation = false;
            return this;
        }

        /**
         * Create new instance of {@link LacRedisCacheManager} with configuration options applied.
         *
         * @return new instance of {@link LacRedisCacheManager}.
         */
        public LacRedisCacheManager build() {

            LacRedisCacheManager cm = new LacRedisCacheManager(cacheWriter, defaultCacheConfiguration, initialCaches,
                    allowInFlightCacheCreation);

            cm.setTransactionAware(enableTransactions);

            return cm;
        }
    }

}
