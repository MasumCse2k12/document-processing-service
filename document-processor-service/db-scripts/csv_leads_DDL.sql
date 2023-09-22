drop table if exists lead;

create table  lead  (
	id serial not null primary key,
	email varchar(30) UNIQUE,
	phone varchar(20),
	job_id integer,
	version bigint not null,
	created timestamp not null,
	updated timestamp
);

drop table if exists temp_lead;

create table  temp_lead  (
	id serial not null primary key,
	email varchar(30),
	phone varchar(20),
	job_id integer,
	reason text,
	error text,
	version bigint not null,
	created timestamp not null,
	updated timestamp
);


drop table if exists error_lead;

create table  error_lead  (
	id serial not null primary key,
	job_id integer,
	data text,
	error_desc text,
	version bigint not null,
	created timestamp not null,
	updated timestamp
);

drop table if exists batch_job;

create table  batch_job  (
	id serial not null primary key,
	job_name varchar(100),
	data_source varchar(100),
	user_data_source varchar(100),
	job_id integer,
	source_type integer,
	total_records integer,
	version bigint not null,
	created timestamp not null,
	updated timestamp
);