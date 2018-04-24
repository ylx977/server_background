CREATE TABLE `t_appkey_info` (
  `appkey_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键唯一标识',
  `desc` varchar(30) NOT NULL COMMENT '描述',
  `key` varchar(100) NOT NULL COMMENT '密钥',
  `outer_key` varchar(100) DEFAULT NULL COMMENT '外部渠道密钥md5',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`appkey_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4700002 DEFAULT CHARSET=utf8 COMMENT='appkey实例表';

-- ----------------------------
-- Table structure for t_app_user_operation
-- ----------------------------
CREATE TABLE `t_app_user_operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键唯一标识',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `op_type` smallint(6) NOT NULL COMMENT '操作类型(1-登录,2-注册,3-变更绑手机,4-变更密码)',
  `op_type_detail` smallint(6) NOT NULL COMMENT '操作类型(101-正常登录,102-自动登录,103-qq登录,104-微信登录,105-新浪微博登录,201-正常注册,202-qq注册,203-微信注册,202-新浪微博注册)',
  `first_login` smallint(6) DEFAULT NULL COMMENT '是否首次登录(1-是，2-否)',
  `op_date` varchar(8) NOT NULL COMMENT '操作日期',
  `op_ip` varchar(20) DEFAULT NULL COMMENT '操作ip',
  `op_imei` varchar(20) DEFAULT NULL COMMENT '设备编号',
  `terminal_version` varchar(20) DEFAULT NULL COMMENT '终端版本',
  `terminal_dt` varchar(150) DEFAULT NULL COMMENT '终端机型',
  `terminal_name` varchar(30) DEFAULT NULL COMMENT '终端名称',
  `terminal_type` smallint(6) DEFAULT NULL COMMENT '终端类型(1-ios,2-安卓)',
  `version` varchar(15) DEFAULT NULL COMMENT 'app版本号',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `index_user_type` (`user_id`,`op_type`)
) ENGINE=InnoDB AUTO_INCREMENT=161235 DEFAULT CHARSET=utf8 COMMENT='app用户操作记录表';

-- ----------------------------
-- Table structure for t_cat_hd_relation
-- to delete : 已废弃
-- ----------------------------
CREATE TABLE `t_cat_hd_relation` (
  `relation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '对照id',
  `user_id` bigint(20) NOT NULL COMMENT '游戏猫用户Id',
  `hd_user_id` varchar(15) NOT NULL COMMENT '浩动用户Id',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=318745 DEFAULT CHARSET=utf8 COMMENT='游戏猫Id与浩动Id对照表';

-- ----------------------------
-- Table structure for t_channel
-- to delete : 之前渠道同步用的
-- ----------------------------
CREATE TABLE `t_channel` (
  `channel_name` varchar(50) NOT NULL COMMENT '主键,渠道名',
  `app_chl_id` varchar(50) NOT NULL COMMENT 'app渠道id',
  `sdk_chl_id` varchar(50) NOT NULL COMMENT 'sdk渠道id',
  `ctime` int(11) DEFAULT NULL COMMENT '创建时间(秒)',
  `utime` int(11) DEFAULT NULL COMMENT '更新时间(秒)',
  `is_delete` smallint(2) DEFAULT NULL COMMENT ' 是否删除 0:否 1：是',
  PRIMARY KEY (`channel_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_everyday_get_exp
-- ----------------------------
CREATE TABLE `t_everyday_get_exp` (
  `record_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户Id',
  `user_op_type` smallint(3) NOT NULL COMMENT '用户操作类型(1-发帖,2-参与评论,3-参与回复,4-评论被赞,6-分享。评论新闻/攻略/评测,)',
  `user_op_typeId` int(10) DEFAULT NULL COMMENT '用户操作类型ID(评论ID,帖子ID,被分享的资源Id)',
  `user_op_date` date NOT NULL COMMENT '操作日期',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`record_id`),
  KEY `index_uid_type_date` (`user_id`,`user_op_type`,`user_op_typeId`,`user_op_date`)
) ENGINE=InnoDB AUTO_INCREMENT=438 DEFAULT CHARSET=utf8 COMMENT='每日一次性获得经验表';

-- ----------------------------
-- Table structure for t_exp_get_limit
-- ----------------------------
CREATE TABLE `t_exp_get_limit` (
  `limit_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户Id',
  `user_op` smallint(3) NOT NULL COMMENT '用户操作(1-发帖,2-参与评论,3-参与回复,4-评论被赞,6-分享)',
  `user_op_time` smallint(2) NOT NULL COMMENT '用户操作次数',
  `user_op_date` date NOT NULL COMMENT '操作日期',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`limit_id`),
  KEY `index_uid_op_date` (`user_id`,`user_op`,`user_op_date`)
) ENGINE=InnoDB AUTO_INCREMENT=8829 DEFAULT CHARSET=utf8 COMMENT='每日限定次数获取经验限制表';

-- ----------------------------
-- Table structure for t_exp_grade
-- ----------------------------
CREATE TABLE `t_exp_grade` (
  `grade_kind` varchar(4) NOT NULL COMMENT '经验等级(Lv1-Lv9)',
  `grade_exp` int(6) NOT NULL COMMENT '等级对应经验',
  PRIMARY KEY (`grade_kind`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='经验等级表';

-- ----------------------------
-- Table structure for t_init_platform_account
-- ----------------------------
CREATE TABLE `t_init_platform_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account` varchar(15) NOT NULL COMMENT '喵号',
  `is_use` smallint(6) NOT NULL DEFAULT '1' COMMENT '是否使用(1-是,2-否)',
  `is_nice_no` smallint(6) NOT NULL DEFAULT '2' COMMENT '是否靓号(1-是,2-否)',
  `is_redis` smallint(6) NOT NULL DEFAULT '2' COMMENT '是否存储在缓存(1-是,2-否)',
  `first_num` smallint(6) DEFAULT NULL COMMENT '号码首位数字',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_account` (`account`),
  KEY `index_account_prop` (`is_use`,`is_nice_no`,`is_redis`,`first_num`)
) ENGINE=InnoDB AUTO_INCREMENT=9101982 DEFAULT CHARSET=utf8 COMMENT='喵号生成表';

-- ----------------------------
-- Table structure for t_msg_option
-- ----------------------------
CREATE TABLE `t_msg_option` (
  `msg_code` varchar(20) NOT NULL,
  `msg_title` varchar(100) DEFAULT NULL,
  `msg_content` varchar(300) NOT NULL,
  `msg_remark` varchar(200) DEFAULT NULL,
  `ctime` datetime NOT NULL,
  `utime` datetime NOT NULL,
  `is_delete` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`msg_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_onetime_get_gmlcoin
-- ----------------------------
CREATE TABLE `t_onetime_get_gmlcoin` (
  `record_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户Id',
  `user_op_type` smallint(3) NOT NULL COMMENT '用户操作类型(101-游戏首次下载,102-首次绑定手机,103-首次绑定邮箱,104-加圈子)',
  `user_op_typeId` int(10) DEFAULT NULL COMMENT '用户操作类型ID(目前主要是游戏首次下载记录)',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`record_id`),
  KEY `index_uid_type_id` (`user_id`,`user_op_type`,`user_op_typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='一次性获得猫币记录表';

-- ----------------------------
-- Table structure for t_pc_user_token
-- to delete : 已废弃
-- ----------------------------
CREATE TABLE `t_pc_user_token` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `pc_token` varchar(100) NOT NULL COMMENT 'pc session',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除: 1:正常, 0:删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `index_pc_token` (`pc_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户pc端登录token';

-- ----------------------------
-- Table structure for t_plat_device
-- ----------------------------
CREATE TABLE `t_plat_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(100) NOT NULL,
  `terminal_name` varchar(50) DEFAULT NULL,
  `terminal_type` smallint(2) DEFAULT NULL COMMENT '1-ios ,2-android',
  `devices_id` varchar(100) DEFAULT NULL COMMENT '设备自带标识码',
  `merchant_id` varchar(50) DEFAULT NULL COMMENT 'APP渠道，若一个设备安装过多个版本的APP，则 通过 “”|“”方式来区分',
  `unique_key` varchar(160) DEFAULT NULL COMMENT '认证码 加密前的 信息',
  `ctime` datetime NOT NULL,
  `utime` datetime NOT NULL,
  `is_delete` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `union_id` (`unique_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5646594 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sdk_user_openid
-- ----------------------------
CREATE TABLE `t_sdk_user_openid` (
  `id` bigint(6) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(10) NOT NULL,
  `channel_id` varchar(30) NOT NULL,
  `open_id` varchar(50) NOT NULL,
  `ctime` datetime NOT NULL,
  `utime` datetime NOT NULL,
  `is_delete` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `openid` (`open_id`),
  UNIQUE KEY `userid_chl` (`user_id`,`channel_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29492 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sdk_user_operation
-- to delete : 消息队列
-- ----------------------------
CREATE TABLE `t_sdk_user_operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键唯一标识',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `open_id` varchar(50) NOT NULL COMMENT '游戏openId',
  `promoter_id` varchar(30) DEFAULT NULL COMMENT '推广员id',
  `op_type` smallint(6) NOT NULL COMMENT '操作类型(1-登录,2-注册,3-变更绑手机,4-变更密码)',
  `op_type_detail` smallint(6) NOT NULL COMMENT '操作类型(101-正常登录,102-自动登录,103-qq登录,104-一键登录,201-正常注册,202-一键注册,203-快速注册,204-qq注册)',
  `outter_game_id` varchar(50) NOT NULL COMMENT '游戏id',
  `platform_id` varchar(50) NOT NULL COMMENT '平台id',
  `chl_id` varchar(150) NOT NULL COMMENT '渠道包id',
  `first_login` smallint(6) DEFAULT NULL COMMENT '是否首次登录(1-是，2-否)',
  `op_date` varchar(8) NOT NULL COMMENT '操作日期',
  `op_ip` varchar(20) DEFAULT NULL COMMENT '操作ip',
  `op_imei` varchar(20) DEFAULT NULL COMMENT '设备编号',
  `terminal_version` varchar(20) DEFAULT NULL COMMENT '终端版本',
  `terminal_dt` varchar(150) DEFAULT NULL COMMENT '终端机型',
  `terminal_name` varchar(30) DEFAULT NULL COMMENT '终端名称',
  `terminal_type` smallint(6) DEFAULT NULL COMMENT '终端类型(1-ios,2-安卓)',
  `version` varchar(15) DEFAULT NULL COMMENT 'sdk版本号',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `index_promoter_type_time` (`promoter_id`,`op_type`,`ctime`),
  KEY `index_user_date_type` (`user_id`,`outter_game_id`,`op_date`,`op_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1176662 DEFAULT CHARSET=utf8 COMMENT='sdk用户操作记录表';

-- ----------------------------
-- Table structure for t_sdk_user_token
-- ----------------------------
CREATE TABLE `t_sdk_user_token` (
  `id` bigint(6) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(10) NOT NULL,
  `channel_id` varchar(30) NOT NULL,
  `game_id` varchar(50) NOT NULL COMMENT '游戏id',
  `sdk_token` varchar(35) NOT NULL,
  `ctime` datetime NOT NULL,
  `utime` datetime NOT NULL,
  `is_delete` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `index_sdk_token` (`sdk_token`)
) ENGINE=InnoDB AUTO_INCREMENT=172435 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sign_count
-- to delete : 已废弃
-- ----------------------------
CREATE TABLE `t_sign_count` (
  `sign_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户Id',
  `tmonth_sign_count` smallint(2) DEFAULT NULL COMMENT '当月累计签到次数',
  `sign_month` varchar(6) NOT NULL COMMENT '签到年月',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`sign_id`),
  KEY `index_uid_month` (`user_id`,`sign_month`)
) ENGINE=InnoDB AUTO_INCREMENT=55326 DEFAULT CHARSET=utf8 COMMENT='签到统计表';

-- ----------------------------
-- Table structure for t_sign_detail
-- to delete : 已废弃
-- ----------------------------
CREATE TABLE `t_sign_detail` (
  `detail_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户Id',
  `sign_date` date NOT NULL COMMENT '签到日期',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`detail_id`),
  KEY `index_uid_date` (`user_id`,`sign_date`)
) ENGINE=InnoDB AUTO_INCREMENT=129168 DEFAULT CHARSET=utf8 COMMENT='签到明细表';

-- ----------------------------
-- Table structure for t_user_base_info
-- ----------------------------
CREATE TABLE `t_user_base_info` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_platform_id` int(10) NOT NULL COMMENT '用户平台ID',
  `user_password` varchar(50) NOT NULL COMMENT '密码',
  `user_headImg_path` varchar(256) DEFAULT NULL COMMENT '头像路径',
  `user_alias` varchar(30) DEFAULT NULL COMMENT '别名(可用于用户登录)',
  `user_name` varchar(30) NOT NULL DEFAULT '游戏猫' COMMENT '用户昵称',
  `user_sex` smallint(1) DEFAULT NULL COMMENT '性别(0-男,1-女)',
  `user_age` varchar(10) DEFAULT NULL COMMENT '出生日期',
  `user_qq` varchar(15) DEFAULT NULL COMMENT '用户QQ',
  `user_status` smallint(1) NOT NULL COMMENT '用户状态(0-正常,1-冻结)',
  `user_lastLogin_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  `user_register_ip` varchar(40) DEFAULT NULL COMMENT '注册ip',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '更新时间',
  `user_register_account` varchar(50) DEFAULT NULL COMMENT '用户帐号,如果是自定义或者第三方登录,则记录游戏猫Id',
  `isMovement` smallint(1) DEFAULT '0' COMMENT '是否接受推送 0-是,1-否',
  `first_zm` varchar(1) DEFAULT '#' COMMENT '首字母',
  `user_age_num` int(3) DEFAULT '0' COMMENT '年龄值',
  `user_online_state` smallint(1) DEFAULT '1' COMMENT '用户在线状态(1-在线,2-离线,3-隐身)',
  `sign_name` varchar(100) DEFAULT NULL COMMENT '用户签名',
  `user_register_chl` varchar(50) DEFAULT NULL COMMENT '注册渠道号',
  `user_version` varchar(50) DEFAULT NULL COMMENT '注册版本号',
  `user_addr` varchar(150) DEFAULT NULL COMMENT '注册地址',
  `user_register_date` varchar(10) DEFAULT NULL COMMENT '注册日期',
  `terminalType` smallint(1) DEFAULT '0' COMMENT '注册终端(0-非终端,1-ios,2-安卓)',
  `register_ua` smallint(6) DEFAULT NULL COMMENT '用户注册来源（1-app,2-sdk）',
  PRIMARY KEY (`user_id`),
  KEY `index_user_platform_id` (`user_platform_id`),
  KEY `index_user_name` (`user_name`),
  KEY `index_chl_ctm` (`user_register_chl`,`ctime`)
) ENGINE=InnoDB AUTO_INCREMENT=1634524 DEFAULT CHARSET=utf8 COMMENT='用户基本资料信息表';

-- ----------------------------
-- Table structure for t_user_catpt
-- ----------------------------
CREATE TABLE `t_user_catpt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '获取id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `account` varchar(30) NOT NULL COMMENT '帐号',
  `total` int(11) NOT NULL COMMENT '喵点累计充值数量',
  `surplus_total` int(11) NOT NULL COMMENT '喵点剩余数量',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8 COMMENT='用户喵点';

-- ----------------------------
-- Table structure for t_user_count
-- to delete : 无效统计
-- ----------------------------
CREATE TABLE `t_user_count` (
  `count_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `today_login` int(8) DEFAULT NULL COMMENT '当日登录数',
  `today_register` int(8) DEFAULT NULL COMMENT '当日注册数',
  `today_temp1` int(8) DEFAULT NULL COMMENT '临时字段',
  `today_temp2` int(8) DEFAULT NULL COMMENT '临时字段',
  `count_date` date NOT NULL COMMENT '统计日期',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`count_id`)
) ENGINE=InnoDB AUTO_INCREMENT=329 DEFAULT CHARSET=utf8 COMMENT='统计信息表';

-- ----------------------------
-- Table structure for t_user_count_chl
-- to delete : 无效统计
-- ----------------------------
CREATE TABLE `t_user_count_chl` (
  `count_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `merchant_chl` varchar(30) DEFAULT NULL COMMENT '商户渠道号',
  `android_rgst` int(11) DEFAULT '0' COMMENT '安卓注册总数',
  `ios_rgst` int(11) DEFAULT '0' COMMENT 'ios注册总数',
  `count_date` varchar(8) NOT NULL COMMENT '统计日期(统计昨天之前数据)',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`count_id`),
  KEY `index_chl_count_date` (`merchant_chl`,`count_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户用户注册统计信息表';

-- ----------------------------
-- Table structure for t_user_every_count_chl
-- to delete : 无效统计
-- ----------------------------
CREATE TABLE `t_user_every_count_chl` (
  `count_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `merchant_chl` varchar(30) DEFAULT NULL COMMENT '商户渠道号',
  `today_android_rgst` int(11) DEFAULT '0' COMMENT '当日安卓注册数',
  `today_ios_rgst` int(11) DEFAULT '0' COMMENT '当日ios注册数',
  `today_sum_rgst` int(11) DEFAULT '0' COMMENT '当日总人数',
  `register_date` varchar(8) NOT NULL COMMENT '注册日期',
  `register_month` varchar(6) NOT NULL COMMENT '注册年月',
  `count_date` varchar(8) NOT NULL COMMENT '统计日期(注册日期后一天)',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`count_id`),
  KEY `index_chl__rgst_date` (`merchant_chl`,`register_date`),
  KEY `index_chl_count_date` (`merchant_chl`,`count_date`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8 COMMENT='商户用户每日注册统计信息表';

-- ----------------------------
-- Table structure for t_user_exp_count
-- ----------------------------
CREATE TABLE `t_user_exp_count` (
  `count_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户Id',
  `tday_getExp` varchar(22) DEFAULT NULL COMMENT '当日获得经验',
  `exp_total` varchar(22) DEFAULT NULL COMMENT '经验总数',
  `exp_grade` smallint(2) DEFAULT NULL COMMENT '当前经验等级',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`count_id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=66925 DEFAULT CHARSET=utf8 COMMENT='用户经验统计表';

-- ----------------------------
-- Table structure for t_user_exp_detail
-- ----------------------------
CREATE TABLE `t_user_exp_detail` (
  `detail_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户Id',
  `user_op_type` smallint(3) NOT NULL COMMENT '用户操作类型(1-发帖,2-参与评论,3-参与回复,4-评论被赞,5-帖子被评论,6-分享)',
  `user_op_id` int(10) DEFAULT NULL COMMENT '操作类型Id,评论记录,资讯视频的id,回复记录评论的id',
  `user_op_title` varchar(150) DEFAULT '' COMMENT '操作类型标题',
  `remark` varchar(150) DEFAULT '' COMMENT '备注',
  `exp_get` int(11) NOT NULL COMMENT '获得经验数',
  `user_op_date` date NOT NULL COMMENT '操作日期',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`detail_id`),
  KEY `index_uid_date` (`user_id`,`user_op_date`,`ctime`)
) ENGINE=InnoDB AUTO_INCREMENT=12887665 DEFAULT CHARSET=utf8 COMMENT='用户经验获取明细表';

-- ----------------------------
-- Table structure for t_user_frozen
-- ----------------------------
CREATE TABLE `t_user_frozen` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `operation_type` smallint(2) NOT NULL DEFAULT '0' COMMENT '操作类型：0 - 解冻；1 - 冻结',
  `oper_id` bigint(20) NOT NULL COMMENT '冻结人/解冻人 ID',
  `oper_name` varchar(40) DEFAULT NULL COMMENT '冻结人/解冻人 名称',
  `frozen_reason` varchar(150) DEFAULT NULL COMMENT '冻结原因',
  `ctime` datetime NOT NULL,
  `utime` datetime NOT NULL,
  `is_delete` smallint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`,`operation_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1042 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_game_binding
-- ----------------------------
CREATE TABLE `t_user_gmlcoin_count` (
  `count_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户Id',
  `tday_gmlCoin_get` decimal(11,2) DEFAULT '0.00' COMMENT '当日获得猫币数',
  `tday_gmlCoin_consume` decimal(11,2) DEFAULT '0.00' COMMENT '当日消费猫币数',
  `gmlCoin_total` decimal(11,2) DEFAULT NULL COMMENT '猫币总数',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`count_id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28196 DEFAULT CHARSET=utf8 COMMENT='用户猫币统计表';

-- ----------------------------
-- Table structure for t_user_gmlcoin_detail
-- ----------------------------
CREATE TABLE `t_user_gmlcoin_detail` (
  `detail_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户Id',
  `gmlCoin_get` decimal(11,2) DEFAULT '0.00' COMMENT '获得猫币数',
  `gmlCoin_consume` decimal(11,2) DEFAULT '0.00' COMMENT '支出猫币数',
  `get_use_way` smallint(3) NOT NULL COMMENT '获取及支出用途(401-领取礼包,402-抽奖,301-淘宝箱获取,201-签到,202-在线1小时,203-反馈)',
  `remark` varchar(150) DEFAULT '' COMMENT '备注',
  `user_op_date` date NOT NULL COMMENT '操作日期',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `user_op_id` varchar(15) DEFAULT '' COMMENT '操作类型Id',
  `user_op_title` varchar(150) DEFAULT '' COMMENT '操作类型标题',
  PRIMARY KEY (`detail_id`),
  KEY `index_uid_date` (`user_id`,`user_op_date`,`ctime`)
) ENGINE=InnoDB AUTO_INCREMENT=6209394 DEFAULT CHARSET=utf8 COMMENT='用户猫币获取明细表';

-- ----------------------------
-- Table structure for t_user_lng_lat
-- to delete : 由业务记录
-- ----------------------------
CREATE TABLE `t_user_lng_lat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `user_id` bigint(20) NOT NULL COMMENT '用户Id',
  `lng` decimal(9,6) NOT NULL COMMENT '经度',
  `lat` decimal(9,6) NOT NULL COMMENT '纬度',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=526894 DEFAULT CHARSET=utf8 COMMENT='用户经度纬度表';

-- ----------------------------
-- Table structure for t_user_log
-- to delete : 放到消息队列
-- ----------------------------
CREATE TABLE `t_user_log` (
  `log_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `account` varchar(50) DEFAULT NULL COMMENT '账号(如果是登录,则记录登录账号,如果是注册,则记录注册账号)',
  `operate_type` smallint(2) NOT NULL COMMENT '操作类型(0-登录,1-注册,00-其他)',
  `operate_ip` varchar(40) DEFAULT NULL COMMENT '操作ip',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `device_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6700797 DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Table structure for t_user_loginlog
-- to delete : 放到消息队列
-- ----------------------------
CREATE TABLE `t_user_loginlog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `login_date` varchar(8) NOT NULL COMMENT '登录日期',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_uid_date` (`user_id`,`login_date`)
) ENGINE=InnoDB AUTO_INCREMENT=2576306 DEFAULT CHARSET=utf8 COMMENT='用户登录日志';

-- ----------------------------
-- Table structure for t_user_openid_relation
-- ----------------------------
CREATE TABLE `t_user_openid_relation` (
  `relation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户关系id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `open_id` varchar(100) NOT NULL COMMENT '用户openId',
  `md5_open_id` varchar(100) NOT NULL COMMENT '新的用户openId',
  `chl_id` varchar(30) NOT NULL COMMENT '渠道id',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`relation_id`),
  UNIQUE KEY `index_user_open_id` (`user_id`,`open_id`,`chl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8 COMMENT='老用户openId与新生成openId关联表';

-- ----------------------------
-- Table structure for t_user_openid_unionid
-- ----------------------------
CREATE TABLE `t_user_openid_unionid` (
  `relation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `open_id` varchar(100) NOT NULL COMMENT '第三方登录openId',
  `union_id` varchar(100) NOT NULL COMMENT '第三方登录unionId',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`relation_id`),
  UNIQUE KEY `index_open_id` (`open_id`),
  KEY `index_user_id` (`user_id`),
  KEY `index_union_id` (`union_id`),
  KEY `index_open_union_id` (`open_id`,`union_id`)
) ENGINE=InnoDB AUTO_INCREMENT=371364 DEFAULT CHARSET=utf8 COMMENT='用户openId及unionId绑定表';

-- ----------------------------
-- Table structure for t_user_openid_unionid_temp
-- ----------------------------
CREATE TABLE `t_user_openid_unionid_temp` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `open_id` varchar(100) NOT NULL COMMENT '第三方登录openId',
  `status` smallint(6) NOT NULL COMMENT '处理状态(1-成功,2-失败)',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `index_open_id` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户openId及unionId绑定临时表';

-- ----------------------------
-- Table structure for t_user_option
-- ----------------------------
CREATE TABLE `t_user_option` (
  `id` bigint(6) NOT NULL AUTO_INCREMENT,
  `account_length` smallint(3) NOT NULL DEFAULT '0' COMMENT '喵号长度',
  `ctime` datetime NOT NULL,
  `utime` datetime NOT NULL,
  `is_delete` smallint(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_platform_info
-- to delete ： 冗余数据
-- ----------------------------
CREATE TABLE `t_user_platform_info` (
  `user_platform_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户平台ID',
  `user_platform_account` varchar(10) NOT NULL COMMENT '用户平台账号',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_platform_id`),
  UNIQUE KEY `index_user_platform_account` (`user_platform_account`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1634890 DEFAULT CHARSET=utf8 COMMENT='用户平台信息表';

-- ----------------------------
-- Table structure for t_user_register_chl
-- ----------------------------
CREATE TABLE `t_user_register_chl` (
  `register_chl_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `user_register_chl` int(2) NOT NULL COMMENT '用户拥有的登录类型(1-平台ID,2-手机,3-邮箱,4-自定义账号注册,5-QQ,00-其他)',
  `user_register_account` varchar(40) NOT NULL COMMENT '注册账号(如果是QQ则记录openId)',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`register_chl_id`),
  UNIQUE KEY `index_account` (`user_register_account`) USING BTREE,
  KEY `index_user_id_chl` (`user_id`,`user_register_chl`),
  KEY `index_register_chl_account` (`user_register_chl`,`user_register_account`)
) ENGINE=InnoDB AUTO_INCREMENT=2883805 DEFAULT CHARSET=utf8 COMMENT='用户拥有登录类型信息表';

-- ----------------------------
-- Table structure for t_user_safe
-- to delete : 冗余数据
-- ----------------------------
CREATE TABLE `t_user_safe` (
  `safe_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `safe_mobile` varchar(11) DEFAULT NULL COMMENT '安全手机',
  `safe_email` varchar(50) DEFAULT NULL COMMENT '安全邮箱',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`safe_id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1468432 DEFAULT CHARSET=utf8 COMMENT='安全信息表';

-- ----------------------------
-- Table structure for t_user_sid_bind
-- ----------------------------
CREATE TABLE `t_user_sid_bind` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键唯一标识',
  `inner_device_id` varchar(150) NOT NULL COMMENT '平台内部设备号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `register_way` smallint(6) DEFAULT NULL COMMENT '注册途径(1-一键登录,2-正常注册,3-第三方注册)',
  `is_update_pwd` smallint(6) NOT NULL DEFAULT '2' COMMENT '是否已修改密码(1-是,2-否)',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_user_id` (`user_id`),
  KEY `index_inner_device_id` (`inner_device_id`,`register_way`),
  KEY `index_inner_device_id_ctime` (`inner_device_id`,`register_way`,`ctime`)
) ENGINE=InnoDB AUTO_INCREMENT=12886551 DEFAULT CHARSET=utf8 COMMENT='用户与平台内部设备号绑定表';

-- ----------------------------
-- Table structure for t_user_sms
-- ----------------------------
CREATE TABLE `t_user_sms` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `account` varchar(30) NOT NULL,
  `hy_status` char(1) NOT NULL DEFAULT '0' COMMENT '环信注册状态 0 注册失败 1注册成功',
  `gmt_create` datetime NOT NULL,
  `gmt_modify` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6666255 DEFAULT CHARSET=utf8 COMMENT='用户环信注册表（首次注册失败用户进入此表，定时处理状态失败的用户）';

-- ----------------------------
-- Table structure for t_user_token
-- ----------------------------
CREATE TABLE `t_user_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `token` varchar(40) DEFAULT NULL COMMENT '用户Token',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `is_delete` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `accessToken` varchar(255) DEFAULT NULL COMMENT '浩动token',
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`),
  KEY `index_token` (`token`),
  KEY `index_access_token` (`accessToken`)
) ENGINE=InnoDB AUTO_INCREMENT=1065343 DEFAULT CHARSET=utf8 COMMENT='用户Token信息表';

