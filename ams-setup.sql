drop table Item;

create table Item
	(upc integer not null PRIMARY KEY,
	title varchar2(20),
	type varchar2(10),
	category varchar2(10),
	company varchar2(20),
	year integer,
	price integer,
	stock integer);

drop table LeadSinger;

create table LeadSinger
	(upc integer not null PRIMARY KEY,
	name varchar2(20) not null PRIMARY KEY,
	Foreign Key (LeadSinger_upc) REFERENCES Item);

drop table HasSong;

create table HasSong
	(upc integer not null PRIMARY KEY,
	title varchar2(15) PRIMARY KEY
	PRIMARY KEY (upc, title),
	Foreign Key (upc) references Item);

drop table Purchase;

create table Purchase
	(receiptId integer not null PRIMARY KEY,
	pdate integer,
	cid integer FOREIGN KEY REFERENCES Customer,
	cardnum integer,
	expiryDate integer,
	expectedDate integer,
	deliveredDate integer);

drop table PurchaseItem;

create table PurchaseItem
	(receiptId integer not null PRIMARY KEY,
	upc integer not null PRIMARY KEY,
	quantity integer
	Foreign Key (receiptId) REFERENCES Purchase
	Foreign Key (upc) REFERENCES Item);

drop table Customer;

create table Customer
	(cid integer not null,
	password varchar(8),
	name varchar2(20),
	address varchar2(20),
	phone integer,
	PRIMARY KEY (cid));

drop table Return;

create table Return
	(retid integer not null,
	retdate integer,
	receiptId integer,
	Primary Key (retid),
	Foreign Key (receiptId) REFERENCES Purchase);

drop table ReturnItem;

create table ReturnItem
	(retid integer not null,
	upc integer not null,
	quantity integer,
	PRIMARY KEY (retid, upc),
	FOREIGN KEY (upc) REFERENCES Item);


