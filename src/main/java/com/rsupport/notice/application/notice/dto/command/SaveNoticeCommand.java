package com.rsupport.notice.application.notice.dto.command;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveNoticeCommand {
    private final String title;
    private final String content;
    private final LocalDateTime noticeStartAt;
    private final LocalDateTime noticeEndAt;
    private final Set<Long> fileIds;
    private final Long userId;
}
