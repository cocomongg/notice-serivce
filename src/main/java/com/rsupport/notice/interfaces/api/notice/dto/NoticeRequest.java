package com.rsupport.notice.interfaces.api.notice.dto;

import com.rsupport.notice.application.notice.dto.query.SearchNoticeListQuery;
import com.rsupport.notice.domain.notice.dto.command.UpdateNoticeCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

public class NoticeRequest {

    @Getter
    @Setter
    public static class GetNoticeListRequest {
        @Schema(description = "페이지 번호", example = "")
        private int page;

        @Min(10)
        @Max(100)
        @Schema(description = "페이지 크기", example = "10")
        private int size;

        @Schema(description = "검색 타입", example = "title", allowableValues = {"title", "titleAndContent"})
        private String searchType;

        @Schema(description = "검색어", example = "공지사항")
        private String keyword;

        @Schema(description = "시작 날짜", example = "2025-01-01T00:00:00")
        private LocalDateTime from;

        @Schema(description = "종료 날짜", example = "2025-01-01T23:59:59")
        private LocalDateTime to;

        public SearchNoticeListQuery toSearchNoticeListQuery() {
            return new SearchNoticeListQuery(page, size, searchType, keyword, from, to);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SaveNoticeRequest {
        @Size(min = 1, max = 100)
        @NotEmpty
        @Schema(description = "공지사항 제목", example = "제목")
        private String title;

        @Size(max = 3000)
        @Schema(description = "공지사항 내용", example = "내용")
        private String content;

        @NotNull
        @Schema(description = "시작 날짜", example = "2025-01-01T00:00:00")
        private LocalDateTime noticeStartAt;

        @NotNull
        @Schema(description = "종료 날짜", example = "2025-01-01T23:59:59")
        private LocalDateTime noticeEndAt;

        @Size(max = 10)
        @Schema(description = "첨부파일 ID 목록", example = "[1, 2, 3]")
        private List<Long> fileIds;

        @NotNull
        @Schema(description = "사용자 ID", example = "1", allowableValues = {"1", "2"})
        private Long userId;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateNoticeRequest {
        @Size(min = 1, max = 100)
        @NotEmpty
        @Schema(description = "공지사항 제목", example = "수정된 공지사항")
        private String title;

        @Size(max = 3000)
        @Schema(description = "공지사항 내용", example = "수정된 공지사항 내용입니다.")
        private String content;

        @NotNull
        @Schema(description = "시작 날짜", example = "2025-01-01T00:00:00")
        private LocalDateTime startDate;

        @NotNull
        @Schema(description = "종료 날짜", example = "2025-01-01T23:59:59")
        private LocalDateTime endDate;

        @Size(max = 10)
        @Schema(description = "새로 추가할 첨부파일 ID 목록", example = "[1, 2, 3]")
        private List<Long> newFileIds;

        @Size(max = 10)
        @Schema(description = "삭제할 첨부파일 ID 목록", example = "[1, 2, 3]")
        private List<Long> deletedFileIds;

        public UpdateNoticeCommand toUpdateNoticeCommand(Long noticeId) {
            return UpdateNoticeCommand.builder()
                .noticeId(noticeId)
                .title(title)
                .content(content)
                .noticeStartAt(startDate)
                .noticeEndAt(endDate)
                .newFileIds(CollectionUtils.isEmpty(newFileIds) ? Collections.emptySet(): new HashSet<>(newFileIds))
                .deletedFileIds(CollectionUtils.isEmpty(deletedFileIds) ? Collections.emptySet(): new HashSet<>(deletedFileIds))
                .build();
        }
    }
}
