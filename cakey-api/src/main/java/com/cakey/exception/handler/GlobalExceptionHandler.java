package com.cakey.exception.handler;

import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 400 - MissingServletRequestParameterException
     * 발생 이유 : 필수 파라미터 없을 때 발생
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<?>> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        final String errorMessage = "누락된 파라미터 : " + e.getParameterName();
        return ApiResponseUtil.failure(ErrorCode.BAD_REQUEST_MISSING_PARAM, errorMessage);
    }


    /**
     * 400 - MethodArgumentTypeMismatchException
     * 발생 이유 : 메서드 인자의 타입이 잘못되었을 때 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        final String errorMessage = "잘못된 인자값 : " + e.getParameter().getParameterName();
        return ApiResponseUtil.failure(ErrorCode.BAD_REQUEST_METHOD_ARGUMENT_TYPE, errorMessage);
    }
}
