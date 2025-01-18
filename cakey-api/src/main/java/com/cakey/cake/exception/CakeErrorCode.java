package com.cakey.cake.exception;

import com.cakey.common.rescode.ApiCode;
import org.springframework.http.HttpStatus;

public class CakeErrorCode implements ApiCode {








    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
