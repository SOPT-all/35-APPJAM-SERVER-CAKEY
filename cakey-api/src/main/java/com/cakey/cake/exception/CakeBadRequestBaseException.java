package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CakeBadRequestBaseException extends CakeApiBaseException {

    protected CakeBadRequestBaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
