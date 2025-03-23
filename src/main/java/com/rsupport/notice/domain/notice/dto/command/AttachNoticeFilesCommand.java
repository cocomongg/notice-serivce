package com.rsupport.notice.domain.notice.dto.command;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttachNoticeFilesCommand {
    private final Set<Long> fileIds;
    private final Long noticeId;
    private final String path;
}
