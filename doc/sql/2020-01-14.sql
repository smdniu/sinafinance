#账户表新增账户状态
ALTER TABLE tb_account ADD state  varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '账户状态 //normal 1:正常  freeze 2:冻结' after account;
