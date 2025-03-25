package com.rsupport.notice.application.notice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

@Getter
@RequiredArgsConstructor
public class NoticeFileResourceInfo {

    private final String fileName;
    private final int fileSize;
    private final Resource resource;
}
