package com.rsupport.notice.application.notice.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeListItem {
    private final Long noticeId;
    private final String title;
    private final boolean hasAttachments;
    private final LocalDateTime createdAt;
    private final int viewCount;
    private final WriterInfo writer;
}
