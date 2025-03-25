package com.rsupport.notice.domain.notice.repository;

import static com.rsupport.notice.domain.notice.entity.QNotice.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rsupport.notice.domain.notice.dto.query.GetNoticeListQuery;
import com.rsupport.notice.domain.notice.entity.Notice;
import io.micrometer.common.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Notice> getNoticeList(GetNoticeListQuery query) {
        PageRequest pageable = PageRequest.of(query.getPageNo(), query.getPageSize());
        JPAQuery<Notice> listQuery = this.generateNoticeListQuery(query)
            .select(notice)
            .orderBy(notice.createdAt.desc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset());

        JPAQuery<Long> countQuery = listQuery.clone()
            .select(notice.count());

        List<Notice> content = listQuery.clone()
            .select(notice)
            .orderBy(notice.createdAt.desc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> {
            Long count = countQuery.fetchOne();
            return count != null ? count : 0;
        });
    }

    private JPAQuery<?> generateNoticeListQuery(GetNoticeListQuery query) {
        return queryFactory.from(notice)
            .where(
                this.containsKeyword(query.getSearchType(), query.getKeyword()),
                this.createdAtBetween(query.getFrom(), query.getTo()),
                this.deletedAtIsNull()
            );
    }

    private BooleanExpression containsKeyword(String searchType, String keyword) {
        if(StringUtils.isEmpty(keyword) || StringUtils.isEmpty(searchType)) {
            return null;
        }

        if("title".equals(searchType)) {
            return notice.title.like(keyword + "%");
        } else if("titleAndContent".equals(searchType)) {
            return notice.title.like(keyword + "%")
                .or(notice.content.like(keyword + "%"));
        } else {
            return null;
        }
    }

    private BooleanExpression createdAtBetween(LocalDateTime from, LocalDateTime to) {
        if(from == null || to == null) {
            return null;
        }

        return notice.createdAt.between(from, to);
    }

    private BooleanExpression deletedAtIsNull() {
        return notice.deletedAt.isNull();
    }
}
