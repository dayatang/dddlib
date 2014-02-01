/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50018
Source Host           : localhost:3306
Source Database       : rams

Target Server Type    : MYSQL
Target Server Version : 50018
File Encoding         : 65001

Date: 2010-05-11 23:44:23
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `auth_test`
-- ----------------------------
DROP TABLE IF EXISTS `test_auth`;
CREATE TABLE `test_auth` (
  `id` int(10) default NULL,
  `name` varchar(10) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of auth_test
-- ----------------------------
