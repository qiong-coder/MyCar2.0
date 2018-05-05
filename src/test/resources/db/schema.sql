CREATE TABLE Account (
  `id`              INT AUTO_INCREMENT PRIMARY KEY,
  `username`        VARCHAR(20) UNIQUE NOT NULL,
  `password`        VARCHAR(20) NOT NULL,
  `name`            VARCHAR(20) NOT NULL,
  `phone`           VARCHAR(20) NOT NULL,
  `sid`             INT(10) NOT NULL,
  `role`            INT(10) NOT NULL DEFAULT 2,
  `status`          INT(10) NOT NULL DEFAULT 0,
  `create_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Store (
  `id`              INT AUTO_INCREMENT PRIMARY KEY,
  `name`            VARCHAR(100) NOT NULL,
  `address`         VARCHAR(255) NOT NULL,
  `phone`           VARCHAR(255) NOT NULL,
  `status`          INT NOT NULL DEFAULT 0,
  `create_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
);

CREATE TABLE MyCarOrder (
  `id`              INT AUTO_INCREMENT PRIMARY KEY,
  `oid`             VARCHAR(50) UNIQUE NOT NULL,
  `viid`            INT(10) NOT NULL,
  `vid`             INT(10) DEFAULT NULL,
  `begin`           DATETIME DEFAULT NULL,
  `end`             DATETIME DEFAULT NULL,
  `rent_sid`        INT(10) NOT NULL,
  `return_sid`      INT(10) NOT NULL,
  `name`            VARCHAR(50) NOT NULL,
  `drivers`         TEXT NOT NULL,
  `identity`        VARCHAR(20) NOT NULL,
  `phone`           VARCHAR(20) NOT NULL,
  `bill`            TEXT DEFAULT NULL,
  `insurance`       VARCHAR(100) DEFAULT NULL,
  `pre_cost`        TEXT DEFAULT NULL,
  `pay_info`        TEXT,
  `rbegin`          DATETIME DEFAULT NULL,
  `rend`            DATETIME DEFAULT NULL,
  `rrent_sid`       INT(10) DEFAULT NULL,
  `rreturn_sid`     INT(10) DEFAULT NULL,
  `distance`        INT(10) DEFAULT NULL,
  `cost_info`       TEXT,
  `status`          INT(10) NOT NULL DEFAULT 0,
  `create_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
);


CREATE TABLE Vehicle (
  `id`              INT(10) AUTO_INCREMENT PRIMARY KEY,
  `viid`            INT(10) NOT NULL,
  `number`          VARCHAR(20) NOT NULL UNIQUE,
  `description`     TEXT NOT NULL,
  `status`          INT(10) NOT NULL DEFAULT 0,
  `begin`           DATETIME DEFAULT NULL,
  `end`             DATETIME DEFAULT NULL,
  `create_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sid`             INT(4) NOT NULL,
);

CREATE TABLE VehicleInfo (
  `id`              INT(10) AUTO_INCREMENT PRIMARY KEY,
  `name`            VARCHAR(50) NOT NULL,
  `displacement`    VARCHAR(50) NOT NULL,
  `gearbox`         VARCHAR(50) NOT NULL,
  `boxes`           VARCHAR(50) NOT NULL,
  `manned`          VARCHAR(50) NOT NULL,
  `oil`             VARCHAR(50) NOT NULL,
  `spare`           INT(10) NOT NULL DEFAULT 0,
  `description`     TEXT NOT NULL,
  `type`            VARCHAR(50) NOT NULL,
  `picture`         VARCHAR(100) NOT NULL,
  `cost`            TEXT NOT NULL,
  `create_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status`          INT NOT NULL DEFAULT 0,
);
