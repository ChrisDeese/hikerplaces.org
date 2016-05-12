# --- !Ups

create table "USER" (
    "ID" serial primary key,
    "USERNAME" varchar not null,
    "PASSWORD" varchar not null
);


create table "AUTH_TOKEN" (
    "TOKEN" varchar not null primary key,
    "USER_ID" bigint not null,
    foreign key ("USER_ID") references "USER"("ID")
);

insert into "USER"("USERNAME", "PASSWORD") values ('hikerbot', 'asdf');
insert into "AUTH_TOKEN"("TOKEN", "USER_ID") values ('token', (select "ID" from "USER" where "USERNAME" = 'hikerbot'))

# --- !Downs

drop table "USER";
drop table "AUTH_TOKEN";
