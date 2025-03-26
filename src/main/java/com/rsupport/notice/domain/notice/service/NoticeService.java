package com.rsupport.notice.domain.notice.service;

import com.rsupport.notice.common.error.CoreException;
import com.rsupport.notice.domain.notice.dto.command.ChangeNoticeCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeCommand;
import com.rsupport.notice.domain.notice.dto.query.GetNoticeListQuery;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.error.NoticeErrorCode;
import com.rsupport.notice.domain.notice.repository.NoticeRepository;
import com.rsupport.notice.domain.notice.repository.NoticeViewCountRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeViewCountRepository noticeViewCountRepository;

    @Transactional
    public Notice createNotice(CreateNoticeCommand command) {
        Notice notice = new Notice(command);
        return noticeRepository.save(notice);
    }

    @Transactional(readOnly = true)
    public Notice getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
            .orElseThrow(() -> new CoreException(NoticeErrorCode.NOTICE_NOT_FOUND));
        if(notice.deleted()) {
            throw new CoreException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }

        return notice;
    }

    @Transactional(readOnly = true)
    public Page<Notice> getNoticeList(GetNoticeListQuery query) {
        return noticeRepository.getNoticeList(query);
    }

    @Transactional
    public Notice changeNotice(ChangeNoticeCommand command) {
        Notice notice = this.getNotice(command.getNoticeId());
        notice.change(command);
        return notice;
    }

    @Transactional
    public void deleteNotice(Long noticeId, LocalDateTime now) {
        Notice notice = this.getNotice(noticeId);
        notice.delete(now);
    }

    public int increaseViewCount(Long noticeId, Long userId) {
        return (int)noticeViewCountRepository.incrementViewCount(noticeId, userId);
    }

    @Transactional
    public void updateViewCount(Long noticeId, int viewCount) {
        Notice notice = this.getNotice(noticeId);
        notice.updateViewCount(viewCount);
    }
}
