package com.cakey.user.exception;

import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserDiscordFeignException extends UserApiBaseException {
    public UserDiscordFeignException(final ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}
