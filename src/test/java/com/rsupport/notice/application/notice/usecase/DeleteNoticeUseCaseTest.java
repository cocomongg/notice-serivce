package com.rsupport.notice.application.notice.usecase;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rsupport.notice.domain.file.service.StorageService;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.service.NoticeFileService;
import com.rsupport.notice.domain.notice.service.NoticeService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteNoticeUseCaseTest {

    @Mock
    private NoticeService noticeService;

    @Mock
    private NoticeFileService noticeFileService;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private DeleteNoticeUseCase deleteNoticeUseCase;

    @DisplayName("Notice를 삭제하고, 첨부파일이 존재하면 파일도 삭제한다.")
    @Test
    void execute_whenNoticeFilesExist_thenDeleteFilesAndRemoveStorage() {
        // given
        Long noticeId = 1L;
        LocalDateTime now = LocalDateTime.now();

        String tempFile1 = "path/to/file1";
        String tempFile2 = "path/to/file2";

        CreateNoticeFileCommand createNoticeFileCommand = new CreateNoticeFileCommand(1L, "file1",
            "file1", "path/to", 100);
        NoticeFile file1 = new NoticeFile(createNoticeFileCommand);

        createNoticeFileCommand = new CreateNoticeFileCommand(1L, "file2",
            "file2", "path/to", 200);
        NoticeFile file2 = new NoticeFile(createNoticeFileCommand);

        List<NoticeFile> noticeFiles = Arrays.asList(file1, file2);
        when(noticeFileService.getNoticeFileList(noticeId)).thenReturn(noticeFiles);

        // when
        deleteNoticeUseCase.execute(noticeId, now);

        // then
        verify(noticeService).deleteNotice(noticeId, now);
        verify(noticeFileService).getNoticeFileList(noticeId);
        verify(noticeFileService).deleteNoticeFiles(noticeId, now);
        verify(storageService).remove(tempFile1);
        verify(storageService).remove(tempFile2);
    }

    @DisplayName("Notice를 삭제하고, 첨부파일이 존재하지 않으면 파일 삭제 관련 호출을 하지 않는다.")
    @Test
    void execute_whenNoNoticeFiles_thenSkipDeletionAndStorageRemoval() {
        // given
        Long noticeId = 1L;
        LocalDateTime now = LocalDateTime.now();
        when(noticeFileService.getNoticeFileList(noticeId)).thenReturn(Collections.emptyList());

        // when
        deleteNoticeUseCase.execute(noticeId, now);

        // then
        verify(noticeService).deleteNotice(noticeId, now);
        verify(noticeFileService).getNoticeFileList(noticeId);
        verify(noticeFileService, never()).deleteNoticeFiles(noticeId, now);
        verify(storageService, never()).remove(org.mockito.ArgumentMatchers.anyString());
    }
}