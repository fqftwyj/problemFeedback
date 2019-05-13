/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50622
Source Host           : localhost:3306
Source Database       : webframe

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2018-12-12 14:38:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL,
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `permission_name` varchar(200) DEFAULT NULL COMMENT '权限名',
  `permission_code` varchar(200) DEFAULT NULL COMMENT '编号',
  `permission_type` smallint(2) DEFAULT NULL COMMENT '权限类型【enum】(0:普通菜单:menu,1:链接:link,2:功能:method)',
  `permission_url` varchar(200) DEFAULT NULL COMMENT '权限url',
  `permission_icon` varchar(200) DEFAULT NULL COMMENT '权限图标',
  `description` varchar(4000) DEFAULT NULL COMMENT '描述',
  `sort` int(10) DEFAULT NULL COMMENT '排序',
  `parent_id` int(11) DEFAULT NULL COMMENT '父权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '2018-08-07 09:47:32', '2018-08-07 09:47:34', '系统', 'adminxx', '0', '', null, '系统初级节点不可删除', '1', '0');
INSERT INTO `sys_permission` VALUES ('101', '2018-08-16 09:58:51', '2018-08-16 09:58:53', '主页', 'index', '1', 'index/detail', null, '主页', '1', '1');
INSERT INTO `sys_permission` VALUES ('102', '2018-08-16 10:01:38', '2018-08-16 10:01:40', '配置管理', 'set', '0', null, null, '配置管理', '2', '1');
INSERT INTO `sys_permission` VALUES ('10201', '2018-08-17 13:20:45', '2018-08-17 13:20:48', '用户权限管理', 'mhml', '0', '', null, '权限管理', '1', '102');
INSERT INTO `sys_permission` VALUES ('1020101', '2018-08-31 14:53:19', '2018-08-31 14:53:22', '权限管理', null, '1', '/sys/permission/index', null, '权限管理', '1', '10201');
INSERT INTO `sys_permission` VALUES ('1020102', '2018-08-31 13:32:49', '2018-08-31 13:32:52', '角色管理', null, '1', '/sys/role/index', null, '角色管理', '2', '10201');
INSERT INTO `sys_permission` VALUES ('1020103', '2018-08-31 13:36:51', '2018-08-31 13:36:47', '用户管理', null, '1', '/sys/user/index', null, '用户管理', '3', '10201');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `name` varchar(200) DEFAULT NULL COMMENT '角色名',
  `description` varchar(4000) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '2018-08-07 14:56:57', '2018-08-07 14:56:59', 'admin', '管理员');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='角色权限关系表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1', '1', '1');
INSERT INTO `sys_role_permission` VALUES ('2', '1', '101');
INSERT INTO `sys_role_permission` VALUES ('3', '1', '102');
INSERT INTO `sys_role_permission` VALUES ('4', '1', '10201');
INSERT INTO `sys_role_permission` VALUES ('5', '1', '1020101');
INSERT INTO `sys_role_permission` VALUES ('6', '1', '1020102');
INSERT INTO `sys_role_permission` VALUES ('7', '1', '1020103');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `status` int(2) DEFAULT NULL COMMENT '账户状态【enum】(0:禁用:disabled,1:启用:enabled)',
  `error_num` int(2) DEFAULT NULL COMMENT '错误次数',
  `built_in` int(2) DEFAULT NULL COMMENT '是否内置【enum】(0:FALSE:,1:是:TRUE)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('32', '2018-10-11 09:28:47', '2018-12-12 14:38:02', 'admin', 'F59BD65F7EDAFB087A81D4DCA06C4910', '1', '1','0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '32', '1');
