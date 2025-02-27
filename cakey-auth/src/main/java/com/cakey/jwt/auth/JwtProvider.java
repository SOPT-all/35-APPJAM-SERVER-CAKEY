package com.cakey.jwt.auth;

import com.cakey.Constants;
import com.cakey.exception.AuthRTCacheException;
import com.cakey.exception.AuthWrongJwtException;
import com.cakey.jwt.domain.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProvider {
    private final JwtGenerator jwtGenerator;

    public Token issueToken(final long userId) {
        return Token.of(
                generateAccessToken(userId),
                generateRefreshToken(userId)
        );
    }

    private String generateAccessToken(final long userId) {
        return jwtGenerator.generateAccessToken(userId);
    }

    public String generateRefreshToken(final long userId) {
        return jwtGenerator.generateRefreshToken(userId);
    }

    //RT 캐시에서 삭제
    @CacheEvict(value = Constants.REFRESH_TOKEN, key = "#userId")
    public void deleteRefreshToken(final long userId) { }

    //RT 캐시에서 조회
    @Cacheable(value = Constants.REFRESH_TOKEN, key = "#userId")
    public String findRTFromCache(final long userId) {
        log.error("--------No RT In Cache --------");
        log.error("userId = {}", userId);
        log.error("------------------------------");
        throw new AuthRTCacheException(); ///아무 값이 없으면 예외 던지기
    }

    //jwtSubject에서 userId추출
    public long getUserIdFromSubject(final String token) {
        final String subject = jwtGenerator.parseToken(token)
                .getBody()
                .getSubject();

        //subject가 숫자문자열인지 예외처리
        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            log.error("---------JWT NumberFormatException-------------" + e.getMessage() + e.getCause().toString());
            throw new AuthWrongJwtException();
        }
    }
}