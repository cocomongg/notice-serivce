package com.rsupport.notice.application.notice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.user.entity.User;
import java.util.List;
import lombok.Getter;

@Getter
public class NoticeDetailInfo {
    private final Notice notice;
    private final List<NoticeFile> noticeFileList;
    private final User user;

    @JsonCreator
    public NoticeDetailInfo(
        @JsonProperty("notice") Notice notice,
        @JsonProperty("noticeFileList") List<NoticeFile> noticeFileList,
        @JsonProperty("user") User user
    ) {
        this.notice = notice;
        this.noticeFileList = noticeFileList;
        this.user = user;
    }
}
