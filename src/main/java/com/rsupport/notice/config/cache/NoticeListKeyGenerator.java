package com.rsupport.notice.config.cache;

import com.rsupport.notice.application.notice.dto.query.SearchNoticeListQuery;
import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("noticeListKeyGenerator")
public class NoticeListKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params.length > 0 && params[0] instanceof SearchNoticeListQuery) {
            SearchNoticeListQuery query = (SearchNoticeListQuery) params[0];
            return String.format("%d-%d-%s-%s-%s-%s",
                query.getPageNo(),
                query.getPageSize(),
                StringUtils.hasText(query.getSearchType()) ? query.getSearchType() : "",
                StringUtils.hasText(query.getKeyword()) ? query.getKeyword() : "",
                query.getFrom() != null ? query.getFrom().toString() : "",
                query.getTo() != null ? query.getTo().toString() : "");
        }
        return null;
    }
}
