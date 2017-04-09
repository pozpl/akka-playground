# Users schema

# --- !Ups

CREATE TABLE  IF NOT EXISTS messages(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  from_uid  varchar(255) NOT NULL,
  to_segment varchar(255) NOT NULL,
  to_target varchar(255),
  `date` TIMESTAMP,
  text TEXT,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE messages;