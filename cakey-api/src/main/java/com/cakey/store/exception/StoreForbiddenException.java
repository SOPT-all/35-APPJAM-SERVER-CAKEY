package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class StoreForbiddenException extends StoreApiException {
    protected StoreForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
