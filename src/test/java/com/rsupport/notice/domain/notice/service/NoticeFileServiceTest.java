package com.rsupport.notice.domain.notice.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.repository.NoticeFileRepository;
import com.rsupport.notice.support.DatabaseCleanUp;
import com.rsupport.notice.support.TestContainerSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoticeFileServiceTest extends TestContainerSupport {

    @Autowired
    private NoticeFileService noticeFileService;

    @Autowired
    private NoticeFileRepository noticeFileRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("입력값을 통해 NoticeFile을 생성 및 저장하고 반환한다.")
    @Test
    void createNoticeFile() {
        // given
        CreateNoticeFileCommand command = new CreateNoticeFileCommand(1L,
            "originalFileName", "filePath", 1);

        // when
        NoticeFile result = noticeFileService.createNoticeFile(command);

        // then
        noticeFileRepository.findById(result.getNoticeFileId())
                .orElseThrow(() -> new AssertionError("저장된 NoticeFile을 찾을 수 없습니다."));

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(command.getUserId());
        assertThat(result.getOriginalFileName()).isEqualTo(command.getOriginalFileName());
        assertThat(result.getFilePath()).isEqualTo(command.getFilePath());
        assertThat(result.getFileSize()).isEqualTo(command.getFileSize());
    }
}