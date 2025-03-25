package com.rsupport.notice.application.notice.dto;

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
}
