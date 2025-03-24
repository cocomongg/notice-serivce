package com.rsupport.notice.domain.notice.service;

import com.rsupport.notice.domain.notice.dto.command.AttachNoticeFilesCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.repository.NoticeFileRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NoticeFileService {

    private final NoticeFileRepository noticeFileRepository;

    @Transactional
    public NoticeFile createNoticeFile(CreateNoticeFileCommand command) {
        NoticeFile noticeFile = new NoticeFile(command);
        return noticeFileRepository.save(noticeFile);
    }

    @Transactional
    public void attachNoticeFiles(AttachNoticeFilesCommand command) {
        noticeFileRepository.updateNoticeFiles(command.getNoticeId(), command.getPath(), command.getFileIds());
    }

    @Transactional(readOnly = true)
    public List<NoticeFile> getNoticeFileList(Set<Long> ids) {
        return noticeFileRepository.findAllByNoticeFileIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<NoticeFile> getNoticeFileList(Long noticeId) {
        return noticeFileRepository.findAllByNoticeId(noticeId);
    }

    @Transactional
    public void deleteNoticeFiles(Long noticeId, LocalDateTime now) {
        noticeFileRepository.updateNoticeFileDeletedAt(noticeId, now);
    }
}
