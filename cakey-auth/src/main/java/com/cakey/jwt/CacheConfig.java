package com.cakey.jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;
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
        // CaffeineCacheManager를 사용해 동적으로 캐시 이름을 생성
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // 모든 캐시에 동일한 기본 설정을 적용
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)                  // 초기 용량
                .maximumSize(500)                      // 최대 캐시 크기
                .recordStats());                       // 캐시 통계 활성화

        return cacheManager;
    }
}
