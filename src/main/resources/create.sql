DROP DATABASE IF EXISTS product_manage_system;
-- 创建数据库
CREATE DATABASE product_manage_system DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 使用数据库
use product_manage_system;


CREATE TABLE IF NOT EXISTS SPRING_SESSION (
                                              PRIMARY_ID CHAR(36) NOT NULL,
                                              SESSION_ID CHAR(36) NOT NULL,
                                              CREATION_TIME BIGINT NOT NULL,
                                              LAST_ACCESS_TIME BIGINT NOT NULL,
                                              MAX_INACTIVE_INTERVAL INT NOT NULL,
                                              EXPIRY_TIME BIGINT NOT NULL,
                                              PRINCIPAL_NAME VARCHAR(100),
                                              CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX  SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX  SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX  SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES (
                                                         SESSION_PRIMARY_ID CHAR(36) NOT NULL,
                                                         ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
                                                         ATTRIBUTE_BYTES BLOB NOT NULL,
                                                         CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
                                                         CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;


-- 创建一个图片表，用blob
CREATE TABLE image (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       content LONGBLOB NOT NULL
);


-- 创建商品表
CREATE TABLE product (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description LONGTEXT,
                         image_id INT,
                         price DECIMAL(10, 2) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (image_id) REFERENCES image(id)
);

-- 创建款式表
CREATE TABLE style (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       description TEXT,
                       image_id INT,
                       product_id INT,
                       FOREIGN KEY (image_id) REFERENCES image(id),
                       FOREIGN KEY (product_id) REFERENCES product(id)
);

-- 创建用户表
CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      email VARCHAR(100) NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建日志表
CREATE TABLE log (
                     id INT AUTO_INCREMENT PRIMARY KEY,
                     user_id INT NOT NULL,
                     action VARCHAR(100) NOT NULL,
                     description VARCHAR(255),
                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 创建分类表
CREATE TABLE category (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL
);

-- 创建商品与分类的关联表
CREATE TABLE product_category (
                                  product_id INT NOT NULL,
                                  category_id INT NOT NULL,
                                  PRIMARY KEY (product_id, category_id),
                                  FOREIGN KEY (product_id) REFERENCES product(id),
                                  FOREIGN KEY (category_id) REFERENCES category(id)
);
-- 添加数据给category，内容是食物的种类，多整点40条数据，也可以包括菜系等等，只要能对食物进行分类就行
INSERT INTO category (name) VALUES ('饮料'),
                                   ('主食'),
                                   ('小吃'),
                                   ('水果'),
                                   ('蔬菜'),
                                   ('肉类'),
                                   ('海鲜'),
                                   ('奶制品'),
                                   ('糕点'),
                                   ('零食'),
                                   ('酒类'),
                                   ('调味品');

