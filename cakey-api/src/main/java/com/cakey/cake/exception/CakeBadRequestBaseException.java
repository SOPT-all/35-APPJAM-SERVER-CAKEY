package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class CakeBadRequestBaseException extends CakeApiBaseException {

    protected CakeBadRequestBaseException(ErrorBaseCode errorBaseCode) {
        super(errorBaseCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
