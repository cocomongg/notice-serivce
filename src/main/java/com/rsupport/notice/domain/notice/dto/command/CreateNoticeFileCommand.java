package com.rsupport.notice.domain.notice.dto.command;

import com.rsupport.notice.domain.file.dto.FileInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateNoticeFileCommand {
    private final Long userId;
    private final String originalFileName;
    private final String filePath;
    private final int fileSize;

    public CreateNoticeFileCommand(Long userId, FileInfo fileInfo) {
        this.userId = userId;
        this.originalFileName = fileInfo.getFileName();
        this.filePath = fileInfo.getFilePath();
        this.fileSize = (int)fileInfo.getFileSize();
    }
}
