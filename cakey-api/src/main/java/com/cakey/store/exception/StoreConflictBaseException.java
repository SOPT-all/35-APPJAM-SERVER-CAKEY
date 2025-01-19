package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class StoreConflictBaseException extends StoreApiBaseException {
    protected StoreConflictBaseException(ErrorBaseCode errorBaseCode) {
        super(errorBaseCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
