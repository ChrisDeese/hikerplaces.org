# --- !Ups

create table "USER" (
    "ID" bigint auto_increment primary key,
    "USERNAME" varchar not null,
    "PASSWORD" varchar not null
);

create table "AUTH_TOKEN" (
    "TOKEN" varchar not null primary key,
    "USER_ID" bigint not null,
    foreign key (USER_ID) references USER(ID)
);

insert into USER(USERNAME, PASSWORD) values ('hikerbot', 'asdf');

# --- !Downs

drop table "USER";
