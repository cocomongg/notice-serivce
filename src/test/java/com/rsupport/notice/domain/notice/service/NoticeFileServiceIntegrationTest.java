package com.rsupport.notice.domain.notice.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.rsupport.notice.domain.notice.dto.command.AttachNoticeFilesCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.repository.NoticeFileRepository;
import com.rsupport.notice.support.DatabaseCleanUp;
import com.rsupport.notice.support.TestContainerSupport;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoticeFileServiceIntegrationTest extends TestContainerSupport {

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
            "originalFileName", "uploadFileName", "filePath", 1);

        // when
        NoticeFile result = noticeFileService.createNoticeFile(command);

        // then
        noticeFileRepository.findById(result.getNoticeFileId())
                .orElseThrow(() -> new AssertionError("저장된 NoticeFile을 찾을 수 없습니다."));

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(command.getUserId());
        assertThat(result.getOriginalFileName()).isEqualTo(command.getOriginalFileName());
        assertThat(result.getUploadFileName()).isEqualTo(command.getUploadFileName());
        assertThat(result.getFilePath()).isEqualTo(command.getFilePath());
        assertThat(result.getFileSize()).isEqualTo(command.getFileSize());
    }
    
    @DisplayName("NoticeFile을 첨부하면 noticeId와 filePath가 업데이트된다.")
    @Test
    void should_UpdateNoticeFile_WhenAttached() {
        // given
        CreateNoticeFileCommand createNoticeFileCommand1 = new CreateNoticeFileCommand(1L,
            "originalFileName1", "uploadFileName1", "filePath", 1);
        CreateNoticeFileCommand createNoticeFileCommand2 = new CreateNoticeFileCommand(1L,
            "originalFileName2", "uploadFileName2", "filePath", 1);
        NoticeFile noticeFile1 = noticeFileService.createNoticeFile(createNoticeFileCommand1);
        NoticeFile noticeFile2 = noticeFileService.createNoticeFile(createNoticeFileCommand2);
        List<NoticeFile> noticeFiles = noticeFileRepository.saveAll(
            List.of(noticeFile1, noticeFile2));

        List<Long> noticeFileList = noticeFiles.stream()
            .map(NoticeFile::getNoticeFileId)
            .toList();
        Set<Long> fileIds = new HashSet<>(noticeFileList);
        Long noticeId = 1L;
        String newPath = "newPath";
        AttachNoticeFilesCommand command = new AttachNoticeFilesCommand(fileIds, noticeId, newPath);

        // when
        noticeFileService.attachNoticeFiles(command);

        // then
        List<NoticeFile> updatedFiles = noticeFileRepository.findAllById(fileIds);
        for(NoticeFile updatedFile : updatedFiles) {
            assertThat(updatedFile.getNoticeId()).isEqualTo(noticeId);
            assertThat(updatedFile.getFilePath()).isEqualTo(newPath);
        }
    }

    @DisplayName("noticeFileIds에 해당하는 NoticeFile 목록을 반환한다.")
    @Test
    void should_ReturnNoticeFileList_When_ByNoticeFileIds() {
        // given
        CreateNoticeFileCommand createNoticeFileCommand1 = new CreateNoticeFileCommand(1L,
            "originalFileName1", "uploadFileName1", "filePath", 1);
        CreateNoticeFileCommand createNoticeFileCommand2 = new CreateNoticeFileCommand(1L,
            "originalFileName2", "uploadFileName2", "filePath", 1);
        NoticeFile noticeFile1 = noticeFileService.createNoticeFile(createNoticeFileCommand1);
        NoticeFile noticeFile2 = noticeFileService.createNoticeFile(createNoticeFileCommand2);
        List<NoticeFile> noticeFiles = noticeFileRepository.saveAll(
            List.of(noticeFile1, noticeFile2));

        List<Long> noticeFileList = noticeFiles.stream()
            .map(NoticeFile::getNoticeFileId)
            .toList();
        Set<Long> fileIds = new HashSet<>(noticeFileList);

        // when
        List<NoticeFile> result = noticeFileService.getNoticeFileList(fileIds);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(noticeFile1, noticeFile2);
    }
}