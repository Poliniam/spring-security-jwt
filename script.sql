create database IF NOT EXISTS TraderSystem;
use TraderSystem;

create table role_table
(
 id int primary key auto_increment,
 name varchar(20)
);

insert into role_table(name) values ('ADMIN');
insert into role_table(name) values ('ROLE_USER');
insert into role_table(name) values ('ANONIM');

create table user_table
(
 id int primary key auto_increment,
 login varchar(500),
 password varchar(5000),
 activationCode varchar(5000),
 role_id int,
 email varchar(500),
 FOREIGN KEY (role_id)  REFERENCES role_table (id)
);

добавить админа

 create table game_table
 (
  id int primary key auto_increment,
  gameName varchar(500)
 );

 create table gameObject_table
(
 id int primary key auto_increment,
 title VARCHAR(50),
 text TEXT,
 status TINYINT default false,
 author_id int,
 created_at DATE,
 updated_at DATE,
 game_id int,
 FOREIGN KEY (author_id)  REFERENCES user_table (id),
 FOREIGN KEY (game_id)  REFERENCES game_table (id)
);

create table comment_table
(
 id int primary key auto_increment,
 message TEXT,
 created_at DATE,
 author_id int,
 post_id int,
 rate int,
 approved TINYINT default false,
 FOREIGN KEY (author_id)  REFERENCES user_table (id),
 FOREIGN KEY (post_id)  REFERENCES gameObject_table (id)
);