package com.rsupport.notice.domain.file.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInfo {
    private final String fileName;
    private final long fileSize;
    private final String filePath;
    private final String fileType;
}
