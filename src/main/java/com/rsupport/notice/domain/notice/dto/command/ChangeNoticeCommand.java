package com.rsupport.notice.domain.notice.dto.command;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChangeNoticeCommand {
    private final Long noticeId;
    private final String title;
    private final String content;
    private final LocalDateTime noticeStartAt;
    private final LocalDateTime noticeEndAt;

    public ChangeNoticeCommand(UpdateNoticeCommand command) {
        this.noticeId = command.getNoticeId();
        this.title = command.getTitle();
        this.content = command.getContent();
        this.noticeStartAt = command.getNoticeStartAt();
        this.noticeEndAt = command.getNoticeEndAt();
    }
}
