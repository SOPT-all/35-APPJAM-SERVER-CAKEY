package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class CakeNotFoundException extends CakeApiBaseException{
    public CakeNotFoundException(CakeErrorCode cakeErrorCode) {
        super(cakeErrorCode);
    }

    @Override
    HttpStatus getStatus() {
        return null;
    }
}
