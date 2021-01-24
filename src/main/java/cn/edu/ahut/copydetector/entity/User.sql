-- auto Generated on 2021-01-24
-- DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`(
	id INT (11) NOT NULL AUTO_INCREMENT COMMENT 'id',
	username VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'username',
	`password` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'password',
	realname VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'realname',
	department VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'department',
	major VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'major',
	create_time VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'createTime',
	last_login_time VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'lastLoginTime',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'user';
