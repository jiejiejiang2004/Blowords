DROP DATABASE IF EXISTS blowords;
CREATE DATABASE IF NOT EXISTS blowords;

USE blowords;

DROP TABLE IF EXISTS blowords.user;
CREATE TABLE IF NOT EXISTS blowords.user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telephone VARCHAR(40) NOT UNIQUE,
    role VARCHAR(10) DEFAULT 'user',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO blowords.user (username, password, email, role) 
VALUES ('admin998', '2443531e9+7', 'admin@example.com', 'admin');

DROP TABLE IF EXISTS `user_token`;
CREATE TABLE IF NOT EXISTS `user_token` (
  `user_token_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Token主键ID',
  `user_id` BIGINT NOT NULL COMMENT '关联用户ID（外键）',
  `token` varchar(512) NOT NULL COMMENT 'JWT Token',
  `refresh_token` varchar(512) DEFAULT NULL COMMENT '刷新Token',
  `client_type` varchar(20) NOT NULL COMMENT '客户端类型：IOS/ANDROID/WEB/MINI_PROGRAM',
  `device_info` varchar(255) DEFAULT NULL COMMENT '设备信息',
  `ip` varchar(50) DEFAULT NULL COMMENT '登录IP',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1-有效 2-已作废 3-已过期',
  `expire_time` datetime NOT NULL COMMENT 'Token过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_token_id`),
  UNIQUE KEY `uk_token` (`token`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status_expire` (`status`,`expire_time`)
  FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户Token表（多端登录管理）';

