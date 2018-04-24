-- ----------------------------
-- Table structure for user_drawcoin_apply
-- 用户提币的申请
-- ----------------------------
CREATE TABLE `user_drawcoin_apply` (
  `uid` bigint NOT NULL COMMENT '用户id',
  `coin_type` tinyint(1) NOT NULL COMMENT '提币的类型：1、usdg，2、bty',
  `inner_address` varchar(50) NOT NULL COMMENT '用户在交易所对应币种的地址',
  `outer_address` varchar(50) NOT NULL COMMENT '用户把币提出去对应的地址',
  `coin_amount` decimal(10,5) NOT NULL COMMENT '提币的数量',
  `miner_amount` decimal(10,5) NOT NULL COMMENT '矿工费',
  `apply_statue` tinyint(1) NOT NULL COMMENT '提币审核：1、待审核，2、通过，3、拒绝',
  `ctime` int NOT NULL COMMENT '创建时间',
  `utime` int NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户提币的申请表';

