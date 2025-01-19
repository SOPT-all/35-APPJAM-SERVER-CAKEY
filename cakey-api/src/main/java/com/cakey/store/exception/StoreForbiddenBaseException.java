package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class StoreForbiddenBaseException extends StoreApiBaseException {
    protected StoreForbiddenBaseException(ErrorBaseCode errorBaseCode) {
        super(errorBaseCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
