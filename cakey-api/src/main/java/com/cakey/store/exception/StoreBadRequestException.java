package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class StoreBadRequestException extends StoreApiException {

    protected StoreBadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return null;
    }
}
