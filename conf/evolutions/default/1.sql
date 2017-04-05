# Users schema

# --- !Ups

CREATE TABLE IF NOT EXISTS users (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  uid varchar(255) NOT NULL,
  login varchar(255) NOT NULL,
  mobile BIGINT(11),
  email varchar(255),
  PRIMARY KEY (id)
);


CREATE TABLE  IF NOT EXISTS messages(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  from_uid  varchar(255) NOT NULL,
  to_segment varchar(255) NOT NULL,
  to_target varchar(255),
  `date` TIMESTAMP,
  text TEXT
);

# --- !Downs

DROP TABLE users;
DROP TABLE messages;