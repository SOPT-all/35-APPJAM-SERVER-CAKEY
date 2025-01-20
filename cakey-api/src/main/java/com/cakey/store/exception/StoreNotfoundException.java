package com.cakey.store.exception;

import com.cakey.common.rescode.ErrorBaseCode;
import org.springframework.http.HttpStatus;

public class StoreNotfoundException extends StoreApiBaseException {

  public StoreNotfoundException(StoreErrorCode storeErrorCode) {
    super(storeErrorCode);
  }

  @Override
  HttpStatus getStatus() {
    return null;
  }
}
