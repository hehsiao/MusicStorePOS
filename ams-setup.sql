create table Item
	(upc number(10) not null,
	title varchar(20) not null,
	type varchar(10),
	category varchar(10),
	company varchar(20),
	year integer,
	price number(7,2),
	stock number(4),
    PRIMARY KEY (upc));

create table LeadSinger
	(upc number(10) not null,
	name varchar(20) not null,
    PRIMARY KEY (upc, name),
	Foreign Key (upc) REFERENCES Item(upc));

create table HasSong
	(upc number(10) not null,
	title varchar(15) not null,
	PRIMARY KEY (upc, title),
	Foreign Key (upc) references Item(upc));

create table Customer
	(cid number(10) not null,
	password varchar(8),
	name varchar(20),
	address varchar(20),
	phone varchar(10),
	PRIMARY KEY (cid));

create table Purchase
	(receiptId integer not null,
	pdate DATE default(sysdate),
	cid number(10) ,
	cardnum varchar(16),
	expiryDate varchar(5),
	expectedDate DATE,
	deliveredDate DATE,
    PRIMARY KEY (receiptId),
    Foreign Key (cid) REFERENCES Customer);

create table PurchaseItem
	(receiptId number(15) not null,
	upc number(10) not null,
	quantity number(4),
    PRIMARY KEY (receiptId, upc),
	Foreign Key (receiptId) REFERENCES Purchase,
	Foreign Key (upc) REFERENCES Item);

create table Return
	(retid number(15) not null,
	retdate DATE default (sysdate),
	receiptId number(15),
	Primary Key (retid),
	Foreign Key (receiptId) REFERENCES Purchase);

create table ReturnItem
	(retid number(15) not null,
	upc number(10) not null,
	quantity number(4),
	PRIMARY KEY (retid, upc),
	FOREIGN KEY (upc) REFERENCES Item);