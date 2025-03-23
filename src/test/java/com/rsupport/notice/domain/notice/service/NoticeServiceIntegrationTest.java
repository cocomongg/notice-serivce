package com.rsupport.notice.domain.notice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.rsupport.notice.domain.notice.dto.command.CreateNoticeCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.repository.NoticeRepository;
import com.rsupport.notice.support.DatabaseCleanUp;
import com.rsupport.notice.support.TestContainerSupport;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoticeServiceIntegrationTest extends TestContainerSupport {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("입력값을 통해 Notice를 생성 및 저장하고 반환한다.")
    @Test
    void should_SaveAndReturnNotice() {
        // given
        CreateNoticeCommand command = CreateNoticeCommand.builder()
            .title("title")
            .content("content")
            .noticeStartAt(LocalDateTime.now())
            .noticeEndAt(LocalDateTime.now())
            .userId(1L)
            .build();

        // when
        Notice result = noticeService.createNotice(command);

        // then
        noticeRepository.findById(result.getNoticeId())
            .orElseThrow(() -> new AssertionError("저장된 Notice를 찾을 수 없습니다."));

        assertNotNull(result);
        assertEquals(result.getTitle(), command.getTitle());
        assertEquals(result.getContent(), command.getContent());
        assertEquals(result.getNoticeStartAt(), command.getNoticeStartAt());
        assertEquals(result.getNoticeEndAt(), command.getNoticeEndAt());
        assertEquals(result.getUserId(), command.getUserId());
    }
}