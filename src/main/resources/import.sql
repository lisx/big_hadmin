/*
 Navicat Premium Data Transfer

 Source Server         : 21
 Source Server Type    : MySQL
 Source Server Version : 50637
 Source Host           : 192.168.4.21
 Source Database       : base

 Target Server Type    : MySQL
 Target Server Version : 50637
 File Encoding         : utf-8

 Date: 09/04/2017 11:28:37 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `big_exam`
-- ----------------------------
DROP TABLE IF EXISTS `big_exam`;
CREATE TABLE `big_exam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `area_name` varchar(255) DEFAULT NULL,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `exam_name` varchar(255) DEFAULT NULL,
  `judge_num` int(11) DEFAULT NULL,
  `judge_score` int(11) DEFAULT NULL,
  `multiple_num` int(11) DEFAULT NULL,
  `multiple_score` int(11) DEFAULT NULL,
  `rank_num` int(11) DEFAULT NULL,
  `rank_score` int(11) DEFAULT NULL,
  `single_num` int(11) DEFAULT NULL,
  `single_score` int(11) DEFAULT NULL,
  `station_name` varchar(255) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_exam_log`
-- ----------------------------
DROP TABLE IF EXISTS `big_exam_log`;
CREATE TABLE `big_exam_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `exam_time` datetime DEFAULT NULL,
  `if_use` int(11) NOT NULL,
  `score` int(11) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `bank_id` int(11) DEFAULT NULL,
  `exam_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm8uv8q4a3aflpeeb8u2atiphf` (`bank_id`),
  KEY `FKrfoh2kp9g3wdfhcukv5nsp152` (`exam_id`),
  KEY `FKhw657ws48xydgxsanb1sai19p` (`user_id`),
  CONSTRAINT `FKhw657ws48xydgxsanb1sai19p` FOREIGN KEY (`user_id`) REFERENCES `big_user` (`id`),
  CONSTRAINT `FKm8uv8q4a3aflpeeb8u2atiphf` FOREIGN KEY (`bank_id`) REFERENCES `big_question_bank` (`id`),
  CONSTRAINT `FKrfoh2kp9g3wdfhcukv5nsp152` FOREIGN KEY (`exam_id`) REFERENCES `big_exam` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_exam_log_question_logs`
-- ----------------------------
DROP TABLE IF EXISTS `big_exam_log_question_logs`;
CREATE TABLE `big_exam_log_question_logs` (
  `exam_log_id` int(11) NOT NULL,
  `question_logs_id` int(11) NOT NULL,
  UNIQUE KEY `UK_20cwqfxu4u2m8c0klwx3pjmu5` (`question_logs_id`),
  KEY `FK5d4o7ujrl18w0238oh81p5w9l` (`exam_log_id`),
  CONSTRAINT `FK38hrqli4k50oax8bxkcg4lbn` FOREIGN KEY (`question_logs_id`) REFERENCES `big_question_log` (`id`),
  CONSTRAINT `FK5d4o7ujrl18w0238oh81p5w9l` FOREIGN KEY (`exam_log_id`) REFERENCES `big_exam_log` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_file`
-- ----------------------------
DROP TABLE IF EXISTS `big_file`;
CREATE TABLE `big_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `check_id` varchar(255) DEFAULT NULL,
  `check_status` varchar(255) DEFAULT NULL,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_size` varchar(255) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `folder_id` varchar(255) DEFAULT NULL,
  `if_use` varchar(255) DEFAULT NULL,
  `station_id` varchar(255) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `affiliation` varchar(255) DEFAULT NULL,
  `byte_size` varchar(255) DEFAULT NULL,
  `folder_name` varchar(255) DEFAULT NULL,
  `if_folder` int(11) DEFAULT NULL,
  `menu_type` varchar(255) DEFAULT NULL,
  `node_code` varchar(255) DEFAULT NULL,
  `folder_file_id` int(11) DEFAULT NULL,
  `notice_id` int(11) DEFAULT NULL,
  `station_file_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlxcqfnuxwin8t9sst02n5x14c` (`folder_file_id`),
  KEY `FKfhwbuwqdev2mvxa99peufo87q` (`notice_id`),
  KEY `FKrobqje0nwta072bft64gnkxe` (`station_file_id`),
  CONSTRAINT `FKfhwbuwqdev2mvxa99peufo87q` FOREIGN KEY (`notice_id`) REFERENCES `big_notice` (`id`),
  CONSTRAINT `FKlxcqfnuxwin8t9sst02n5x14c` FOREIGN KEY (`folder_file_id`) REFERENCES `big_file` (`id`),
  CONSTRAINT `FKrobqje0nwta072bft64gnkxe` FOREIGN KEY (`station_file_id`) REFERENCES `big_line_station` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `big_file`
-- ----------------------------
BEGIN;
INSERT INTO `big_file` VALUES ('1', null, null, null, null, '题库', null, null, null, null, '0', null, null, null, null, null, null, '1', '培训资料', null, null, null, null), ('2', null, null, null, null, '文档资料', null, null, null, null, '0', null, null, null, null, null, null, '1', '培训资料', null, null, null, null), ('3', null, null, null, null, '信号平面图', null, null, null, null, '0', null, null, null, null, null, null, '1', '培训资料', null, null, null, null), ('4', null, null, null, null, '案例库', null, null, null, null, '0', null, null, null, null, null, null, '1', '培训资料', null, null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `big_line_station`
-- ----------------------------
DROP TABLE IF EXISTS `big_line_station`;
CREATE TABLE `big_line_station` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `if_use` bit(1) NOT NULL,
  `node_code` varchar(255) DEFAULT NULL,
  `node_name` varchar(255) DEFAULT NULL,
  `sorting` varchar(255) DEFAULT NULL,
  `menu_type` varchar(255) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `big_line_station`
-- ----------------------------
BEGIN;
INSERT INTO `big_line_station` VALUES ('744', null, null, b'0', '000001', '2号线', null, null, null, null), ('745', null, null, b'0', '000001001', '2号线西直门站区', null, null, null, null), ('746', null, null, b'0', '000001001001', '阜成门站', null, null, null, '2017-08-23 17:14:51'), ('747', null, null, b'0', '000001001002', '车公庄站', null, null, null, null), ('748', null, null, b'0', '000001001003', '2号线西直门站', null, null, null, null), ('749', null, null, b'0', '000001001004', '积水潭站', null, null, null, null), ('750', null, null, b'0', '000001002', '雍和宫站区', null, null, null, null), ('751', null, null, b'0', '000001002001', '鼓楼大街站', null, null, null, '2017-08-25 15:59:49'), ('752', null, null, b'0', '000001002002', '安定门站', null, null, null, null), ('753', null, null, b'0', '000001002003', '雍和宫站', null, null, null, null), ('754', null, null, b'0', '000001002004', '2号线东直门站', null, null, null, null), ('755', null, null, b'0', '000001003', '建国门站区', null, null, null, null), ('756', null, null, b'0', '000001003001', '东四十条站', null, null, null, null), ('757', null, null, b'0', '000001003002', '朝阳门站', null, null, null, null), ('758', null, null, b'0', '000001003003', '2号线建国门站', null, null, null, null), ('759', null, null, b'0', '000001003004', '1号线建国门站', null, null, null, null), ('760', null, null, b'0', '000001003005', '北京站站', null, null, null, null), ('761', null, null, b'0', '000001004', '前门站站区', null, null, null, null), ('762', null, null, b'0', '000001004001', '崇文门站', null, null, null, null), ('763', null, null, b'0', '000001004002', '前门站', null, null, null, null), ('764', null, null, b'0', '000001004003', '和平门站', null, null, null, null), ('765', null, null, b'0', '000001004004', '宣武门站', null, null, null, null), ('766', null, null, b'0', '000001004005', '长椿街站', null, null, null, null), ('767', null, null, b'0', '000002', '8号线', null, null, null, null), ('768', null, null, b'0', '000002001', '鼓楼大街站区', null, null, null, null), ('769', null, null, b'0', '000002001001', '中国美术馆站', null, null, null, null), ('770', null, null, b'0', '000002001002', '南锣鼓巷站', null, null, null, null), ('771', null, null, b'0', '000002001003', '什刹海站', null, null, null, null), ('772', null, null, b'0', '000002001004', '安德里北街站', null, null, null, null), ('773', null, null, b'0', '000002002', '北土城站区', null, null, null, null), ('774', null, null, b'0', '000002002001', '安华桥站', null, null, null, null), ('775', null, null, b'0', '000002002002', '10号线北土城站', null, null, null, null), ('776', null, null, b'0', '000002002003', '8号线北土城站', null, null, null, null), ('777', null, null, b'0', '000002002004', '奥体中心站', null, null, null, null), ('778', null, null, b'0', '000002002005', '奥林匹克公园站', null, null, null, null), ('779', null, null, b'0', '000002003', '永泰庄站区', null, null, null, null), ('780', null, null, b'0', '000002003001', '森林公园南门站', null, null, null, null), ('781', null, null, b'0', '000002003002', '林萃桥站', null, null, null, null), ('782', null, null, b'0', '000002003003', '永泰庄站', null, null, null, null), ('783', null, null, b'0', '000002003004', '西小口站', null, null, null, null), ('784', null, null, b'0', '000002003005', '育新站', null, null, null, null), ('785', null, null, b'0', '000002004', '回龙观东大街站区', null, null, null, null), ('786', null, null, b'0', '000002004001', '霍营站', null, null, null, null), ('787', null, null, b'0', '000002004002', '回龙观东大街站', null, null, null, null), ('788', null, null, b'0', '000002004003', '平西府站', null, null, null, null), ('789', null, null, b'0', '000002004004', '育知路站', null, null, null, null), ('790', null, null, b'0', '000003', '10号线', null, null, null, null), ('791', null, null, b'0', '000003001', '巴沟站区', null, null, null, null), ('792', null, null, b'0', '000003001001', '巴沟站', null, null, null, null), ('793', null, null, b'0', '000003001002', '苏州街站', null, null, null, null), ('794', null, null, b'0', '000003001003', '海淀黄庄站', null, null, null, null), ('795', null, null, b'0', '000003001004', '知春里站', null, null, null, null), ('796', null, null, b'0', '000003002', '牡丹园站区', null, null, null, null), ('797', null, null, b'0', '000003002001', '知春路站', null, null, null, null), ('798', null, null, b'0', '000003002002', '西土城站', null, null, null, null), ('799', null, null, b'0', '000003002003', '牡丹园站', null, null, null, null), ('800', null, null, b'0', '000003002004', '健德门站', null, null, null, null), ('801', null, null, b'0', '000003003', '安贞门站区', null, null, null, null), ('802', null, null, b'0', '000003003001', '安贞门站', null, null, null, null), ('803', null, null, b'0', '000003003002', '10号线芍药居站', null, null, null, null), ('804', null, null, b'0', '000003003003', '太阳宫站', null, null, null, null), ('805', null, null, b'0', '000003003004', '三元桥站', null, null, null, null), ('806', null, null, b'0', '000003004', '亮马桥站区', null, null, null, null), ('807', null, null, b'0', '000003004001', '亮马桥站', null, null, null, null), ('808', null, null, b'0', '000003004002', '农业展览馆站', null, null, null, null), ('809', null, null, b'0', '000003004003', '团结湖站', null, null, null, null), ('810', null, null, b'0', '000003004004', '呼家楼站', null, null, null, null), ('811', null, null, b'0', '000003005', '国贸站站区', null, null, null, null), ('812', null, null, b'0', '000003005001', '金台夕照站', null, null, null, null), ('813', null, null, b'0', '000003005002', '国贸站', null, null, null, null), ('814', null, null, b'0', '000003005003', '双井站', null, null, null, null), ('815', null, null, b'0', '000003005004', '劲松站', null, null, null, null), ('816', null, null, b'0', '000003006', '成寿寺站区', null, null, null, null), ('817', null, null, b'0', '000003006001', '潘家园站', null, null, null, null), ('818', null, null, b'0', '000003006002', '十里河站', null, null, null, null), ('819', null, null, b'0', '000003006003', '分钟寺站', null, null, null, null), ('820', null, null, b'0', '000003006004', '成寿寺站', null, null, null, null), ('821', null, null, b'0', '000003007', '大红门站区', null, null, null, null), ('822', null, null, b'0', '000003007001', '石榴庄站', null, null, null, null), ('823', null, null, b'0', '000003007002', '大红门站', null, null, null, null), ('824', null, null, b'0', '000003007003', '角门东站', null, null, null, null), ('825', null, null, b'0', '000003007004', '角门西站', null, null, null, null), ('826', null, null, b'0', '000003008', '首经贸站区', null, null, null, null), ('827', null, null, b'0', '000003008001', '草桥站', null, null, null, null), ('828', null, null, b'0', '000003008002', '纪家庙站', null, null, null, null), ('829', null, null, b'0', '000003008003', '首经贸站', null, null, null, null), ('830', null, null, b'0', '000003008004', '丰台站站', null, null, null, null), ('831', null, null, b'0', '000003008005', '泥洼站', null, null, null, null), ('832', null, null, b'0', '000003009', '西局站区', null, null, null, null), ('833', null, null, b'0', '000003009001', '西局站', null, null, null, null), ('834', null, null, b'0', '000003009002', '莲花桥站', null, null, null, null), ('835', null, null, b'0', '000003009003', '公主坟站', null, null, null, null), ('836', null, null, b'0', '000003009004', '西钓鱼台站', null, null, null, null), ('837', null, null, b'0', '000003010', '慈寿寺站区', null, null, null, null), ('838', null, null, b'0', '000003010001', '10号线慈寿寺站', null, null, null, null), ('839', null, null, b'0', '000003010002', '6号线慈寿寺站', null, null, null, null), ('840', null, null, b'0', '000003010003', '车道沟站', null, null, null, null), ('841', null, null, b'0', '000003010004', '长春桥站', null, null, null, null), ('842', null, null, b'0', '000003010005', '火器营站', null, null, null, null), ('843', null, null, b'0', '000004', '13号线', null, null, null, null), ('844', null, null, b'0', '000004001', '13号线西直门站区', null, null, null, null), ('845', null, null, b'0', '000004001001', '13号线西直门站', null, null, null, null), ('846', null, null, b'0', '000004001002', '大钟寺站', null, null, null, null), ('847', null, null, b'0', '000004001003', '五道口站', null, null, null, null), ('848', null, null, b'0', '000004001004', '上地站', null, null, null, null), ('849', null, null, b'0', '000004002', '霍营站区', null, null, null, null), ('850', null, null, b'0', '000004002001', '龙泽站', null, null, null, null), ('851', null, null, b'0', '000004002002', '回龙观站', null, null, null, null), ('852', null, null, b'0', '000004002003', '立水桥站', null, null, null, null), ('853', null, null, b'0', '000004003', '东直门站区', null, null, null, null), ('854', null, null, b'0', '000004003001', '北苑站', null, null, null, null), ('855', null, null, b'0', '000004003002', '望京西站', null, null, null, null), ('856', null, null, b'0', '000004003003', '13号线芍药居站', null, null, null, null), ('857', null, null, b'0', '000004003004', '光熙门站', null, null, null, null), ('858', null, null, b'0', '000004003005', '柳芳站', null, null, null, null), ('859', null, null, b'0', '000004003006', '13号线东直门站', null, null, null, null), ('999', null, null, b'0', '000', '运三分公司', null, null, null, '2017-08-23 17:14:40');
COMMIT;

-- ----------------------------
--  Table structure for `big_line_station_banks`
-- ----------------------------
DROP TABLE IF EXISTS `big_line_station_banks`;
CREATE TABLE `big_line_station_banks` (
  `station_id` int(11) NOT NULL,
  `banks_id` int(11) NOT NULL,
  UNIQUE KEY `UK_s3d3yuqpuusoc63w4hhh2pdj2` (`banks_id`),
  KEY `FKce4c5fmdmiuuexvpfpujn9b0h` (`station_id`),
  CONSTRAINT `FKce4c5fmdmiuuexvpfpujn9b0h` FOREIGN KEY (`station_id`) REFERENCES `big_line_station` (`id`),
  CONSTRAINT `FKhve0iwi7na9upu3f5630re0wf` FOREIGN KEY (`banks_id`) REFERENCES `big_question_bank` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_notice`
-- ----------------------------
DROP TABLE IF EXISTS `big_notice`;
CREATE TABLE `big_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `create_id` int(11) DEFAULT NULL,
  `create_time` varchar(255) DEFAULT NULL,
  `if_use` int(11) NOT NULL,
  `send_person` varchar(255) DEFAULT NULL,
  `send_postion` varchar(255) DEFAULT NULL,
  `station_name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_proper`
-- ----------------------------
DROP TABLE IF EXISTS `big_proper`;
CREATE TABLE `big_proper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `name` longtext DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `question_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdx6fki8uobsa4xa9ydlks19lr` (`question_id`),
  CONSTRAINT `FKdx6fki8uobsa4xa9ydlks19lr` FOREIGN KEY (`question_id`) REFERENCES `big_question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_question`
-- ----------------------------
DROP TABLE IF EXISTS `big_question`;
CREATE TABLE `big_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `answer` varchar(255) DEFAULT NULL,
  `bank_id` varchar(255) DEFAULT NULL,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `if_use` int(11) NOT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `menu_type` varchar(255) DEFAULT NULL,
  `proper` longtext DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `question_bank_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc50ymd5de26xugnh9oj025jq9` (`question_bank_id`),
  CONSTRAINT `FKc50ymd5de26xugnh9oj025jq9` FOREIGN KEY (`question_bank_id`) REFERENCES `big_question_bank` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_question_bank`
-- ----------------------------
DROP TABLE IF EXISTS `big_question_bank`;
CREATE TABLE `big_question_bank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `if_use` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpx1rjo5x48bnyd33g2iqyq2h0` (`station_id`),
  CONSTRAINT `FKpx1rjo5x48bnyd33g2iqyq2h0` FOREIGN KEY (`station_id`) REFERENCES `big_line_station` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_question_bank_question_list`
-- ----------------------------
DROP TABLE IF EXISTS `big_question_bank_question_list`;
CREATE TABLE `big_question_bank_question_list` (
  `question_bank_id` int(11) NOT NULL,
  `question_list_id` int(11) NOT NULL,
  UNIQUE KEY `UK_9ix106cst1nihj0ky3qksmu9v` (`question_list_id`),
  KEY `FKs7gspqudns9jno3dmvassfk0j` (`question_bank_id`),
  CONSTRAINT `FKl94qqmj1hhk106719g34plbua` FOREIGN KEY (`question_list_id`) REFERENCES `big_question` (`id`),
  CONSTRAINT `FKs7gspqudns9jno3dmvassfk0j` FOREIGN KEY (`question_bank_id`) REFERENCES `big_question_bank` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_question_log`
-- ----------------------------
DROP TABLE IF EXISTS `big_question_log`;
CREATE TABLE `big_question_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `log_id` int(11) DEFAULT NULL,
  `question_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm6nvmix6h3psu3yn4sp93gydi` (`log_id`),
  KEY `FKiby3lx9f1lfvdi6dr3uklj0sd` (`question_id`),
  CONSTRAINT `FKiby3lx9f1lfvdi6dr3uklj0sd` FOREIGN KEY (`question_id`) REFERENCES `big_question` (`id`),
  CONSTRAINT `FKm6nvmix6h3psu3yn4sp93gydi` FOREIGN KEY (`log_id`) REFERENCES `big_exam_log` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_question_log_select_proper`
-- ----------------------------
DROP TABLE IF EXISTS `big_question_log_select_proper`;
CREATE TABLE `big_question_log_select_proper` (
  `log_id` int(11) NOT NULL,
  `proper_id` int(11) NOT NULL,
  KEY `FK5hsh7qd229foe2y8lpvo8lm0v` (`proper_id`),
  KEY `FKkpy6m34oy9vwx25gynef8qio9` (`log_id`),
  CONSTRAINT `FK5hsh7qd229foe2y8lpvo8lm0v` FOREIGN KEY (`proper_id`) REFERENCES `big_proper` (`id`),
  CONSTRAINT `FKkpy6m34oy9vwx25gynef8qio9` FOREIGN KEY (`log_id`) REFERENCES `big_question_log` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_question_propers`
-- ----------------------------
DROP TABLE IF EXISTS `big_question_propers`;
CREATE TABLE `big_question_propers` (
  `question_id` int(11) NOT NULL,
  `propers_id` int(11) NOT NULL,
  UNIQUE KEY `UK_rn23ql5y0bc37p89p78pb53ox` (`propers_id`),
  KEY `FK9wr2n43uqpoxyqaat3l9thmf0` (`question_id`),
  CONSTRAINT `FK5q8vetd4rjnu9lrtinfysnfhi` FOREIGN KEY (`propers_id`) REFERENCES `big_proper` (`id`),
  CONSTRAINT `FK9wr2n43uqpoxyqaat3l9thmf0` FOREIGN KEY (`question_id`) REFERENCES `big_question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_resource`
-- ----------------------------
DROP TABLE IF EXISTS `big_resource`;
CREATE TABLE `big_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `is_hide` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `source_key` varchar(255) DEFAULT NULL,
  `source_url` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbbebrq42dc6knu2bm85hvof08` (`parent_id`),
  CONSTRAINT `FKbbebrq42dc6knu2bm85hvof08` FOREIGN KEY (`parent_id`) REFERENCES `big_resource` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `big_resource`
-- ----------------------------
BEGIN;
INSERT INTO `big_resource` VALUES ('1', null, '2017-01-10 13:56:51', '基础信息维护', 'fa-cubes', '0', '2', '基础信息维护', '2', 'system:demo:index', null, '0', null, null, null), ('2', null, '2017-01-10 13:56:51', '用户管理', 'fa-cubes', '0', '2', '用户管理', '1', 'system:user:index', '/admin/user/index', '1', null, null, null), ('3', null, '2017-01-10 13:56:51', '用户编辑', null, '0', '3', '用户编辑', '1', 'system:user:edit', '/admin/user/edit*', '2', '1', null, '2017-08-31 14:42:30'), ('4', null, '2017-01-11 16:48:48', '用户添加', null, '0', '3', '用户添加', '2', 'system:user:add', '/admin/user/add', '2', '1', null, null), ('5', null, '2017-01-11 16:48:48', '用户删除', null, '0', '3', '用户删除', '3', 'system:user:deleteBatch', '/admin/user/deleteBatch', '2', '1', null, null), ('6', null, '2017-01-11 16:48:48', '角色分配', null, '0', '3', '角色分配', '4', 'system:user:grant', '/admin/user/grant/**', '2', '1', null, null), ('7', null, '2017-01-12 16:45:10', '角色管理', null, '0', '2', '角色管理', '2', 'system:role:index', '/admin/role/index', '1', null, null, null), ('8', null, '2017-01-12 16:47:02', '角色编辑', null, '0', '3', '角色编辑', '1', 'system:role:edit', '/admin/role/edit*', '2', '1', null, null), ('9', null, '2017-01-12 16:47:23', '角色添加', null, '0', '3', '角色添加', '2', 'system:role:add', '/admin/role/add', '2', '6', null, null), ('10', null, '2017-01-12 16:47:23', '角色删除', null, '0', '3', '角色删除', '3', 'system:role:deleteBatch', '/admin/role/deleteBatch', '2', '6', null, null), ('11', null, '2017-01-12 16:47:23', '资源分配', null, '0', '3', '资源分配', '4', 'system:role:grant', '/admin/role/grant/**', '2', '6', null, null), ('12', null, '2017-01-17 11:21:12', '资源管理', null, '0', '2', '资源管理', '3', 'system:resource:index', '/admin/resource/index', '1', null, null, null), ('13', null, '2017-01-17 11:21:52', '资源编辑', null, '0', '3', '资源编辑', '1', 'system:resource:edit', '/admin/resource/edit*', '2', '11', null, null), ('14', null, '2017-01-17 11:21:54', '资源添加', null, '0', '3', '资源添加', '2', 'system:resource:add', '/admin/resource/add', '2', '11', null, null), ('15', null, '2017-01-17 11:21:54', '资源删除', null, '0', '3', '资源删除', '3', 'system:resource:deleteBatch', '/admin/resource/deleteBatch', '2', '11', null, null), ('16', null, '2017-01-10 13:56:51', '车站信息', null, '0', '2', '车站信息', '4', 'system:station:index', '/admin/station/index', '1', null, null, null), ('17', null, '2017-08-31 09:43:54', '上传按钮', null, '0', '3', '上传文件', '1', 'system:station:uploadFile', 'admin/station/uploadFile', '2', '15', null, '2017-08-31 14:38:57'), ('18', null, '2017-08-31 09:43:54', '新增按钮', null, '0', '3', '车站新增', '1', 'system:station:add', 'admin/station/add', '2', '15', null, '2017-08-31 14:38:57'), ('19', null, '2017-08-31 09:43:54', '编辑按钮', null, '0', '3', '车站编辑', '1', 'system:station:edit', 'admin/station/edit', '2', '15', null, '2017-08-31 14:38:57'), ('20', null, '2017-08-31 09:43:54', '删除按钮', null, '0', '3', '车站删除', '1', 'system:station:del', 'admin/station/del', '2', '15', null, '2017-08-31 15:11:15'), ('21', null, '2017-08-31 09:43:54', '下载按钮', null, '0', '3', '车站文件下载', '1', 'system:station:down', 'admin/station/down', '2', '15', null, '2017-08-31 14:38:57'), ('22', null, '2017-08-31 09:43:54', '删除按钮', null, '0', '3', '车站文件删除', '1', 'system:station:delete', 'admin/station/delete', '2', '15', null, '2017-08-31 14:38:57'), ('23', null, '2017-01-10 13:56:51', '运营管理', 'fa-cubes', '0', '2', '运营管理', '2', 'system:demo:index', null, '0', null, null, null), ('24', null, '2017-01-10 13:56:51', '学习园地', null, '0', '2', '学习园地', '5', 'system:train:index', '/admin/train/index', '1', null, null, null), ('25', null, '2017-08-31 09:43:54', '培训资料', null, '0', '3', '培训资料', '1', 'system:train:tab', 'admin/trian/tab', '2', '15', null, '2017-08-31 14:38:57'), ('26', null, '2017-08-31 09:43:54', '练习考试', null, '0', '3', '练习考试', '1', 'system:bank:tab', 'admin/bank/tab', '2', '15', null, '2017-08-31 14:38:57'), ('27', null, '2017-08-31 09:43:54', '试卷类型', null, '0', '3', '试卷类型', '1', 'system:exam:tab', 'admin/exam/tab', '2', '15', null, '2017-08-31 14:38:57'), ('28', null, '2017-08-31 09:43:54', '培训资料查看', null, '0', '3', '培训资料查看', '1', 'system:train:show', 'admin/train/show', '2', '15', null, '2017-08-31 14:38:57'), ('29', null, '2017-08-31 09:43:54', '培训资料新建文件夹', null, '0', '3', '培训资料新建文件夹', '1', 'system:train:addFolder', 'admin/train/addFolder', '2', '15', null, '2017-08-31 14:38:57'), ('30', null, '2017-08-31 09:43:54', '培训资料上传文件', null, '0', '3', '培训资料上传文件', '1', 'system:train:uploadFile', 'admin/train/uploadFile', '2', '15', null, '2017-08-31 14:38:57'), ('31', null, '2017-08-31 09:43:54', '培训资料下载', null, '0', '3', '培训资料下载', '1', 'system:train:down', 'admin/train/down', '2', '15', null, '2017-08-31 14:38:57'), ('32', null, '2017-08-31 09:43:54', '培训资料删除', null, '0', '3', '培训资料删除', '1', 'system:train:down', 'admin/train/down', '2', '15', null, '2017-08-31 14:38:57'), ('34', null, '2017-08-31 09:43:54', '练习考试创建题库', null, '0', '3', '练习考试创建题库', '1', 'system:bank:add', 'admin/bank/add', '2', '15', null, '2017-08-31 14:38:57'), ('35', null, '2017-08-31 09:43:54', '练习考试批量导入', null, '0', '3', '练习考试批量导入', '1', 'system:bank:uploadFile', 'admin/bank/uploadFile', '2', '15', null, '2017-08-31 14:38:57'), ('36', null, '2017-08-31 09:43:54', '练习考试查看', null, '0', '3', '练习考试查看', '1', 'system:bank:show', 'admin/bank/show', '2', '15', null, '2017-08-31 14:38:57'), ('37', null, '2017-08-31 09:43:54', '练习考试删除', null, '0', '3', '练习考试删除', '1', 'system:bank:delete', 'admin/bank/delete', '2', '15', null, '2017-08-31 14:38:57'), ('38', null, '2017-08-31 09:43:54', '练习考试试题删除', null, '0', '3', '练习考试试题删除', '1', 'system:bank:questiondelete', 'admin/bank/questiondelete', '2', '15', null, '2017-08-31 14:38:57'), ('39', null, '2017-08-31 09:43:54', '配置试卷', null, '0', '3', '配置试卷', '1', 'system:exam:add', 'admin/exam/add', '2', '15', null, '2017-08-31 14:38:57'), ('40', null, '2017-08-31 09:43:54', '配置试卷编辑', null, '0', '3', '配置试卷编辑', '1', 'system:exam:edit', 'admin/exam/edit', '2', '15', null, '2017-08-31 14:38:57'), ('41', null, '2017-08-31 09:43:54', '配置试卷删除', null, '0', '3', '配置试卷删除', '1', 'system:exam:delete', 'admin/exam/delete', '2', '15', null, '2017-08-31 14:38:57'), ('42', null, '2017-01-10 13:56:51', '考试记录', null, '0', '2', '考试记录', '5', 'system:examlog:index', '/admin/examlog/index', '1', null, null, null), ('43', null, '2017-01-10 13:56:51', '应急预案', null, '0', '2', '应急预案', '5', 'system:emergency:index', '/admin/emergency/index', '1', null, null, null), ('44', null, '2017-01-10 13:56:51', '规章制度', null, '0', '2', '规章制度', '5', 'system:rules:index', '/admin/rules/index', '1', null, null, null), ('45', null, '2017-01-10 13:56:51', '消防安全文件', null, '0', '2', '消防安全文件', '5', 'system:fire:index', '/admin/fire/index', '1', null, null, null), ('46', null, '2017-01-10 13:56:51', '运行图管理', null, '0', '2', '运行图管理', '5', 'system:runing:index', '/admin/running/index', '1', null, null, null), ('47', null, '2017-01-10 13:56:51', '通知管理', null, '0', '2', '通知管理', '5', 'system:notice:index', '/admin/notice/index', '1', null, null, null), ('48', null, '2017-01-10 13:56:51', '首页滚播图', null, '0', '2', '首页滚播图', '5', 'system:rollPlay:index', '/admin/rollPlay/index', '1', null, null, null), ('49', null, '2017-01-10 13:56:51', '前端版本更新', null, '0', '2', '前端版本更新', '5', 'system:edtion:index', '/admin/edtion/index', '1', null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `big_role`
-- ----------------------------
DROP TABLE IF EXISTS `big_role`;
CREATE TABLE `big_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `role_key` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `big_role`
-- ----------------------------
BEGIN;
INSERT INTO `big_role` VALUES ('1', '1', '2017-08-21 08:36:12', '管理员说明', '管理员', 'admin', '0', null, null), ('2', null, '2017-08-31 15:59:21', '站区管理员', '站区管理员', 'area', null, null, '2017-08-31 15:59:21');
COMMIT;

-- ----------------------------
--  Table structure for `big_role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `big_role_resource`;
CREATE TABLE `big_role_resource` (
  `role_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`resource_id`),
  KEY `FKniai4081j0futu93ec7qmv5ke` (`resource_id`),
  CONSTRAINT `FK7fp9bhh35d6iy6ujud6upe0o9` FOREIGN KEY (`role_id`) REFERENCES `big_role` (`id`),
  CONSTRAINT `FKniai4081j0futu93ec7qmv5ke` FOREIGN KEY (`resource_id`) REFERENCES `big_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `big_role_resource`
-- ----------------------------
BEGIN;
INSERT INTO `big_role_resource` VALUES ('1', '1'), ('2', '1'), ('1', '2'), ('2', '2'), ('1', '3'), ('2', '3'), ('1', '4'), ('2', '4'), ('1', '5'), ('2', '5'), ('1', '6'), ('1', '7'), ('2', '7'), ('1', '8'), ('1', '9'), ('1', '10'), ('1', '11'), ('1', '12'), ('1', '13'), ('1', '14'), ('1', '15'), ('2', '15'), ('1', '16'), ('2', '16'), ('1', '17'), ('2', '17'), ('1', '18'), ('2', '18'), ('1', '19'), ('2', '19'), ('1', '20'), ('2', '20'), ('1', '21'), ('2', '21'), ('1', '22'), ('2', '22'), ('1', '23'), ('2', '23'), ('1', '24'), ('2', '24'), ('1', '25'), ('2', '25'), ('1', '26'), ('2', '26'), ('1', '27'), ('2', '27'), ('1', '28'), ('2', '28'), ('1', '29'), ('2', '29'), ('1', '30'), ('2', '30'), ('1', '31'), ('1', '32'), ('2', '32'), ('2', '33'), ('1', '34'), ('2', '34'), ('1', '35'), ('2', '35'), ('1', '36'), ('2', '36'), ('1', '37'), ('2', '37'), ('1', '38'), ('2', '38'), ('1', '39'), ('2', '39'), ('1', '40'), ('2', '40'), ('1', '41'), ('2', '41'), ('1', '42'), ('2', '42'), ('1', '43'), ('2', '43'), ('1', '44'), ('1', '45'), ('2', '45'), ('1', '46'), ('2', '46'), ('1', '47'), ('1', '48'), ('1', '49');
COMMIT;

-- ----------------------------
--  Table structure for `big_running`
-- ----------------------------
DROP TABLE IF EXISTS `big_running`;
CREATE TABLE `big_running` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `date_type` varchar(255) DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `file_id` int(11) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_size` varchar(255) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `if_use` int(11) NOT NULL,
  `line_name` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_user`
-- ----------------------------
DROP TABLE IF EXISTS `big_user`;
CREATE TABLE `big_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `fasz_url` varchar(255) DEFAULT NULL,
  `fwxxk_url` varchar(255) DEFAULT NULL,
  `if_use` int(11) DEFAULT NULL,
  `job` varchar(255) DEFAULT NULL,
  `line` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `photo_url` varchar(255) DEFAULT NULL,
  `station` varchar(255) DEFAULT NULL,
  `station_area` varchar(255) DEFAULT NULL,
  `user_code` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `zkysgz_url` varchar(255) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `big_user`
-- ----------------------------
BEGIN;
INSERT INTO `big_user` VALUES ('1', '1', '2017-08-21 08:22:21', 'FAS证', '服务信息证', '0', '综控员', '2号线', 'UUKHSDDI5KPA43A8VL06V0TU2', '头像证', '', '运三分公司', '123456', '运三管理员', '综控员证', null, null, null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `big_user_logs`
-- ----------------------------
DROP TABLE IF EXISTS `big_user_logs`;
CREATE TABLE `big_user_logs` (
  `user_id` int(11) NOT NULL,
  `logs_id` int(11) NOT NULL,
  UNIQUE KEY `UK_pog8tv3fvdmou4ymverq3dnqs` (`logs_id`),
  KEY `FKbc7cycxtag1fl54ov7henfw3x` (`user_id`),
  CONSTRAINT `FKbc7cycxtag1fl54ov7henfw3x` FOREIGN KEY (`user_id`) REFERENCES `big_user` (`id`),
  CONSTRAINT `FKjt44e3gwcvcewb4a75ya2vkbv` FOREIGN KEY (`logs_id`) REFERENCES `big_exam_log` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `big_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `big_user_role`;
CREATE TABLE `big_user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKrpum8ydujqvp54nhux1e0cdc6` (`role_id`),
  CONSTRAINT `FKfkl5vh95otydqe1svrkbvkvoi` FOREIGN KEY (`role_id`) REFERENCES `big_role` (`id`),
  CONSTRAINT `FKr47jwtejkfmg7r7ecq8cak4nl` FOREIGN KEY (`user_id`) REFERENCES `big_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `big_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `big_user_role` VALUES ('1', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
