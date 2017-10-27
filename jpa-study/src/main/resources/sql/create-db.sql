create table member (
	id varchar(255) not null,
	name varchar(255),
	age integer,
	roleType varchar(255),
	createdDate timestamp,
	lastModifiedDate timestamp,
	description clob,
	primary key (id)
);