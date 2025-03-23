package com.rsupport.notice.domain.file.validator;

import com.rsupport.notice.common.error.CoreException;
import com.rsupport.notice.domain.file.error.FileErrorCode;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator {

    private final Long maxFileSize;
    private final Set<String> allowedMimeTypes;

    public FileValidator(
        @Value("${file.upload.max-size:10485760}") Long maxFileSize,
        @Value("${file.upload.allowed-mime-types:image/jpeg,image/png,application/pdf}") Set<String> allowedMimeTypes
    ) {
        this.maxFileSize = maxFileSize;
        this.allowedMimeTypes = allowedMimeTypes;
    }

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CoreException(FileErrorCode.FILE_IS_EMPTY);
        }

        long fileSize = file.getSize();
        if (fileSize > maxFileSize) {
            throw new CoreException(FileErrorCode.FILE_SIZE_EXCEEDED, "fileSize: " + fileSize);
        }

        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !allowedMimeTypes.contains(contentType.toLowerCase())) {
            throw new CoreException(FileErrorCode.FILE_TYPE_NOT_ALLOWED, "contentType: " + contentType);
        }
    }
}
