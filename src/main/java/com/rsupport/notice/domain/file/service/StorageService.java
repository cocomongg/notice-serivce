package com.rsupport.notice.domain.file.service;

import com.rsupport.notice.domain.file.dto.FileInfo;
import com.rsupport.notice.domain.file.dto.command.FileUploadCommand;
import org.springframework.core.io.Resource;

public interface StorageService {
    FileInfo upload(FileUploadCommand command);
    String moveObject(String sourcePath, String targetDir);
    void remove(String filePath);
    Resource loadAsResource(String fileFullPath);
}
