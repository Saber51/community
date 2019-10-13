alter table QUESTION alter column CREATOR BIGINT NOT NULL;
alter table COMMENT alter column COMMENTATOR BIGINT NOT NULL;
comment on column comment.commentator is '评论人id';