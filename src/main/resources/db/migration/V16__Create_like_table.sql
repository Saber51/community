create table liked(
	id bigint auto_increment primary key not null,
	like_creator bigint not null,
	gmt_create bigint not null,
	gmt_modified bigint not null,
	like_comment bigint not null,
	status smallint(1) default 1
);


