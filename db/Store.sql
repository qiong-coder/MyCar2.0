SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for Store
-- ----------------------------
DROP TABLE IF EXISTS `Store`;
CREATE TABLE `Store` (
  `id`              INT(10) NOT NULL AUTO_INCREMENT,
  `name`            VARCHAR(100) NOT NULL COMMENT '门店名称',
  `address`         VARCHAR(255) NOT NULL COMMENT '门店地址',
  `phone`           VARCHAR(255) NOT NULL COMMENT '门店电话',
  `city`            VARCHAR(50)  NOT NULL COMMENT '城市',
  `status`          INT NOT NULL DEFAULT 0,
  `create_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

INSERT `Store` (name, address, phone, city, status) VALUES ('义乌','义乌商贸城一期A14门市','4006470600,0470-6230866', '义乌',10);
INSERT `Store` (name, address, phone, city, status) VALUES ('滿洲里','满洲里西郊国际机场','4006470600,0470-6230866', '满洲里', 10);
INSERT `Store` (name, address, phone, city) VALUES ('满洲里市义乌商贸城一期14号门市', '满洲里-义乌店','4006470600', '满洲里');
INSERT `Store` (name, address, phone, city) VALUES ('满洲里市西郊机场', '满洲里-机场店','0470-2823766（24H）', '满洲里');
INSERT `Store` (name, address, phone, city, status) VALUES ('海拉尔机场路**号', '海拉尔机场店','4006470600', '满洲里', 10);
INSERT `Store` (name, address, phone, city, status) VALUES ('满洲里市套娃酒店', '满洲里市义乌一期14号门市','4006470600', '满洲里', 10);
INSERT `Store` (name, address, phone, city, status) VALUES ('满洲里市套娃景区酒店', '满洲里-套娃店','4006470600', '满洲里', 0);
INSERT `Store` (name, address, phone, city, status) VALUES ('海拉尔市机场路**号', '海拉尔-机场路店','4006470600', '海拉尔', 0);