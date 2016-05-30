# --- !Ups

create table "users" (
    "id" serial primary key,
    "username" varchar not null unique,
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

create table "article_revisions" (
  "id" serial primary key,
  "text" varchar not null
);

create table "articles" (
  "id" serial primary key,
  "name" varchar not null,
  "revision_id" bigint not null,
  foreign key ("revision_id") references "article_revisions"("id")
);

-- sample data
with sample_user as (
    insert into "users"("username", "password") values ('hikerbot', 'asdf') returning *
)

insert into "auth_tokens"("token", "user_id") values ('token', (select "id" from sample_user));

insert into "places"("name", "geom") values ('Hawk Mountain Shelter', ST_GeomFromText('Point(34.666078 -84.136395)', 4326));

with sample_revision as (
    insert into "article_revisions"("text") values ('this is some text') returning *
)

insert into "articles"("name", "revision_id") values ('Example', (select "id" from sample_revision));

# --- !Downs

drop table if exists "auth_tokens";
drop table if exists "users";
drop table if exists "places";
drop table if exists "articles";
drop table if exists "article_revisions";
