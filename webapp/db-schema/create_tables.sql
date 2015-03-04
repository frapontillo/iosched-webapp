CREATE TABLE system_users (
	id serial PRIMARY KEY,
	flags integer,
	human_name varchar(128),
	email varchar(256),
	image varchar(1024)
);

CREATE TABLE user_authentication_information (
	id serial PRIMARY KEY,
	users_id integer references system_users(id),
	authenticator_type integer,
	information varchar(256)
);

CREATE TABLE series (
	id serial PRIMARY KEY,
	parent_id integer references series(id),
	series_name text 
);

CREATE TABLE series_permission (
	id serial PRIMARY KEY,
	series_id integer references series(id),
	users_id integer references system_users(id),
	permission integer
);

CREATE TABLE series_configuration (
	id serial PRIMARY KEY,
	series_id integer references series(id),
	property_name varchar(32),
	property_value text
);

CREATE TABLE location (
	id serial PRIMARY KEY,
	location_name varchar(128),
	address varchar(1024)
);

CREATE TABLE location_links (
	id serial PRIMARY KEY,
	location_id integer references location(id),
	link_name varchar(128),
	link varchar(1024)
);

CREATE TABLE conference (
	id serial PRIMARY KEY,
	series_id integer references series(id),
	location_id integer references location(id),
	conference_name varchar(128),
  hashtag varchar(64),
  timezone varchar(64)
);

CREATE TABLE conference_metadata (
	id serial PRIMARY KEY,
	conference_id integer references conference(id),
	last_update timestamp with time zone,
	last_published timestamp with time zone
);

CREATE TABLE talk_location (
	id serial PRIMARY KEY,
	conference_id integer references conference(id),
	location_name varchar(128),
	address varchar(1024)
);

CREATE TABLE conference_permission (
	id serial PRIMARY KEY,
	conference_id integer references conference(id),
	users_id integer references system_users(id),
	permission integer
);

CREATE TABLE conference_configuration (
	id serial PRIMARY KEY,
	conference_id integer references conference(id),
	property_name varchar(32),
	property_value text
);

CREATE TABLE conference_day (
	id serial PRIMARY KEY,
	conference_id integer references conference(id),
	day date
);

CREATE TABLE track (
	id serial PRIMARY KEY,
	conference_id integer references conference(id),
	colour char(8),
	track_name varchar(1024),
	description varchar(4096)
);

CREATE TABLE talk_slot (
	id serial PRIMARY KEY,
	conference_day integer references conference_day(id),
	slot_start timestamp with time zone,
	slot_end timestamp with time zone,
	event text,
	location_id integer references location(id)
);

CREATE TABLE presenter (
	id serial PRIMARY KEY,
	conference_id integer references conference(id),
  user_id integer references system_users(id),
	name varchar(1024),
	image_url varchar(1024),
	social_link varchar(1024),
	short_biography text,
	long_biography text
);

CREATE TABLE talk (
	id serial PRIMARY KEY,
  type integer,
	conference_id integer references conference(id),
	track_id integer references track(id),
	talk_slot_id integer references talk_slot(id),
	talk_location_id integer references talk_location(id),
	title varchar(256),
	abstract text,
	long_description text,
	information_url text
);

CREATE TABLE talk_permission (
	id serial PRIMARY KEY,
	talk_id integer references talk(id),
	users_id integer references system_users(id),
	permission integer
);

CREATE TABLE voter (
  id serial PRIMARY KEY,
  user_id integer references system_users(id)
);

CREATE TABLE talk_vote (
  id serial PRIMARY KEY,
  talk_id integer references talk(id),
  voter_id integer references voter(id),
  vote integer
);

CREATE TABLE talk_presenter (
	id serial PRIMARY KEY,
	talks_id integer references talk(id),
	presenters_id integer references presenter(id)
);

CREATE TABLE vote_series (
	id serial PRIMARY KEY,
	title varchar(1024),
	description text,
	relation_to integer,
	relation_id integer
);

CREATE TABLE vote_option (
	id serial PRIMARY KEY,
	vote_series_id integer references vote_series(id),
	relation_to integer,
	relation_id integer,
	name varchar(1024)
);

CREATE TABLE vote (
	id serial PRIMARY KEY,
	vote_series_id integer references vote_series(id),
	user_id integer references system_users(id),
	vote_option_id integer references vote_option(id)
);

CREATE TABLE password_reset_requests (
	id serial PRIMARY KEY,
	user_id integer references system_users(id),
	request_timestamp timestamp with time zone,
	secret varchar(1024),
	state integer
);

CREATE TABLE publication_endpoint (
  id serial PRIMARY KEY,
  conference_id integer references conference(id),
  type integer,
  url varchar(1024),
  apikey varchar(1024)
);

CREATE TABLE last_modification_times (
  id serial PRIMARY KEY,
  conference_id integer references conference(id),
  last_modification timestamp with time zone,
  name varchar(1024)
);

CREATE TABLE last_export_details (
  id serial PRIMARY KEY,
  serial_number integer,
  export_type integer,
  conference_id integer references conference(id),
  last_export timestamp with time zone,
  name varchar(1024)
);

CREATE TABLE survey (
  id serial PRIMARY KEY,
  conference_id integer references conference(id),
  apikey text
);

CREATE TABLE survey_question (
  id serial PRIMARY KEY,
  survey_id integer references survey(id),
  type integer,
  position integer,
  question text
);

CREATE TABLE survey_response (
  id SERIAL PRIMARY KEY,
  attendee TEXT
);

CREATE TABLE survey_answer (
  id serial PRIMARY KEY,
  talk_id integer references talk(id),
  question_id integer references survey_question(id),
  response_id integer references survey_response(id),
  answer text
);

create sequence hibernate_sequence;
