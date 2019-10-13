alter table COMMENT add comment_count INT DEFAULT 0;

comment on column COMMENT.comment_count is '评论数';

