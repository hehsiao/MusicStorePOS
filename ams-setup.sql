create table Item
	(upc integer not null PRIMARY KEY,
	title varchar(20),
	type varchar(10),
	category varchar(10),
	company varchar(20),
	year integer,
	price integer,
	stock integer);

create table LeadSinger
	(upc integer not null,
	name varchar(20) not null,
    PRIMARY KEY (upc, name),
	Foreign Key (upc) REFERENCES Item);

create table HasSong
	(upc integer not null,
	title varchar(15) not null,
	PRIMARY KEY (upc, title),
	Foreign Key (upc) references Item);

create table Purchase
	(receiptId integer not null PRIMARY KEY,
	pdate integer,
	cid integer ,
	cardnum integer,
	expiryDate integer,
	expectedDate integer,
	deliveredDate integer,
    Foreign Key (cid) REFERENCES Customer));

create table PurchaseItem
	(receiptId integer not null,
	upc integer not null,
	quantity integer
    PRIMARY KEY (receiptId, upc),
	Foreign Key (receiptId) REFERENCES Purchase
	Foreign Key (upc) REFERENCES Item);

create table Customer
	(cid integer not null,
	password varchar(8),
	name varchar(20),
	address varchar(20),
	phone integer,
	PRIMARY KEY (cid));

create table Return
	(retid integer not null,
	retdate integer,
	receiptId integer,
	Primary Key (retid),
	Foreign Key (receiptId) REFERENCES Purchase);

create table ReturnItem
	(retid integer not null,
	upc integer not null,
	quantity integer,
	PRIMARY KEY (retid, upc),
	FOREIGN KEY (upc) REFERENCES Item);