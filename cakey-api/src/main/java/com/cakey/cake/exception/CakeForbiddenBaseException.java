package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class CakeForbiddenBaseException extends CakeApiBaseException {
    protected CakeForbiddenBaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
