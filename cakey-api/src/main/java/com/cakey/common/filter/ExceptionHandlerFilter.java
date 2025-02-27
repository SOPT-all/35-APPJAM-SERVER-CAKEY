package com.cakey.common.filter;

import com.cakey.Constants;
import com.cakey.common.response.BaseResponse;
import com.cakey.rescode.ErrorBaseCode;
import com.cakey.rescode.ErrorCode;
import com.cakey.user.exception.UserUnAuthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UserUnAuthorizedException e) {
            handleUnauthorizedException(response, e);
        } catch (Exception ee) {
            handleException(response, ee);
        }
    }

    private void handleUnauthorizedException(HttpServletResponse response, Exception e) throws IOException {
        UserUnAuthorizedException ue = (UserUnAuthorizedException) e;
        ErrorCode errorCode = ue.getErrorCode();
        HttpStatus httpStatus = errorCode.getHttpStatus();
        setResponse(response, httpStatus, errorCode);
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        log.error("-------------- Exception Handler Filter ------------ \n" + e.getCause() + e.getMessage());
        log.error("\n ---------------------------------------------------------");
        setResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, ErrorBaseCode.INTERNAL_SERVER_ERROR);
    }

    private void setResponse(HttpServletResponse response, HttpStatus httpStatus, ErrorCode errorBaseCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Constants.CHARACTER_TYPE);
        response.setStatus(httpStatus.value());
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(BaseResponse.of(errorBaseCode)));
    }
}
