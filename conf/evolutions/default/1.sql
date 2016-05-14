# --- !Ups

create table "users" (
    "id" serial primary key,
    "username" varchar not null,
    "password" varchar not null
);


create table "auth_tokens" (
    "token" varchar not null primary key,
    "user_id" bigint not null,
    foreign key ("user_id") references "users"("id")
);

insert into "users"("username", "password") values ('hikerbot', 'asdf');
insert into "auth_token"("token", "user_id") values ('token', (select "id" from "users" where "username" = 'hikerbot'))

# --- !Downs

drop table "auth_tokens";
drop table "users";
