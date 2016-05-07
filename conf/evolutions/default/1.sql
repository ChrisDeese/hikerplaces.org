# --- !Ups

create table "USER" (
    "ID" bigint auto_increment primary key,
    "USERNAME" varchar not null,
    "PASSWORD" varchar not null
);

insert into USER(USERNAME, PASSWORD) values ('hikerbot', 'asdf');

# --- !Downs

drop table "USER";
