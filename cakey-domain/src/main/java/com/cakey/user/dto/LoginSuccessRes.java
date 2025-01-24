package com.cakey.user.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record LoginSuccessRes(
        long userId,
        String userName,
        String accessToken
) {
    public static LoginSuccessRes of(
            final long userId,
            final String userName,
            final String accessToken) {
        return LoginSuccessRes.builder()
                .userId(userId)
                .userName(userName)
                .accessToken(accessToken)
                .build();
    }
}