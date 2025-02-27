package com.cakey.user.exception;

import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserUnAuthorizedException extends UserApiBaseException {
    public UserUnAuthorizedException(final ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
