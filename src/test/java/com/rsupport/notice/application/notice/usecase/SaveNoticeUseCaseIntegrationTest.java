package com.rsupport.notice.application.notice.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.rsupport.notice.application.notice.dto.command.SaveNoticeCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.repository.NoticeFileRepository;
import com.rsupport.notice.domain.notice.repository.NoticeRepository;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.repository.UserRepository;
import com.rsupport.notice.support.DatabaseCleanUp;
import com.rsupport.notice.support.TestContainerSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
class SaveNoticeUseCaseIntegrationTest extends TestContainerSupport {

    @Autowired
    private SaveNoticeUseCase saveNoticeUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoticeFileRepository noticeFileRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Value("${file.upload.tmp-dir:uploads/tmp}")
    private String uploadTmpDirProp;

    @Value("${file.upload.notice-dir:uploads/notice}")
    private String uploadNoticeFileDirProp;

    private String uploadTmpDir;
    private String uploadNoticeFileDir;

    @TempDir
    Path tempTestDir;

    @BeforeEach
    public void setUp() throws IOException {
        this.uploadTmpDir = tempTestDir.resolve(uploadTmpDirProp).toString();
        this.uploadNoticeFileDir = tempTestDir.resolve(uploadNoticeFileDirProp).toString();
        ReflectionTestUtils.setField(saveNoticeUseCase, "uploadTmpDir", uploadTmpDir);
        ReflectionTestUtils.setField(saveNoticeUseCase, "uploadNoticeFileDir", uploadNoticeFileDir);

        Files.createDirectories(Paths.get(uploadTmpDir));
        Files.createDirectories(Paths.get(uploadNoticeFileDir));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("첨부파일이 있으면 임시디렉토리에서 noitce하위로 이동시키고, Notice를 생성하고 반환한다.")
    @Test
    void should_SaveNoticeAndMoveFile_When_FileAttached() throws IOException {
        // given
        User user = new User(null, "testUser", LocalDateTime.now(), LocalDateTime.now());
        User savedUser = userRepository.save(user);

        NoticeFile noticeFile1 = this.createFile(savedUser.getUserId(), "file1.pdf",
            "uploadFileName1");
        NoticeFile noticeFile2 = this.createFile(savedUser.getUserId(), "file2.pdf",
            "uploadFileName2");
        Set<Long> fileIds = new HashSet<>(
            Arrays.asList(noticeFile1.getNoticeFileId(), noticeFile2.getNoticeFileId()));

        LocalDateTime now = LocalDateTime.now();
        SaveNoticeCommand command = SaveNoticeCommand.builder()
            .userId(savedUser.getUserId())
            .title("제목")
            .content("내용")
            .noticeStartAt(now)
            .noticeEndAt(now.plusDays(1))
            .fileIds(fileIds)
            .build();

        // when
        Notice result = saveNoticeUseCase.execute(command);

        // then
        assertThat(result).isNotNull();

        Notice notice = noticeRepository.findById(result.getNoticeId())
            .orElseThrow(() -> new AssertionError("저장된 Notice를 찾을 수 없습니다."));
        assertThat(result.getNoticeId()).isEqualTo(notice.getNoticeId());
        assertThat(result.getTitle()).isEqualTo(notice.getTitle());
        assertThat(result.getContent()).isEqualTo(notice.getContent());

        List<NoticeFile> noticeFiles = noticeFileRepository.findAllByNoticeFileIdIn(fileIds);
        for(NoticeFile noticeFile : noticeFiles) {
            assertThat(noticeFile.getNoticeId()).isEqualTo(notice.getNoticeId());
            assertThat(noticeFile.getFilePath()).startsWith(String.format("%s/%s", uploadNoticeFileDir, notice.getNoticeId()));

            String destinationPath = String.format("%s/%s/%s", uploadNoticeFileDir,
                notice.getNoticeId(), noticeFile.getUploadFileName());
            assertTrue(Files.exists(Paths.get(destinationPath)));

            String tempPath = String.format("%s/%s", uploadTmpDir, noticeFile.getUploadFileName());
            assertFalse(Files.exists(Paths.get(tempPath)));
        }
    }

    @DisplayName("첨부파일이 없으면 Notice만 생성하고 반환한다.")
    @Test
    void should_SaveNotice_When_FileNotAttached() {
        // given
        User user = new User(null, "testUser", LocalDateTime.now(), LocalDateTime.now());
        User savedUser = userRepository.save(user);

        LocalDateTime now = LocalDateTime.now();
        SaveNoticeCommand command = SaveNoticeCommand.builder()
            .userId(savedUser.getUserId())
            .title("제목")
            .content("내용")
            .noticeStartAt(now)
            .noticeEndAt(now.plusDays(1))
            .build();

        // when
        Notice result = saveNoticeUseCase.execute(command);

        // then
        assertThat(result).isNotNull();
        Notice notice = noticeRepository.findById(result.getNoticeId())
            .orElseThrow(() -> new AssertionError("저장된 Notice를 찾을 수 없습니다."));
        assertThat(result.getNoticeId()).isEqualTo(notice.getNoticeId());
        assertThat(result.getTitle()).isEqualTo(notice.getTitle());
        assertThat(result.getContent()).isEqualTo(notice.getContent());

        Path noticeDir = Paths.get(String.format("%s/%s", uploadNoticeFileDir, notice.getNoticeId()));
        assertFalse(Files.exists(noticeDir));
    }

    private NoticeFile createFile(Long userId, String fileName, String uploadFileName)
        throws IOException {
        CreateNoticeFileCommand createNoticeFileCommand = new CreateNoticeFileCommand(
            userId, fileName, uploadFileName, String.format("%s/%s", uploadTmpDir, uploadFileName), 100);

        Path filePath = Paths.get(String.format("%s/%s", uploadTmpDir, uploadFileName));
        Files.write(filePath, "content".getBytes());

        return noticeFileRepository.save(new NoticeFile(createNoticeFileCommand));
    }
}