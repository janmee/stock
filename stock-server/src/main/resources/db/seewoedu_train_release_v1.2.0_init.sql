/*
Navicat MySQL Data Transfer

Source Server         : 172.18.45.175
Source Server Version : 50538
Source Host           : 172.18.45.175:3306
Source Database       : janmee.stock_release

Target Server Type    : MYSQL
Target Server Version : 50538
File Encoding         : 65001

Date: 2016-04-11 15:28:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT '业务唯一id',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT 'banner名称',
  `thumb_url` text NOT NULL COMMENT '缩略图URL',
  `action_url` varchar(255) NOT NULL DEFAULT '' COMMENT '活动URL',
  `show_order` int(32) NOT NULL DEFAULT '0' COMMENT '展示顺序（从小到大，小在前）',
  `color` varchar(16) DEFAULT '' COMMENT '颜色',
  `type` int(11) DEFAULT '0' COMMENT '类型：0为首页banner，1为软件banner',
  `unit_uid` varchar(32) DEFAULT '' COMMENT '组织的uid',
  `is_published` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否发布 0 不发布；  1 发布',
  `published_at` datetime DEFAULT NULL COMMENT '开始展现的时间',
  `creator_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'banner 上传者uid',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0未删除 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of banner
-- ----------------------------

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `uid` varchar(32) NOT NULL,
  `name` varchar(64) NOT NULL COMMENT '课程名称',
  `thumb_url` text COMMENT '课程缩略图URL thumb_type=0 则为字符串格式，thumb_type=1 则是JSON：{“SURL”:"","MURL":"","LURL":"",...}',
  `video_url` text COMMENT '视频地址',
  `barcode_url` text COMMENT '二维码的上传地址',
  `place` varchar(255) DEFAULT '' COMMENT '上课地点，只在线下课程有效',
  `description` text COMMENT '课程简介',
  `start_at` datetime DEFAULT NULL COMMENT '开始时间，对线下课程有意义',
  `close_at` datetime DEFAULT NULL COMMENT '结束时间',
  `total_time` int(11) DEFAULT '0' COMMENT '课程总时长',
  `deadline` datetime DEFAULT NULL COMMENT '报名截止限制',
  `limit_num` int(5) DEFAULT '0' COMMENT '报名限制人数',
  `is_published` tinyint(1) NOT NULL DEFAULT '0' COMMENT '课程是否发布',
  `published_at` datetime DEFAULT NULL COMMENT '发布时间',
  `show_order` int(32) NOT NULL DEFAULT '0' COMMENT '顺序 从小到大',
  `is_public` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否机构内所有人可见',
  `is_vip` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否VIP 0 免费  1 VIP',
  `is_commented` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启留言功能 1是 0 否',
  `is_closed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '关闭课程，线下课程有效',
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '1是点播课程，2是直播课程',
  `creator_uid` varchar(32) NOT NULL,
  `unit_uid` varchar(32) DEFAULT '',
  `keyword` varchar(255) DEFAULT '' COMMENT 'seo,关键字',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`),
  KEY `idx_creator_uid` (`creator_uid`),
  KEY `idx_unit_uid` (`unit_uid`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8 COMMENT='课程信息表';

-- ----------------------------
-- Records of course
-- ----------------------------

-- ----------------------------
-- Table structure for course_category
-- ----------------------------
DROP TABLE IF EXISTS `course_category`;
CREATE TABLE `course_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(32) NOT NULL,
  `puid` varchar(32) NOT NULL DEFAULT '',
  `name` varchar(64) NOT NULL DEFAULT '',
  `unit_uid` varchar(32) NOT NULL,
  `intro` text,
  `avatar` text,
  `code` varchar(32) NOT NULL DEFAULT '' COMMENT '分类标识，一般跟id保持一致',
  `relationship` varchar(255) NOT NULL DEFAULT '' COMMENT '由code拼接起来的一个链',
  `is_public` tinyint(1) NOT NULL DEFAULT '1' COMMENT '分类是否所有人可见',
  `show_order` int(11) DEFAULT '0' COMMENT '展示次序，从小到大',
  `creator_uid` varchar(32) DEFAULT '',
  `creator` varchar(32) DEFAULT '',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`),
  KEY `idx_unit_uid` (`unit_uid`),
  KEY `idx_code` (`code`),
  KEY `idx_puid` (`puid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='组织-课程分类信息';

-- ----------------------------
-- Records of course_category
-- ----------------------------
INSERT INTO `course_category` VALUES ('1', 'a140eeb3d4bc43a6b83a6ae9d2505aea', '', '希沃软件', '', null, null, '', '', '1', '1', null, null, '2016-02-29 11:36:31', '2016-02-29 11:36:31', '0');
INSERT INTO `course_category` VALUES ('2', 'f73dd88f600108df58fc702ad9327f74', '', '微课制作', '', null, null, '', '', '1', '2', null, '', '2016-03-04 13:45:43', '2016-03-04 13:45:45', '0');
INSERT INTO `course_category` VALUES ('3', '4f7cc9d52dc4859aa352a9563ed0c50c', '', 'Office工具', '', null, null, '', '', '1', '3', '', '', '2016-03-04 13:47:17', '2016-03-04 13:47:25', '0');
INSERT INTO `course_category` VALUES ('4', '2d984434c92e6093ac2718470801af46', '', '学科工具', '', null, null, '', '', '1', '4', '', '', '2016-03-04 13:47:21', '2016-03-04 13:47:28', '0');
INSERT INTO `course_category` VALUES ('5', '8ee9ce83c9e3ba46deb8886e922235db', '', '精彩课例', '', null, null, '', '', '1', '5', '', '', '2016-03-04 13:48:55', '2016-03-04 13:48:58', '0');

-- ----------------------------
-- Table structure for course_category_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_category_rel`;
CREATE TABLE `course_category_rel` (
  `course_uid` varchar(32) NOT NULL,
  `course_category_uid` varchar(32) NOT NULL,
  UNIQUE KEY `idx_unique_course_uid` (`course_uid`,`course_category_uid`) USING BTREE,
  KEY `idx_course_uid` (`course_uid`),
  KEY `idx_course_category_uid` (`course_category_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程类型关系表';

-- ----------------------------
-- Records of course_category_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_comment
-- ----------------------------
DROP TABLE IF EXISTS `course_comment`;
CREATE TABLE `course_comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(32) NOT NULL,
  `puid` varchar(32) NOT NULL DEFAULT '' COMMENT '问题的uid即puid为空字符串是问题，puid不为空则是uid问题的回答',
  `course_uid` varchar(32) NOT NULL,
  `is_selected` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否精选留言，0 否 1 是',
  `has_answered` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否回答 0 未回答 1 回答',
  `user_uid` varchar(32) NOT NULL,
  `content` text NOT NULL COMMENT '评论内容',
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`),
  KEY `idx_puid` (`puid`),
  KEY `idx_course_uid` (`course_uid`)
) ENGINE=InnoDB AUTO_INCREMENT=15917 DEFAULT CHARSET=utf8 COMMENT='课程-留言';

-- ----------------------------
-- Records of course_comment
-- ----------------------------

-- ----------------------------
-- Table structure for course_group
-- ----------------------------
DROP TABLE IF EXISTS `course_group`;
CREATE TABLE `course_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT '业务唯一id',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '课题组名称',
  `thumb_url` text COMMENT '课程缩略图',
  `description` text COMMENT '描述',
  `creator_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人，user.resourceid',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间用户组最新排序',
  `course_count` int(32) DEFAULT '0' COMMENT '课题组课程数，查多插入少',
  `is_published` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否发布，0 不发布 1 发布',
  `published_at` datetime DEFAULT NULL COMMENT '发布时间',
  `is_public` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否公开 0 不公开  1 公开',
  `is_top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否置顶 0 不置顶  1 置顶',
  `unit_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '组织id，unit.id',
  `keyword` varchar(255) DEFAULT '' COMMENT 'seo，关键字',
  `show_order` int(11) DEFAULT '0' COMMENT '课组排序',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 未删除  1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`),
  KEY `idx_unit_uid` (`unit_uid`),
  KEY `idx_updated_at` (`updated_at`),
  KEY `idx_creator_uid` (`creator_uid`),
  KEY `idx_published_at` (`published_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8 COMMENT='课程组-一系列课程的组合';

-- ----------------------------
-- Records of course_group
-- ----------------------------

-- ----------------------------
-- Table structure for course_group_category_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_group_category_rel`;
CREATE TABLE `course_group_category_rel` (
  `course_category_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'unit_course_cat.uid',
  `course_group_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'coursegroup.uid',
  UNIQUE KEY `idx_course_cat_uid_coursegroup_uid` (`course_category_uid`,`course_group_uid`),
  KEY `idx_coursegroup_uid` (`course_group_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程与课题组的关系表';

-- ----------------------------
-- Records of course_group_category_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_group_course_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_group_course_rel`;
CREATE TABLE `course_group_course_rel` (
  `course_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'course.uid',
  `course_group_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'coursegroup.uid',
  UNIQUE KEY `idx_course_uid_cgroup_uid` (`course_group_uid`,`course_uid`),
  KEY `idx_course_uid` (`course_uid`),
  KEY `idx_course_group_uid` (`course_group_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_group_course_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_group_lecturer_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_group_lecturer_rel`;
CREATE TABLE `course_group_lecturer_rel` (
  `course_group_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'coursegroup.uid',
  `lecturer_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '培训讲师uid，user.resourceid',
  UNIQUE KEY `idx_coursegroup_uid_trainer_uid` (`course_group_uid`,`lecturer_uid`),
  KEY `idx_trainer_uid` (`lecturer_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_group_lecturer_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_group_software_info_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_group_software_info_rel`;
CREATE TABLE `course_group_software_info_rel` (
  `course_group_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'coursegroup.uid',
  `software_info_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'software_info.uid',
  UNIQUE KEY `idx_coursegroup_uid_software_info_uid` (`course_group_uid`,`software_info_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_group_software_info_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_group_status_ico_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_group_status_ico_rel`;
CREATE TABLE `course_group_status_ico_rel` (
  `course_group_uid` varchar(32) NOT NULL COMMENT 'course_group.uid',
  `status_ico_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'status_ico.uid',
  PRIMARY KEY (`course_group_uid`,`status_ico_uid`),
  KEY `idx_status_ico_uid` (`status_ico_uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_group_status_ico_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_group_tag_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_group_tag_rel`;
CREATE TABLE `course_group_tag_rel` (
  `tag_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'tag.uid',
  `course_group_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'coursegroup.uid',
  UNIQUE KEY `idx_tag_uid_coursegroup_uid` (`tag_uid`,`course_group_uid`),
  KEY `idx_coursegroup_uid` (`course_group_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_group_tag_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_lecturer
-- ----------------------------
DROP TABLE IF EXISTS `course_lecturer`;
CREATE TABLE `course_lecturer` (
  `id` int(32) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'seewo_sys_users.resourceid',
  `intro` text COMMENT '讲师简介',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0未删除 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_course_uid_user_uid` (`uid`) USING BTREE,
  KEY `idx_uid` (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='讲师表';

-- ----------------------------
-- Records of course_lecturer
-- ----------------------------

-- ----------------------------
-- Table structure for course_lecturer_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_lecturer_rel`;
CREATE TABLE `course_lecturer_rel` (
  `course_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'course.uid',
  `lecturer_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'seewo_sys_users.resourceid',
  UNIQUE KEY `idx_course_uid_user_uid` (`course_uid`,`lecturer_uid`),
  KEY `idx_lecturer_uid` (`lecturer_uid`) USING BTREE,
  KEY `idx_course_uid` (`course_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程讲师关系表';

-- ----------------------------
-- Records of course_lecturer_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_software_info_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_software_info_rel`;
CREATE TABLE `course_software_info_rel` (
  `course_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'coursegroup.uid',
  `software_info_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'software_info.uid',
  UNIQUE KEY `idx_course_uid_software_info_uid` (`course_uid`,`software_info_uid`) USING BTREE,
  KEY `idx_course_uid` (`course_uid`),
  KEY `idx_software_info_uid` (`software_info_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_software_info_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_status_ico_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_status_ico_rel`;
CREATE TABLE `course_status_ico_rel` (
  `course_uid` varchar(32) NOT NULL COMMENT 'course_group.uid',
  `status_ico_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'status_ico.uid',
  PRIMARY KEY (`course_uid`,`status_ico_uid`),
  KEY `idx_status_ico_uid` (`status_ico_uid`) USING BTREE,
  KEY `idx_course_uid` (`course_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_status_ico_rel
-- ----------------------------

-- ----------------------------
-- Table structure for course_tag_rel
-- ----------------------------
DROP TABLE IF EXISTS `course_tag_rel`;
CREATE TABLE `course_tag_rel` (
  `course_uid` varchar(32) NOT NULL,
  `tag_uid` varchar(32) NOT NULL,
  UNIQUE KEY `idx_course_id` (`course_uid`,`tag_uid`),
  KEY `idx_tag_uid` (`tag_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程-标记信息表';

-- ----------------------------
-- Records of course_tag_rel
-- ----------------------------

-- ----------------------------
-- Table structure for like_log
-- ----------------------------
DROP TABLE IF EXISTS `like_log`;
CREATE TABLE `like_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(32) NOT NULL,
  `course_group_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'coursegroup.uid,入口课组',
  `course_uid` varchar(32) NOT NULL,
  `user_uid` varchar(32) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`),
  UNIQUE KEY `idx_coursegroup_uid_courese_uid_user_uid` (`course_group_uid`,`course_uid`,`user_uid`),
  KEY `idx_course_uid_user_uid` (`course_uid`,`user_uid`),
  KEY `idx_user_uid` (`user_uid`),
  KEY `idx_created_at` (`created_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8 COMMENT='用户访问课程流水记录';

-- ----------------------------
-- Records of like_log
-- ----------------------------

-- ----------------------------
-- Table structure for software_download_log
-- ----------------------------
DROP TABLE IF EXISTS `software_download_log`;
CREATE TABLE `software_download_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT '业务唯一标识',
  `software_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '软件信息uid',
  `request_ip` varchar(255) NOT NULL DEFAULT '' COMMENT '请求地址ip',
  `browser` varchar(255) NOT NULL DEFAULT '' COMMENT '浏览器',
  `creator_uid` varchar(32) DEFAULT '' COMMENT '创建者user.resourceid',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 未删除 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`),
  KEY `idx_software_uid` (`software_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10653 DEFAULT CHARSET=utf8 COMMENT='软件下载/点击记录表';

-- ----------------------------
-- Records of software_download_log
-- ----------------------------

-- ----------------------------
-- Table structure for software_info
-- ----------------------------
DROP TABLE IF EXISTS `software_info`;
CREATE TABLE `software_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT '业务唯一标识',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '软件名称',
  `environment` varchar(255) NOT NULL DEFAULT '' COMMENT '运行环境',
  `advert` varchar(255) NOT NULL DEFAULT '' COMMENT '宣传语',
  `ico_url` text NOT NULL COMMENT '图标URL',
  `description` text NOT NULL COMMENT '软件介绍',
  `download_url` varchar(2048) NOT NULL DEFAULT '' COMMENT '下载URL',
  `type` int(11) DEFAULT '0' COMMENT '软件类型：0为seewo软件，1为第三方',
  `show_order` int(11) NOT NULL DEFAULT '0' COMMENT '展示顺序',
  `creator_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '创建者user.resourceid',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 未删除 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of software_info
-- ----------------------------

-- ----------------------------
-- Table structure for status_ico
-- ----------------------------
DROP TABLE IF EXISTS `status_ico`;
CREATE TABLE `status_ico` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT '业务唯一uid',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '状态ico名称',
  `color` varchar(16) NOT NULL DEFAULT '' COMMENT 'ico颜色',
  `type` int(11) DEFAULT '0' COMMENT '图标类型，0为自定义，1为系统',
  `creator_uid` varchar(32) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0未删除 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='状态图标表';

-- ----------------------------
-- Records of status_ico
-- ----------------------------
INSERT INTO `status_ico` VALUES ('1', 'fda9904970f394af87856d29078dc344', '预告中', '#3DA6FF', '1', '001', '2016-03-04 09:49:07', '2016-03-31 17:15:16', '0');
INSERT INTO `status_ico` VALUES ('2', '9405afc059c80da31e76f0d55c656b3d', '直播中', '#fd5e5d', '1', '001', '2016-03-04 09:49:10', '2016-03-04 09:49:16', '0');
INSERT INTO `status_ico` VALUES ('3', '4125afc059c80da31e76fda55c656b42', 'NEW', '#ff9b00', '1', '001', '2016-03-04 09:50:59', '2016-03-04 09:51:02', '0');

-- ----------------------------
-- Table structure for studied_log
-- ----------------------------
DROP TABLE IF EXISTS `studied_log`;
CREATE TABLE `studied_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(32) NOT NULL,
  `course_group_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'coursegroup.uid,入口课组',
  `course_uid` varchar(32) NOT NULL,
  `user_uid` varchar(32) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`),
  KEY `idx_course_uid_user_uid` (`course_uid`,`user_uid`),
  KEY `idx_user_uid` (`user_uid`),
  KEY `idx_coursegroup_uid_courese_uid_user_uid` (`course_group_uid`,`course_uid`,`user_uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8 COMMENT='用户访问课程流水记录';

-- ----------------------------
-- Records of studied_log
-- ----------------------------

-- ----------------------------
-- Table structure for study_log
-- ----------------------------
DROP TABLE IF EXISTS `study_log`;
CREATE TABLE `study_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(32) NOT NULL,
  `course_group_uid` varchar(32) NOT NULL DEFAULT '' COMMENT 'coursegroup.uid,入口课组',
  `course_uid` varchar(32) NOT NULL,
  `user_uid` varchar(32) NOT NULL,
  `play_time` datetime DEFAULT NULL COMMENT '视频播放时间',
  `end_time` int(11) DEFAULT NULL COMMENT '视频播放结束时间（秒）',
  `last` int(11) DEFAULT '0' COMMENT '持续时间',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid` (`uid`),
  KEY `idx_user_uid` (`user_uid`),
  KEY `idx_course_croup_uid_course_uid` (`course_group_uid`,`course_uid`) USING BTREE,
  KEY `idx_course_uid` (`course_uid`) USING BTREE,
  KEY `idx_created_at` (`created_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10045 DEFAULT CHARSET=utf8 COMMENT='用户访问课程流水记录';

-- ----------------------------
-- Records of study_log
-- ----------------------------

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT '业务唯一标识',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT 'tag名称',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_uid` varchar(32) DEFAULT '' COMMENT '创建人 user.resourceid',
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0未删除 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`),
  UNIQUE KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tag
-- ----------------------------
