package com.rsupport.notice.application.notice.usecase;

import static com.rsupport.notice.common.CacheName.CACHE_NOTICE_DETAIL;
import static com.rsupport.notice.common.CacheName.CACHE_NOTICE_LIST;

import com.rsupport.notice.domain.file.service.StorageService;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class DeleteNoticeUseCase {

    private final NoticeService noticeService;
    private final NoticeFileService noticeFileService;
    private final StorageService storageService;

    @Caching(evict = {
        @CacheEvict(value = CACHE_NOTICE_LIST, allEntries = true, cacheManager = "noticeCacheManager"),
        @CacheEvict(value = CACHE_NOTICE_DETAIL, key = "#noticeId", cacheManager = "noticeCacheManager")
    })
    @Transactional
    public void execute(Long noticeId, LocalDateTime now) {
        noticeService.deleteNotice(noticeId, now);

        List<NoticeFile> deletedNoticeFileList = noticeFileService.getNoticeFileList(noticeId);
        if(deletedNoticeFileList.isEmpty()) {
            return;
        }

        noticeFileService.deleteNoticeFiles(noticeId, now);

        for (NoticeFile noticeFile : deletedNoticeFileList) {
            String fileFullPath = noticeFile.generateFileFullPath();
            storageService.remove(fileFullPath);
        }
    }
}
