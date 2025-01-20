package com.cakey.common.filter;

import com.cakey.jwt.auth.JwtProvider;
import com.cakey.jwt.auth.UserAuthentication;
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
public class OptionalAuthenticationFilter extends OncePerRequestFilter { //로그인 상관 X
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String accessToken = getAccessTokenFromCookie(request);
        if (accessToken != null) {
            final Long userId = jwtProvider.getUserIdFromSubject(accessToken);
            SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userId, null, null));
        } else {
            SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(null, null, null));
        }

        filterChain.doFilter(request, response);
    }

    public String getAccessTokenFromCookie(@NonNull HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
