ALTER TABLE `order_base`
ADD COLUMN `refund_result` VARCHAR(450) NULL COMMENT '退款理由' ;

ALTER TABLE `refund_apply`
ADD COLUMN `nickname` VARCHAR(45) NULL COMMENT '用户昵称' AFTER `utime`;

ALTER TABLE `trade_base`
CHANGE COLUMN `ctime` `ctime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ;