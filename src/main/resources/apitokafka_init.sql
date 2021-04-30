create table if not exists api_info
(
	id int auto_increment,
	type varchar(255),
	url varchar(255),
	interval_time varchar(255),
	name varchar(255),
	topic_name varchar(255),
	otherparams text,
	status varchar(255),
	current_flag varchar(255),
	constraint api_info_pk
		primary key (id)
);

create table if not exists API_CONFIG
(
	id int auto_increment,
	address varchar(255),
	username varchar(255),
	password varchar(255),
	constraint API_CONFIG_pk
		primary key (id)
);




create table if not exists QRTZ_BLOB_TRIGGERS
(
	SCHED_NAME VARCHAR(120) not null,
	TRIGGER_NAME VARCHAR(200) not null,
	TRIGGER_GROUP VARCHAR(200) not null,
	BLOB_DATA BLOB
);

create table if not exists QRTZ_CALENDARS
(
	SCHED_NAME VARCHAR(120) not null,
	CALENDAR_NAME VARCHAR(200) not null,
	CALENDAR BLOB not null,
	constraint PK_QRTZ_CALENDARS
		primary key (SCHED_NAME, CALENDAR_NAME)
);

create table if not exists QRTZ_FIRED_TRIGGERS
(
	SCHED_NAME VARCHAR(120) not null,
	ENTRY_ID VARCHAR(95) not null,
	TRIGGER_NAME VARCHAR(200) not null,
	TRIGGER_GROUP VARCHAR(200) not null,
	INSTANCE_NAME VARCHAR(200) not null,
	FIRED_TIME BIGINT not null,
	SCHED_TIME BIGINT not null,
	PRIORITY INTEGER not null,
	STATE VARCHAR(16) not null,
	JOB_NAME VARCHAR(200),
	JOB_GROUP VARCHAR(200),
	IS_NONCONCURRENT BOOLEAN,
	REQUESTS_RECOVERY BOOLEAN,
	constraint PK_QRTZ_FIRED_TRIGGERS
		primary key (SCHED_NAME, ENTRY_ID)
);

create table if not exists QRTZ_JOB_DETAILS
(
	SCHED_NAME VARCHAR(120) not null,
	JOB_NAME VARCHAR(200) not null,
	JOB_GROUP VARCHAR(200) not null,
	DESCRIPTION VARCHAR(250),
	JOB_CLASS_NAME VARCHAR(250) not null,
	IS_DURABLE BOOLEAN not null,
	IS_NONCONCURRENT BOOLEAN not null,
	IS_UPDATE_DATA BOOLEAN not null,
	REQUESTS_RECOVERY BOOLEAN not null,
	JOB_DATA BLOB,
	constraint PK_QRTZ_JOB_DETAILS
		primary key (SCHED_NAME, JOB_NAME, JOB_GROUP)
);

create table if not exists QRTZ_LOCKS
(
	SCHED_NAME VARCHAR(120) not null,
	LOCK_NAME VARCHAR(40) not null,
	constraint PK_QRTZ_LOCKS
		primary key (SCHED_NAME, LOCK_NAME)
);

create table if not exists QRTZ_PAUSED_TRIGGER_GRPS
(
	SCHED_NAME VARCHAR(120) not null,
	TRIGGER_GROUP VARCHAR(200) not null,
	constraint PK_QRTZ_PAUSED_TRIGGER_GRPS
		primary key (SCHED_NAME, TRIGGER_GROUP)
);

create table if not exists QRTZ_SCHEDULER_STATE
(
	SCHED_NAME VARCHAR(120) not null,
	INSTANCE_NAME VARCHAR(200) not null,
	LAST_CHECKIN_TIME BIGINT not null,
	CHECKIN_INTERVAL BIGINT not null,
	constraint PK_QRTZ_SCHEDULER_STATE
		primary key (SCHED_NAME, INSTANCE_NAME)
);

create table if not exists QRTZ_TRIGGERS
(
	SCHED_NAME VARCHAR(120) not null,
	TRIGGER_NAME VARCHAR(200) not null,
	TRIGGER_GROUP VARCHAR(200) not null,
	JOB_NAME VARCHAR(200) not null,
	JOB_GROUP VARCHAR(200) not null,
	DESCRIPTION VARCHAR(250),
	NEXT_FIRE_TIME BIGINT,
	PREV_FIRE_TIME BIGINT,
	PRIORITY INTEGER,
	TRIGGER_STATE VARCHAR(16) not null,
	TRIGGER_TYPE VARCHAR(8) not null,
	START_TIME BIGINT not null,
	END_TIME BIGINT,
	CALENDAR_NAME VARCHAR(200),
	MISFIRE_INSTR SMALLINT,
	JOB_DATA BLOB,
	constraint PK_QRTZ_TRIGGERS
		primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
	constraint FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS
		foreign key (SCHED_NAME, JOB_NAME, JOB_GROUP) references QRTZ_JOB_DETAILS
);

create table if not exists QRTZ_CRON_TRIGGERS
(
	SCHED_NAME VARCHAR(120) not null,
	TRIGGER_NAME VARCHAR(200) not null,
	TRIGGER_GROUP VARCHAR(200) not null,
	CRON_EXPRESSION VARCHAR(120) not null,
	TIME_ZONE_ID VARCHAR(80),
	constraint PK_QRTZ_CRON_TRIGGERS
		primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
	constraint FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS
		foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS
			on delete cascade
);

create table if not exists QRTZ_SIMPLE_TRIGGERS
(
	SCHED_NAME VARCHAR(120) not null,
	TRIGGER_NAME VARCHAR(200) not null,
	TRIGGER_GROUP VARCHAR(200) not null,
	REPEAT_COUNT BIGINT not null,
	REPEAT_INTERVAL BIGINT not null,
	TIMES_TRIGGERED BIGINT not null,
	constraint PK_QRTZ_SIMPLE_TRIGGERS
		primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
	constraint FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS
		foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS
			on delete cascade
);

create table if not exists QRTZ_SIMPROP_TRIGGERS
(
	SCHED_NAME VARCHAR(120) not null,
	TRIGGER_NAME VARCHAR(200) not null,
	TRIGGER_GROUP VARCHAR(200) not null,
	STR_PROP_1 VARCHAR(512),
	STR_PROP_2 VARCHAR(512),
	STR_PROP_3 VARCHAR(512),
	INT_PROP_1 INTEGER,
	INT_PROP_2 INTEGER,
	LONG_PROP_1 BIGINT,
	LONG_PROP_2 BIGINT,
	DEC_PROP_1 DECIMAL(13,4),
	DEC_PROP_2 DECIMAL(13,4),
	BOOL_PROP_1 BOOLEAN,
	BOOL_PROP_2 BOOLEAN,
	constraint PK_QRTZ_SIMPROP_TRIGGERS
		primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
	constraint FK_QRTZ_SIMPROP_TRIGGERS_QRTZ_TRIGGERS
		foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references QRTZ_TRIGGERS
			on delete cascade
);

