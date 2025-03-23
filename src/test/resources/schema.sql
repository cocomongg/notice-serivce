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
    notice_id          bigint       null comment 'notice PK',
    original_file_name varchar(255) not null comment '첨부파일 원본 이름',
    file_path          varchar(255) not null comment '첨부파일 경로',
    file_size          int          not null comment '첨부파일 크기',
    created_at         datetime     not null comment '생성일시',
    deleted_at         datetime     null comment '삭제일시'
)
    comment '공지사항 첨부파일';

create index notice_files_notice_id_index
    on notice_files (notice_id);