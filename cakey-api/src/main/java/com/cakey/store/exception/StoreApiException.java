package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorCode;
import com.cakey.exception.CakeyException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class StoreApiException extends CakeyException {
    private final ErrorCode errorCode;

    abstract HttpStatus getStatus();

}
