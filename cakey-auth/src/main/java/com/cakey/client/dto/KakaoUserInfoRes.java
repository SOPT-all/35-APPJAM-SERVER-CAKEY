package com.cakey.client.dto;

import com.cakey.client.SocialType;
import com.cakey.jwt.domain.UserRole;

public record KakaoUserInfoRes(
        String socialId,
        String name,
        SocialType socialType,
        UserRole userRole
) {
    public static KakaoUserInfoRes of(
            final String socialId,
            final String name,
            final SocialType socialType,
            final UserRole userRole
    ) {
        return new KakaoUserInfoRes(socialId, name, socialType, userRole);
    }
}
