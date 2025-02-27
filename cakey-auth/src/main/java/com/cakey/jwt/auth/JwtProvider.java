package com.cakey.jwt.auth;

import com.cakey.Constants;
import com.cakey.exception.AuthExpiredJwtException;
import com.cakey.exception.AuthWrongJwtException;
import com.cakey.exception.CakeyBaseException;
import com.cakey.jwt.domain.Token;
import com.cakey.rescode.ErrorBaseCode;
import com.cakey.rescode.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    @CacheEvict(value = "refresh")
    public void deleteRefreshToken(final long userId) { }

    public long getUserIdFromSubject(final String token) {
        final String subject = jwtGenerator
                .parseToken(token)
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