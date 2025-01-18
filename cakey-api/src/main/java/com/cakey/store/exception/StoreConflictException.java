package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class StoreConflictException extends StoreApiException {
    protected StoreConflictException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
