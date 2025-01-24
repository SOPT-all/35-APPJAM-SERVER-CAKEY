package com.cakey.jwt.auth;

import com.cakey.TestConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(classes = TestConfiguration.class)
@TestPropertySource(properties = {
        "jwt.secret=testasdfasdfasddfsecretfdadfsdfasdfasdfasdfasdf",
        "jwt.accessTokenExpirationTime=3600",
        "jwt.refreshTokenExpirationTime=604800"
})
@ComponentScan(basePackages = "com.cakey")
class JwtGeneratorTest {

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("리프레시 토큰 캐시에 등록")
    void generateRefreshToken() {
        // Given
        // 캐시 초기화
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });

        long firstId = 123L;
        long secondId = 456L;

        final String token1 = jwtGenerator.generateRefreshToken(firstId);
        System.out.println("token 1 : " + token1);

        final String token2 = jwtGenerator.generateRefreshToken(secondId);
        System.out.println("token 2 : " + token2);

        // When
        final Cache cache = cacheManager.getCache("refresh");

        // Then
        // 캐시에서 값 가져오기
        final String firstCachedToken = cache.get(firstId, String.class);
        assertThat(firstCachedToken).isNotNull();
        System.out.println("token 1: " + firstCachedToken);
        assertThat(firstCachedToken).isEqualTo(token1); // 캐시된 값이 첫 번째 토큰과 동일해야 함

        final String secondCachedToken = cache.get(secondId, String.class);
        assertThat(secondCachedToken).isNotNull();
        System.out.println("token 2: " + secondCachedToken);
        assertThat(secondCachedToken).isEqualTo(token2); // 캐시된 값이 두 번째 토큰과 동일해야 함
    }

    @Test
    @DisplayName("리프레시 토큰 캐시 삭제")
    void deleteRefreshToken() {
        //Given
        final long userId = 123L;

        Cache cache = cacheManager.getCache("refresh");
        System.out.println(cache);
        assertThat(cache).isNotNull();

        String cachedValue = cache.get(userId, String.class);
        System.out.println(cachedValue);
        assertThat(cachedValue).isNotNull();

        //When
        jwtProvider.deleteRefreshToken(userId);

        //Then
        cache = cacheManager.getCache("refresh");
        System.out.println(cache);

        cachedValue = cache.get(userId, String.class);
        System.out.println(cachedValue);

        Assertions.assertThat(cachedValue).isNull(); // 키에 해당하는 값이 삭제되었는지 확인
    }
}
