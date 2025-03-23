package com.rsupport.notice.domain.file.dto.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class FileUploadCommand {
    private final MultipartFile multipartFile;
}
