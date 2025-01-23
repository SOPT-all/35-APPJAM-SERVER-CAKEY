package com.cakey.user.exception;

import com.cakey.rescode.ApiCode;
import com.cakey.rescode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {


    /**
     * 404 Not Found
     */
    USER_SOCIAL_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, 40430, "유저의 소셜타입을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 40431, "유저를 찾을 수 없습니다."),

    /**
     * 500 Server Internal Error
     */
    KAKAO_LOGIN_FAILED(HttpStatus.BAD_REQUEST, 50030, "카카오 로그인에 실패하였습니다"),
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
