package com.rsupport.notice.interfaces.api.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiSuccessResponse<T> {
    private final int status;
    private final T data;

    public static ApiSuccessResponse<?> OK() {
        return new ApiSuccessResponse<>(HttpStatus.OK.value(), null);
    }

    public static <T> ApiSuccessResponse<T> OK(T data) {
        return new ApiSuccessResponse<>(HttpStatus.OK.value(), data);
    }

    public static <T> ApiSuccessResponse<T> CREATED(T data) {
        return new ApiSuccessResponse<>(HttpStatus.CREATED.value(), data);
    }
}
