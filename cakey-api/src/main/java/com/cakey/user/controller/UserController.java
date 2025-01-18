package com.cakey.user.controller;

//import com.cakey.client.dto.LoginReq;
//import com.cakey.common.resolver.user.UserId;
//import com.cakey.common.response.ApiResponseUtil;
//import com.cakey.common.response.BaseResponse;
//import com.cakey.common.rescode.SuccessCode;
//import com.cakey.jwt.service.TokenService;
//import com.cakey.user.dto.LoginSuccessRes;
//import com.cakey.client.dto.KakaoUserInfoRes;
//import com.cakey.common.resolver.user.UserId;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.common.rescode.SuccessCode;
//import com.cakey.user.dto.UserInfoRes;
import com.cakey.user.service.UserService;
//import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//   private final TokenService tokenService;
//
//    @PostMapping("/login")
//    public ResponseEntity<BaseResponse<?>> login(@RequestHeader(value = "Authorization") final String authorization,
//                                                 @RequestBody final LoginReq loginReq,
//                                                 HttpServletResponse response) {
//       LoginSuccessRes loginSuccessRes = userService.create(authorization, loginReq);
//       response.addHeader("Set-Cookie", userService.accessCookie(loginSuccessRes).toString());
//       response.addHeader("Set-Cookie", userService.refreshCookie(loginSuccessRes).toString());
//       return ApiResponseUtil.success(SuccessCode.OK);
//   }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getUserInfo(@RequestHeader(value = "userId") final Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, userService.getUserInfo(userId));
    }
}
