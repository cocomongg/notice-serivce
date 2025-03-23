package com.rsupport.notice.application.notice.dto.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class UploadNoticeFileCommand {
    private final MultipartFile multipartFile;
    private final Long userId;
}
