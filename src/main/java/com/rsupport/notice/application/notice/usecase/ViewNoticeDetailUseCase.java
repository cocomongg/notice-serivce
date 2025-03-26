package com.rsupport.notice.application.notice.usecase;

import static com.rsupport.notice.common.CacheName.CACHE_NOTICE_DETAIL;

import com.rsupport.notice.application.notice.dto.NoticeDetailInfo;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ViewNoticeDetailUseCase {

    private final NoticeService noticeService;
    private final NoticeFileService noticeFileService;
    private final UserService userService;

    @Cacheable(value = CACHE_NOTICE_DETAIL, key = "#noticeId", unless = "#result == null", cacheManager = "noticeCacheManager")
    @Transactional(readOnly = true)
    public NoticeDetailInfo execute(Long noticeId) {
        Notice notice = noticeService.getNotice(noticeId);
        List<NoticeFile> noticeFileList = noticeFileService.getNoticeFileList(noticeId);
        User user = userService.getUser(notice.getUserId());

        noticeService.increaseViewCount(noticeId, user.getUserId());

        return new NoticeDetailInfo(notice, noticeFileList, user);
    }
}
