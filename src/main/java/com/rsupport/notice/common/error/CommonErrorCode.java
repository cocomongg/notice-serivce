package com.rsupport.notice.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements CoreErrorCode{
    INTERNAL_SERVER_ERROR(ErrorType.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");

    private final ErrorType errorType;
    private final String message;

    @Override
    public String getErrorCode() {
        return this.name();
    }

    @Override
    public ErrorType getErrorType() {
        return this.errorType;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
