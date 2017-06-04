CREATE TABLE addresses (
	id		INT,
	street	VARCHAR(100),
	city	VARCHAR(100),
	state	VARCHAR(50),
	PRIMARY KEY (id)
);

INSERT INTO addresses VALUES (1, '5587 Nunc. Avenue', 'Erie', 'Rhode Island 24975');
INSERT INTO addresses VALUES (2, 'P.O. Box 132 1599 Curabitur Rd.', 'Bandera', 'South Dakota 45149');
INSERT INTO addresses VALUES (3, '6351 Fringilla Avenue', 'Gardena', 'Colorado 37547');
INSERT INTO addresses VALUES (4, 'P.O. Box 686 7014 Amet Street', 'Corona', 'Oklahoma 55246');

CREATE TABLE users (
	id			INT,
	name  		VARCHAR(30),
	email 		VARCHAR(50),
	address_id	INT,
	PRIMARY KEY (id),
	CONSTRAINT users_address_contstraint FOREIGN KEY(address_id) REFERENCES addresses(id)
);

INSERT INTO users VALUES (1, 'Bob', 'bob@yahoo.com', 3);
INSERT INTO users VALUES (2, 'Martin', 'martin@hotmail.com', 1);
INSERT INTO users VALUES (3, 'Joel', 'joel@gmail.com', 2);

CREATE TABLE groups (
	id		INT,
	name 	VARCHAR(30),
	PRIMARY KEY (id)
);

INSERT INTO groups VALUES (1, 'Developer');
INSERT INTO groups VALUES (2, 'Requirements Engineer');

CREATE TABLE group_has_users (
	group_id	INT,
	user_id		INT,
	CONSTRAINT ghu_group_contstraint FOREIGN KEY(group_id) REFERENCES groups(id),
	CONSTRAINT ghu_user_contstraint FOREIGN KEY(user_id) REFERENCES users(id)
);

INSERT INTO group_has_users VALUES (1, 1);
INSERT INTO group_has_users VALUES (1, 2);
INSERT INTO group_has_users VALUES (2, 2);
INSERT INTO group_has_users VALUES (2, 3);

CREATE TABLE profiles (
	id		INT,
	data	VARCHAR(1000),
	user_id	INT UNIQUE,
	PRIMARY KEY (id),
	CONSTRAINT profiles_user_contstraint FOREIGN KEY(user_id) REFERENCES users(id)
);

INSERT INTO profiles VALUES (1, '{sortOrder:[1,4,5,2],dateFormat:"yyyyMMdd HH:mm:ss"}', 3);

CREATE TABLE contacts (
	id		INT,
	data	VARCHAR(1000),
	user_id	INT UNIQUE,
	PRIMARY KEY (id),
	CONSTRAINT contacts_user_contstraint FOREIGN KEY(user_id) REFERENCES users(id)
);

INSERT INTO contacts VALUES (1, '{email:[bla@foo,foo@bla],sureName:"Foo",foreignName:"Bla",phone:[{number:"110",mobile:true}]}', 2);
