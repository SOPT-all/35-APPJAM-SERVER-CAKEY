package com.cakey.client.dto;

import com.cakey.client.SocialType;
import com.cakey.jwt.domain.UserRole;

public record KakaoUserInfoDto(
        String socialId,
        String name,
        SocialType socialType,
        UserRole userRole,
        String socialEmail
) {
    public static KakaoUserInfoDto of(
            final String socialId,
            final String name,
            final SocialType socialType,
            final UserRole userRole,
            final String socialEmail
    ) {
        return new KakaoUserInfoDto(socialId, name, socialType, userRole, socialEmail);
    }
}
