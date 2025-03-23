package com.rsupport.notice.application.notice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rsupport.notice.application.notice.dto.command.UploadNoticeFileCommand;
import com.rsupport.notice.application.notice.usecase.UploadNoticeFileUseCase;
import com.rsupport.notice.domain.file.dto.FileInfo;
import com.rsupport.notice.domain.file.dto.command.FileUploadCommand;
import com.rsupport.notice.domain.file.service.StorageService;
import com.rsupport.notice.domain.file.validator.FileValidator;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.user.entity.User;
import com.rsupport.notice.domain.user.service.UserService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class UploadNoticeFileUseCaseTest {

    @Mock
    private NoticeFileService noticeFileService;
    @Mock
    private UserService userService;
    @Mock
    private StorageService storageService;
    @Mock
    private FileValidator fileValidator;

    @InjectMocks
    private UploadNoticeFileUseCase uploadNoticeFileUseCase;
    
    @DisplayName("입력된 파일을 업로드하고, noticeFile을 저장해서 반환한다.")
    @Test
    void should_UploadFileAndNoticeFile() {
        // given
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.txt", "text/plain", "test".getBytes()
        );
        UploadNoticeFileCommand command = new UploadNoticeFileCommand(file, userId);
        User user = new User(userId, "username", LocalDateTime.now(), null);
        FileInfo fileInfo = FileInfo.builder()
            .fileName("test.txt")
            .fileSize(4L)
            .filePath("test.txt")
            .fileType("text/plain")
            .build();
        NoticeFile noticeFile = new NoticeFile(new CreateNoticeFileCommand(userId, fileInfo));

        doNothing().when(fileValidator).validate(file);
        when(storageService.upload(any(FileUploadCommand.class))).thenReturn(fileInfo);
        when(userService.getUser(userId)).thenReturn(user);
        when(noticeFileService.createNoticeFile(any(CreateNoticeFileCommand.class))).thenReturn(noticeFile);

        // when
        NoticeFile result = uploadNoticeFileUseCase.execute(command);
    
        // then
        assertThat(result).isNotNull();
        verify(fileValidator).validate(file);

        ArgumentCaptor<FileUploadCommand> fileUploadCaptor = ArgumentCaptor.forClass(FileUploadCommand.class);
        verify(storageService).upload(fileUploadCaptor.capture());
        assertEquals(file, fileUploadCaptor.getValue().getMultipartFile());

        verify(userService).getUser(userId);

        ArgumentCaptor<CreateNoticeFileCommand> createCmdCaptor = ArgumentCaptor.forClass(CreateNoticeFileCommand.class);
        verify(noticeFileService).createNoticeFile(createCmdCaptor.capture());
        assertThat(userId).isEqualTo(createCmdCaptor.getValue().getUserId());
        assertThat(fileInfo.getFileName()).isEqualTo(createCmdCaptor.getValue().getOriginalFileName());
        assertThat(fileInfo.getFilePath()).isEqualTo(createCmdCaptor.getValue().getFilePath());
        assertThat((int)fileInfo.getFileSize()).isEqualTo(createCmdCaptor.getValue().getFileSize());

        assertThat(result).isEqualTo(noticeFile);
    }
}