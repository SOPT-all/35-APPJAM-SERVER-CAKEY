package com.cakey.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();

        List<Cache> cacheList =  new ArrayList();


        cacheList.add(
                // ABC
                new CaffeineCache("refresh",
                        Caffeine.newBuilder()
                                .expireAfterAccess(10, TimeUnit.MINUTES)
                                .initialCapacity(200)
                                .softValues()
                                .maximumSize(1000)
                                .recordStats()
                                .build()
                )
        );

        simpleCacheManager.setCaches(cacheList);

        return simpleCacheManager;
    }
}
