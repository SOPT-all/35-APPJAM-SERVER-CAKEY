package com.cakey.cake.exception;

import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class CakeBadRequestBaseException extends CakeApiBaseException {

    protected CakeBadRequestBaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
