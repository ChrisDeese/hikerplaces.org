# --- !Ups

create table "user" (
    "id" serial primary key,
    "username" varchar not null,
    "password" varchar not null
);


create table "auth_token" (
    "token" varchar not null primary key,
    "user_id" bigint not null,
    foreign key ("user_id") references "user"("id")
);

insert into "user"("username", "password") values ('hikerbot', 'asdf');
insert into "auth_token"("token", "user_id") values ('token', (select "id" from "user" where "username" = 'hikerbot'))

# --- !Downs

drop table "auth_token";
drop table "user";
