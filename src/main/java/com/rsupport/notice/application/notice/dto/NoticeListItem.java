package com.rsupport.notice.application.notice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public NoticeListItem(
        @JsonProperty("noticeId") Long noticeId,
        @JsonProperty("title") String title,
        @JsonProperty("hasAttachments") boolean hasAttachments,
        @JsonProperty("createdAt") LocalDateTime createdAt,
        @JsonProperty("viewCount") int viewCount,
        @JsonProperty("writer") WriterInfo writer
    ) {
        this.noticeId = noticeId;
        this.title = title;
        this.hasAttachments = hasAttachments;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.writer = writer;
    }
}
