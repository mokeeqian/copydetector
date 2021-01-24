-- auto Generated on 2021-01-24
-- DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role(
	id INT (11) NOT NULL AUTO_INCREMENT COMMENT 'id',
	userid INT (11) NOT NULL DEFAULT -1 COMMENT 'userid',
	roleid INT (11) NOT NULL DEFAULT -1 COMMENT 'roleid',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'user_role';
