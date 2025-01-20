package com.cakey.user.exception;

import com.cakey.exception.CakeBaseException;
import com.cakey.rescode.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserApiBaseException extends CakeBaseException {
    private final ErrorCode errorCode;

    abstract HttpStatus getStatus();
}
