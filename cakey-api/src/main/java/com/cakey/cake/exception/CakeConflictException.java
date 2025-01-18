package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class CakeConflictException extends CakeApiException{
    protected CakeConflictException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
