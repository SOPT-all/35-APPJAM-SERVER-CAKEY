package com.cakey.user.dto;

public record UserInfoDto(
        String userName,
        String userEmail
) {
    public UserInfoDto(
            final String userName,
            final String userEmail
    ) {
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
