package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class CakeBadRequestException extends CakeApiException{

    protected CakeBadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
