/*
 Navicat Premium Data Transfer

 Source Server         : 222.30.195.177
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : 222.30.195.177:4000
 Source Schema         : movie

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 07/10/2021 18:26:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hot
-- ----------------------------
DROP TABLE IF EXISTS `hot`;
CREATE TABLE `hot` (
  `movie_id` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='存储Flink实时统计的最近热播数据';

-- ----------------------------
-- Table structure for movies
-- ----------------------------
DROP TABLE IF EXISTS `movies`;
CREATE TABLE `movies` (
  `movie_id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `genres` varchar(255) DEFAULT NULL,
  `avg_rating` double DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`movie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='存储电影数据';

-- ----------------------------
-- Table structure for ratings
-- ----------------------------
DROP TABLE IF EXISTS `ratings`;
CREATE TABLE `ratings` (
  `user_id` int(11) DEFAULT NULL,
  `movie_id` int(11) DEFAULT NULL,
  `rating` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='存储电影数据';

-- ----------------------------
-- Table structure for ts_ratings
-- ----------------------------
DROP TABLE IF EXISTS `ts_ratings`;
CREATE TABLE `ts_ratings` (
  `count` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='存储过去十次统计的十秒内评分数量';

SET FOREIGN_KEY_CHECKS = 1;
