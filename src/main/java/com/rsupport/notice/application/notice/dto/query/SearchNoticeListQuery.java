package com.rsupport.notice.application.notice.dto.query;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchNoticeListQuery {
    private final int pageNo;
    private final int pageSize;
    private final String searchType;
    private final String keyword;
    private final LocalDateTime from;
    private final LocalDateTime to;
}
