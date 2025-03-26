package com.rsupport.notice.domain.notice.dto.command;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateNoticeCommand {
    private final Long noticeId;
    private final String title;
    private final String content;
    private final LocalDateTime noticeStartAt;
    private final LocalDateTime noticeEndAt;
    private final Set<Long> newFileIds;
    private final Set<Long> deletedFileIds;
}
