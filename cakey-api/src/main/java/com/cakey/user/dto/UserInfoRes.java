package com.cakey.user.dto;


public record UserInfoRes(
        String userName,
        String userEmail
) {
    public static UserInfoRes from(final UserInfoDto userInfoDto) {
        return new UserInfoRes(userInfoDto.userName(), userInfoDto.userEmail());
    }
}
