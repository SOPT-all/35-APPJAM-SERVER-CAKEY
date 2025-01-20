package com.cakey.user.exception;


import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserBadRequestException extends UserApiBaseException{

    public UserBadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
