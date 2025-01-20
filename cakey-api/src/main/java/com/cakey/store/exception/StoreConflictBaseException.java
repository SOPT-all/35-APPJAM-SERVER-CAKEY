package com.cakey.store.exception;

import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class StoreConflictBaseException extends StoreApiBaseException {
    protected StoreConflictBaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
