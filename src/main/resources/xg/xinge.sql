-- -------------- ---
-- 信鸽---------------
-- -------------- ---


CREATE TABLE `t_messages_record` (
  `id` bigint(20) NOT NULL COMMENT '消息主键id',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `account_id` bigint(20) DEFAULT NULL COMMENT '账号Id,账户变动的各种ID',
  `type` tinyint(4) NOT NULL COMMENT '消息类型',
  `os_type` tinyint(2) unsigned NOT NULL COMMENT '平台类型：1：IOS  2：Android',
  `title` varchar(30) NOT NULL COMMENT '消息标题',
  `content` varchar(512) NOT NULL COMMENT '消息内容',
  `url` varchar(30) DEFAULT NULL COMMENT '给以后的h5跳转用',
  `sender` varchar(30) NOT NULL COMMENT '发送人',
  `is_delete` tinyint(4) NOT NULL COMMENT '是否删除：1、是，2、否',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  `operation_id` bigint(20) DEFAULT NULL COMMENT '运营消息Id',
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人消息记录表'



CREATE TABLE `t_notices_record` (
  `id` bigint(20) NOT NULL COMMENT '消息主键id',
  `account_id` bigint(20) DEFAULT NULL COMMENT '各种通知广播ID',
  `type` tinyint(4) NOT NULL COMMENT '消息类型',
  `title` varchar(30) NOT NULL COMMENT '消息标题',
  `content` varchar(512) NOT NULL COMMENT '消息内容',
  `os_type` tinyint(4) NOT NULL COMMENT '操作系统类型,1,IOS,2,安卓,3,全平台',
  `url` varchar(30) DEFAULT NULL COMMENT '给以后的h5跳转用',
  `sender` varchar(30) NOT NULL COMMENT '发送人',
  `is_delete` tinyint(4) NOT NULL COMMENT '是否删除：1、是，2、否',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  `operation_id` bigint(20) DEFAULT NULL COMMENT '运营消息Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广播通知记录表'

CREATE TABLE `t_operation_mess_record` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作后台的消息id',
  `type` tinyint(4) NOT NULL COMMENT '1, "账户变动",2, "版本升级",3,"新游预约",4,"系统消息",5,"运营广播消息",6,"运营个人消息"',
  `title` varchar(30) NOT NULL COMMENT '消息标题',
  `content` varchar(512) NOT NULL COMMENT '消息内容',
  `sender` varchar(30) NOT NULL COMMENT '发送人',
  `receiver` varchar(1500) NOT NULL COMMENT '接收人',
  `url` varchar(255) DEFAULT NULL COMMENT '链接',
  `is_delete` tinyint(4) NOT NULL COMMENT '是否删除：1、是，2、否',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运营操作消息记录表'


-- -------------- ---
-- 以下两个sql暂定---------------
-- -------------- ---
CREATE TABLE `t_uid_certId` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '主键id',
  `uid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户的uid',
  `cert_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '证书Id',
  `is_delete` tinyint(4) NOT NULL COMMENT '是否删除：1、是，2、否',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户的uid和证书certId的关联表'

CREATE TABLE `t_certId_message` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '主键id',
  `cert_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '证书',
  `access_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '信鸽的accessId',
  `secret_key` bigint(20) NOT NULL DEFAULT '0' COMMENT '信鸽的secretKey',
  `is_delete` tinyint(4) NOT NULL COMMENT '是否删除：1、是，2、否',
  `ctime` int(11) NOT NULL COMMENT '创建时间',
  `utime` int(11) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='证书的id和信鸽的accessId的关联表'
