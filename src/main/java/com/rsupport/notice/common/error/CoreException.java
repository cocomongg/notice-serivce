package com.rsupport.notice.common.error;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {
    private final CoreErrorCode coreErrorCode;

    public CoreException(CoreErrorCode coreErrorCode) {
        super(coreErrorCode.getMessage());
        this.coreErrorCode = coreErrorCode;
    }

    public CoreException(CoreErrorCode coreErrorCode, String message) {
        super(String.format("%s: %s", coreErrorCode.getMessage(), message));
        this.coreErrorCode = coreErrorCode;
    }
}
