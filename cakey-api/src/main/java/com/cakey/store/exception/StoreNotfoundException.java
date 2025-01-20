package com.cakey.store.exception;

import com.cakey.rescode.ErrorCode;
import org.springframework.http.HttpStatus;

public class StoreNotfoundException extends StoreApiBaseException {

  public StoreNotfoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  @Override
  HttpStatus getStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
