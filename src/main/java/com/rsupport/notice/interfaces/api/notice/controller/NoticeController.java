package com.rsupport.notice.interfaces.api.notice.controller;

import static com.rsupport.notice.interfaces.api.notice.dto.NoticeRequest.UpdateNoticeRequest;
import static com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.SaveNoticeResponse;

import com.rsupport.notice.application.notice.dto.NoticeDetailInfo;
import com.rsupport.notice.application.notice.dto.NoticeListInfo;
import com.rsupport.notice.application.notice.dto.command.SaveNoticeCommand;
import com.rsupport.notice.application.notice.dto.query.SearchNoticeListQuery;
import com.rsupport.notice.application.notice.usecase.DeleteNoticeUseCase;
import com.rsupport.notice.application.notice.usecase.SaveNoticeUseCase;
import com.rsupport.notice.application.notice.usecase.SearchNoticeListUseCase;
import com.rsupport.notice.application.notice.usecase.UploadNoticeFileUseCase;
import com.rsupport.notice.application.notice.dto.command.UploadNoticeFileCommand;
import com.rsupport.notice.application.notice.usecase.ViewNoticeDetailUseCase;
import com.rsupport.notice.domain.notice.entity.Notice;
import com.rsupport.notice.domain.notice.entity.NoticeFile;
import com.rsupport.notice.interfaces.api.common.response.ApiSuccessResponse;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeRequest;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeRequest.GetNoticeListRequest;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.FileItem;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.GetNoticeDetailResponse;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.UpdateNoticeResponse;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.UploadFileResponse;
import com.rsupport.notice.interfaces.api.notice.dto.NoticeResponse.Writer;
import jakarta.validation.Valid;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final ViewNoticeDetailUseCase viewNoticeDetailUseCase;
    private final SearchNoticeListUseCase searchNoticeListUseCase;

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
        SaveNoticeCommand command = SaveNoticeCommand.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .noticeStartAt(request.getNoticeStartAt())
            .noticeEndAt(request.getNoticeEndAt())
            .fileIds(new HashSet<>(request.getFileIds()))
            .userId(request.getUserId())
            .build();
        Notice notice = saveNoticeUseCase.execute(command);
        SaveNoticeResponse saveNoticeResponse = new SaveNoticeResponse(notice.getNoticeId());

        return ApiSuccessResponse.CREATED(saveNoticeResponse);
    }

    @Override
    @PutMapping("/{noticeId}")
    public ApiSuccessResponse<UpdateNoticeResponse> updateNotice(@PathVariable Long noticeId,
        @Valid @RequestBody UpdateNoticeRequest request) {
        FileItem fileItem = new FileItem(1L, "file.pdf", 10000);
        List<FileItem> files = List.of(fileItem);
        Writer writer = new Writer(1L, "사용자1");
        UpdateNoticeResponse updateNoticeResponse = UpdateNoticeResponse.builder()
            .noticeId(noticeId)
            .title("제목")
            .content("내용")
            .createdAt(LocalDateTime.now())
            .viewCount(0)
            .files(files)
            .writer(writer)
            .build();

        return ApiSuccessResponse.OK(updateNoticeResponse);
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
    @GetMapping("/{postId}/files/{fileId}/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long postId, @PathVariable Long fileId)
         {
        String fileName = "README.md";
        byte[] bytes = null;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("README.md");
            bytes = is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length))
            .body(resource);
    }
}
