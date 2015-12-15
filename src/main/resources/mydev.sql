create table contacts (
	id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '����������ID',
	firstName varchar(30) not null,
	lastName varchar(50) not null,
	phoneNumber varchar(13),
	emailAddress varchar(30),
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

create table productorder (
	id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '����������ID',
	userId  BIGINT(20) COMMENT '�û�ID',
	productId  BIGINT(20),
	createTime DATETIME DEFAULT NULL COMMENT '����ʱ��',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

create table product (
	id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '����������ID',
	productType varchar(13) COMMENT '��Ʒ����',
	storageCount BIGINT(10) COMMENT '�����',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '��Ʒ����';