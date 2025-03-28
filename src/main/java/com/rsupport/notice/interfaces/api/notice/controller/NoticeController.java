package com.rsupport.notice.interfaces.api.notice.controller;

import static com.rsupport.notice.interfaces.api.notice.dto.NoticeRequest.UpdateNoticeRequest;
import static com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.SaveNoticeResponse;

import com.rsupport.notice.application.notice.dto.NoticeDetailInfo;
import com.rsupport.notice.application.notice.dto.NoticeFileResourceInfo;
import com.rsupport.notice.application.notice.dto.NoticeListInfo;
import com.rsupport.notice.application.notice.dto.command.SaveNoticeCommand;
import com.rsupport.notice.application.notice.dto.query.SearchNoticeListQuery;
import com.rsupport.notice.application.notice.usecase.DeleteNoticeUseCase;
import com.rsupport.notice.application.notice.usecase.DownloadNoticeFileUseCase;
import com.rsupport.notice.application.notice.usecase.SaveNoticeUseCase;
import com.rsupport.notice.application.notice.usecase.SearchNoticeListUseCase;
import com.rsupport.notice.application.notice.usecase.UpdateNoticeUseCase;
import com.rsupport.notice.application.notice.usecase.UploadNoticeFileUseCase;
import com.rsupport.notice.application.notice.dto.command.UploadNoticeFileCommand;
import com.rsupport.notice.application.notice.usecase.ViewNoticeDetailUseCase;
import com.rsupport.notice.domain.notice.dto.command.UpdateNoticeCommand;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.interfaces.api.common.response.ApiSuccessResponse;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeRequest;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeRequest.GetNoticeListRequest;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.GetNoticeDetailResponse;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.UploadFileResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
@RestController
public class NoticeController implements NoticeControllerDocs{

    private final UploadNoticeFileUseCase uploadNoticeFileUseCase;
    private final SaveNoticeUseCase saveNoticeUseCase;
    private final DeleteNoticeUseCase deleteNoticeUseCase;
    private final UpdateNoticeUseCase updateNoticeResponse;
    private final ViewNoticeDetailUseCase viewNoticeDetailUseCase;
    private final SearchNoticeListUseCase searchNoticeListUseCase;
    private final DownloadNoticeFileUseCase downloadNoticeFileUseCase;

    @Override
    @GetMapping("/{noticeId}")
    public ApiSuccessResponse<GetNoticeDetailResponse> getNoticeDetail(@PathVariable Long noticeId) {
        NoticeDetailInfo noticeDetailInfo = viewNoticeDetailUseCase.execute(noticeId);
        GetNoticeDetailResponse response = new GetNoticeDetailResponse(noticeDetailInfo);
        return ApiSuccessResponse.OK(response);
    }

    @Override
    @GetMapping("")
    public ApiSuccessResponse<NoticeListInfo> getNoticeList(@Valid GetNoticeListRequest request) {
        SearchNoticeListQuery searchNoticeListQuery = request.toSearchNoticeListQuery();
        NoticeListInfo info = searchNoticeListUseCase.execute(searchNoticeListQuery);

        return ApiSuccessResponse.OK(info);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ApiSuccessResponse<SaveNoticeResponse> saveNotice(@Valid @RequestBody NoticeRequest.SaveNoticeRequest request) {
        SaveNoticeCommand saveNoticeCommand = request.toSaveNoticeCommand();
        Notice notice = saveNoticeUseCase.execute(saveNoticeCommand);
        SaveNoticeResponse saveNoticeResponse = new SaveNoticeResponse(notice.getNoticeId());

        return ApiSuccessResponse.CREATED(saveNoticeResponse);
    }

    @Override
    @PutMapping("/{noticeId}")
    public ApiSuccessResponse<GetNoticeDetailResponse> updateNotice(@PathVariable Long noticeId,
        @Valid @RequestBody UpdateNoticeRequest request) {
        UpdateNoticeCommand updateNoticeCommand = request.toUpdateNoticeCommand(noticeId);
        NoticeDetailInfo noticeDetailInfo = updateNoticeResponse.execute(updateNoticeCommand);

        GetNoticeDetailResponse response = new GetNoticeDetailResponse(noticeDetailInfo);
        return ApiSuccessResponse.OK(response);
    }

    @Override
    @DeleteMapping("/{noticeId}")
    public ApiSuccessResponse<?> deleteNotice(@PathVariable Long noticeId) {
        deleteNoticeUseCase.execute(noticeId, LocalDateTime.now());
        return ApiSuccessResponse.OK();
    }

    @Override
    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiSuccessResponse<UploadFileResponse> uploadFiles(@RequestPart MultipartFile file,
        @RequestParam Long userId) {

        UploadNoticeFileCommand command = new UploadNoticeFileCommand(file, userId);
        NoticeFile noticeFile = uploadNoticeFileUseCase.execute(command);
        UploadFileResponse uploadFileResponse = new UploadFileResponse(noticeFile.getNoticeFileId(),
            noticeFile.getOriginalFileName(), noticeFile.getFileSize());
        return ApiSuccessResponse.OK(uploadFileResponse);
    }

    @Override
    @GetMapping("/{noticeId}/files/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long noticeId, @PathVariable Long fileId) {
        NoticeFileResourceInfo info = downloadNoticeFileUseCase.execute(noticeId, fileId);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + info.getFileName() + "\"")
            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(info.getFileSize()))
            .body(info.getResource());
    }
}
