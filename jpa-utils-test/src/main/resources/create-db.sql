CREATE TABLE addresses (
	id		INTEGER PRIMARY KEY, 
	street	VARCHAR(100),
	city	VARCHAR(100),
	state	VARCHAR(50)
);

INSERT INTO addresses VALUES (1, '5587 Nunc. Avenue', 'Erie', 'Rhode Island 24975');
INSERT INTO addresses VALUES (2, 'P.O. Box 132 1599 Curabitur Rd.', 'Bandera', 'South Dakota 45149');
INSERT INTO addresses VALUES (3, '6351 Fringilla Avenue', 'Gardena', 'Colorado 37547');
INSERT INTO addresses VALUES (4, 'P.O. Box 686 7014 Amet Street', 'Corona', 'Oklahoma 55246');

CREATE TABLE users (
	id			INTEGER PRIMARY KEY,
	name  		VARCHAR(30),
	email 		VARCHAR(50),
	address_id	INTEGER,
	CONSTRAINT users_address_contstraint FOREIGN KEY(address_id) REFERENCES addresses
);

INSERT INTO users VALUES (1, 'Bob', 'bob@yahoo.com', 3);
INSERT INTO users VALUES (2, 'Martin', 'martin@hotmail.com', 1);
INSERT INTO users VALUES (3, 'Joel', 'joel@gmail.com', 2);

CREATE TABLE groups (
	id		INTEGER PRIMARY KEY,
	name 	VARCHAR(30)
);

INSERT INTO groups VALUES (1, 'Developer');
INSERT INTO groups VALUES (2, 'Requirements Engineer');

CREATE TABLE group_has_users (
	group_id	INTEGER, 
	user_id		INTEGER,
	CONSTRAINT ghu_group_contstraint FOREIGN KEY(group_id) REFERENCES groups,
	CONSTRAINT ghu_user_contstraint FOREIGN KEY(user_id) REFERENCES users
);

INSERT INTO group_has_users VALUES (1, 1);
INSERT INTO group_has_users VALUES (1, 2);
INSERT INTO group_has_users VALUES (2, 2);
INSERT INTO group_has_users VALUES (2, 3);

CREATE TABLE profiles (
	id		INTEGER PRIMARY KEY, 
	data	VARCHAR(1000),
	user_id	INTEGER UNIQUE,
	CONSTRAINT profiles_user_contstraint FOREIGN KEY(user_id) REFERENCES users
);

INSERT INTO profiles VALUES (1, '{sortOrder:[1,4,5,2],dateFormat:"yyyyMMdd HH:mm:ss"}', 3);

CREATE TABLE contacts (
	id		INTEGER PRIMARY KEY, 
	data	VARCHAR(1000),
	user_id	INTEGER UNIQUE,
	CONSTRAINT contacts_user_contstraint FOREIGN KEY(user_id) REFERENCES users
);

INSERT INTO contacts VALUES (1, '{email:[bla@foo,foo@bla],sureName:"Foo",foreignName:"Bla",phone:[{number:"110",mobile:true}]}', 2);
