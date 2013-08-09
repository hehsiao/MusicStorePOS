-- -- UNCOMMENT TO DELETE ALL TABLES
drop table ITEM cascade constraints;
drop table LEADSINGER cascade constraints;
drop table HASSONG cascade constraints;
drop table CUSTOMER cascade constraints;
drop table PURCHASE cascade constraints;
drop table PURCHASEITEM cascade constraints;
drop table RETURN cascade constraints;
drop table RETURNITEM cascade constraints;
-- -- DELETE TABLE CODE ENDS 

drop sequence return_sequence;
drop sequence purchase_sequence;

drop TRIGGER return_trigger;
drop TRIGGER purchase_trigger;

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
	Foreign Key (upc) REFERENCES Item(upc) on DELETE cascade);

create table HasSong
	(upc number(10) not null, 
	title varchar(30) not null,
	PRIMARY KEY (upc, title),
	Foreign Key (upc) references Item(upc) on DELETE cascade);

create table Customer
	(cid varchar(8), 
	password varchar(8),
	name varchar(20), 
	address varchar(20),
	phone varchar(10), 
	PRIMARY KEY (cid));

create table Purchase
	(receiptId integer, 
	pdate DATE default(sysdate),
	cid varchar(8), 
	cardnum char(16),
	expiryDate char(5), 
	expectedDate DATE,
	deliveredDate DATE, 
	PRIMARY KEY (receiptId),
    Foreign Key (cid) REFERENCES Customer);

create table PurchaseItem
	(receiptId integer,
	upc number(10) not null,
	quantity number(4), 
	PRIMARY KEY (receiptId, upc),
	Foreign Key (receiptId) REFERENCES Purchase ON DELETE cascade,
	Foreign Key (upc) REFERENCES Item
);

create table Return
	(retid number(15),
	retdate DATE default (sysdate),
	receiptId number(15), 
	Primary Key (retid),
	Foreign Key (receiptId) REFERENCES Purchase);

create table ReturnItem
	(retid number(15) ,
	upc number(10) not null,
	quantity number(4),	
	PRIMARY KEY (retid, upc),
	FOREIGN KEY (upc) REFERENCES Item,
	FOREIGN KEY (retid) REFERENCES Return ON DELETE cascade);
	
	
CREATE SEQUENCE return_sequence
START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE purchase_sequence
START WITH 1 INCREMENT BY 1;


CREATE OR REPLACE TRIGGER return_trigger
BEFORE INSERT
ON Return
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT return_sequence.nextval INTO :NEW.RETID FROM dual;
END;
/	

CREATE OR REPLACE TRIGGER purchase_trigger
BEFORE INSERT
ON Purchase
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT purchase_sequence.nextval INTO :NEW.RECEIPTID FROM dual;
END;	
/



