package com.rsupport.notice.application.notice.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.rsupport.notice.domain.notice.dto.command.AttachNoticeFilesCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.repository.NoticeRepository;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.support.DatabaseCleanUp;
import com.rsupport.notice.support.TestContainerSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeleteNoticeUseCaseIntegrationTest extends TestContainerSupport {

    @Autowired
    private DeleteNoticeUseCase deleteNoticeUseCase;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeFileService noticeFileService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @TempDir
    private Path tempDir;

    @Value("${file.upload.tmp-dir:uploads/tmp}")
    private String uploadTmpDirProp;

    @Value("${file.upload.notice-dir:uploads/notice}")
    private String uploadNoticeFileDirProp;

    private String uploadTmpDir;
    private String uploadNoticeFileDir;

    @BeforeEach
    void setUp() throws IOException {
        this.uploadTmpDir = tempDir.resolve(uploadTmpDirProp).toString();
        this.uploadNoticeFileDir = tempDir.resolve(uploadNoticeFileDirProp).toString();

        Files.createDirectories(Paths.get(uploadTmpDir));
        Files.createDirectories(Paths.get(uploadNoticeFileDir));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("Notice를 삭제하고, 첨부파일이 존재하면 파일도 삭제한다.")
    @Test
    void execute_whenNoticeFilesExist_thenDeleteFilesAndRemoveStorage() throws IOException {
        // given
        LocalDateTime now = LocalDateTime.now();
        CreateNoticeCommand createNoticeCommand = new CreateNoticeCommand("title", "content", now,
            now, 1L);
        Notice notice = new Notice(createNoticeCommand);
        Notice savedNotice = noticeRepository.save(notice);

        this.createFile(1L, savedNotice.getNoticeId(), "file1", "file1");
        this.createFile(1L, savedNotice.getNoticeId(), "file2", "file2");

        // when
        deleteNoticeUseCase.execute(savedNotice.getNoticeId(), now);

        // then
        Notice result = noticeRepository.findById(savedNotice.getNoticeId())
            .orElseThrow(() -> new AssertionError("저장된 Notice를 찾을 수 없습니다."));
        assertThat(result.getDeletedAt()).isEqualTo(now);

        noticeFileService.getNoticeFileList(savedNotice.getNoticeId())
            .forEach(noticeFile -> {
                assertThat(noticeFile.getDeletedAt()).isEqualTo(now);
                Path path = Paths.get(noticeFile.getFileFullPath());
                assertThat(Files.exists(path)).isFalse();
            });
    }

    @DisplayName("Notice를 삭제하고, 첨부파일이 존재하지 않으면 파일 삭제 관련 호출을 하지 않는다.")
    @Test
    void execute_whenNoNoticeFiles_thenSkipDeletionAndStorageRemoval() {
        // given
        LocalDateTime now = LocalDateTime.now();
        CreateNoticeCommand createNoticeCommand = new CreateNoticeCommand("title", "content", now,
            now, 1L);
        Notice notice = new Notice(createNoticeCommand);
        Notice savedNotice = noticeRepository.save(notice);

        // when
        deleteNoticeUseCase.execute(savedNotice.getNoticeId(), now);

        // then
        Notice result = noticeRepository.findById(savedNotice.getNoticeId())
            .orElseThrow(() -> new AssertionError("저장된 Notice를 찾을 수 없습니다."));
        assertThat(result.getDeletedAt()).isEqualTo(now);

        assertThat(noticeFileService.getNoticeFileList(savedNotice.getNoticeId())).isEmpty();
    }

    private Long createFile(Long userId, Long noticeId, String fileName, String uploadFileName)
        throws IOException {
        CreateNoticeFileCommand createNoticeFileCommand = new CreateNoticeFileCommand(
            userId, fileName, uploadFileName, String.format("%s/%s", uploadTmpDir, uploadFileName), 100);

        Path filePath = Paths.get(String.format("%s/%s", uploadTmpDir, uploadFileName));
        Files.write(filePath, "content".getBytes());

        NoticeFile noticeFile = noticeFileService.createNoticeFile(createNoticeFileCommand);

        HashSet<Long> fileIds = new HashSet<>(Arrays.asList(noticeFile.getNoticeFileId()));
        String path = String.format("%s/%s", uploadNoticeFileDir, noticeId);
        AttachNoticeFilesCommand attachNoticeFilesCommand = new AttachNoticeFilesCommand(
            fileIds, noticeId, path);

        noticeFileService.attachNoticeFiles(attachNoticeFilesCommand);

        return noticeFile.getNoticeFileId();
    }
}