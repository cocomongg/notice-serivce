package com.rsupport.notice.domain.notice.repository;

import com.rsupport.notice.domain.notice.entity.NoticeFile;
import java.util.List;
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

    List<NoticeFile> findAllByNoticeFileIdIn(Set<Long> ids);
}
