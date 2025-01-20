package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class StoreForbiddenBaseException extends StoreApiBaseException {
    protected StoreForbiddenBaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
