package com.rsupport.notice.domain.notice.repository;

import com.rsupport.notice.domain.notice.entity.NoticeFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {

    @Modifying
    @Query("UPDATE NoticeFile nf "
        + "SET nf.noticeId = :noticeId, nf.filePath = :path "
        + "WHERE nf.noticeFileId IN :noticeFileIds")
    int updateNoticeFiles(@Param("noticeId") Long noticeId,
        @Param("path") String path,
        @Param("noticeFileIds") Set<Long> noticeFileIds);

    List<NoticeFile> findAllByNoticeFileIdInAndDeletedAtIsNull(Set<Long> ids);

    List<NoticeFile> findAllByNoticeIdAndDeletedAtIsNull(Long noticeId);

    Optional<NoticeFile> findByNoticeFileIdAndNoticeIdAndDeletedAtIsNull(Long noticeFileId, Long noticeId);

    @Modifying
    @Query("UPDATE NoticeFile nf "
        + "SET nf.deletedAt =:now "
        + "WHERE nf.noticeId = :noticeId")
    void updateNoticeFileDeletedAt(@Param("noticeId") Long noticeId, @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE NoticeFile nf "
        + "SET nf.deletedAt =:now "
        + "WHERE nf.noticeFileId IN :noticeFileIds")
    void updateNoticeFileDeletedAt(@Param("noticeFileIds") Set<Long> noticeFileIds, @Param("now") LocalDateTime now);
}
