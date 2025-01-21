package com.cakey.config;

import com.cakey.common.filter.OptionalAuthenticationFilter;
import com.cakey.common.filter.RequiredAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final OptionalAuthenticationFilter optionalAuthenticationFilter;
    private final RequiredAuthenticationFilter requiredAuthenticationFilter;

    @Bean
    public FilterRegistrationBean<OptionalAuthenticationFilter> optionalAuthenticationFilterRegistration() {
        FilterRegistrationBean<OptionalAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(optionalAuthenticationFilter);

        // 필수 아닌 거
        registrationBean.addUrlPatterns(
                "/api/v1/cake/rank",
                "/api/v1/store/latest/*",
                "/api/v1/store/popularity/*",
                "/api/v1/cake/station/latest/*",
                "/api/v1/cake/station/popularity/*",
                "/api/v1/store/select/*",
                "/api/v1/cake/latest/*",
                "/api/v1/cake/popularity/*",
                "/api/v1/cake/select/*",
                "/api/v1/store/design/*"
                );

        /// 필터 우선순위 설정
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequiredAuthenticationFilter> requiredAuthenticationFilterRegistration() {
        FilterRegistrationBean<RequiredAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(requiredAuthenticationFilter);

        // 필수
        registrationBean.addUrlPatterns(
                "/api/v1/store/likes/latest/*",
                "/api/v1/store/likes/popularity/*",
                "/api/v1/store/likes/coordinate",
                "/api/v1/cake/store/likes/cake/latest/*",
                "/api/v1/cake/store/likes/cake/popularity/*",
                "/api/v1/store/likes/*",
                "/api/v1/cake/likes/*",
                "/api/v1/cake/likes/latest/*",
                "/api/v1/cake/likes/popularity/*",
                "/api/v1/user/name-email"
        );

        registrationBean.setOrder(2);

        return registrationBean;
    }
}