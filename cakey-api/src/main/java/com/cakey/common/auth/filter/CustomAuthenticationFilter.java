package com.cakey.common.auth.filter;

import com.cakey.common.auth.JwtTokenProvider;
import com.cakey.common.auth.UserAuthentication;
import com.cakey.jwt.service.TokenService;
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
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
//    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String accessToken = getAccessTokenFromCookie(request);
            if (accessToken != null) {
                final Long userId = jwtTokenProvider.getUserFromJwt(accessToken);
                SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userId, null, null));
            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
        }
        filterChain.doFilter(request, response);
    }

    public String getAccessTokenFromCookie(@NonNull HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
