ALTER TABLE order_base ADD is_archive INT(1) DEFAULT 0 NOT NULL COMMENT '是否归档，解绑后设置：0,未归档 1,归档';