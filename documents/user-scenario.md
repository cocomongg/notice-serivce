# 유저 시나리오

## 1. 공지사항 작성
### 1.1 공지사항 작성 페이지 이동
### 1.2 첨부파일 업로드(optional) (call [POST] /api/v1/notices/files)
### 1.3 공지사항 작성
### 1.4 공지사항 작성 완료 (call [POST] /api/v1/notices)
<br/>

## 2. 공지사항 조회
### 2.1 공지사항 목록 조회 (call [GET] /api/v1/notices)
### 2.2 공지사항 상세 조회 (call [GET] /api/v1/notices/{noticeId})
### 2.3 첨부파일 다운로드(optional) (call [GET] /api/v1/notices/{noticeId}/files/{fileId})
<br/>

## 3. 공지사항 수정
### 3.1 공지사항 수정 페이지 이동
### 3.2 기존 첨부파일 삭제(optional)
### 3.3 첨부파일 업로드(optional) (call [POST] /api/v1/notices/files)
### 3.4 공지사항 수정 (call [PUT] /api/v1/notices/{noticeId})