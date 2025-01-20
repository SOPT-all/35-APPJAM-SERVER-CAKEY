package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class CakeForbiddenBaseException extends CakeApiBaseException {
    protected CakeForbiddenBaseException(CakeErrorCode cakeErrorCode) {
        super(cakeErrorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
