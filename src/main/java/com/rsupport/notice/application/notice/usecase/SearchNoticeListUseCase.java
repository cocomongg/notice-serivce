package com.rsupport.notice.application.notice.usecase;

import static com.rsupport.notice.common.CacheName.CACHE_NOTICE_LIST;

import com.rsupport.notice.application.notice.dto.NoticeListInfo;
import com.rsupport.notice.application.notice.dto.mapper.NoticeMapper;
import com.rsupport.notice.application.notice.dto.query.SearchNoticeListQuery;
import com.rsupport.notice.domain.notice.dto.query.GetNoticeListQuery;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SearchNoticeListUseCase {

    private final NoticeService noticeService;
    private final NoticeFileService noticeFileService;
    private final UserService userService;
    private final NoticeMapper noticeMapper;

    @Cacheable(value = CACHE_NOTICE_LIST, keyGenerator = "noticeListKeyGenerator", cacheManager = "noticeCacheManager")
    @Transactional(readOnly = true)
    public NoticeListInfo execute(SearchNoticeListQuery query) {
        GetNoticeListQuery getNoticeListQuery = new GetNoticeListQuery(query);
        Page<Notice> noticeList = noticeService.getNoticeList(getNoticeListQuery);

        Set<Long> noticeIdList = new HashSet<>();
        List<Long> userIdList = new ArrayList<>();
        noticeList.forEach(notice -> {
            noticeIdList.add(notice.getNoticeId());
            userIdList.add(notice.getUserId());
        });

        List<NoticeFile> noticeFileList = noticeFileService.getNoticeFileList(noticeIdList);
        List<User> userList = userService.getUsers(userIdList);

        return noticeMapper.toNoticeListInfo(noticeList, noticeFileList, userList);
    }
}
