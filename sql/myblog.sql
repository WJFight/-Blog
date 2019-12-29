/*
Navicat MySQL Data Transfer

Source Server         : W_J_M
Source Server Version : 50562
Source Host           : localhost:3306
Source Database       : myblog

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2019-08-19 18:58:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blog_admin
-- ----------------------------
DROP TABLE IF EXISTS `blog_admin`;
CREATE TABLE `blog_admin` (
  `admin_id` int(50) NOT NULL,
  `admin_no` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `admin_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `admin_password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `admin_phone` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_admin
-- ----------------------------

-- ----------------------------
-- Table structure for blog_category
-- ----------------------------
DROP TABLE IF EXISTS `blog_category`;
CREATE TABLE `blog_category` (
  `user_id` int(50) NOT NULL,
  `category_id` int(50) NOT NULL COMMENT '类型id和文章id共同组成分类表的主键对应着一个文章多种类型',
  `category_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '文章类型名',
  PRIMARY KEY (`category_id`),
  KEY `fk_category_user_id` (`user_id`),
  CONSTRAINT `fk_category_user_id` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_category
-- ----------------------------

-- ----------------------------
-- Table structure for blog_comment
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment` (
  `comment_id` int(50) NOT NULL COMMENT '评论表id',
  `post_id` int(50) NOT NULL,
  `comment_content` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` int(50) NOT NULL,
  `answer_id` int(50) DEFAULT NULL,
  `comment_date` datetime NOT NULL,
  `comment_support` int(255) DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`comment_id`,`post_id`),
  KEY `fk_comment_user_id` (`user_id`),
  KEY `fk_comment_poss_id` (`post_id`),
  CONSTRAINT `fk_comment_poss_id` FOREIGN KEY (`post_id`) REFERENCES `blog_post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user_id` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_comment
-- ----------------------------

-- ----------------------------
-- Table structure for blog_post
-- ----------------------------
DROP TABLE IF EXISTS `blog_post`;
CREATE TABLE `blog_post` (
  `user_id` int(50) NOT NULL COMMENT '用户id',
  `comment_id` int(50) NOT NULL COMMENT '评论id',
  `category_id` int(50) NOT NULL COMMENT '分类id',
  `post_id` int(50) NOT NULL,
  `post_title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `post_content` text COLLATE utf8_unicode_ci NOT NULL COMMENT '博客内容',
  `type_id` int(255) NOT NULL,
  `post_browseNumber` int(11) unsigned DEFAULT '0',
  `post_createDate` date NOT NULL,
  `version` int(11) DEFAULT '1' COMMENT '版本号',
  PRIMARY KEY (`post_id`),
  KEY `fk_usr_post_id` (`user_id`),
  KEY `fk_post_type_id` (`type_id`),
  CONSTRAINT `fk_post_type_id` FOREIGN KEY (`type_id`) REFERENCES `blog_type` (`type_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_usr_post_id` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_post
-- ----------------------------

-- ----------------------------
-- Table structure for blog_post_category
-- ----------------------------
DROP TABLE IF EXISTS `blog_post_category`;
CREATE TABLE `blog_post_category` (
  `category_id` int(50) NOT NULL,
  `post_id` int(50) NOT NULL,
  `category_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`category_id`,`post_id`),
  KEY `fk_post_category_post_id` (`post_id`),
  CONSTRAINT `fk_post_category_post_id` FOREIGN KEY (`post_id`) REFERENCES `blog_post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_post_category
-- ----------------------------

-- ----------------------------
-- Table structure for blog_role
-- ----------------------------
DROP TABLE IF EXISTS `blog_role`;
CREATE TABLE `blog_role` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `role_Desc` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_role
-- ----------------------------
INSERT INTO `blog_role` VALUES ('1', '普通用户', 'ROLE_USER');
INSERT INTO `blog_role` VALUES ('2', '管理员', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for blog_role_user
-- ----------------------------
DROP TABLE IF EXISTS `blog_role_user`;
CREATE TABLE `blog_role_user` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_role_id` (`role_id`),
  CONSTRAINT `fk_role_id` FOREIGN KEY (`role_id`) REFERENCES `blog_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role_id` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_role_user
-- ----------------------------
INSERT INTO `blog_role_user` VALUES ('30', '1');
INSERT INTO `blog_role_user` VALUES ('31', '1');
INSERT INTO `blog_role_user` VALUES ('32', '1');
INSERT INTO `blog_role_user` VALUES ('41', '1');

-- ----------------------------
-- Table structure for blog_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag` (
  `tag_id` int(50) NOT NULL COMMENT '标签id',
  `tag_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '标签名',
  `tag_count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_name` (`tag_name`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_tag
-- ----------------------------

-- ----------------------------
-- Table structure for blog_tag_post
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag_post`;
CREATE TABLE `blog_tag_post` (
  `tag_id` int(50) NOT NULL,
  `post_id` int(50) NOT NULL,
  `tag_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`tag_id`,`post_id`),
  KEY `fk_relate_post_id_post_id` (`post_id`),
  CONSTRAINT `fk_relate_post_id_post_id` FOREIGN KEY (`post_id`) REFERENCES `blog_post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_relate_tag_id_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `blog_tag` (`tag_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_tag_post
-- ----------------------------

-- ----------------------------
-- Table structure for blog_type
-- ----------------------------
DROP TABLE IF EXISTS `blog_type`;
CREATE TABLE `blog_type` (
  `type_id` int(11) NOT NULL,
  `type_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `type_name` (`type_name`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_type
-- ----------------------------
INSERT INTO `blog_type` VALUES ('1', 'JavaWeb');
INSERT INTO `blog_type` VALUES ('0', 'Java基础');
INSERT INTO `blog_type` VALUES ('3', 'JVM');
INSERT INTO `blog_type` VALUES ('2', 'Mybatis');
INSERT INTO `blog_type` VALUES ('8', 'MySQL');
INSERT INTO `blog_type` VALUES ('9', 'Oracle');
INSERT INTO `blog_type` VALUES ('10', 'Redis');
INSERT INTO `blog_type` VALUES ('5', 'SpringBoot');
INSERT INTO `blog_type` VALUES ('6', 'SpringCloud');
INSERT INTO `blog_type` VALUES ('4', 'SpringMVC');
INSERT INTO `blog_type` VALUES ('7', 'SpringSecurity');
INSERT INTO `blog_type` VALUES ('16', '其他');
INSERT INTO `blog_type` VALUES ('14', '分布式学习');
INSERT INTO `blog_type` VALUES ('13', '前端系列');
INSERT INTO `blog_type` VALUES ('11', '多线程并发');
INSERT INTO `blog_type` VALUES ('15', '缓存或加密');
INSERT INTO `blog_type` VALUES ('12', '网站构建');

-- ----------------------------
-- Table structure for blog_user
-- ----------------------------
DROP TABLE IF EXISTS `blog_user`;
CREATE TABLE `blog_user` (
  `user_id` int(50) NOT NULL AUTO_INCREMENT,
  `user_realname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(2555) COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '登陆名(账号)',
  `user_sex` int(2) NOT NULL DEFAULT '0',
  `user_image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_phone` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '电话号',
  `version` int(11) DEFAULT '1' COMMENT '版本号',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_no` (`username`) USING HASH,
  UNIQUE KEY `user_name` (`user_realname`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of blog_user
-- ----------------------------
INSERT INTO `blog_user` VALUES ('30', 'zs', '$2a$10$b4mIM4xTtE1YJ0VApXZlTOKySHpIzMB/20LEvd6n1xcBqVwYNzfdi', 'zs', '0', null, '8888888888', '2');
INSERT INTO `blog_user` VALUES ('31', '王健', '634c0a68dcb43676e37f6c0655fa7cd31fa53f7c02dccbde0a50d1a511d29bac', 'ls', '0', 'http://localhost:8080/static/HeadImages/2019-08-034e84ecae-013c-4b6e-9342-d3cce82d7c5f.jpg', '99999999999', '4');
INSERT INTO `blog_user` VALUES ('32', 'ww', 'ea304bbc22d95dcebd1e4d147295a1071a676f4bf73bee9f61e378e5947b3b94', 'ww', '0', null, '15362913245', '1');
INSERT INTO `blog_user` VALUES ('41', 'hh', 'c26d0e189558e8c207a4ddc19565f7b28f24e240014e975e87ec5cc9b2106443', 'hh', '1', 'http://localhost:8080/static/HeadImages/2019-08-19b53afbf4-0a92-4196-8f41-31b62240c68f.jpg', '15362913245', '5');
