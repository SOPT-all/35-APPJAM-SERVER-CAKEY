package com.cakey.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Token {

    private String accessToken;

    private String refreshToken;

    public static Token of(
            final String accessToken,
            final String refreshToken
    ) {
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
