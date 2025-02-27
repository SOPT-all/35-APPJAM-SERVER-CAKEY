package com.cakey.common.filter;

import com.cakey.Constants;
import com.cakey.exception.AuthExpiredJwtException;
import com.cakey.exception.AuthWrongJwtException;
import com.cakey.jwt.auth.JwtProvider;
import com.cakey.jwt.auth.UserAuthentication;
import com.cakey.rescode.ErrorBaseCode;
import com.cakey.user.exception.UserUnAuthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class OptionalAuthenticationFilter extends OncePerRequestFilter { //로그인 상관 X
    private final JwtProvider jwtProvider;

    // 필터를 건너뛸 API 경로 목록
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/v1/store/likes/latest/*",
            "/api/v1/store/likes/popularity/*",
            "/api/v1/store/likes/coordinate",
            "/api/v1/cake/store/likes/cake/latest/*",
            "/api/v1/cake/store/likes/cake/popularity/*",
            "/api/v1/store/likes/*",
            "/api/v1/cake/likes/*",
            "/api/v1/cake/likes/latest/*",
            "/api/v1/cake/likes/popularity/*",
            "/api/v1/user/name-email",
            "/api/v1/user/logout",

            "/api/v1/store/rank",
            "/api/v1/store/coordinate-list/*",
            "/api/v1/store/station",
            "/api/v1/store/*/select/coordinate",
            "/api/v1/store/*/size",
            "/api/v1/store/*/information",
            "/api/v1/store/*/kakaoLink",
            "api/v1/user/login"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        // 요청 경로가 제외 목록에 포함되어 있는지 확인
        return EXCLUDED_PATHS.contains(requestURI);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) {

        String accessToken = request.getHeader(Constants.AUTHORIZATION);

        if (accessToken != null) { ///Authorization 헤더 왔을 때
            if (StringUtils.hasText(accessToken) && accessToken.startsWith(Constants.BEARER)) {
                accessToken = accessToken.substring(Constants.BEARER.length());
            } else {
                throw new UserUnAuthorizedException(ErrorBaseCode.UNAUTHORIZED_WRONG_AT); ///액세스토큰 비어있거나 Bearer로 시작안할때
            }

            try {
                final long userId = jwtProvider.getUserIdFromSubject(accessToken);
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UserAuthentication(userId, null, null));
                filterChain.doFilter(request, response);

            } catch (AuthExpiredJwtException e) {
                throw new UserUnAuthorizedException(ErrorBaseCode.UNAUTHORIZED_AT_EXPIRED);
            } catch (AuthWrongJwtException e) {
                throw new UserUnAuthorizedException(ErrorBaseCode.UNAUTHORIZED_WRONG_AT);
            } catch (Exception e) {
                log.error("-------UNAUTHORIZED ERROR LOG -----------\n" + e.getMessage(), e);
                throw new UserUnAuthorizedException(ErrorBaseCode.INTERNAL_SERVER_ERROR);
            }
        } else { ///Authorization 헤더 안왔을 때
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UserAuthentication(null, null, null));
            try {
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                log.error("-------UNAUTHORIZED ERROR LOG ----------- \n " + e.getMessage(), e);
                throw new UserUnAuthorizedException(ErrorBaseCode.INTERNAL_SERVER_ERROR);            }
        }
    }
}
