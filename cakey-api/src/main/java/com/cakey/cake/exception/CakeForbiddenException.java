package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class CakeForbiddenException extends CakeApiException{
    protected CakeForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
