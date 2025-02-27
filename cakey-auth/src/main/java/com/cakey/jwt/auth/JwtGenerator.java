package com.cakey.jwt.auth;

import com.cakey.exception.AuthExpiredJwtException;
import com.cakey.exception.AuthWrongJwtException;
import com.cakey.jwt.domain.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtGenerator {
    private final JwtProperties jwtProperties;

    //액세스 토큰 발급
    public String generateAccessToken(final long userId) {
        final Date now = new Date();
        final Date expireDate = generateExpirationDate(now, true);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Cacheable(value = "refresh")
    public String generateRefreshToken(final long userId) {
        final Date now = new Date();
        final Date expireDate = generateExpirationDate(now, false);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date generateExpirationDate(final Date now, final boolean isAccessToken) {
        if (isAccessToken) {
            return new Date(now.getTime() + jwtProperties.getAccessTokenExpirationTime());
        } else {
            return new Date(now.getTime() + jwtProperties.getRefreshTokenExpirationTime());
        }
    }

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(encodeSecretKeyToBase64().getBytes());
    }

    private String encodeSecretKeyToBase64() {
        return Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
    }

    public Jws<Claims> parseToken(final String token) {
        try {
            final JwtParser jwtParser = getJwtParser();
            return jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) { ///만료된 jwt 예외처리
            throw new AuthExpiredJwtException();
        } catch (UnsupportedJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) { ///잘못된 jwt 예외처리
            throw new AuthWrongJwtException();

        }
    }
    public JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
    }
}