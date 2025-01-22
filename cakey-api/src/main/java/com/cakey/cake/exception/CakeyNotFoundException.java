package com.cakey.cake.exception;

import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class CakeyNotFoundException extends CakeyApiBaseException {
    public CakeyNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return null;
    }
}
