# ER Diagram

<img width="592" alt="Image" src="https://github.com/user-attachments/assets/ed2e2835-c484-442f-a370-35c9d9dc235b" />
<br/>

# Description
- `notices` 테이블과 `notice_files`테이블은 1대다 관계로 설정했다.
  - 공지사항 하나에 여러개의 첨부파일이 존재할 수 있기에 공지사항과 첨부파일을 분리하여 1대다 관계로 설정했다.
<br/><br/>
- user테이블을 제외하고 `notices` 테이블과 `notice_files`테이블에 soft delete를 위해 `deleted_at` 컬럼을 추가했다.
<br/><br/>
- ERD상으로 표현하기 위해 FK를 설정했지만, 실제 ddl에서는 FK를 설정하지 않았다.
  - FK를 설정함으로써, 예상치 못한 데드락 현상이 발생할 수 있고, 성능 저하가 발생할 수 있다고 판단했다.

# DDL
```sql
create table users
(
    user_id    bigint auto_increment comment 'user PK'
        primary key,
    username   varchar(20) null comment '사용자 이름',
    created_at datetime    not null comment '생성일시',
    updated_at datetime    null comment '수정일시'
)
    comment '사용자';


create table notices
(
    notice_id       bigint auto_increment comment 'notice PK'
        primary key,
    user_id         bigint        not null comment '공지사항 작성자 PK',
    title           varchar(100)  not null comment '공지사항 제목',
    content         text          not null comment '공지사항 내용',
    notice_start_at datetime      not null comment '공지 시작일시',
    notice_end_at   datetime      not null comment '공지 종료일시',
    view_count      int default 0 null comment '공지사항 조회 수',
    created_at      datetime      not null comment '생성일시',
    updated_at      datetime      null comment '수정일시',
    deleted_at      datetime      null comment '삭제일시'
)
    comment '공지사항';

create index notices_created_at_index
    on notices (created_at);


create table notice_files
(
    notice_file_id     bigint auto_increment comment 'notice attachements PK'
        primary key,
    user_id            bigint       not null comment 'user PK',
    notice_id          bigint       not null comment 'notice PK',
    original_file_name varchar(255) not null comment '첨부파일 원본 이름',
    file_path          varchar(255) not null comment '첨부파일 경로',
    file_size          int          not null comment '첨부파일 크기',
    created_at         datetime     not null comment '생성일시',
    deleted_at         datetime     null comment '삭제일시'
)
    comment '공지사항 첨부파일';

create index notice_files_notice_id_index
    on notice_files (notice_id);
```