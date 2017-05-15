DROP DATABASE IF EXISTS `bank`;
CREATE DATABASE `bank` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON bank.* to bank@'%' IDENTIFIED BY 'bank_password';

use bank;

DROP TABLE IF EXISTS `tb_user_info`;
CREATE TABLE `tb_user_info` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增数据库主键',
  `avatarId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户头像',
  `userName` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `email` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '邮箱',
  `cellphone` VARCHAR(16) NOT NULL DEFAULT '' COMMENT '手机号',
  `password` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '密码',
  `sex` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0：未知，1：男，2：女',
  `description` VARCHAR(400) DEFAULT NULL COMMENT '关于我',
  `birthday` DATE DEFAULT NULL COMMENT '出生日期',
  `userType` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '用户类型，0：未知，1：学生，2：老师',
  `status` TINYINT(10) UNSIGNED NOT NULL DEFAULT '1' COMMENT '用户状态,0：无效，1：有效',
  `roleId` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户角色，0普通用户，1超级管理员',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_userName` (`userName`),
  KEY `idx_password` (`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

DROP TABLE IF EXISTS `tb_user_login_log`;
CREATE TABLE `tb_user_login_log`(
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `ip` VARCHAR(15) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `loginTime` INT(5) NOT NULL DEFAULT 0 COMMENT '连续登录次数，每天值增加一次',
  `logoutTime` DATETIME DEFAULT NULL COMMENT '退出时间',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登录记录表';

DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(10) UNSIGNED NOT NULL COMMENT '用户id',
  `roleId` INT(6) UNSIGNED NOT NULL DEFAULT '0' COMMENT '用户角色',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_userId_roleId` (`userId`,`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

DROP TABLE IF EXISTS `tb_user_student`;
CREATE TABLE `tb_user_student` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `sno` INT(11) UNSIGNED NOT NULL COMMENT '学号',
  `userId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '姓名',
  `dept` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '学院',
  `major` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '专业班级',
  `remark` VARCHAR(32) DEFAULT NULL COMMENT '备注',
  `attchmentId` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '附件id',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sno` (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生信息表';

DROP TABLE IF EXISTS `tb_user_teacher`;
CREATE TABLE `tb_user_teacher` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `tno` INT(11) UNSIGNED NOT NULL COMMENT '教师号',
  `name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '姓名',
  `dept` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '学院或部门',
  `remark` VARCHAR(32) DEFAULT NULL COMMENT '备注',
  `attchmentId` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '附件id',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tno` (`tno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教师信息表';

DROP TABLE IF EXISTS `tb_user_auth_attachment`;
CREATE TABLE `tb_user_auth_attachment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `attchmentId` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '附件id',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教师信息表';