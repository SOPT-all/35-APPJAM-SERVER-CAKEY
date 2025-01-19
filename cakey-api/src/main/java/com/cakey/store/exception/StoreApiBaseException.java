package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import com.cakey.exception.CakeBaseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class StoreApiBaseException extends CakeBaseException {
    private final ErrorBaseCode errorBaseCode;

    abstract HttpStatus getStatus();

}
