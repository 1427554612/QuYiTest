/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : quyi-api-autotest

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 10/11/2023 14:32:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for test_module
-- ----------------------------
DROP TABLE IF EXISTS `test_module`;
CREATE TABLE `test_module`  (
  `class_id` char(19) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类的id',
  `class_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类的名称',
  `class_mark` tinytext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '类的功能说明',
  `methods` json NULL COMMENT '类的方法列表',
  `attributes` json NULL COMMENT '类的属性列表',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_module
-- ----------------------------
INSERT INTO `test_module` VALUES ('1697535966706786306', 'com.zhangjun.quyi.api_auto_test.api_core.components.param.get.impl.PathParamsGetting', '获取参数到地址中', '[{\"isStatic\": false, \"methodName\": \"getParams\", \"paramTypes\": [{\"paramName\": \"sources\", \"paramType\": \"List\"}, {\"paramName\": \"target\", \"paramType\": \"ApiTestCaseEntity\"}], \"returnType\": \"ApiTestCaseEntity\"}]', '[]', '2023-09-01 17:04:47', '2023-09-01 17:04:47');

-- ----------------------------
-- Table structure for test_params
-- ----------------------------
DROP TABLE IF EXISTS `test_params`;
CREATE TABLE `test_params`  (
  `param_id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数id',
  `param_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数名称,',
  `case_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用例名称，Div类型参数都以div开头',
  `param_from` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数来源',
  `param_eq` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `param_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实的值',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_params
-- ----------------------------
INSERT INTO `test_params` VALUES ('1698891237538426881', '_id', 'div1', 'div', '11', '15462351246125978541', '2023-09-05 12:46:27', '2023-09-05 12:46:27');

SET FOREIGN_KEY_CHECKS = 1;
