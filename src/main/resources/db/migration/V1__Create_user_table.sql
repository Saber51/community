CREATE TABLE USER(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    account_id VARCHAR(100),
    name VARCHAR(50),
    token CHAR(36),
    gmt_create BIGINT,
    gmt_modified BIGINT
);

