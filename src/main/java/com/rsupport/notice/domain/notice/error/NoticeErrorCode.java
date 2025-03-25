package com.rsupport.notice.domain.notice.error;

import com.rsupport.notice.common.error.CoreErrorCode;
import com.rsupport.notice.common.error.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode implements CoreErrorCode {
    NOTICE_NOT_FOUND(ErrorType.NOT_FOUND, "공지사항을 찾을 수 없습니다."),
    NOTICE_FILE_NOT_FOUND(ErrorType.NOT_FOUND, "공지사항 파일을 찾을 수 없습니다.");

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
