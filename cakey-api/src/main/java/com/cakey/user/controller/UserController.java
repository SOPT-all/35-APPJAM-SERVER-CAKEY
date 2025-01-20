package com.cakey.user.controller;


import com.cakey.client.dto.LoginReq;
import com.cakey.common.resolver.user.UserId;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.rescode.SuccessCode;
import com.cakey.user.dto.LoginSuccessRes;
import com.cakey.user.service.UserService;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<?>> login(
            @RequestHeader(value = "Authorization") final String authorization,
                                                 @RequestBody final LoginReq loginReq) {
        final LoginSuccessRes loginSuccessRes = userService.login(authorization, loginReq);
       return ApiResponseUtil.success(SuccessCode.OK, loginSuccessRes);
   }

    //유저 정보 조회(이름, 이메일)
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getUserInfo(
            @UserId final Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, userService.getUserInfo(userId));
    }

    //로그아웃
//    @

//    //jwt 재발급
//    @GetMapping("/reissue")
//    public ResponseEntity<BaseResponse<?>> jwtReissue(
//            @CookieValue(name = "refreshToken") Cookie cookie
//    ) {
//        final String refreshToken = cookie.getValue();
//        final LoginSuccessRes loginSuccessRes = userService.jwtReissue(refreshToken);
//        return ApiResponseUtil.success(SuccessCode.OK, loginSuccessRes);
//    }
}
