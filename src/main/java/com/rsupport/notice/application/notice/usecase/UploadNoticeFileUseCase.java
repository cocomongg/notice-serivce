package com.rsupport.notice.application.notice.usecase;

import com.rsupport.notice.application.notice.dto.command.UploadNoticeFileCommand;
import com.rsupport.notice.domain.file.dto.FileInfo;
import com.rsupport.notice.domain.file.dto.command.FileUploadCommand;
import com.rsupport.notice.domain.file.service.StorageService;
import com.rsupport.notice.domain.file.validator.FileValidator;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UploadNoticeFileUseCase {

    private final NoticeFileService noticeFileService;
    private final UserService userService;
    private final StorageService storageService;
    private final FileValidator fileValidator;

    public NoticeFile execute(UploadNoticeFileCommand command) {
        fileValidator.validate(command.getMultipartFile());

        FileUploadCommand fileUploadCommand = new FileUploadCommand(command.getMultipartFile());
        FileInfo fileInfo = storageService.upload(fileUploadCommand);

        User user = userService.getUser(command.getUserId());
        CreateNoticeFileCommand createNoticeFileCommand = new CreateNoticeFileCommand(
            user.getUserId(), fileInfo);

        return noticeFileService.createNoticeFile(createNoticeFileCommand);
    }
}
