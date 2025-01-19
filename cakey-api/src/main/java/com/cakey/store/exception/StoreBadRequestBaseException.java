package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class StoreBadRequestBaseException extends StoreApiBaseException {

    protected StoreBadRequestBaseException(ErrorBaseCode errorBaseCode) {
        super(errorBaseCode);
    }

    @Override
    HttpStatus getStatus() {
        return null;
    }
}
