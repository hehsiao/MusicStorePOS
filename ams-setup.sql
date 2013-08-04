-- -- UNCOMMENT TO DELETE ALL TABLES
-- drop table ITEM cascade constraints;
-- drop table LEADSINGER cascade constraints;
-- drop table HASSONG cascade constraints;
-- drop table CUSTOMER cascade constraints;
-- drop table PURCHASE cascade constraints;
-- drop table PURCHASEITEM cascade constraints;
-- drop table RETURN cascade constraints;
-- drop table RETURNITEM cascade constraints;
-- -- DELETE TABLE CODE ENDS 

-- CREATE Tables
create table Item
	(upc number(10) not null, 
	title varchar(30),
	type varchar(10), 
	category varchar(10),
	company varchar(20), 
	year integer,
	price number(7,2), 
	stock number(4),
    PRIMARY KEY (upc));

create table LeadSinger
	(upc number(10) not null, 
	name varchar(30) not null,
    PRIMARY KEY (upc, name),
	Foreign Key (upc) REFERENCES Item(upc));

create table HasSong
	(upc number(10) not null, 
	title varchar(30) not null,
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
	cid number(10), 
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

-- DUMMY DATA 
INSERT into Item values (1, 'Levels', 'CD', 'House', 'Universal', 2011, '19.99', '10');
INSERT into LeadSinger values (1, ' Avicii');
INSERT into HasSong values (1, 'Levels (original version)');
INSERT into Customer values (1, '1234', 'Matthew', '100 Hastings', '6045551234' );
INSERT into Customer values (2, '2345', 'Risa', '100 Main', '6045551235' );
INSERT into Customer values (3, '1334', 'Henry', '100 Vancouver', '6045551254' );
INSERT into Customer values (4, '2234', 'Shanifer', '100 Burnaby', '604555444' );

