package com.rsupport.notice.application.notice.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.rsupport.notice.application.notice.dto.NoticeDetailInfo;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.service.UserService;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ViewNoticeDetailUseCaseTest {
    @Mock
    private NoticeService noticeService;

    @Mock
    private NoticeFileService noticeFileService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ViewNoticeDetailUseCase viewNoticeDetailUseCase;

    @DisplayName("noticeId에 해당하는 NoticeDetailInfo를 반환한다.")
    @Test
    void should_ReturnNoticeDetailInfo_When_ByNoticeId() {
        // given
        Long noticeId = 1L;
        LocalDateTime now = LocalDateTime.now();
        CreateNoticeCommand createNoticeCommand = new CreateNoticeCommand("title", "content", now,
            now, 1L);
        Notice notice = new Notice(createNoticeCommand);
        User user = new User(1L, "사용자", now, now);
        int viewCount = 1;

        when(noticeService.getNotice(noticeId)).thenReturn(notice);
        when(noticeFileService.getNoticeFileList(noticeId)).thenReturn(Collections.EMPTY_LIST);
        when(userService.getUser(notice.getUserId())).thenReturn(user);
        when(noticeService.increaseViewCount(noticeId, user.getUserId())).thenReturn(viewCount);

        // when
        NoticeDetailInfo result = viewNoticeDetailUseCase.execute(noticeId);

        // then
        assertThat(result.getNotice()).isEqualTo(notice);
        assertThat(result.getNoticeFileList()).isEmpty();
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getViewCount()).isEqualTo(viewCount);
    }

    @DisplayName("조회수 증가 실패 시 기존 조회수를 반환한다.")
    @Test
    void should_ReturnPreviousViewCount_When_FailToIncreaseViewCount() {
        // given
        Long noticeId = 1L;
        LocalDateTime now = LocalDateTime.now();
        CreateNoticeCommand createNoticeCommand = new CreateNoticeCommand("title", "content", now,
            now, 1L);
        Notice notice = new Notice(createNoticeCommand);
        ReflectionTestUtils.setField(notice, "viewCount", 10);
        User user = new User(1L, "사용자", now, now);
        int viewCount = 0;
        int previousViewCount = notice.getViewCount();

        when(noticeService.getNotice(noticeId)).thenReturn(notice);
        when(noticeFileService.getNoticeFileList(noticeId)).thenReturn(Collections.EMPTY_LIST);
        when(userService.getUser(notice.getUserId())).thenReturn(user);
        when(noticeService.increaseViewCount(noticeId, user.getUserId())).thenReturn(0);

        // when
        NoticeDetailInfo result = viewNoticeDetailUseCase.execute(noticeId);

        // then
        assertThat(result.getNotice()).isEqualTo(notice);
        assertThat(result.getNoticeFileList()).isEmpty();
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getViewCount()).isEqualTo(previousViewCount);
    }

}