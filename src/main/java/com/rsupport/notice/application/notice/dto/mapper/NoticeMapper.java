package com.rsupport.notice.application.notice.dto.mapper;

import com.rsupport.notice.application.notice.dto.NoticeListInfo;
import com.rsupport.notice.application.notice.dto.NoticeListItem;
import com.rsupport.notice.application.notice.dto.WriterInfo;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.user.entity.User;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class NoticeMapper {

    public NoticeListInfo toNoticeListInfo(Page<Notice> noticePage,
        List<NoticeFile> noticeFiles,
        List<User> userList) {

        Map<Long, List<NoticeFile>> noticeFileMap = createNoticeFileMap(noticeFiles);
        Map<Long, User> userMap = createUserMap(userList);

        List<NoticeListItem> noticeInfoList = noticePage.getContent().stream()
            .map(notice -> toNoticeInfo(notice,
                noticeFileMap.getOrDefault(notice.getNoticeId(), Collections.emptyList()),
                userMap.getOrDefault(notice.getUserId(), new User())))
            .collect(Collectors.toList());

        return NoticeListInfo.builder()
            .pageNo(noticePage.getNumber() + 1)
            .pageSize(noticePage.getSize())
            .totalElements(noticePage.getTotalElements())
            .totalPages(noticePage.getTotalPages())
            .notices(noticeInfoList)
            .build();
    }

    public NoticeListItem toNoticeInfo(Notice notice, List<NoticeFile> noticeFiles, User user) {
        return NoticeListItem.builder()
            .noticeId(notice.getNoticeId())
            .title(notice.getTitle())
            .hasAttachments(!noticeFiles.isEmpty())
            .createdAt(notice.getCreatedAt())
            .viewCount(notice.getViewCount())
            .writer(toWriterInfo(user))
            .build();
    }

    public WriterInfo toWriterInfo(User user) {
        if(user == null) {
            return new WriterInfo(null, "Unknown");
        }
        return new WriterInfo(user.getUserId(), user.getUsername());
    }

    public Map<Long, List<NoticeFile>> createNoticeFileMap(List<NoticeFile> noticeFileList) {
        return noticeFileList.stream()
            .collect(Collectors.groupingBy(
                NoticeFile::getNoticeId));
    }

    public Map<Long, User> createUserMap(List<User> users) {
        return users.stream()
            .collect(Collectors.toMap(
                User::getUserId,
                User -> User
            ));
    }
}
