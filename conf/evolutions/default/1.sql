# --- !Ups

create table user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id VARCHAR(100) NOT NULL,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  full_name VARCHAR(200),
  email VARCHAR(256),
  avatar_url VARCHAR(256),
  PRIMARY KEY(id),
  KEY(user_id)
);


create table login_info (
  id bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  provider_id VARCHAR(256) NOT NULL,
  provider_key VARCHAR(256) NOT NULL
);

create table user_login_info (
  user_id VARCHAR(100) NOT NULL,
  login_info_id BIGINT NOT NULL,
  CONSTRAINT uli_user_id_fk FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
  CONSTRAINT uli_login_info_id_fk FOREIGN KEY (login_info_id) REFERENCES login_info (id) ON DELETE CASCADE
);

create table password_info (
  id bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY ,
  hasher VARCHAR(100) NOT NULL,
  password VARCHAR(256) NOT NULL,
  salt VARCHAR(100),
  login_info_id BIGINT(20) NOT NULL,
  CONSTRAINT pi_login_info_id_fk FOREIGN KEY (login_info_id) REFERENCES login_info (id) ON DELETE CASCADE
);


create table oauth1_info (
  id bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  token VARCHAR(256) NOT NULL,
  secret VARCHAR(256) NOT NULL,
  login_info_id BIGINT(20) NOT NULL,
  CONSTRAINT o1i_login_info_id_fk FOREIGN KEY (login_info_id) REFERENCES login_info(id) ON DELETE CASCADE
);


create table oauth2_info (
  id bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  accesstoken VARCHAR(256) NOT NULL,
  tokentype VARCHAR(100),
  expiresin INTEGER,
  refreshtoken VARCHAR(256),
  login_info_id BIGINT(20) NOT NULL,
  CONSTRAINT o2i_login_info_id_fk FOREIGN KEY (login_info_id) REFERENCES login_info(id) ON DELETE CASCADE
);

create table openid_info (
  id VARCHAR(256) NOT NULL PRIMARY KEY,
  login_info_id BIGINT(20) NOT NULL,
  CONSTRAINT opid_login_info_id_fk FOREIGN KEY (login_info_id) REFERENCES login_info(id) ON DELETE CASCADE
);
create table openid_attributes (
  id VARCHAR(256) NOT NULL,
  `key` VARCHAR(256) NOT NULL,
  `value` VARCHAR(256) NOT NULL
);


# --- !Downs

drop table openid_attributes;
drop table openid_info;
drop table oauth2_info;
drop table oauth1_info;
drop table password_info;
drop table user_login_info;
drop table login_info;
drop table user;