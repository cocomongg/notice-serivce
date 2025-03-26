package com.rsupport.notice.application.notice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeListInfo {
    private final int pageNo;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final List<NoticeListItem> notices;

    @JsonCreator
    public NoticeListInfo(
        @JsonProperty("pageNo") int pageNo,
        @JsonProperty("pageSize") int pageSize,
        @JsonProperty("totalElements") long totalElements,
        @JsonProperty("totalPages") int totalPages,
        @JsonProperty("notices") List<NoticeListItem> notices
    ) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.notices = notices;
    }
}
