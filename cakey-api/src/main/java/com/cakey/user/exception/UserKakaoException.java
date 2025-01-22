package com.cakey.user.exception;

import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserKakaoException extends UserApiBaseException{
    public UserKakaoException(final ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
