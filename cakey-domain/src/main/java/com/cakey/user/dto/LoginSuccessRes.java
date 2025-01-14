package com.cakey.user.dto;

public record LoginSuccessRes(
        String accessToken,
        String refreshToken
) {
    public static LoginSuccessRes of(
            final String accessToken,
            final String refreshToken) {
        return new LoginSuccessRes(accessToken, refreshToken);
    }
}