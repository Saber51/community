create table comment
(
	id BIGINT auto_increment,
	parent_id BIGINT not null,
	type int not null,
	commentator int not null,
	gmt_create BIGINT not null,
	gmt_modified BIGINT not null,
	like_count BIGINT default 0,
	constraint comment_pk
		primary key (id)
);

comment on column comment.parent_id is '父类ID';

comment on column comment.type is '父类类型';

comment on column comment.commentator is '评论人id';

comment on column comment.gmt_create is '创建时间';

comment on column comment.gmt_modified is '更新时间';

comment on column comment.like_count is '点赞数';

