
use bank;

# SELECT concat('DROP TABLE IF EXISTS ', table_name, ';')
# FROM information_schema.tables
# WHERE table_schema = 'bank';
SET FOREIGN_KEY_CHECKS = 0;

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
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tno` (`tno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教师信息表';


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

DROP TABLE IF EXISTS `tb_user_auth_attachment`;
CREATE TABLE `tb_user_auth_attachment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `attchmentId` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '附件id',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教师信息表';

DROP TABLE IF EXISTS `tb_account_log`;
CREATE TABLE `tb_account_log`(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `money` INT(11) NOT NULL DEFAULT 0 COMMENT '变动积分',
  `type` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '积分变动类型,1:注册奖励：2:充值成功，3：充值失败，4：连续登录，5:上传花费积分，6：被别人下载获得，7：下载扣除',
  `projectId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '相关id,eg:连续登录天数',
  `balance` INT(11) NOT NULL DEFAULT 0 COMMENT '余额',
  #   `accountId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '账户id',
  `remark` VARCHAR(32) DEFAULT NULL COMMENT '备注',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账户变更记录表';

DROP TABLE IF EXISTS `tb_account_pay`;
CREATE TABLE `tb_account_pay`(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `payer` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `money` INT(11) NOT NULL DEFAULT 0 COMMENT '变动积分',
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '1:已支付，待充值，2：充值成功，3：充值失败，退款中，4：已退款',
  `remark` VARCHAR(32) DEFAULT NULL COMMENT '备注',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户充值记录表';

DROP TABLE IF EXISTS `tb_account`;
CREATE TABLE `tb_account`(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `totalMoney` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '总积分',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账户表';


DROP TABLE IF EXISTS `tb_tag_meta`;
CREATE TABLE `tb_tag_meta`(
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '标签名称',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签元数据表';


DROP TABLE IF EXISTS `tb_task_log`;
CREATE TABLE `tb_task_log`(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '同 taskStatus',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务信息日志表';

DROP TABLE IF EXISTS `tb_task_complete`;
CREATE TABLE `tb_task_complete`(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `sender` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '任务创建者',
  `receiver` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '接单者',
  `taskId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '任务id',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '1:已接单，进行中，2：已接单，对方申请关闭，3：同意对方关闭，4：不同意对方关闭，进行中，5：已关闭，6：申请关闭中，进行中，7：对方同意关闭，已结束，8：对方不同意关闭，进行中，9：完成失败，10完成成功',
  `completeTime` DATETIME DEFAULT NULL COMMENT '完成时间',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_taskId` (`taskId`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务信息接单表';

DROP TABLE IF EXISTS `tb_task_tag`;
CREATE TABLE `tb_task_tag`(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(11) NOT NULL DEFAULT 0 COMMENT '用户id',
  `tagId` INT(11) NOT NULL DEFAULT 0 COMMENT '标签id',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务标签信息表';


DROP TABLE IF EXISTS `tb_task_info`;
CREATE TABLE `tb_task_info`(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `title` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '主题',
  `desctiption` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '描述',
  `demand` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '承接要求',
  `personal` VARCHAR(500) NOT NULL DEFAULT '' COMMENT '隐藏信息',
  `address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '服务地点',
  `remark` VARCHAR(100) DEFAULT NULL COMMENT '备注',
  `serviceTime` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '服务时间，例如：周六下午3-5点，预计2小时',
  `urgent` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否加急，0：否，1，：加急',
  `urgentMoney` INT(11) NOT NULL DEFAULT 0 COMMENT '加急积分',
  `money` INT(11) NOT NULL DEFAULT 0 COMMENT '变动积分',
  `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '1:已发送，待接单，2：未接单，已关闭,退款中，3：未接单，已退款，4：已接单，5未完成，关闭中，退款中，6：已关闭，已退款，6：已接单，进行中，7：已接单，已完成，8：已接单，未完成',
  `deadTime` DATETIME NOT NULL COMMENT '截止日期',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务信息表';


DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`(
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `sender` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送者id,0: 系统',
  `receiver` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '接收id',
  `message` VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '类型，1：点赞，2：关注',
  `projectId` INT(11) NOT NULL DEFAULT 0 COMMENT '当为点赞时，点赞的projectId',
  `status` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '消息状态，0：草稿，1：已发送，待查看，2：已查看',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `modifyTime` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息信息表';


SET FOREIGN_KEY_CHECKS = 1;