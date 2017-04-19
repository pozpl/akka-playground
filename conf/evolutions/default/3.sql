# Users schema

# --- !Ups

create table if not exists user_individual_subscription(
  `id` INT NOT NULL PRIMARY KEY,
  `user_id` varchar(100) NOT NULL,
  `subscribed_user_id` varchar(100) NOT NULL,
  `position` INT(10) NOT NULL DEFAULT 0,
  CONSTRAINT `uis_user_id_sub_user_id_uc` UNIQUE (user_id, subscribed_user_id),
  CONSTRAINT uis_user_id_fk FOREIGN KEY (user_id) REFERENCES `user`(user_id) ON delete cascade,
  CONSTRAINT uis_subscribed_user_id_fk FOREIGN KEY (subscribed_user_id) REFERENCES `user`(user_id) ON delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# --- !Downs

DROP TABLE user_individual_subscription;

