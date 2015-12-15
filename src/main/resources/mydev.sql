create table contacts (
	id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '自增长类型ID',
	firstName varchar(30) not null,
	lastName varchar(50) not null,
	phoneNumber varchar(13),
	emailAddress varchar(30),
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

create table productorder (
	id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '自增长类型ID',
	userId  BIGINT(20) COMMENT '用户ID',
	productId  BIGINT(20),
	createTime DATETIME DEFAULT NULL COMMENT '创建时间',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

create table product (
	id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '自增长类型ID',
	productType varchar(13) COMMENT '商品类型',
	storageCount BIGINT(10) COMMENT '库存量',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '商品类型';