package com.cakey.user.dto;

public record AccessTokenGetSuccess(
        String accessToken
) {
    public static AccessTokenGetSuccess of(String accessToken) {
        return new AccessTokenGetSuccess(accessToken);
    }
}
