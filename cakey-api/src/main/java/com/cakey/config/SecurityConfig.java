package com.cakey.config;

//import com.cakey.common.auth.CustomAccessDeniedHandler;
//import com.cakey.common.auth.CustomJwtAuthenticationEntryPoint;
//import com.cakey.common.auth.JwtTokenProvider;
//import com.cakey.common.auth.filter.OptionalAuthenticationFilter;
//import com.cakey.common.auth.filter.RequiredAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final RequiredAuthenticationFilter jwtAuthenticationFilter;
//    private final OptionalAuthenticationFilter customAuthenticationFilter;
//    private final CustomAccessDeniedHandler customAccessDeniedHandler;
//    private final CustomJwtAuthenticationEntryPoint customJwtAuthenticationEntryPoint;
//    private final JwtTokenProvider jwtTokenProvider;

//    private static final String[] AUTH_WHITELIST = {
//            "/actuator/health",
//            "/api/v1/user/login",
//            "/token-refresh",
//            "/api/v1/user/hi",
//    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( auth -> auth.anyRequest().permitAll()) //todo: 추후 변경
                .build();
//                .exceptionHandling(
//                        exception -> exception.authenticationEntryPoint(customJwtAuthenticationEntryPoint)
//                                .accessDeniedHandler(customAccessDeniedHandler))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(AUTH_WHITELIST).permitAll() // 화이트리스트 경로 허용
//                        .anyRequest().authenticated())
//                .addFilterBefore(new RequiredAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new OptionalAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
//                .build();
    }

//    @Bean
//    public FilterRegistrationBean<RequiredAuthenticationFilter> firstFilterFilterRegistrationBean() {
//        FilterRegistrationBean<RequiredAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(jwtAuthenticationFilter);
//        registrationBean.addUrlPatterns("/api/v1/user/test1"); // /test1 경로에만 FirstFilter 적용
//        registrationBean.setOrder(1); // 필터 순서 설정
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<OptionalAuthenticationFilter> secondFilterFilterRegistrationBean() {
//        FilterRegistrationBean<OptionalAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(customAuthenticationFilter);
//        registrationBean.addUrlPatterns("/v1/user/test2"); // /test2 경로에만 SecondFilter 적용
//        registrationBean.setOrder(2); // 필터 순서 설정
//        return registrationBean;
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers(AUTH_WHITELIST);
//    }
}