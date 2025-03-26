# API 명세서

## 목차
0. [공통](#0-공통)
    - [공통 응답](#공통-응답)
    - [공통 에러](#공통-에러)
1. [API 목록](#1-api-목록)
    - [공지사항 등록 API](#공지사항-등록-api)
    - [공지사항 수정 API](#공지사항-수정-api)
    - [공지사항 삭제 API](#공지사항-삭제-api)
    - [공지사항 목록 조회 API](#공지사항-목록-조회-api)
    - [공지사항 상세 조회 API](#공지사항-상세-조회-api)
    - [첨부파일 업로드 API](#첨부파일-업로드-api)
    - [첨부파일 다운로드 API](#첨부파일-다운로드-api)
    - [첨부파일 삭제 API](#첨부파일-삭제-api)
      <br/>
      <br/>

## 0. 공통
### 공통 응답
- Response Body
    - status (Integer, 필수): HTTP 상태 코드.
    - data (Object, 선택): 응답 데이터.
```json
{
  "status": 200,
  "data": {
    "key": "value"
  }
}
```
<br/>

### 공통 에러
- Response Body
    - code (String, 필수): 에러 코드.
    - message (String, 필수): 에러 메시지.
```json
{
  "code": "ERROR_CODE",
  "message": "에러 메시지"
}
```

- 공통 에러 (http status code: 500 Internal Server Error)
```json
{
  "code": "INTERNAL_SERVER_ERROR",
  "message": "서버 에러가 발생했습니다."
}
```
<br/>

## 1. API 목록
### 공지사항 등록 API
- **설명**: 공지사항을 등록한다.

**Request**
- **URL**: `/api/v1/notices`
- **Http Method**: `POST`
- **Request Body**
```json
{
  "title": "공지사항 제목",
  "content": "공지사항 내용",
  "noticeStartAt": "2025-03-20T15:30:00Z",
  "noticeEndAt": "2025-03-30T15:30:00Z",
  "fileIds": [1, 2, 3],
  "userId": 1
}
```
- 필드 설명
    - title (String, 필수): 공지사항 제목. 예: 최대 255자.
    - content (String, 필수): 공지사항 내용.
    - noticeStartAt (String, 필수): 공지 시작일시. ISO 8601 형식.
    - noticeEndAt (String, 필수): 공지 종료일시. ISO 8601 형식.
    - fileIds (Array, 선택): 첨부파일 목록.
        - fileId (Integer, 필수): 첨부파일 ID.
    - userId (Integer, 필수): 유저 ID
      <br/>
      <br/>

**Response**
- 성공 (http status: 201 Created)
```json
{
  "status": 200,
  "data": {
    "noticeId": 1
  }
}
```
- 실패 - 유효하지 않은 입력값이 있음 (http status code: 400 Bad Request)
```json
{
  "code": "INVALID_REQUEST",
  "message": "유효하지 않은 값입니다."
}
```
- 실패 - userId에 해당하는 유저 없음 (http status code: 404 Not Found)
```json
{
  "code": "USER_NOT_FOUND",
  "message": "유저를 찾을 수 없습니다."
}
```
<br/>

### 공지사항 수정 API
- **설명**: 공지사항을 수정한다.

**Request**
- **URL**: `/api/v1/notices/{noticeId}`
- **Http Method**: `PUT`
- **Request Body**
```json
{
  "title": "공지사항 제목",
  "content": "공지사항 내용",
  "noticeStartAt": "2025-03-20T15:30:00Z",
  "noticeEndAt": "2025-03-30T15:30:00Z",
  "newFileIds": [1, 2],
  "deletedFileIds": [3]
}
```
- 필드 설명
    - title (String, 선택): 공지사항 제목. 예: 최대 255자.
    - content (String, 선택): 공지사항 내용.
    - noticeStartAt (String, 선택): 공지 시작일시. ISO 8601 형식.
    - noticeEndAt (String, 선택): 공지 종료일시. ISO 8601 형식.
    - newFileIds (Array, 선택): 추가할 첨부파일 목록.
        - fileId (Integer, 필수): 첨부파일 ID.
    - deletedFileIds (Array, 선택): 삭제할 첨부파일 목록.
        - fileId (Integer, 필수): 첨부파일 ID.
          <br/>
          <br/>

**Response**
- 성공 (http status code: 200)
```json
{
  "status": 200,
  "data": {
    "id": 1,
    "title": "수정된 제목",
    "content": "수정된 내용",
    "createdAt": "2025-01-01T00:00:00",
    "viewCount": 100,
    "files": [
      {
        "fileId": 1,
        "fileName": "file.pdf",
        "fileSize": 10000
      }
    ],
    "writer": {
      "userId": 1,
      "userName": "사용자1"
    }
  }
}
```
- 실패 - 유효하지 않은 입력값(http status: 400 Bad Request)
```json
{
  "code": "INVALID_REQUEST",
  "message": "유효하지 않은 값입니다."
}
```
- 실패 - NoticeId에 해당하는 공지사항 없음 (http status code: 404 Not Found)
```json
{
  "code": "NOTICE_NOT_FOUND",
  "message": "공지사항을 찾을 수 없습니다."
}
```
<br/>

### 공지사항 삭제 API
**설명**: 공지사항을 삭제한다.

**Request**
- **URL**: `/api/v1/notices/{noticeId}`
- **Http Method**: `DELETE`

**Response**
- 성공 (http status code: 200)
```json
{
  "status": 200,
  "data": {

  }
}
```
- 실패 - NoticeId에 해당하는 공지사항 없음 (http status code: 404 Not Found)
```json
{
  "code": "NOTICE_NOT_FOUND",
  "message": "공지사항을 찾을 수 없습니다."
}
```
<br/>

### 공지사항 목록 조회 API
**설명**: 검색 조건에 맞는 공지사항 목록을 조회한다.

**Request**
- **URL**: `/api/v1/notices`
- **Http Method**: `GET`
- **Query Parameter**
    - `pageNo` (integer, 선택): 페이지 번호. 기본값: 0
    - `pageSize` (integer, 선택): 페이지 크기. 기본값: 10
    - `searchType` (string, 선택): 검색 조건. ex) title, all(title + content)
    - `keyword` (string, 선택): 검색어
    - `from` (string, 선택): 등록일자 검색기간 시작일. ISO 8601 형식.
    - `to` (string, 선택): 등록일자 검색기간 종료일. ISO 8601 형식.

**Response**
- 성공 (http status code: 200)
```json
{
  "status": 200,
  "data": [
    {
      "pageNo": 1,
      "pageSize": 20,
      "totalElements": 57,
      "totalPages": 3,
      "notices": [
        {
          "noticeId": 1,
          "title": "공지사항 제목 예시",
          "hasAttachments": true,
          "createdAt": "2025-03-20T15:30:00Z",
          "viewCount": 10,
          "writer": {
            "userId": 1,
            "userName": "작성자 이름"
          }
        },
        {
          "id": 2,
          "title": "또 다른 공지사항",
          "hasAttachments": false,
          "createdAt": "2025-03-19T12:00:00Z",
          "viewCount": 11,
          "writer": {
            "userId": 1,
            "userName": "작성자 이름"
          }
        }
      ]
    }
  ]
}
```
- 필드설명
    - pageNo (Integer): 현재 페이지 번호.
    - pageSize (Integer): 페이지 당 항목 수.
    - totalElements (Integer): 전체 항목 수.
    - totalPages (Integer): 전체 페이지 수.
    - notices (Array): 공지사항 목록.
        - id (Integer): 공지사항 고유 식별자.
        - title (String): 공지사항 제목.
        - hasAttachments (Boolean): 첨부파일 존재 여부.
        - createdAt (String): 공지사항 등록일시 (ISO 8601 형식).
        - viewCount (Integer): 조회수.
        - author (String): 작성자 이름 또는 식별 정보.
    - writer (Object): 작성자 정보.
        - userId (Integer): 작성자 ID.
        - userName (String): 작성자 이름.

<br/>

- 실패 - 유효하지 않은 입력값(http status: 400 Bad Request)
```json
{
  "code": "INVALID_REQUEST",
  "message": "유효하지 않은 값입니다."
}
```

<br/>

### 공지사항 상세 조회 API
**설명**: noticeId에 해당하는 공지사항 상세 정보를 조회한다.

**Request**
- **URL**: `/api/v1/notices/{noticeId}`
- **Http Method**: `GET`
- **Path Variable**: `noticeId` (Integer, 필수): 공지사항 ID

**Response**
- 성공 (http status code: 200)
```json
{
  "status": 200,
  "data": {
    "noticeId": 2,
    "title": "제목",
    "content": "내용",
    "createdAt": "2025-03-19T12:00:00Z",
    "viewCount": 11,
    "files": [
      {
        "fileId": 1,
        "fileName": "file.pdf",
        "fileSize": 10000
      }
    ],
    "writer": {
      "userId": 1,
      "userName": "작성자 이름"
    }
  }
}
```
- 실패 - 존재하지 않은 공지사항 (http status code: 404)
```json
{
  "code": "NOTICE_NOT_FOUND",
  "message": "notice not found"
}
```
<br/>

### 첨부파일 업로드 API
**설명**: 파일을 업로드한다.

**Request**
- **URL**: `/api/v1/notices/files`
- **Http Method**: `POST`
- **Http Header**: `Content-Type: multipart/form-data`
- **Request Parameter**
    - `file` (File, 필수): 업로드할 파일
    - `userId` (Integer, 필수): 유저 ID

**Response**
- 성공 (http status code: 200)
```json
{
  "status": 200,
  "data": {
    "fileId": 1,
    "fileName": "file.pdf",
    "fileSize": 10000
  }
}
```
- 실패 - 허용된 파일 사이즈 초과 (http status code: 400)
```json
{
  "code": "FILE_SIZE_EXCEEDED",
  "message": "최대 10MB의 파일을 업로드 할 수 있습니다."
}
```
- 실패 - 허용된 파일 타입이 아님 (http status code: 400)
```json
{
  "code": "FILE_TYPE_NOT_ALLOWED",
  "message": "허용된 파일 타입이 아닙니다."
}
```
- 실패 - 파일이 비어있을 경우 (http status code: 400)
```json
{
  "code": "FILE_IS_EMPTY",
  "message": "업로드할 파일이 존재하지 않습니다."
}
```
- 실패 - 파일 업로드 실패 (http status code: 500)
```json
{
  "code": "FILE_UPLOAD_FAILED",
  "message": "파일 업로드에 실패했습니다."
}
```
- 실패 - 파일 디렉토리 생성 실패 (http status code: 500)
```json
{
  "code": "FILE_DIR_CREATION_FAILED",
  "message": "파일 업로드 디렉토리를 생성하는데 실패했습니다."
}
```
<br/>

### 첨부파일 다운로드 API
- **설명**: fileId에 해당하는 파일을 다운로드한다.

**Request**
- **URL**: `/api/v1/notices/{noticeId}/files/{fileId}/download`
- **Http Method**: `GET`
- **Path Variable**: 
  - `noticeId` (Integer, 필수): 공지사항 ID
  - `fileId` (Integer, 필수): 첨부파일 ID

**Response**
- 성공 (http status code: 200)
- 응답 데이터: 파일 바이너리 데이터 스트림

<br/>

- 실패 (http status code: 404)
```json
{
  "code": "FILE_NOT_FOUND",
  "message": "첨부파일을 찾을 수 없습니다."
}
```
<br/>
<br/>
