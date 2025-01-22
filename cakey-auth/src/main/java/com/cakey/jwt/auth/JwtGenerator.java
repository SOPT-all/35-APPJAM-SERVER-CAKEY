package com.cakey.jwt.auth;

import com.cakey.jwt.domain.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
            JwtParser jwtParser = getJwtParser();
            return jwtParser.parseClaimsJws(token);
        }
        //todo: 추후 수정
//        } catch (ExpiredJwtException e) {
//            throw new Ex
//        } catch (UnsupportedJwtException e) {
//            throw new UnauthorizedException(FailureCode.UNSUPPORTED_TOKEN_TYPE);
//        } catch (MalformedJwtException e) {
//            throw new UnauthorizedException(FailureCode.MALFORMED_TOKEN);
//        } catch (SignatureException e) {
//            throw new UnauthorizedException(FailureCode.INVALID_SIGNATURE_TOKEN);
        catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }

    public JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
    }
}