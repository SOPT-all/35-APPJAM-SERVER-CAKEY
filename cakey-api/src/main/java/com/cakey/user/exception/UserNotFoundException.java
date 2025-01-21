package com.cakey.user.exception;

import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserApiBaseException {

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
