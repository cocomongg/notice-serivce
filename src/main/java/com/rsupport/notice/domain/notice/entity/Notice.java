package com.rsupport.notice.domain.notice.entity;

import com.rsupport.notice.domain.notice.dto.command.ChangeNoticeCommand;
import com.rsupport.notice.domain.notice.dto.command.CreateNoticeCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notices",
    indexes = @Index(name = "notices_created_at_index", columnList = "created_at")
)
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "notice_start_at")
    private LocalDateTime noticeStartAt;

    @Column(name = "notice_end_at")
    private LocalDateTime noticeEndAt;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Notice(CreateNoticeCommand command) {
        this.title = command.getTitle();
        this.content = command.getContent();
        this.noticeStartAt = command.getNoticeStartAt();
        this.noticeEndAt = command.getNoticeEndAt();
        this.userId = command.getUserId();
        this.createdAt = LocalDateTime.now();
    }

    public void delete(LocalDateTime now) {
        this.deletedAt = now;
    }

    public void change(ChangeNoticeCommand command) {
        this.title = command.getTitle();
        this.content = command.getContent();
        this.noticeStartAt = command.getNoticeStartAt();
        this.noticeEndAt = command.getNoticeEndAt();
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Notice notice = (Notice) o;
        return Objects.equals(this.noticeId, notice.getNoticeId());
    }

    @Override
    public int hashCode() {
        return this.noticeId.intValue();
    }
}
