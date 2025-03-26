package com.rsupport.notice.infra.file;

import com.rsupport.notice.common.error.CoreException;
import com.rsupport.notice.domain.file.dto.FileInfo;
import com.rsupport.notice.domain.file.dto.command.FileUploadCommand;
import com.rsupport.notice.domain.file.error.FileErrorCode;
import com.rsupport.notice.domain.file.service.StorageService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
            log.error("Init FileSystemStorageService failed", e);
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
        } catch (IOException e) {
            log.error("upload file failed", e);
            throw new CoreException(FileErrorCode.FILE_UPLOAD_FAILED);
        }

        return FileInfo.builder()
            .originalFileName(originalFilename)
            .fileSize(multipartFile.getSize())
            .uploadFileName(customFileName)
            .filePath(this.uploadDir)
            .fileType(multipartFile.getContentType())
            .build();
    }

    @Override
    public String moveObject(String sourcePathStr, String targetDir) {
        Path targetDirPath = Paths.get(targetDir);
        if(!Files.exists(targetDirPath)) {
            try {
                Files.createDirectories(targetDirPath);
            } catch (IOException e) {
                log.error("move object failed", e);
                throw new CoreException(FileErrorCode.FILE_DIR_CREATION_FAILED);
            }
        }

        Path sourcePath = Paths.get(sourcePathStr);
        Path targetPath = targetDirPath.resolve(sourcePath.getFileName());

        try {
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("move object failed", e);
            throw new CoreException(FileErrorCode.FILE_MOVE_FAILED);
        }

        return targetPath.toString();
    }

    @Override
    public void remove(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("remove file failed", e);
            throw new CoreException(FileErrorCode.FILE_REMOVE_FAILED);
        }
    }

    @Override
    public Resource loadAsResource(String fileFullPath) {
        Path filePath = Paths.get(fileFullPath);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() == false || resource.isFile() == false) {
                throw new RuntimeException("file not found : " + filePath.toString());
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("file not found : " + filePath.toString());
        }
    }
}
