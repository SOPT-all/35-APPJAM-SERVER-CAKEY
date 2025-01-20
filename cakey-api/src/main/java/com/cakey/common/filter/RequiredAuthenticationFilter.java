package com.cakey.common.filter;

import com.cakey.jwt.auth.JwtProvider;
import com.cakey.jwt.auth.UserAuthentication;
import com.cakey.jwt.auth.JwtValidationType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RequiredAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider; //로그인 필수

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
        throw new Exception();
    }
}