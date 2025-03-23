create table users
(
    user_id    bigint auto_increment comment 'user PK'
        primary key,
    username   varchar(20) null comment '사용자 이름',
    created_at datetime    not null comment '생성일시',
    updated_at datetime    null comment '수정일시'
)
    comment '사용자';