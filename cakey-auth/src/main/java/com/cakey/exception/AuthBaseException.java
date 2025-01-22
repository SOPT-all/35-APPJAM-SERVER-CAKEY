package com.cakey.exception;

import com.cakey.rescode.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public abstract class AuthBaseException extends CakeBaseException { }
