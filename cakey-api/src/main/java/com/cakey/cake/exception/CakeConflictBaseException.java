package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class CakeConflictBaseException extends CakeApiBaseException {
    protected CakeConflictBaseException(CakeErrorCode cakeErrorCode) {
        super(cakeErrorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
