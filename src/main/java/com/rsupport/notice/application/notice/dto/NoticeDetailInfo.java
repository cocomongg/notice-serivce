package com.rsupport.notice.application.notice.dto;

import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.user.entity.User;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoticeDetailInfo {
    private final Notice notice;
    private final List<NoticeFile> noticeFileList;
    private final User user;
    private final int viewCount;
}
