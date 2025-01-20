package com.cakey.store.exception;

import com.cakey.rescode.ErrorCode;
import com.cakey.exception.CakeBaseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class StoreApiBaseException extends CakeBaseException {
    private final ErrorCode errorCode;

    abstract HttpStatus getStatus();

}
