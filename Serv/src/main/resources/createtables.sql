create table users (
id varchar(255) not null,
login varchar(255) not null,
password varchar(255) not null)

create table user_session (
id varchar(255) not null,
session varchar(255) null
)
alter table users 
add constraint PK_users
primary key(id)
alter table users
add constraint UNQ_users
unique(id)
alter table user_session
add constraint PK_user_session
primary key(id)
alter table user_session
add constraint FK_users_user_session
foreign key(id)
references user_session

--drop table user_session
--drop table users

