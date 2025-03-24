package com.rsupport.notice.application.notice.usecase;

import com.rsupport.notice.application.notice.dto.command.SaveNoticeCommand;
import com.rsupport.notice.domain.file.service.StorageService;
import com.rsupport.notice.domain.notice.dto.command.AttachNoticeFilesCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SaveNoticeUseCase {

    private final NoticeService noticeService;
    private final NoticeFileService noticeFileService;
    private final UserService userService;
    private final StorageService storageService;

    private final String uploadNoticeFileDir;
    private final String uploadTmpDir;

    public SaveNoticeUseCase(
        @Value("${file.upload.tmp-dir:uploads/tmp}") String uploadNoticeFileDir,
        @Value("${file.upload.notice-dir:uploads/notice}") String uploadTmpDir,
        NoticeService noticeService,
        NoticeFileService noticeFileService,
        UserService userService,
        StorageService storageService
    ) {
        this.uploadNoticeFileDir = uploadNoticeFileDir;
        this.uploadTmpDir = uploadTmpDir;
        this.noticeService = noticeService;
        this.noticeFileService = noticeFileService;
        this.userService = userService;
        this.storageService = storageService;
    }

    @Transactional
    public Notice execute(SaveNoticeCommand command) {
        // user 조회
        User user = userService.getUser(command.getUserId());

        // notice 생성
        CreateNoticeCommand createNoticeCommand = CreateNoticeCommand.builder()
            .title(command.getTitle())
            .content(command.getContent())
            .noticeStartAt(command.getNoticeStartAt())
            .noticeEndAt(command.getNoticeEndAt())
            .userId(user.getUserId())
            .build();
        Notice notice = noticeService.createNotice(createNoticeCommand);

        // notice file - notice 연결
        String noticeFilePath = uploadNoticeFileDir + "/" + notice.getNoticeId();
        AttachNoticeFilesCommand attachNoticeFilesCommand = AttachNoticeFilesCommand.builder()
            .fileIds(command.getFileIds())
            .noticeId(notice.getNoticeId())
            .path(noticeFilePath)
            .build();
        noticeFileService.attachNoticeFiles(attachNoticeFilesCommand);

        // notice file 이동 (tmp -> notice dir)
        List<NoticeFile> noticeFileList = noticeFileService.getNoticeFileList(command.getFileIds());
        for(NoticeFile noticeFile : noticeFileList) {
            String tmpFilePath = uploadTmpDir + "/" + noticeFile.getUploadFileName();
            storageService.moveObject(tmpFilePath, noticeFilePath);
        }

        return notice;
    }
}
