package com.cakey.client.kakao.api.dto;

import com.cakey.client.SocialType;
import com.cakey.jwt.domain.UserRole;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserCreateDto(
        String userName,
        UserRole userRole,
        SocialType socialType,
        long socialId,
        String socialEmail
) {
    public static UserCreateDto of(final String userName, final UserRole userRole, final SocialType socialType, final long socialId, final String socialEmail) {
        return UserCreateDto.builder()
                .userName(userName)
                .userRole(userRole)
                .socialType(socialType)
                .socialId(socialId)
                .socialEmail(socialEmail)
                .build();
    }

}
