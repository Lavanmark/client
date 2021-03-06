drop table if exists user;
drop table if exists project;
drop table if exists field;
drop table if exists batch;
drop table if exists record;


create table user
(
	id integer not null primary key autoincrement,
	username text unique not null,
	password text not null,
	firstName text not null,
	lastName text not null,
	email text unique not null,
	recordsIndexed integer,
	currentBatch integer
);

--top of chain 
create table project
(
	key integer not null primary key autoincrement,
	title text unique not null,	
	recordsPerImage integer,
	firstYCoord integer,
	recordHeight integer
);

--holds project key and record key and order for record and required info
create table field
(
	id integer not null primary key autoincrement,
	projectKey integer not null,
	recordOrder integer not null,
	title text not null,
	xCoord integer not null,
	width integer not null,
	helpHtml text not null,
	knownData text
);

--holds id for records, project key to link image to fields and overarching project
create table batch
(
	id integer not null primary key autoincrement,	
	projectKey integer not null,
	file text unique not null,
	status integer not null
);

--record just holds number and then can find its respected fields in field table
--also holds project and image key so that records can be connected to those respectively
create table record
(
	recordNum integer not null primary key autoincrement,
	projectKey integer not null,
	imageKey integer not null,
	fieldKey integer not null,
	data text
);
