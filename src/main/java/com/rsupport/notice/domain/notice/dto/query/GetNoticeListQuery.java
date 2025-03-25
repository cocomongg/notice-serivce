package com.rsupport.notice.domain.notice.dto.query;

import com.rsupport.notice.application.notice.dto.query.SearchNoticeListQuery;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetNoticeListQuery {
    private final int pageNo;
    private final int pageSize;
    private final String searchType;
    private final String keyword;
    private final LocalDateTime from;
    private final LocalDateTime to;

    public GetNoticeListQuery(SearchNoticeListQuery query) {
        this.pageNo = query.getPageNo();
        this.pageSize = query.getPageSize();
        this.searchType = query.getSearchType();
        this.keyword = query.getKeyword();
        this.from = query.getFrom();
        this.to = query.getTo();
    }
}
