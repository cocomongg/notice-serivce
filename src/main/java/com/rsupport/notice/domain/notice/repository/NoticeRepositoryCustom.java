package com.rsupport.notice.domain.notice.repository;

import com.rsupport.notice.domain.notice.dto.query.GetNoticeListQuery;
import com.rsupport.notice.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;

public interface NoticeRepositoryCustom {
    Page<Notice> getNoticeList(GetNoticeListQuery query);
}
