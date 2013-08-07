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

drop sequence test_sequence1;
drop sequence test_sequence2;
drop sequence test_sequence3;
drop sequence test_sequence4;
drop sequence test_sequence5;

drop trigger test_trigger1;
drop trigger test_trigger2;
drop trigger test_trigger3;
drop trigger test_trigger4;
--drop trigger test_trigger5;


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
	(cid number(10), 
	password varchar(8),
	name varchar(20), 
	address varchar(20),
	phone varchar(10), 
	PRIMARY KEY (cid));

create table Purchase
	(receiptId integer, 
	pdate DATE default(sysdate),
	cid number(10), 
	cardnum number(16),
	expiryDate DATE, 
	expectedDate DATE,
	deliveredDate DATE, 
	PRIMARY KEY (receiptId),
    Foreign Key (cid) REFERENCES Customer);

create table PurchaseItem
	(receiptId integer,
	upc number(10) not null,
	quantity number(4), 
	PRIMARY KEY (receiptId, upc),
	Foreign Key (receiptId) REFERENCES Purchase,
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
	FOREIGN KEY (retid) REFERENCES Return);
	
	
CREATE SEQUENCE test_sequence1
START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER test_trigger1
BEFORE INSERT
ON Return
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT test_sequence1.nextval INTO :NEW.RETID FROM dual;
END;
/	

CREATE SEQUENCE test_sequence2
START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER test_trigger2
BEFORE INSERT
ON Purchase
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT test_sequence2.nextval INTO :NEW.RECEIPTID FROM dual;
END;	
/


CREATE SEQUENCE test_sequence4
START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER test_trigger4
BEFORE INSERT
ON ReturnItem
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT test_sequence4.nextval INTO :NEW.retID FROM dual;
END;	
/

-- CREATE SEQUENCE test_sequence5
-- START WITH 1 INCREMENT BY 1;

-- CREATE OR REPLACE TRIGGER test_trigger5
-- BEFORE INSERT
-- ON PurchaseItem
-- REFERENCING NEW AS NEW
-- FOR EACH ROW
-- BEGIN
-- SELECT test_sequence5.nextval INTO :NEW.receiptID FROM dual;
-- END;	
-- /


-- DUMMY DATA 
INSERT into Item values (1, 'Levels', 'CD', 'House', 'Universal', 2011, '19.99', '10');
INSERT into LeadSinger values (1, 'Avicii');
INSERT into HasSong values (1, 'Levels (original version)');
INSERT into Item values (2, 'Random Access Memories', 'CD', 'Electric', 'Daft Life', 2012, '19.99', '10');
INSERT into LeadSinger values (2, 'Daft Punk');
INSERT into HasSong values (2, 'Get Lucky');
INSERT into HasSong values (2, 'One More Time');
INSERT into Customer(cid,password,name,address,phone) values (1,'1234', 'Matthew', '100 Hastings', '6045551234' );
INSERT into Customer(cid,password,name,address,phone) values (2, '2345', 'Risa', '100 Main', '6045551235' );
INSERT into Customer(cid,password,name,address,phone) values (3, '1334', 'Henry', '100 Vancouver', '6045551254' );
INSERT into Customer(cid,password,name,address,phone) values (4, '2234', 'Shanifer', '100 Burnaby', '604555444' );

