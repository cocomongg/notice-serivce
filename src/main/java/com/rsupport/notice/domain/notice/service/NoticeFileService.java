package com.rsupport.notice.domain.notice.service;

import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.domain.notice.repository.NoticeFileRepository;
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
}
