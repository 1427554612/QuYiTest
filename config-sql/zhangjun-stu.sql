/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : zhangjun-stu

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 10/11/2023 14:34:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cource
-- ----------------------------
DROP TABLE IF EXISTS `cource`;
CREATE TABLE `cource`  (
  `cid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cource
-- ----------------------------
INSERT INTO `cource` VALUES ('201', '语文');
INSERT INTO `cource` VALUES ('202', '数学');
INSERT INTO `cource` VALUES ('203', '英语');
INSERT INTO `cource` VALUES ('204', '生物');
INSERT INTO `cource` VALUES ('205', '地理');
INSERT INTO `cource` VALUES ('206', '物理');

-- ----------------------------
-- Table structure for person_1
-- ----------------------------
DROP TABLE IF EXISTS `person_1`;
CREATE TABLE `person_1`  (
  `id` int(1) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of person_1
-- ----------------------------
INSERT INTO `person_1` VALUES (1, 'zhangjun');

-- ----------------------------
-- Table structure for person_2
-- ----------------------------
DROP TABLE IF EXISTS `person_2`;
CREATE TABLE `person_2`  (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of person_2
-- ----------------------------
INSERT INTO `person_2` VALUES (2, 'lisi');

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`  (
  `sid` char(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sco` float(255, 0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score
-- ----------------------------
INSERT INTO `score` VALUES ('101', '201', 60);
INSERT INTO `score` VALUES ('101', '203', 25);
INSERT INTO `score` VALUES ('101', '202', 66);
INSERT INTO `score` VALUES ('101', '204', 95);
INSERT INTO `score` VALUES ('101', '205', 100);
INSERT INTO `score` VALUES ('101', '206', 75);
INSERT INTO `score` VALUES ('102', '201', 80);
INSERT INTO `score` VALUES ('102', '202', 66);
INSERT INTO `score` VALUES ('102', '203', 43);
INSERT INTO `score` VALUES ('102', '204', 99);
INSERT INTO `score` VALUES ('102', '205', 100);
INSERT INTO `score` VALUES ('102', '206', 33);
INSERT INTO `score` VALUES ('103', '201', 85);
INSERT INTO `score` VALUES ('103', '202', NULL);
INSERT INTO `score` VALUES ('103', '203', 94);
INSERT INTO `score` VALUES ('103', '204', 56);
INSERT INTO `score` VALUES ('103', '205', 26);
INSERT INTO `score` VALUES ('103', '206', 61);
INSERT INTO `score` VALUES ('104', '201', 88);
INSERT INTO `score` VALUES ('104', '202', 39);
INSERT INTO `score` VALUES ('104', '203', 54);
INSERT INTO `score` VALUES ('104', '204', 21);
INSERT INTO `score` VALUES ('104', '205', 66);
INSERT INTO `score` VALUES ('104', '206', 13);
INSERT INTO `score` VALUES ('105', '201', 86);
INSERT INTO `score` VALUES ('105', '202', 64);
INSERT INTO `score` VALUES ('105', '203', 79);
INSERT INTO `score` VALUES ('105', '204', 93);
INSERT INTO `score` VALUES ('105', '205', 63);
INSERT INTO `score` VALUES ('105', '206', 95);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `sid` char(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ssex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `saddress` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('101', '张军', '28', '男', '湖南怀化');
INSERT INTO `student` VALUES ('102', '罗霞', '24', '女', '湖南怀化');
INSERT INTO `student` VALUES ('103', '符季霞', '23', '女', '湖南怀化');
INSERT INTO `student` VALUES ('104', '陈善禄', '29', '男', '湖南衡阳');
INSERT INTO `student` VALUES ('105', '谭杰萍', '18', '女', '江西九江');

SET FOREIGN_KEY_CHECKS = 1;
