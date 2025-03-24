package com.rsupport.notice.domain.notice.dto.command;

import com.rsupport.notice.application.notice.dto.command.SaveNoticeCommand;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateNoticeCommand {
    private final String title;
    private final String content;
    private final LocalDateTime noticeStartAt;
    private final LocalDateTime noticeEndAt;
    private final Long userId;

    public CreateNoticeCommand(SaveNoticeCommand command, Long userId) {
        this.title = command.getTitle();
        this.content = command.getContent();
        this.noticeStartAt = command.getNoticeStartAt();
        this.noticeEndAt = command.getNoticeEndAt();
        this.userId = userId;
    }
}
