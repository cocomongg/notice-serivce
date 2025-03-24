package com.rsupport.notice.application.notice.usecase;

import com.rsupport.notice.domain.file.service.StorageService;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class DeleteNoticeUseCase {

    private final NoticeService noticeService;
    private final NoticeFileService noticeFileService;
    private final StorageService storageService;

    @Transactional
    public void execute(Long noticeId, LocalDateTime now) {
        noticeService.deleteNotice(noticeId, now);

        List<NoticeFile> deletedNoticeFileList = noticeFileService.getNoticeFileList(noticeId);
        noticeFileService.deleteNoticeFiles(noticeId, now);

        for (NoticeFile noticeFile : deletedNoticeFileList) {
            String fileFullPath = noticeFile.getFileFullPath();
            storageService.remove(fileFullPath);
        }
    }
}
