package com.rsupport.notice.domain.notice.service;

import com.rsupport.notice.common.error.CoreException;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.error.NoticeErrorCode;
import com.rsupport.notice.domain.notice.repository.NoticeRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice createNotice(CreateNoticeCommand command) {
        Notice notice = new Notice(command);
        return noticeRepository.save(notice);
    }

    @Transactional(readOnly = true)
    public Notice getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CoreException(NoticeErrorCode.NOTICE_NOT_FOUND));
    }

    @Transactional
    public void deleteNotice(Long noticeId, LocalDateTime now) {
        Notice notice = this.getNotice(noticeId);
        notice.delete(now);
    }
}
