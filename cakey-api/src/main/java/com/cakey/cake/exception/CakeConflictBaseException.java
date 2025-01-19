package com.cakey.cake.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class CakeConflictBaseException extends CakeApiBaseException {
    protected CakeConflictBaseException(ErrorBaseCode errorBaseCode) {
        super(errorBaseCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
