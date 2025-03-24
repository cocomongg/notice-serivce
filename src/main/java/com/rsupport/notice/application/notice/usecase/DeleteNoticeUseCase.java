package com.rsupport.notice.application.notice.usecase;

import com.rsupport.notice.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class DeleteNoticeUseCase {

    private final NoticeService noticeService;

    @Transactional
    public void execute(Long noticeId) {
        noticeService.deleteNotice(noticeId);
    }
}
