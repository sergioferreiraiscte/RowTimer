
DROP TABLE IF EXISTS rowing_event;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS race;
DROP TABLE IF EXISTS alignment;
DROP TABLE IF EXISTS competitor;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS crew;
DROP TABLE IF EXISTS crew_member;
DROP TABLE IF EXISTS alignment;
DROP TABLE IF EXISTS boat_type;

CREATE TABLE rowing_event (
	id VARCHAR(128) PRIMARY KEY,
	name VARCHAR(128),
	event_date TIMESTAMP,
     location VARCHAR(128)
);

CREATE TABLE category (
	id VARCHAR(20) NOT NULL PRIMARY KEY,
	name VARCHAR(128),
	gender CHAR(1)
);

CREATE TABLE boat_type (
  boat_id CHAR(10) NOT NULL PRIMARY KEY,
  name VARCHAR(128)
);

CREATE TABLE race (
  event_id VARCHAR(128) REFERENCES rowing_event (id),
  seqno INTEGER,
  hour TIMESTAMP,
  boat_type CHAR(10) REFERENCES boat_type(boat_id),
  category VARCHAR(20) REFERENCES category(id),
  PRIMARY KEY (event_id,seqno)
);

CREATE TABLE competitor (
   id VARCHAR(10) NOT NULL PRIMARY KEY,
   name VARCHAR(128),
   acronym VARCHAR(10)
);

CREATE TABLE person (
  id VARCHAR(20) NOT NULL PRIMARY KEY,
  name VARCHAR(128) NOT NULL
);

CREATE TABLE crew (
  id VARCHAR(20) NOT NULL PRIMARY KEY,
  competitor_id VARCHAR(10) REFERENCES competitor(id),
  description VARCHAR(128)
);

CREATE TABLE crew_member (
  crew_id VARCHAR(10) NOT NULL,
  person_id VARCHAR(20) NOT NULL REFERENCES crew(id),
  PRIMARY KEY (crew_id,person_id)
);

CREATE TABLE alignment (
  event_id VARCHAR(128) REFERENCES rowing_event (id),
  race_no INTEGER, 
  lane INTEGER,
  crew VARCHAR(20) REFERENCES crew(id)
);
