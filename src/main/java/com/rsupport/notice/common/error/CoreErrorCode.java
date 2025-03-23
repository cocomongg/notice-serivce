package com.rsupport.notice.common.error;

public interface CoreErrorCode {
    String getErrorCode();
    ErrorType getErrorType();
    String getMessage();
}