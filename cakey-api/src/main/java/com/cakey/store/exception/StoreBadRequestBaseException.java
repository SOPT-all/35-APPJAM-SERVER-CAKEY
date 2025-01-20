package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class StoreBadRequestBaseException extends StoreApiBaseException {

    protected StoreBadRequestBaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return null;
    }
}
