-- To create the database:
--   CREATE DATABASE blog;
--   GRANT ALL PRIVILEGES ON blog.* TO 'blog'@'localhost' IDENTIFIED BY 'blog';
--
-- To reload the tables:
--   mysql --user=blog --password=blog --database=blog < schema.sql
-- mysql --user=root --password=13961000804 --database=web_test < schema.sql


SET SESSION storage_engine='InnoDB';
ALTER DATABASE CHARACTER SET 'utf8';

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS blog;
DROP TABLE IF EXISTS chat;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS blog_click_rate;

CREATE TABLE users(
  id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(20) NOT NULL,
  regtime VARCHAR(30) NOT NULL
);


CREATE TABLE blog(
  id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(20)NOT NULL,
  title VARCHAR(50)NOT NULL,
  blog TEXT NOT NULL,
  time VARCHAR(30),
  click_rate INT UNSIGNED NOT NULL DEFAULT 0
);


CREATE TABLE chat(
  id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  content TEXT NOT NULL,
  time VARCHAR(30)
);


CREATE TABLE comment(
  id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  blog_id SMALLINT NOT NULL,
  name VARCHAR(20) NOT NULL,
  content TEXT NOT NULL,
  time VARCHAR(30)
);

DROP TABLE IF EXISTS blog_click_rate;
CREATE TABLE blog_click_rate(
  blog_id SMALLINT UNSIGNED NOT NULL,
  click_rate INT UNSIGNED NOT NULL,
  FOREIGN KEY(blog_id) REFERENCES blog(id)
);
