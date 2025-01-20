package com.cakey.jwt.auth;

import com.cakey.exception.ErrorCode;
import com.cakey.jwt.domain.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
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

    public long getUserIdFromSubject(final String token) {
        Jws<Claims> jws = jwtGenerator.parseToken(token);
        String subject = jws.getBody().getSubject();

        //subject가 숫자문자열인지 예외처리
        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.valueOf(ErrorCode.INTERNAL_SERVER_ERROR)); //todo: 추후 변경
        }
    }
}