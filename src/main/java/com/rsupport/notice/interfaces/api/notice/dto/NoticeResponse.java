package com.rsupport.notice.interfaces.api.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

public class NoticeResponse {

    @Getter
    @RequiredArgsConstructor
    public static class CreateNoticeResponse {
        @Schema(description = "공지사항 ID", example = "1")
        private final Long noticeId;
    }

    @Getter
    @SuperBuilder
    public static class CommonNoticeResponse {
        @Schema(description = "공지사항 ID", example = "1")
        private final Long noticeId;

        @Schema(description = "공지사항 제목", example = "제목")
        private final String title;

        @Schema(description = "공지사항 내용", example = "내용")
        private final String content;

        @Schema(description = "생성 날짜", example = "2025-01-01T00:00:00")
        private final LocalDateTime createdAt;

        @Schema(description = "조회수", example = "100")
        private final int viewCount;

        @Schema(description = "첨부파일 목록")
        private final List<FileItem> files;

        @Schema(description = "작성자 정보")
        private final Writer writer;
    }

    @Getter
    @SuperBuilder
    public static class UpdateNoticeResponse extends CommonNoticeResponse {

    }

    @Getter
    @SuperBuilder
    public static class GetNoticeDetailResponse extends CommonNoticeResponse{

    }

    @Getter
    @Builder
    public static class GetNoticeListResponse {
        @Schema(description = "현재 페이지", example = "1")
        private final Long currentPage;

        @Schema(description = "페이지 크기", example = "10")
        private final Long pageSize;

        @Schema(description = "총 항목 수", example = "2")
        private final Long totalElements;

        @Schema(description = "총 페이지 수", example = "1")
        private final Long totalPages;

        @Schema(description = "공지사항 목록")
        private final List<NoticeItem> notices;
    }

    @Getter
    @RequiredArgsConstructor
    public static class NoticeItem {
        @Schema(description = "공지사항 ID", example = "1")
        private final Long noticeId;

        @Schema(description = "공지사항 제목", example = "제목1")
        private final String title;

        @Schema(description = "파일 존재 여부", example = "true")
        private final boolean hasFiles;

        @Schema(description = "생성 날짜", example = "2025-01-01T00:00:00")
        private final LocalDateTime createdAt;

        @Schema(description = "조회수", example = "100")
        private final int viewCount;

        @Schema(description = "작성자 정보")
        private final Writer writer;
    }

    @Getter
    @RequiredArgsConstructor
    public static class FileItem {
        @Schema(description = "파일 ID", example = "1")
        private final Long fileId;

        @Schema(description = "파일 이름", example = "file.pdf")
        private final String fileName;

        @Schema(description = "파일 크기", example = "10000")
        private final int fileSize;

        @Schema(description = "파일 타입", example = "application/pdf")
        private final String fileType;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Writer {
        @Schema(description = "사용자 ID", example = "1")
        private final Long userId;

        @Schema(description = "사용자 이름", example = "사용자1")
        private final String userName;
    }

    @Getter
    @RequiredArgsConstructor
    public static class UploadFileResponse {
        @Schema(description = "파일 ID", example = "1")
        private final Long fileId;

        @Schema(description = "파일 이름", example = "file.pdf")
        private final String fileName;

        @Schema(description = "파일 크기", example = "10000")
        private final int fileSize;
    }
}
