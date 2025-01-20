package com.cakey.cake.exception;

import com.cakey.rescode.ErrorCode;
import com.cakey.exception.CakeBaseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CakeApiBaseException extends CakeBaseException { ///abstract 이유 : 추상 클래스로 상속받는 자식 클래스가 구체적인 구현을 제공하도록 강제
    private final ErrorCode errorCode;

    abstract HttpStatus getStatus();
}
