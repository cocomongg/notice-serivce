package com.rsupport.notice.application.notice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WriterInfo {
    private final Long userId;
    private final String username;
}
