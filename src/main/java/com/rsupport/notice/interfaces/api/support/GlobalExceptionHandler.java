package com.rsupport.notice.interfaces.api.support;


import com.rsupport.notice.common.error.CoreErrorCode;
import com.rsupport.notice.common.error.CoreException;
import com.rsupport.notice.interfaces.api.common.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CoreException.class)
    public ResponseEntity<ApiErrorResponse> handleCoreException(CoreException e) {
        CoreErrorCode errorCode = e.getCoreErrorCode();
        log.error("CoreException occurred: {}", errorCode.getMessage(), e);

        HttpStatus httpStatus;
        switch(errorCode.getErrorType()) {
            case INTERNAL_SERVER_ERROR -> httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            case NOT_FOUND -> httpStatus = HttpStatus.NOT_FOUND;
            case BAD_REQUEST -> httpStatus = HttpStatus.BAD_REQUEST;
            default -> httpStatus = HttpStatus.OK;
        }

        ApiErrorResponse errorResponse =
            new ApiErrorResponse(errorCode.getErrorCode(), errorCode.getMessage());

        return ResponseEntity.status(httpStatus)
            .body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        log.error("Unexpected error occurred", e);

        ApiErrorResponse errorResponse =
            new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
    }
}
