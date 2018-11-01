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

 Date: 01/11/2018 17:34:00
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
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_male` int(255) NOT NULL COMMENT '男方id',
  `name_male` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '男方姓名',
  `id_male_yuelao` int(255) NULL DEFAULT NULL,
  `id_female` int(255) NOT NULL COMMENT '女方id',
  `name_female` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '女方姓名',
  `id_female_yuelao` int(255) NULL DEFAULT NULL,
  `meet_time` datetime(0) NULL DEFAULT NULL COMMENT '见面时间',
  `meet_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '见面地点',
  `meet_state` int(255) NULL DEFAULT NULL COMMENT '等待中（一方预约预扣费，预约失败返还）、预约中、预约取消、预约结束',
  `coin` int(255) NULL DEFAULT NULL COMMENT '预约花费 月老豆（选择项，基础服务200，扩展分类）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of meet
-- ----------------------------
INSERT INTO `meet` VALUES (1, 5, '徐成祥', 3, 6, '郑惠', 3, '2018-10-31 17:53:24', '月老阁奶茶店', 1, 200);
INSERT INTO `meet` VALUES (2, 8, '周金柱', 1, 6, '郑惠', 3, '2018-10-31 17:57:05', '月老阁奶茶店', 2, 200);
INSERT INTO `meet` VALUES (3, 9, '朱磊', 1, 6, '郑惠', 3, '2018-10-31 17:57:57', '月老阁奶茶店', 3, 200);
INSERT INTO `meet` VALUES (4, 1, '李昌文', 1, 6, '郑惠', 3, '2018-10-31 17:59:09', '月老阁奶茶店', 4, 200);

-- ----------------------------
-- Table structure for meet_state
-- ----------------------------
DROP TABLE IF EXISTS `meet_state`;
CREATE TABLE `meet_state`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of meet_state
-- ----------------------------
INSERT INTO `meet_state` VALUES (1, '等待中');
INSERT INTO `meet_state` VALUES (2, '预约中');
INSERT INTO `meet_state` VALUES (3, '预约取消');
INSERT INTO `meet_state` VALUES (4, '预约结束');

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
  `create_date` date NOT NULL COMMENT '录入日期',
  `id_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '身份证号',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '头像地址',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '住址（精确到乡镇）',
  `health` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '健康状况，良好、某部有残疾',
  `pecuniary` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '经济状况：县城有房有车等',
  `hobby` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '爱好选项，逗号隔开',
  `disposition` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '性格选项，逗号隔开',
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
  `vip_date` date NULL DEFAULT NULL COMMENT 'VIP到期时间',
  PRIMARY KEY (`id`, `phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (1, '罗山', '李昌文', '18201116660', '123456', '男', '1989-09-27', 29, '2018-10-29', '411521198909270938', NULL, '罗山竹竿', '良好', '有钱', '打电竞，听歌，摄影', '老实人', '工程师', '北京', 170, 62, '本科', '未婚', '没有', '长得好看，还会做饭', '啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦', 1, '不错不错', 0, 0, 1, NULL, NULL);
