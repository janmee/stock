/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50538
Source Host           : localhost:3306
Source Database       : stock

Target Server Type    : MYSQL
Target Server Version : 50538
File Encoding         : 65001

Date: 2016-04-23 11:48:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `stock`
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名字',
  `cname` varchar(255) NOT NULL DEFAULT '' COMMENT '中文名',
  `category` varchar(255) NOT NULL DEFAULT '' COMMENT '分类名',
  `categroy_id` varchar(255) NOT NULL DEFAULT '' COMMENT '分类id',
  `symbol` varchar(16) NOT NULL DEFAULT '' COMMENT '代码',
  `mktcap` int(11) DEFAULT NULL COMMENT 'mktcap',
  `pe` float(11,0) DEFAULT NULL COMMENT '市盈率',
  `market` varchar(32) NOT NULL DEFAULT '' COMMENT '上市地',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of stock
-- ----------------------------

-- ----------------------------
-- Table structure for `test`
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '名字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test
-- ----------------------------

