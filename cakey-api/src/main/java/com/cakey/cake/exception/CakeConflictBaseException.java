package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class CakeConflictBaseException extends CakeApiBaseException {
    protected CakeConflictBaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
