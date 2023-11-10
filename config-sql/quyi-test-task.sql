/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : quyi-test-task

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 10/11/2023 14:34:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务id',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `type` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行类型',
  `date` int(10) NULL DEFAULT NULL COMMENT '执行毫秒数',
  `cron` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `cron_parse` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron表达式的意思',
  `jobs` json NULL COMMENT '工作数据列表',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('001', 'p1定时任务', 'cron', NULL, '*/5 * * * * ?', '每5秒执行一次', '{\"list\": [\"user_register\", \"user_login\", \"user_info\", \"user_history_dice\", \"user_history_plinko\", \"user_history_crash\", \"user_history_limbo\", \"user_history_fierybot\", \"user_history_ring\", \"user_history_roulette\", \"user_history_double\", \"user_history_mines\", \"user_history_stairs\", \"user_history_double_speed\", \"user_history_coinflip\", \"user_history_fundist\", \"user_cfg\", \"user_vip_list\", \"user_client_analyse\", \"user_spread_award\", \"user_spread_award1\", \"user_client_analyse\", \"user_recharge_min_b\", \"user_recharge_max_b\", \"user_recharge_min_b3\", \"user_recharge_max_b3\", \"user_recharge_min_p\", \"user_recharge_max_p\", \"user_recharge_max_m1\", \"user_recharge_min_m1\", \"user_recharge_min_m2\", \"user_recharge_max_m2\", \"user_recharge_min_v1\", \"user_recharge_max_v1\"], \"configId\": \"1676053814137622529\"}', '2023-05-18 14:02:50', '2023-05-18 14:02:50', '张军');

-- ----------------------------
-- Table structure for task_run_log
-- ----------------------------
DROP TABLE IF EXISTS `task_run_log`;
CREATE TABLE `task_run_log`  (
  `id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行id',
  `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '开始执行时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '执行结束时间',
  `success_count` int(5) NULL DEFAULT NULL COMMENT '正确执行的用例条数',
  `error_count` int(5) NULL DEFAULT NULL COMMENT '执行失败的用例总数',
  `task_id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务id',
  `run_status` int(1) NULL DEFAULT NULL COMMENT '运行状态、0表示未开始、1表示执行中、2表示已终止'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_run_log
-- ----------------------------
INSERT INTO `task_run_log` VALUES ('1663422937020850177', '2023-06-02 14:48:07', '2023-07-24 11:05:19', NULL, NULL, '001', 3);
INSERT INTO `task_run_log` VALUES ('1663447221747372034', '2023-06-02 14:48:07', '2023-07-24 11:05:19', NULL, NULL, '001', 3);
INSERT INTO `task_run_log` VALUES ('1663457760145879041', '2023-06-02 14:48:07', '2023-07-24 11:05:19', NULL, NULL, '001', 3);
INSERT INTO `task_run_log` VALUES ('1663458089541369858', '2023-06-02 14:48:07', '2023-07-24 11:05:19', NULL, NULL, '001', 3);
INSERT INTO `task_run_log` VALUES ('1664524274198495234', '2023-06-02 14:48:07', '2023-07-24 11:05:19', NULL, NULL, '001', 3);

SET FOREIGN_KEY_CHECKS = 1;
