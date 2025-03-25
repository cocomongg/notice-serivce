package com.rsupport.notice.application.notice.usecase;

import com.rsupport.notice.application.notice.dto.NoticeDetailInfo;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ViewNoticeDetailUseCase {

    private final NoticeService noticeService;
    private final NoticeFileService noticeFileService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public NoticeDetailInfo execute(Long noticeId) {
        Notice notice = noticeService.getNotice(noticeId);
        List<NoticeFile> noticeFileList = noticeFileService.getNoticeFileList(noticeId);
        User user = userService.getUser(notice.getUserId());

        int viewCount = noticeService.increaseViewCount(noticeId, user.getUserId());
        if (viewCount == 0) {
            viewCount = notice.getViewCount();
        }

        return new NoticeDetailInfo(notice, noticeFileList, user, viewCount);
    }
}
