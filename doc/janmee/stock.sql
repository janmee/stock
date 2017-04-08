CREATE TABLE `stock` (
	`id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NOT NULL COMMENT '名字',
	`cname` VARCHAR(255) NULL DEFAULT NULL COMMENT '中文名',
	`category` VARCHAR(64) NULL DEFAULT NULL COMMENT '分类名',
	`category_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '分类id',
	`symbol` VARCHAR(64) NULL DEFAULT NULL COMMENT '代码',
	`mktcap` VARCHAR(50) NULL DEFAULT NULL COMMENT '市值',
	`pe` DOUBLE NULL DEFAULT NULL COMMENT '市盈率',
	`market` VARCHAR(50) NULL DEFAULT NULL COMMENT '上市地',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `symbol` (`symbol`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=7644
;


CREATE TABLE `stock_daily` (
	`id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`stock_id` INT(11) NOT NULL DEFAULT '0',
	`stock_cname` VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '中文名',
	`stock_symbol` VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '代码',
	`date` DATE NOT NULL COMMENT '日期',
	`open` DOUBLE NOT NULL DEFAULT '0' COMMENT '开盘价',
	`high` DOUBLE NOT NULL DEFAULT '0' COMMENT '最高价',
	`low` DOUBLE NOT NULL DEFAULT '0' COMMENT '最低价',
	`current` DOUBLE NOT NULL DEFAULT '0' COMMENT '收盘价',
	`volume` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '成交量',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `idx_stockSymbol_date` (`stock_symbol`, `date`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `daily_result` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`symbol` VARCHAR(32) NULL DEFAULT NULL COMMENT '代码',
	`date` DATE NULL DEFAULT NULL COMMENT '日期',
	`price` INT(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '当前价格',
	`strategy` VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '策略',
	PRIMARY KEY (`id`)
)
COMMENT='每天策略结果'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
