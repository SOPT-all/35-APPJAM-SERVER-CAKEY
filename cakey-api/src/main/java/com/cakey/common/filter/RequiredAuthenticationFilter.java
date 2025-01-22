package com.cakey.common.filter;

import com.cakey.jwt.auth.JwtProvider;
import com.cakey.jwt.auth.UserAuthentication;
import com.cakey.jwt.auth.JwtValidationType;
import com.cakey.rescode.ErrorBaseCode;
import com.cakey.rescode.ErrorCode;
import com.cakey.user.exception.UserBadRequestException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RequiredAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider; //로그인 필수

    // 필터를 건너뛸 API 경로 목록
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/v1/cake/rank",
            "/api/v1/store/latest/*",
            "/api/v1/store/popularity/*",
            "/api/v1/cake/station/latest/*",
            "/api/v1/cake/station/popularity/*",
            "/api/v1/store/select/*",
            "/api/v1/cake/latest/*",
            "/api/v1/cake/popularity/*",
            "/api/v1/cake/select/*",
            "/api/v1/store/design/*",

            "/api/v1/store/rank",
            "/api/v1/store/coordinate-list/*",
            "/api/v1/store/station",
            "/api/v1/store/*/select/coordinate",
            "/api/v1/store/*/size",
            "/api/v1/store/*/information",
            "/api/v1/store/*/kakaoLink"

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
    ) throws ServletException, IOException {
        try {
            final String token = getAccessTokenFromCookie(request);

            final Long userId = jwtProvider.getUserIdFromSubject(token);
            SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userId, null, null));
        } catch (Exception e) {
            throw new UserBadRequestException(ErrorBaseCode.UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromCookie(final HttpServletRequest request) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    return cookie.getValue();
                }
            }
        }
        throw new UserBadRequestException(ErrorBaseCode.UNAUTHORIZED);
    }
}