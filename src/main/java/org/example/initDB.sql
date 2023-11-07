DROP DATABASE IF EXISTS test_library;
CREATE DATABASE test_library;
USE test_library;

CREATE TABLE book
(id BIGINT auto_increment not null unique primary key,
 author varchar(50) not null,
 title varchar(50) not null,
 publishedDate Date unique not null);
