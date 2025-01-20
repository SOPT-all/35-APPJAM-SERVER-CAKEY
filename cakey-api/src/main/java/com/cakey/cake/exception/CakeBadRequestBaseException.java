package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class CakeBadRequestBaseException extends CakeApiBaseException {

    protected CakeBadRequestBaseException(CakeErrorCode cakeErrorCode) {
        super(cakeErrorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
