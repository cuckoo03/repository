create table member (
	member_id varchar(255) not null,
	team_id varchar(255),
	username varchar(255),
	primary key (member_id)
);
create table team (
	team_id varchar(255) not null,
	name varchar(255),
	primary key (team_id)
);
alter table meber add constraint fk_member_team
