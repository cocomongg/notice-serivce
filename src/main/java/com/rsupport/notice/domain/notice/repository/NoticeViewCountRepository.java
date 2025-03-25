package com.rsupport.notice.domain.notice.repository;

public interface NoticeViewCountRepository {
    long incrementViewCount(Long noticeId, Long userId);

    long getViewCount(Long noticeId);
}
