package com.rsupport.notice.domain.file.error;

import com.rsupport.notice.common.error.CoreErrorCode;
import com.rsupport.notice.common.error.ErrorType;

public enum FileErrorCode implements CoreErrorCode {
    FILE_IS_EMPTY(ErrorType.BAD_REQUEST, "업로드할 파일이 존재하지 않습니다."),
    FILE_DIR_CREATION_FAILED(ErrorType.INTERNAL_SERVER_ERROR, "파일 업로드 디렉토리를 생성하는데 실패했습니다."),
    FILE_UPLOAD_FAILED(ErrorType.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_SIZE_EXCEEDED(ErrorType.BAD_REQUEST, "파일 크기가 최대 허용 크기를 초과했습니다."),
    FILE_TYPE_NOT_ALLOWED(ErrorType.BAD_REQUEST, "허용되지 않는 파일 타입입니다."),
    FILE_MOVE_FAILED(ErrorType.INTERNAL_SERVER_ERROR, "파일 이동에 실패했습니다."),;

    private final ErrorType errorType;
    private final String message;

    FileErrorCode(ErrorType errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

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
