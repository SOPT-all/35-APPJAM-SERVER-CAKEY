package com.cakey.exception.handler;

import com.cakey.cake.exception.CakeApiBaseException;
import com.cakey.rescode.ErrorBaseCode;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.store.exception.StoreApiBaseException;
import com.cakey.user.exception.UserApiBaseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CakeApiBaseException.class)
    public ResponseEntity<BaseResponse<?>> handleCakeApiBaseException(final CakeApiBaseException e) {
        return ApiResponseUtil.failure(e.getErrorCode());
    }

    @ExceptionHandler(StoreApiBaseException.class)
    public ResponseEntity<BaseResponse<?>> handleStoreApiBaseException(final StoreApiBaseException e) {
        return ApiResponseUtil.failure(e.getErrorCode());
    }

    @ExceptionHandler(UserApiBaseException.class)
    public ResponseEntity<BaseResponse<?>> handleUserApiBaseException(final UserApiBaseException e) {
        return ApiResponseUtil.failure(e.getErrorCode());
    }


        /**
         * 400 - MethodArgumentNotValidException
         * 발생 이유 : @Valid 검증 실패 (@Request Body)
         */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        // 모든 검증 실패 메시지를 추출하고 줄바꿈으로 연결
        final String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n")); // 메시지를 줄바꿈으로 연결

        return ApiResponseUtil.failure(ErrorBaseCode.BAD_REQUEST_REQUEST_BODY_VALID, errorMessage);
    }

    /**
     * 400 - ConstraintViolationException
     * 발생 이유 : @Validated 검증 실패
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<?>> handleConstraintViolationException(final ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> String.format("Field '%s': %s",
                        violation.getPropertyPath(),
                        violation.getMessage()
                ))
                .collect(Collectors.joining(", "));
        return ApiResponseUtil.failure(ErrorBaseCode.BAD_REQUEST_REQUEST_BODY_VALID, errorMessage);
    }

    /**
     * 400 - HandlerMethodValidationException
     * 발생 이유 : @Valid 검증 실패 (@Request Param, @ModelAttribute)
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodValidationException(final HandlerMethodValidationException e) {
        // 모든 검증 실패 메시지를 추출하고 줄바꿈으로 연결
        final String errorMessage = e.getAllValidationResults().stream()
                .flatMap(violation -> violation.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        return ApiResponseUtil.failure(ErrorBaseCode.BAD_REQUEST_REQUEST_PARAM_MODELATTRI, errorMessage);
    }

    /**
     * 400 - MissingServletRequestParameterException
     * 발생 이유 : 필수 파라미터 없을 때 발생
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<?>> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        final String errorMessage = "누락된 파라미터 : " + e.getParameterName();
        return ApiResponseUtil.failure(ErrorBaseCode.BAD_REQUEST_MISSING_PARAM, errorMessage);
    }

    /**
     * 400 - MethodArgumentTypeMismatchException
     * 발생 이유 : 메서드 인자의 타입이 잘못되었을 때 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        final String errorMessage = "잘못된 인자값 : " + e.getParameter().getParameterName();
        return ApiResponseUtil.failure(ErrorBaseCode.BAD_REQUEST_METHOD_ARGUMENT_TYPE, errorMessage);
    }

    /**
     * 400 - HttpMessageNotReadableException
     * 발생 이유 : json 바인딩 오류 or @requsetBody 필수값이 누락되거나 데이터 자료형이 잘못된 경우 or json 이외의 형식(데이터포맷)일 경우에 발생
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<?>> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        if (e.getCause() instanceof JsonMappingException jsonMappingException) { //json 매핑이 잘못되었을 경우
            // 모든 잘못된 필드 추출
            String errorMessage = jsonMappingException.getPath().stream()
                    .map(ref -> String.format("잘못된 필드 값 : '%s'", ref.getFieldName()))
                    .collect(Collectors.joining("\n"));

            return ApiResponseUtil.failure(ErrorBaseCode.BAD_REQUEST_NOT_READABLE, errorMessage);
        } else { //그 외의 경우들
            return ApiResponseUtil.failure(ErrorBaseCode.BAD_REQUEST_NOT_READABLE);
        }
    }

    /**
     * 400 - IllegalArgumentException
     * 발생 이유: 잘못된 인자값이 전달되었을 때 발생
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<?>> handleException(IllegalArgumentException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.BAD_REQUEST, e.getCause().getMessage());
    }

    /**
     * 404 - EntityNotFoundException
     * 발생 이유 :  리소스를 찾을 수 없음
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleEntityNotFoundException(final EntityNotFoundException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.NOT_FOUND_ENTITY);
    }

    /**
     * 404 - NoResourceFoundException
     * 발생 이유 : 잘못된 엔드포인트로 요청했을 때 발생
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleNoResourceFoundException(final NoResourceFoundException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.NOT_FOUND_API);
    }

    /**
     * 404 - NoHandlerFoundException
     * 발생 이유 : 잘못된 api로 요청했을 때 발생
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleNoHandlerFoundException(final NoHandlerFoundException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.NOT_FOUND_API);
    }

    /**
     * 405 - HttpRequestMethodNotSupportedException
     * 발생 이유 : 지원하지 않는 HTTP Method로 요청했을 때, 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<?>> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 409 - DataIntegrityViolationException
     * 발생 이유 : Unique Key 제약 조건 위반 or NULL 값이 들어갈 수 없는 컬럼에 NULL 삽입 or 외래 키 제약 조건 위반
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<?>> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        if (e.getCause() instanceof ConstraintViolationException constraintViolationException) {

            // 제약 조건 이름 추출
            String constraintName = constraintViolationException.getConstraintViolations().toString();
            String errorMessage = String.format("제약 조건 '%s' 위반이 발생했습니다.", constraintName);
            return ApiResponseUtil.failure(ErrorBaseCode.INTEGRITY_CONFLICT, errorMessage);
        } else {
            return ApiResponseUtil.failure(ErrorBaseCode.INTEGRITY_CONFLICT);
        }
    }


    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<BaseResponse<?>> handleInvocationTargetException(final InvocationTargetException e) {
        Throwable cause = e.getCause();
        System.out.println(cause.getMessage());
        log.error(cause.getMessage());
        return ApiResponseUtil.failure(ErrorBaseCode.INTERNAL_SERVER_ERROR, cause.getMessage());
    }

    /**
     * 500 - 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleAllExceptions(final Exception e) {
        log.error("e.getMessage() + e.getCause().getMessage()");
        return ApiResponseUtil.failure(ErrorBaseCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
