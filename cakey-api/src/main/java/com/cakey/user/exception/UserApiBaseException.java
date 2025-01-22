package com.cakey.user.exception;

import com.cakey.exception.CakeyBaseException;
import com.cakey.rescode.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class UserApiBaseException extends CakeyBaseException {
    private final ErrorCode errorCode;

    abstract HttpStatus getStatus();
}