INSERT INTO `member` VALUES (5, '罗山', '徐成祥', '18697731302', '123456', '男', '1995-04-07', 24, '2018-10-29', '411521199504070000', NULL, '罗山城关', '良好', '两套房，有车', '运动', '阳光，上进', '自由职业', '罗山', 175, 86, '大专', '未婚', '两次', '身高：160-175，年龄：22-28，其他：看对眼，什么都行', NULL, 3, 'NICE', 0, 0, 1, NULL, NULL);
INSERT INTO `member` VALUES (6, '罗山', '郑惠', '15907138348', '123456', '女', '1995-12-09', 23, '2018-10-29', '421126199512091129', NULL, '罗山城关', '良好', '良好', '唱歌，美食，摄影，旅游', '可爱', '摄影师', '罗山', 165, 55, '大专', '未婚', '一次', '身高：175以上，年龄：26-30，恋爱史：无，其他：要有责任感，善良孝顺，对事业有自己的想法和目标，脾气好，品行端正', NULL, 3, 'NICE', 0, 0, 1, NULL, NULL);
INSERT INTO `member` VALUES (7, '罗山', '徐军辉', '18737658839', '123456', '男', '1994-12-10', 24, '2018-10-29', '411521199412108315', NULL, '罗山子路', '良好', '良好', '听歌，看电影，打游戏', '阳光，上进', '自由职业', '信阳', 169, 60, '大专', '未婚', '一次', '身高：145以上，年龄：21-25，体重：85-120，学历：高中', NULL, 3, 'NICE', 0, 0, 1, NULL, NULL);
INSERT INTO `member` VALUES (8, '罗山', '周金柱', '18601951454', '123456', '男', '1992-10-14', 26, '2018-10-29', '41152119921014465X', NULL, '罗山城关', '良好', '有房无车', '运动', '阳光，上进', '互联网', '杭州', 168, 58, '本科', '未婚', '一次', '身高：158-170，年龄：22-28', NULL, 1, 'NICE', 0, 0, 1, NULL, NULL);
INSERT INTO `member` VALUES (9, '罗山', '朱磊', '18620869795', '123456', '男', '1989-04-22', 29, '2018-10-29', '411521198904220034', NULL, '罗山城关', '良好', '有房有车', '运动', '阳光，上进', '互联网', '广州', 170, 60, '硕士', '未婚', '没有', '身高：158-163，年龄：22-28，恋爱史：无，体重：不要太瘦，学历：本科，职业：老师、医生最好', NULL, 1, 'NICE', 0, 0, 1, NULL, NULL);
INSERT INTO `member` VALUES (10, '罗山', '查红娟', '15565531185', '123456', '女', '1991-01-01', 27, '2018-11-01', NULL, NULL, '罗山城关', '良好', NULL, NULL, NULL, '公安局', '罗山', NULL, NULL, NULL, NULL, NULL, '身高：175以上，经济条件：有单位，有房', NULL, 2, NULL, 0, 0, 0, NULL, NULL);
INSERT INTO `member` VALUES (11, '罗山', '张倩', '17737468936', '123456', '女', '1992-01-01', 26, '2018-11-01', NULL, NULL, '罗山城关', '良好', NULL, NULL, NULL, '教师', '罗山', NULL, NULL, '本科', NULL, NULL, NULL, NULL, 2, NULL, 0, 0, 0, NULL, NULL);
INSERT INTO `member` VALUES (12, '罗山', 'XXX', '18201116659', '123456', '男', '1989-09-27', 29, '2018-11-01', '', 'null', '罗山城关', '良好', '有房有车', '看书', '乐观', '', '', 0, 0, '', '未婚', '', '', '', 1, '', 0, 0, 0, NULL, NULL);
INSERT INTO `member` VALUES (13, '罗山', 'XXX', '18201116659', '123456', '男', '1989-09-27', 29, '2018-11-01', '', 'null', '罗山城关', '良好', '有房有车', '看书', '乐观', '', '', 0, 0, '', '未婚', '', '', '', 1, '', 0, 0, 0, NULL, NULL);

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(255) NULL DEFAULT NULL COMMENT '充值、预约扣费、预约退款、送花（一朵花，10张卷）、提现',
  `coin` int(255) NULL DEFAULT NULL COMMENT '月老卷数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

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
  `coin` int(10) NULL DEFAULT 0 COMMENT '月老豆',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of yuelao
-- ----------------------------
INSERT INTO `yuelao` VALUES (1, '韩国新', '411521198909270938', '18201116659', '123456', 100, '罗山');
INSERT INTO `yuelao` VALUES (2, '韩桂林', '411521198601170922', '13283768923', '123456', 0, '罗山');
INSERT INTO `yuelao` VALUES (3, '李昌文', '411521199409224614', '18211303040', '123456', 0, '罗山');
INSERT INTO `yuelao` VALUES (5, '韩小旋', '411521198808210944', '13601756543', '123456', 0, '罗山');

SET FOREIGN_KEY_CHECKS = 1;
