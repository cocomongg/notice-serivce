package com.rsupport.notice.application.notice.usecase;

import com.rsupport.notice.application.notice.dto.NoticeDetailInfo;
import com.rsupport.notice.domain.file.service.StorageService;
import com.rsupport.notice.domain.notice.dto.command.AttachNoticeFilesCommand;
import com.rsupport.notice.domain.notice.dto.command.ChangeNoticeCommand;
import com.rsupport.notice.domain.notice.dto.command.UpdateNoticeCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component
public class UpdateNoticeUseCase {

    private final NoticeService noticeService;
    private final NoticeFileService noticeFileService;
    private final UserService userService;
    private final StorageService storageService;
    private final String uploadNoticeFileDir;
    private final String uploadTmpDir;

    public UpdateNoticeUseCase(
        @Value("${file.upload.tmp-dir:uploads/tmp}") String uploadTmpDir,
        @Value("${file.upload.notice-dir:uploads/notice}") String uploadNoticeFileDir,
        NoticeService noticeService,
        NoticeFileService noticeFileService,
        UserService userService,
        StorageService storageService) {
        this.noticeService = noticeService;
        this.noticeFileService = noticeFileService;
        this.userService = userService;
        this.storageService = storageService;
        this.uploadNoticeFileDir = uploadNoticeFileDir;
        this.uploadTmpDir = uploadTmpDir;
    }

    @Transactional
    public NoticeDetailInfo execute(UpdateNoticeCommand command) {
        Notice notice = noticeService.changeNotice(new ChangeNoticeCommand(command));

        Set<Long> newFileIds = command.getNewFileIds();
        if(!CollectionUtils.isEmpty(newFileIds)) {
            String noticeFilePath = String.format("%s/%s", uploadNoticeFileDir, notice.getNoticeId());
            AttachNoticeFilesCommand attachNoticeFilesCommand = new AttachNoticeFilesCommand(newFileIds,
                notice.getNoticeId(), noticeFilePath);
            noticeFileService.attachNoticeFiles(attachNoticeFilesCommand);

            List<NoticeFile> noticeFileList = noticeFileService.getNoticeFileList(newFileIds);

            for(NoticeFile noticeFile : noticeFileList) {
                String tmpFilePath = String.format("%s/%s", uploadTmpDir, noticeFile.getUploadFileName());
                storageService.moveObject(tmpFilePath, noticeFilePath);
            }
        }

        Set<Long> deletedFileIds = command.getDeletedFileIds();
        if(!CollectionUtils.isEmpty(deletedFileIds)) {
            List<NoticeFile> deletedNoticeFileList = noticeFileService.getNoticeFileList(deletedFileIds);
            noticeFileService.deleteNoticeFiles(deletedFileIds, LocalDateTime.now());

            for (NoticeFile noticeFile : deletedNoticeFileList) {
                String fileFullPath = noticeFile.getFileFullPath();
                storageService.remove(fileFullPath);
            }
        }

        List<NoticeFile> noticeFileList = noticeFileService.getNoticeFileList(notice.getNoticeId());
        User user = userService.getUser(notice.getUserId());

        return new NoticeDetailInfo(notice, noticeFileList, user, 0);
    }
}
