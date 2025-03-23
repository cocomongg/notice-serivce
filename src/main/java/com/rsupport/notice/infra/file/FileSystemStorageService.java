package com.rsupport.notice.infra.file;

import com.rsupport.notice.common.error.CoreException;
import com.rsupport.notice.domain.file.dto.FileInfo;
import com.rsupport.notice.domain.file.dto.command.FileUploadCommand;
import com.rsupport.notice.domain.file.error.FileErrorCode;
import com.rsupport.notice.domain.file.service.StorageService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

    private final String uploadDir;
    private final Path absoluteUploadPath;

    public FileSystemStorageService(@Value("${file.upload.tmp-dir:uploads/tmp}") String uploadDir) {
        this.uploadDir = uploadDir;
        this.absoluteUploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.absoluteUploadPath);
        } catch (Exception e) {
            log.error("", e);
            throw new CoreException(FileErrorCode.FILE_DIR_CREATION_FAILED);
        }
    }

    @Override
    public FileInfo upload(FileUploadCommand command) {
        MultipartFile multipartFile = command.getMultipartFile();
        String originalFilename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String customFileName = uuid + "." + extension;

        Path fileLocation = this.absoluteUploadPath.resolve(customFileName);
        try {
            Files.copy(multipartFile.getInputStream(), fileLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new CoreException(FileErrorCode.FILE_UPLOAD_FAILED);
        }

        return FileInfo.builder()
            .originalFileName(originalFilename)
            .fileSize(multipartFile.getSize())
            .uploadFileName(customFileName)
            .filePath(this.uploadDir + "/" + customFileName)
            .fileType(multipartFile.getContentType())
            .build();
    }
}
