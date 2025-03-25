package com.rsupport.notice.application.notice.usecase;

import com.rsupport.notice.application.notice.dto.NoticeFileResourceInfo;
import com.rsupport.notice.domain.file.service.StorageService;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DownloadNoticeFileUseCase {

    private final NoticeFileService noticeFileService;
    private final StorageService storageService;

    public NoticeFileResourceInfo execute(Long noticeId, Long noticeFileId) {
        NoticeFile noticeFile = noticeFileService.getNoticeFile(noticeFileId, noticeId);
        String fileFullPath = noticeFile.getFileFullPath();

        Resource resource = storageService.loadAsResource(fileFullPath);
        return new NoticeFileResourceInfo(noticeFile.getOriginalFileName(), noticeFile.getFileSize(),
            resource);
    }
}
