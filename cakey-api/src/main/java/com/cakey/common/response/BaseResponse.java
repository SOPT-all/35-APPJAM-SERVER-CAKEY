package com.cakey.common.response;

import com.cakey.ApiCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder(access = lombok.AccessLevel.PRIVATE)
public class BaseResponse<T> {
    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static BaseResponse<?> of(final ApiCode apiMessage) {
        return BaseResponse.builder()
                .code(apiMessage.getCode())
                .message(apiMessage.getMessage())
                .build();
    }

    public static <T> BaseResponse<?> of(SuccessCode successCode, T data) {
        return BaseResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    public static BaseResponse<?> of(final int code, final String message) {
        return BaseResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
