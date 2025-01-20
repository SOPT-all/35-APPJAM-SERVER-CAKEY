package com.cakey.user.controller;


import com.cakey.client.dto.LoginReq;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.rescode.SuccessCode;
import com.cakey.user.dto.LoginSuccessRes;
import com.cakey.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
       userService.login(authorization, loginReq);
       return ApiResponseUtil.success(SuccessCode.OK);
   }

    //유저 정보 조회(이름, 이메일)
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getUserInfo(@RequestHeader(value = "userId") final Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, userService.getUserInfo(userId));
    }
}
