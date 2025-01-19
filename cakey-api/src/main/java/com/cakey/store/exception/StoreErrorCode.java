package com.cakey.store.exception;

import com.cakey.common.rescode.ApiCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum StoreErrorCode implements ApiCode {

    /**
     * 404 Not Found
     */
    STORE_NOT_FOUND_ENTITY(HttpStatus.NOT_FOUND, 40402, "스토어를 찾을 수 없습니다."),
    STORE_LIKES_NOT_FOUND_ENTITY(HttpStatus.NOT_FOUND, 40403, "스토어 좋아요를 찾을 수 없습니다."),




    /**
     * 409 Conflict
     */
    STORE_LIKES_CONFLICT(HttpStatus.CONFLICT, 40902, "이미 스토어 좋아요가 있습니다."),


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
