package com.rsupport.notice.domain.file.service;

import com.rsupport.notice.domain.file.dto.FileInfo;
import com.rsupport.notice.domain.file.dto.command.FileUploadCommand;

public interface StorageService {
    FileInfo upload(FileUploadCommand command);
    String moveObject(String sourcePath, String targetDir);
    void remove(String filePath);
}
