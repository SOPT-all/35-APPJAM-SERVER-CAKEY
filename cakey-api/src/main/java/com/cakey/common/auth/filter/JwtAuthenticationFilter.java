//package com.cakey.common.auth.filter;
//
//import com.cakey.common.auth.JwtTokenProvider;
//import com.cakey.common.auth.UserAuthentication;
//import com.cakey.common.auth.JwtValidationType;
//import com.cakey.exception.CakeyException;
//import com.cakey.exception.ErrorCode;
//import com.cakey.jwt.service.TokenService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//        try {
//            final String token = getAccessTokenFromCookie(request);
//            if (token == null) {
//                //todo: exception 처리
//            }
//
//            if (jwtTokenProvider.validateToken(token) == JwtValidationType.INVALID_JWT_TOKEN) {
//                //todo: exception 처리
//            }
//
//            final Long userId = jwtTokenProvider.getUserFromJwt(token);
//            SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userId, null, null));
//
//        } catch (Exception e) {
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    private String getAccessTokenFromCookie(final HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for(Cookie cookie : cookies) {
//                if (cookie.getName().equals("accessToken")) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//}