use luttu; # Byt till eget användarnamn

# Om det finns en tidigare databas
drop table room;
drop table message;
drop table user;
drop table decals;

create table room (
	room_id int NOT NULL AUTO_INCREMENT,
	room_name varchar(64),
	max_users int,
	PRIMARY KEY (room_id)
);

create table message (
	message_id int NOT NULL AUTO_INCREMENT,
	room_id int,
	user_id int,
	time_sent varchar(20),
	message_type varchar(64),
	payload tinytext,
	PRIMARY KEY (message_id)
);

create table user (
	user_id int NOT NULL AUTO_INCREMENT,
	user_name varchar(64),
	user_email varchar(64),
	pass_hash varchar(64),
	PRIMARY KEY (user_id)
);

create table decals (
	decal_id int NOT NULL AUTO_INCREMENT,
	format varchar(64),
	img_binary mediumblob,
	added_by int,
	PRIMARY KEY (decal_id)
);

insert into decals (format, img_binary, added_by) values ('png', LOAD_FILE('/var/lib/mysql-files/decals/horse.bmp'), 1);
insert into decals (format, img_binary, added_by) values ('png', LOAD_FILE('/var/lib/mysql-files/decals/beer.bmp'), 1);
insert into decals (format, img_binary, added_by) values ('png', LOAD_FILE('/var/lib/mysql-files/decals/cat.bmp'), 1);
insert into decals (format, img_binary, added_by) values ('png', LOAD_FILE('/var/lib/mysql-files/decals/cupcake.bmp'), 1);
insert into decals (format, img_binary, added_by) values ('png', LOAD_FILE('/var/lib/mysql-files/decals/ghost.bmp'), 1);
insert into decals (format, img_binary, added_by) values ('png', LOAD_FILE('/var/lib/mysql-files/decals/rabbit.bmp'), 1);

insert into room(room_name, max_users) values ('#ducktales', 10);
insert into room(room_name, max_users) values ('#memes', 10);
insert into room(room_name, max_users) values ('#history', 10);
insert into room(room_name, max_users) values ('#cooking', 10);

insert into user(user_name, user_email, pass_hash) values ('Johan', 'luttu@kth.se', '688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6');
insert into user(user_name, user_email, pass_hash) values ('Camilla', 'camahl@kth.se', '688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6');

insert into message(room_id, user_id, time_sent, message_type, payload) values (1, 1, DATE_FORMAT(NOW(),'%d %b %h:%i %p'), 'text', 'hej');
insert into message(room_id, user_id, time_sent, message_type, payload) values (1, 2, DATE_FORMAT(NOW(),'%d %b %h:%i %p'), 'text', 'hallo!!');



SELECT * FROM message
