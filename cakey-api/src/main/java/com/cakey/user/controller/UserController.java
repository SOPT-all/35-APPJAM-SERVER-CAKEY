package com.cakey.user.controller;


import com.cakey.client.dto.LoginReq;
import com.cakey.common.resolver.user.UserId;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.rescode.SuccessCode;
import com.cakey.user.dto.JwtReissueRes;
import com.cakey.user.dto.LoginSuccessRes;
import com.cakey.user.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<?>> login(
            @RequestHeader(value = "Authorization") final String authorization,
            @RequestBody final LoginReq loginReq,
            HttpServletResponse response
            ) {
        final LoginSuccessRes loginSuccessRes = userService.login(authorization, loginReq.socialType(), loginReq.redirectUri(), response);

       return ApiResponseUtil.success(SuccessCode.OK, loginSuccessRes);
   }

    //유저 정보 조회(이름, 이메일)
    @GetMapping("/name-email")
    public ResponseEntity<BaseResponse<?>> getUserInfo(
            @UserId @Min(value = 1, message = "userId는 1이상이여야합니다.") final long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, userService.getUserInfo(userId));
    }

    //로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity<BaseResponse<?>> logout(
        @UserId @Min(value = 1, message = "userId는 1이상이여야합니다.") final long userId,
        HttpServletResponse response
    ) {
        userService.logout(userId, response);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    //jwt 재발급
    @PatchMapping("/reissue")
    public ResponseEntity<BaseResponse<?>> jwtReissue(
            @RequestHeader(value = "userId") final long userId,
            @CookieValue(name = "refreshToken") final Cookie cookie,
            final HttpServletResponse response

    ) {
        final JwtReissueRes jwtReissueRes = userService.jwtReissue(userId, cookie.getValue(), response);
        return ApiResponseUtil.success(SuccessCode.OK, jwtReissueRes);
    }
}
