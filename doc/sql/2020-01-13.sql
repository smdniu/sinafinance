/*
Navicat MySQL Data Transfer

Source Server         : 111.230.47.79
Source Server Version : 50724
Source Host           : 111.230.47.79:3307
Source Database       : cash-test

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2020-01-15 19:59:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_account
-- ----------------------------
DROP TABLE IF EXISTS `tb_account`;
CREATE TABLE `tb_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) NOT NULL,
  `account` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_pay_password
-- ----------------------------
DROP TABLE IF EXISTS `tb_pay_password`;
CREATE TABLE `tb_pay_password` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `uid` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '修改时间',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `status` varchar(1) DEFAULT NULL COMMENT '使用状态（1正常 0非正常）',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ号码',
  `is_mobile_check` varchar(1) DEFAULT '0' COMMENT '手机是否验证 （0否  1是）',
  `is_email_check` varchar(1) DEFAULT '0' COMMENT '邮箱是否检测（0否  1是）',
  `sex` varchar(1) DEFAULT '1' COMMENT '性别，1男，0女',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for tb_withdrawal_bank
-- ----------------------------
DROP TABLE IF EXISTS `tb_withdrawal_bank`;
CREATE TABLE `tb_withdrawal_bank` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) NOT NULL,
  `cnname` varchar(255) DEFAULT NULL COMMENT '中文名',
  `bankCode` varchar(255) NOT NULL DEFAULT '' COMMENT '卡的缩写 如ICBC',
  `bankName` varchar(255) NOT NULL COMMENT '银行名',
  `bankNumber` varchar(20) NOT NULL COMMENT '卡号',
  `sequence` bigint(20) DEFAULT NULL COMMENT '排序用，从小到大',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_withdrawal_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_withdrawal_info`;
CREATE TABLE `tb_withdrawal_info` (
  `id` bigint(50) NOT NULL,
  `uid` varchar(50) NOT NULL,
  `withdrawOrder` varchar(255) DEFAULT NULL COMMENT '提现订单号,系统自动生成的',
  `withdrawBankId` varchar(255) NOT NULL COMMENT '用户对应的卡的编号',
  `withdrawCharge` decimal(10,2) DEFAULT NULL COMMENT '提现手续费',
  `withdrawRealityTotal` decimal(10,2) DEFAULT NULL COMMENT '实际提现金额',
  `withdrawApplyTotal` decimal(10,2) DEFAULT NULL COMMENT '申请提现的金额',
  `withdrawApplyTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '申请提现时间',
  `status` bigint(20) NOT NULL COMMENT '提现状态 1：发起提现 2：处理中 3：到账 4：提现失败 5：退回成功',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `expectTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '预计到账时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_withdrawal_logs
-- ----------------------------
DROP TABLE IF EXISTS `tb_withdrawal_logs`;
CREATE TABLE `tb_withdrawal_logs` (
  `id` bigint(20) NOT NULL,
  `operater` varchar(255) NOT NULL COMMENT '操作人',
  `withdrawId` bigint(100) NOT NULL COMMENT '交易流水号',
  `withdrawOrder` varchar(255) DEFAULT NULL COMMENT '提现订单号,系统自动生成',
  `status` bigint(20) NOT NULL COMMENT '提现的状态  1：发起提现 2：处理中 3：到账 4：提现失败 5：退回成功',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
