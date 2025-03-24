package com.rsupport.notice.application.notice.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rsupport.notice.application.notice.dto.command.SaveNoticeCommand;
import com.rsupport.notice.domain.file.service.StorageService;
import com.rsupport.notice.domain.notice.dto.command.AttachNoticeFilesCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.service.UserService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SaveNoticeUseCaseTest {
    @Mock
    private NoticeService noticeService;

    @Mock
    private NoticeFileService noticeFileService;

    @Mock
    private UserService userService;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SaveNoticeUseCase saveNoticeUseCase;

    private final String uploadNoticeFileDir = "uploads/notice";
    private final String uploadTmpDir = "uploads/tmp";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(saveNoticeUseCase, "uploadNoticeFileDir", uploadNoticeFileDir);
        ReflectionTestUtils.setField(saveNoticeUseCase, "uploadTmpDir", uploadTmpDir);
    }

    @DisplayName("첨부파일이 있으면 임시디렉토리에서 noitce하위로 이동시키고, Notice를 생성하고 반환한다.")
    @Test
    void should_SaveNoticeAndMoveFile_When_FileAttached () {
        // given
        Long userId = 1L;
        Long noticeId = 1L;
        Set<Long> fileIds = new HashSet<>(Arrays.asList(1L, 2L));
        String title = "제목";
        String content = "내용";
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusDays(1);

        SaveNoticeCommand command = SaveNoticeCommand.builder()
            .userId(userId)
            .title(title)
            .content(content)
            .noticeStartAt(startAt)
            .noticeEndAt(endAt)
            .fileIds(fileIds)
            .build();

        User user = new User();
        ReflectionTestUtils.setField(user, "userId", userId);

        Notice notice = new Notice();
        ReflectionTestUtils.setField(notice, "noticeId", noticeId);

        CreateNoticeFileCommand createNoticeFileCommand1 = new CreateNoticeFileCommand(1L, "file1.pdf",
            "uploadFileName1", "", 1000);
        CreateNoticeFileCommand createNoticeFileCommand2 = new CreateNoticeFileCommand(1L, "file2.pdf",
            "uploadFileName2", "", 1000);

        NoticeFile file1 = new NoticeFile(createNoticeFileCommand1);
        NoticeFile file2 = new NoticeFile(createNoticeFileCommand2);
        List<NoticeFile> noticeFiles = Arrays.asList(file1, file2);

        // when
        when(userService.getUser(userId)).thenReturn(user);
        when(noticeService.createNotice(any(CreateNoticeCommand.class))).thenReturn(notice);
        when(noticeFileService.getNoticeFileList(fileIds)).thenReturn(noticeFiles);

        Notice result = saveNoticeUseCase.execute(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNoticeId()).isEqualTo(noticeId);

        verify(userService, times(1)).getUser(userId);
        verify(noticeService, times(1)).createNotice(any(CreateNoticeCommand.class));
        verify(noticeFileService, times(1)).attachNoticeFiles(any(AttachNoticeFilesCommand.class));
        verify(noticeFileService, times(1)).getNoticeFileList(fileIds);
        verify(storageService, times(1)).moveObject(
            uploadTmpDir + "/" + file1.getUploadFileName(),
            uploadNoticeFileDir + "/" + noticeId
        );
        verify(storageService, times(1)).moveObject(
            uploadTmpDir + "/" + file1.getUploadFileName(),
            uploadNoticeFileDir + "/" + noticeId
        );
    }

    @DisplayName("첨부파일이 없으면, Notice를 생성하고 반환한다.")
    @Test
    void should_SaveNotice_When_FileNotAttached() {
        // given
        Long userId = 1L;
        Long noticeId = 1L;
        String title = "제목";
        String content = "내용";
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusDays(1);

        SaveNoticeCommand command = SaveNoticeCommand.builder()
            .userId(userId)
            .title(title)
            .content(content)
            .noticeStartAt(startAt)
            .noticeEndAt(endAt)
            .build();

        User user = new User();
        ReflectionTestUtils.setField(user, "userId", userId);

        Notice notice = new Notice();
        ReflectionTestUtils.setField(notice, "noticeId", noticeId);

        // when
        when(userService.getUser(userId)).thenReturn(user);
        when(noticeService.createNotice(any(CreateNoticeCommand.class))).thenReturn(notice);

        Notice result = saveNoticeUseCase.execute(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNoticeId()).isEqualTo(noticeId);

        verify(userService, times(1)).getUser(userId);
        verify(noticeService, times(1)).createNotice(any(CreateNoticeCommand.class));
        verify(noticeFileService, never()).attachNoticeFiles(any(AttachNoticeFilesCommand.class));
        verify(noticeFileService, never()).getNoticeFileList(any(HashSet.class));
        verify(storageService, never()).moveObject(any(), any());
    }
}