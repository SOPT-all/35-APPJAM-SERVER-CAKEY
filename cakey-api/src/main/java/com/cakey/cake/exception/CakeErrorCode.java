package com.cakey.cake.exception;

import com.cakey.rescode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CakeErrorCode implements ErrorCode {

    /**
     * 404 Not Found
     */
    CAKE_NOT_FOUND_ENTITY(HttpStatus.NOT_FOUND, 40410, "케이크를 찾을 수 없습니다."),
    CAKE_LIKES_NOT_FOUND_ENTITY(HttpStatus.NOT_FOUND, 40411, "케이크 좋아요를 찾을 수 없습니다."),



    /**
     * 409 Conflict
     */
    CAKE_LIKES_CONFLICT(HttpStatus.CONFLICT, 40910, "이미 케이크 좋아요가 있습니다."),

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
