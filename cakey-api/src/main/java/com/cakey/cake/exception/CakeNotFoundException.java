package com.cakey.cake.exception;

import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class CakeNotFoundException extends CakeApiBaseException{
    public CakeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return null;
    }
}
