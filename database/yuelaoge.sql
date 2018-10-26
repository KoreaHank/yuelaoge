/*
 Navicat Premium Data Transfer

 Source Server         : admin
 Source Server Type    : MariaDB
 Source Server Version : 100136
 Source Host           : localhost:3306
 Source Schema         : yuelaoge

 Target Server Type    : MariaDB
 Target Server Version : 100136
 File Encoding         : 65001

 Date: 26/10/2018 19:08:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of location
-- ----------------------------
INSERT INTO `location` VALUES (1, '罗山县');

-- ----------------------------
-- Table structure for meet
-- ----------------------------
DROP TABLE IF EXISTS `meet`;
CREATE TABLE `meet`  (
  `id` int(11) NOT NULL,
  `id_male` int(255) NOT NULL COMMENT '男方id',
  `id_female` int(255) NOT NULL COMMENT '女方id',
  `name_male` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `name_female` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `meet_time` datetime(0) NULL DEFAULT NULL,
  `meet_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `meet_state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '等待中（一方预约预扣费，预约失败返还）、预约中、预约取消、预约结束',
  `coin` int(255) NULL DEFAULT NULL COMMENT '预约花费 月老豆（选择项，基础服务200，扩展分类）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '所在县（目前只支持罗山县）',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '性别',
  `birthday` date NOT NULL COMMENT '生日',
  `age` int(11) NOT NULL COMMENT '年龄',
  `id_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '身份证号',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '头像地址',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '住址（精确到乡镇）',
  `health` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '健康状况，良好、某部有残疾',
  `pecuniary` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '经济状况：县城有房有车等',
  `hobby` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '爱好选项，逗号隔开',
  `character` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '性格选项，逗号隔开',
  `job` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '工作',
  `job_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '工作地点',
  `height` int(11) NULL DEFAULT NULL COMMENT '身高：cm',
  `weight` int(11) NULL DEFAULT NULL COMMENT '体重kg',
  `education` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '小学、初中、高中、大学、硕士、博士',
  `marry` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '婚姻状况：未婚、离异、丧偶',
  `love` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '恋爱经历',
  `expect` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '择偶期望（暂时不细化）',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `id_yuelao` int(255) NULL DEFAULT NULL COMMENT '录入信息的月老id',
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '录入人点评',
  `flower` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '被送鲜花数（代表人气）',
  `coin` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '月老豆',
  `check` int(11) NULL DEFAULT 0 COMMENT '是否审核通过:0待审核，1审核通过，2审核不通过',
  `vip` tinyint(1) NULL DEFAULT NULL COMMENT '是否是VIP',
  `vip_date` datetime(0) NULL DEFAULT NULL COMMENT 'VIP到期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (1, '罗山', '韩国新', '18201116659', '123456', '男', '1989-09-27', 29, '411521198909270938', NULL, '罗山竹竿', '良好', '有钱', '读书，打球，打电竞，听歌', '老实人', '工程师', '北京', 170, 62, '本科', '未婚', NULL, '长得好看，还会做饭', '啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦', 1, '不错不错', 0, 0, 0, NULL, NULL);
INSERT INTO `member` VALUES (3, '罗山', '王颖颖', '18612183653', '123456', '女', '1989-09-27', 29, '411521198909270938', NULL, '罗山竹竿', '良好', '有钱', '读书，打球，打电竞，听歌', '老实人', '工程师', '北京', 170, 62, '本科', '未婚', NULL, '长得好看，还会做饭', '啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦', 1, '不错不错', 0, 0, 0, NULL, NULL);
INSERT INTO `member` VALUES (4, '罗山', '李昌文', '18201116660', '123456', '男', '1989-09-27', 29, '411521198909270938', NULL, '罗山竹竿', '良好', '有钱', '打电竞，听歌，摄影', '老实人', '工程师', '北京', 170, 62, '本科', '未婚', NULL, '长得好看，还会做饭', '啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦', 0, '不错不错', 0, 0, 0, NULL, NULL);

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int(11) NOT NULL,
  `type` int(255) NULL DEFAULT NULL COMMENT '充值、预约扣费、预约退款、送花（一朵花，10张卷）、提现',
  `coin` int(255) NULL DEFAULT NULL COMMENT '月老卷数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for record_type
-- ----------------------------
DROP TABLE IF EXISTS `record_type`;
CREATE TABLE `record_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of record_type
-- ----------------------------
INSERT INTO `record_type` VALUES (1, '充值');
INSERT INTO `record_type` VALUES (2, '购买VIP');
INSERT INTO `record_type` VALUES (3, '预约扣费');
INSERT INTO `record_type` VALUES (4, '预约退款');
INSERT INTO `record_type` VALUES (5, '送花');
INSERT INTO `record_type` VALUES (6, '提现');

-- ----------------------------
-- Table structure for yuelao
-- ----------------------------
DROP TABLE IF EXISTS `yuelao`;
CREATE TABLE `yuelao`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `id_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '身份证号',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `coin` int(10) NULL DEFAULT NULL COMMENT '月老豆',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of yuelao
-- ----------------------------
INSERT INTO `yuelao` VALUES (1, '韩国新', '411521198909270938', '18201116659', '123456', 100);
INSERT INTO `yuelao` VALUES (2, '韩桂林', '411521198612280938', '13655778899', '123456', 0);

SET FOREIGN_KEY_CHECKS = 1;
