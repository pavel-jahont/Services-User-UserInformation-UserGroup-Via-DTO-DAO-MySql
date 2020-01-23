CREATE DATABASE jd2_datasource;
CREATE TABLE user_group
(
id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL
);
CREATE TABLE user
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    username      VARCHAR(60) NOT NULL,
    password      VARCHAR(60) NOT NULL,
    age           int         NOT NULL,
    is_active     BOOLEAN     NOT NULL DEFAULT TRUE,
    user_group_id int         NOT NULL,
FOREIGN KEY (user_group_id) REFERENCES user_group (id)
);
CREATE TABLE user_information
(
    user_id   INT PRIMARY KEY,
    address   VARCHAR(60) NOT NULL,
    telephone VARCHAR(60) NOT NULL,
FOREIGN KEY (user_id) REFERENCES user (id)
);