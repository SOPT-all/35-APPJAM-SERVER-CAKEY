package com.cakey.common.filter;

import com.cakey.Constants;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.jwt.auth.JwtProvider;
import com.cakey.jwt.auth.UserAuthentication;
import com.cakey.rescode.ErrorBaseCode;
import com.cakey.user.exception.UserBadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequiredAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider; //로그인 필수
    private final ObjectMapper objectMapper;

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
            filterChain.doFilter(request, response); // 다음 필터로 요청 전달
        } catch (Exception e) {
            // 예외 발생 시 JSON 응답 생성
            final ErrorBaseCode errorCode = ErrorBaseCode.UNAUTHORIZED;

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(Constants.CHARACTER_TYPE);
            response.setStatus(errorCode.getHttpStatus().value()); // HTTP 상태 코드 401 설정

            log.error("--------------------쿠키 없음------------------------"); //todo: 추후 삭제(테스트용)
            // `ApiResponseUtil.failure`를 이용해 응답 작성
            final PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(
                    ApiResponseUtil.failure(errorCode).getBody()
            ));
            writer.flush();
            return; // 체인 호출 중단
        }
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