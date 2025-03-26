package com.rsupport.notice.interfaces.api.notice.controller;

import static com.rsupport.notice.interfaces.api.notice.dto.NoticeRequest.SaveNoticeRequest;
import static com.rsupport.notice.interfaces.api.notice.dto.NoticeRequest.UpdateNoticeRequest;

import com.rsupport.notice.application.notice.dto.NoticeListInfo;
import com.rsupport.notice.interfaces.api.common.response.ApiSuccessResponse;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeRequest.GetNoticeListRequest;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.SaveNoticeResponse;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.GetNoticeDetailResponse;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.UploadFileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "공지사항 API", description = "공지사항 관련 API")
public interface NoticeControllerDocs {
    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상세 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "공지사항 상세 조회 성공",
            useReturnTypeSchema = true),
        @ApiResponse(responseCode = "404", description = "notice not found",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "NOTICE_NOT_FOUND",
                        "message": "공지사항을 찾을 수 없습니다."
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "INTERNAL_SERVER_ERROR",
                        "message": "서버 에러가 발생했습니다."
                    }
                    """))),
    })
    ApiSuccessResponse<GetNoticeDetailResponse> getNoticeDetail(Long noticeId);

    @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "공지사항 목록 조회 성공",
            useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 검색 조건",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "INVALID_REQUEST",
                        "message": "유효하지 않은 값입니다."
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "INTERNAL_SERVER_ERROR",
                        "message": "서버 에러가 발생했습니다."
                    }
                    """))),
    })
    ApiSuccessResponse<NoticeListInfo> getNoticeList(@ParameterObject GetNoticeListRequest request);

    @Operation(summary = "공지사항 등록", description = "공지사항 등록")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "공지사항 등록 성공",
            useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "INVALID_REQUEST",
                        "message": "유효하지 않은 값입니다."
                    }
                    """))),
        @ApiResponse(responseCode = "404", description = "user not found",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "USER_NOT_FOUND",
                        "message": "사용자를 찾을 수 없습니다."
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "INTERNAL_SERVER_ERROR",
                        "message": "서버 에러가 발생했습니다."
                    }
                    """))),
    })
    ApiSuccessResponse<SaveNoticeResponse> saveNotice(SaveNoticeRequest request);

    @Operation(summary = "공지사항 수정", description = "공지사항 수정")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "공지사항 수정 성공",
            useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "INVALID_REQUEST",
                        "message": "유효하지 않은 값입니다."
                    }
                    """))),
        @ApiResponse(responseCode = "404", description = "notice not found",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "NOTICE_NOT_FOUND",
                        "message": "공지사항을 찾을 수 없습니다."
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "INTERNAL_SERVER_ERROR",
                        "message": "서버 에러가 발생했습니다."
                    }
                    """))),
    })
    ApiSuccessResponse<GetNoticeDetailResponse> updateNotice(Long noticeId, UpdateNoticeRequest request);

    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "공지사항 삭제 성공",
            useReturnTypeSchema = true),
        @ApiResponse(responseCode = "404", description = "notice not found",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "NOTICE_NOT_FOUND",
                        "message": "공지사항을 찾을 수 없습니다."
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "INTERNAL_SERVER_ERROR",
                        "message": "서버 에러가 발생했습니다."
                    }
                    """))),
    })
    ApiSuccessResponse<?> deleteNotice(Long noticeId);

    @Operation(summary = "공지사항 첨부파일 다운로드", description = "공지사항 첨부파일 다운로드")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파일 다운로드 성공", content = @Content(
                mediaType = "application/octet-stream",
                schema = @Schema(type = "string", format = "binary"))),
        @ApiResponse(responseCode = "404", description = "notice not found",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "NOTICE_NOT_FOUND",
                        "message": "공지사항을 찾을 수 없습니다."
                    }
                    """))),
        @ApiResponse(responseCode = "404", description = "file not found",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "FILE_NOT_FOUND",
                        "message": "첨부파일을 찾을 수 없습니다."
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "code": "INTERNAL_SERVER_ERROR",
                        "message": "서버 에러가 발생했습니다."
                    }
                    """))),
    })
    ResponseEntity<Resource> downloadFile(Long postId, Long fileId);

    @Operation(summary = "첨부파일 업로드", description = "첨부파일 업로드")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "첨부파일 업로드 성공",
            useReturnTypeSchema = true),
        @ApiResponse(
            responseCode = "400", description = "잘못된 요청 - 허용된 파일 타입 및 크기 초과 등",
            content = @Content(mediaType = "application/json", examples = {
                @ExampleObject(
                    name = "FILE_IS_EMPTY",
                    value = """
                    {
                        "code": "FILE_IS_EMPTY",
                        "message": "업로드할 파일이 존재하지 않습니다."
                    }
                    """
                ),
                @ExampleObject(
                    name = "FILE_SIZE_EXCEEDED",
                    value = """
                    {
                        "code": "FILE_SIZE_EXCEEDED",
                        "message": "최대 10MB의 파일을 업로드 할 수 있습니다."
                    }
                    """
                ),
                @ExampleObject(
                    name = "FILE_TYPE_NOT_ALLOWED",
                    value = """
                    {
                        "code": "FILE_TYPE_NOT_ALLOWED",
                        "message": "허용된 파일 타입이 아닙니다."
                    }
                    """)})),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", examples = {
                @ExampleObject(
                    name = "FILE_DIR_CREATION_FAILED",
                    value = """
                    {
                        "code": "FILE_DIR_CREATION_FAILED",
                        "message": "파일 업로드 디렉토리를 생성하는데 실패했습니다."
                    }
                    """
                ),
                @ExampleObject(
                    name = "FILE_UPLOAD_FAILED",
                    value = """
                    {
                        "code": "FILE_UPLOAD_FAILED",
                        "message": "파일 업로드에 실패했습니다."
                    }
                    """
                ),
                @ExampleObject(
                    name = "INTERNAL_SERVER_ERROR",
                    value = """
                    {
                        "code": "INTERNAL_SERVER_ERROR",
                        "message": "서버 에러가 발생했습니다."
                    }
                    """
                ),
            })),
    })
    ApiSuccessResponse<UploadFileResponse> uploadFiles(@Parameter(
        description = "업로드할 파일 jpeg, png, pdf만 업로드 가능하며 최대 10MB까지 업로드 가능",
        content = @Content(
            mediaType = "multipart/form-data",
            schema = @Schema(type = "string", format = "binary")
        )
    ) MultipartFile file, Long userId);
}
