package com.rsupport.notice.domain.notice.entity;

import com.rsupport.notice.domain.notice.dto.command.CreateNoticeFileCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notice_files", indexes = {
    @Index(name = "notice_files_notice_id_index", columnList = "notice_id")
})
public class NoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_file_id")
    private Long noticeFileId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "upload_file_name")
    private String uploadFileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private int fileSize;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public NoticeFile(CreateNoticeFileCommand command) {
        this.userId = command.getUserId();
        this.originalFileName = command.getOriginalFileName();
        this.uploadFileName = command.getUploadFileName();
        this.filePath = command.getFilePath();
        this.fileSize = command.getFileSize();
        this.createdAt = LocalDateTime.now();
    }

    public String getFileFullPath() {
        return String.format("%s/%s", this.filePath, this.uploadFileName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        NoticeFile noticeFile = (NoticeFile) o;
        return Objects.equals(this.noticeFileId, noticeFile.getNoticeFileId());
    }

    @Override
    public int hashCode() {
        return this.noticeFileId.intValue();
    }
}
