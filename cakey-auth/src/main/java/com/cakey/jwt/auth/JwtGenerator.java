package com.cakey.jwt.auth;

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

    //todo: 추후에 properties로 가져오기(데로 참고)
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 2 * 60 * 60 * 1000L; //2시간
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 14* 24 * 60 * 60 * 1000L; //2주
    private static final String SECRET_KEY = "cakeyfsdafasdfjsadrhksadrhskdlrskadjlralsdkrhasdklrhsadr";

    //액세스 토큰 발급
    public String generateAccessToken(final long userId) {
        final Date now = new Date();
        final Date expireDate = generateExpirationDate(now);

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
        final Date expireDate = generateExpirationDate(now);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date generateExpirationDate(final Date now) {
        return new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(encodeSecretKeyToBase64().getBytes());
    }

    private String encodeSecretKeyToBase64() {
        return Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
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