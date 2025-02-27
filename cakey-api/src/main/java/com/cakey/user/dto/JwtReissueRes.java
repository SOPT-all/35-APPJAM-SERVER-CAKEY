package com.cakey.user.dto;

public record JwtReissueRes(
        String accessToken
) {
    public static JwtReissueRes of(final String accessToken) {
        return new JwtReissueRes(accessToken);
    }
}
