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

create table "places" (
  "id" serial primary key,
  "name" varchar,
  "geom" geometry(Point, 4326)
);

insert into "users"("username", "password") values ('hikerbot', 'asdf');
insert into "auth_token"("token", "user_id") values ('token', (select "id" from "users" where "username" = 'hikerbot'))
insert into "places"("name", "geom") values ('Hawk Mountain Shelter', ST_GeomFromText('Point(34.666078 -84.136395)', 4326))

# --- !Downs

drop table "auth_tokens";
drop table "users";
drop table "places";
