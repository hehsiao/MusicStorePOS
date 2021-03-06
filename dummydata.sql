-- DUMMY DATA 
INSERT into Item values (1, 'Levels', 'CD', 'House', 'Universal', 2011, '19.99', '10');
INSERT into LeadSinger values (1, 'Avicii');
INSERT into HasSong values (1, 'Levels (original version)');
INSERT into Item values (2, 'Random Access Memories', 'CD', 'Electric', 'Daft Life', 2012, '19.99', '10');
INSERT into LeadSinger values (2, 'Daft Punk');
INSERT into HasSong values (2, 'Get Lucky');
INSERT into HasSong values (2, 'Instant Crush');
INSERT into Item values (3, 'Tubthumper', 'CD', 'Postpunk', 'Universal', 1997, '9.99', '20');
INSERT into LeadSinger values (3, 'Chumbawamba');
INSERT into HasSong values (3, 'Tubthumping');
INSERT into HasSong values (3, 'I Want More');
INSERT into Item values (4, 'Plans', 'CD', 'IndieRock', 'Atlantic', 2005, '10.99', '30');
INSERT into LeadSinger values (4, 'Death Cab for Cutie');
INSERT into HasSong values (4, 'Soul Meets Body');
INSERT into HasSong values (4, 'Crooked Teeth');
INSERT into Item values (5, 'Phantom of the Opera', 'DVD', 'Musical', 'Warner Bros.', 2004, '9.99', '50');
INSERT into LeadSinger values (5, 'Andrew Lloyd Webber');
INSERT into HasSong values (5, 'Music of the Night');
INSERT into HasSong values (5, 'Point of No Return');
INSERT into Item values (6, 'Les Miserables', 'DVD', 'Musical', 'Universal', 2012, '15.99', '40');
INSERT into LeadSinger values (6, 'Herbert Kretzmer');
INSERT into HasSong values (6, 'I Dreamed a Dream');
INSERT into HasSong values (6, 'The Confrontation');
INSERT into Item values (7, 'Moulin Rouge', 'DVD', 'Musical', '20th Century Fox', 2001, '9.99', '60');
INSERT into LeadSinger values (7, 'Baz Luhrmann');
INSERT into HasSong values (7, 'Your Song');
INSERT into HasSong values (7, 'Come What May');
INSERT into Item values (8, 'Tommy', 'DVD', 'Rock', 'Columbia', 1975, '9.99', '15');
INSERT into LeadSinger values (8, 'The Who');
INSERT into HasSong values (8, 'The Acid Queen');
INSERT into HasSong values (8, 'Pinball Wizard');
INSERT into Item values (9, 'The Sound of Music', 'DVD', 'Musical', '20th Century Fox', 1970, '9.99', '50');
INSERT into LeadSinger values (9, 'Richard Rodgers');
INSERT into HasSong values (9, 'Edelweiss');
INSERT into HasSong values (9, 'My Favorite Things');
INSERT into Item values (10, 'Cats', 'DVD', 'Musical', 'Universal', 1990, '9.99', '40');
INSERT into LeadSinger values (10, 'Andrew Lloyd Webber');
INSERT into HasSong values (10, 'Memories');
INSERT into HasSong values (10, 'Macavity');
INSERT into Customer(cid,password,name,address,phone) values (1,'1234', 'Matthew', '100 Hastings', '6045551234' );
INSERT into Customer(cid,password,name,address,phone) values (2, '2345', 'Risa', '100 Main', '6045551235' );
INSERT into Customer(cid,password,name,address,phone) values (3, '1334', 'Henry', '100 Vancouver', '6045551254' );
INSERT into Customer(cid,password,name,address,phone) values (4, '2234', 'Shanifer', '100 Burnaby', '604555444' );
INSERT into Customer(cid,password,name,address,phone) values (5, '3456', 'Josh', '300 Main Mall', '6048228444');
INSERT into Customer(cid,password,name,address,phone) values (6, '5678', 'Neema', '400 Seattle', '2063607777');
INSERT into Purchase (receiptId, pdate, cid, cardnum, expiryDate, expectedDate, deliveredDate) values (1, sysdate, 7, 1234567, '1990-10-09', '2011-10-14', '2011-10-15');
INSERT into Purchase (receiptId, pdate, cid, cardnum, expiryDate, expectedDate, deliveredDate) values (2, sysdate, 8, 8901234, '1995-04-09', '2012-12-25', '2012-12-25');
INSERT into Purchase (receiptId, pdate, cid, cardnum, expiryDate, expectedDate, deliveredDate) values (3, sysdate, 9, 5467865, '2012-04-07', '1997-04-27', '1997-04-28');
INSERT into Purchase (receiptId, pdate, cid, cardnum, expiryDate, expectedDate, deliveredDate) values (4, sysdate, 10, 6789543, '2013-08-06', '2012-09-05', '2012-10-12');
INSERT into Purchase (receiptId, pdate, cid, cardnum, expiryDate, expectedDate, deliveredDate) values (4, sysdate, 10, 6789543, '2013-09-12', '2012-09-05', '2012-10-12');
INSERT into Purchase (receiptId, pdate, cid, cardnum, expiryDate, expectedDate, deliveredDate) values (2, sysdate, 8, 8901234, '1995-10-11', '2012-12-25', '2012-12-25');
INSERT into PurchaseItem values (11, 1, 22);
INSERT into PurchaseItem values (12, 2, 10);
INSERT into PurchaseItem values (15, 7, 30);
INSERT into PurchaseItem values (14, 4, 5);
INSERT into Return values (1, sysdate, 11);
INSERT into Return values (2, sysdate, 12);
INSERT into Return values (3, sysdate, 13);
INSERT into Return values (4, sysdate, 18);
INSERT into ReturnItem values (5, 1, 14);
INSERT into ReturnItem values (6, 2, 15);
INSERT into ReturnItem values (7, 3, 16);
INSERT into ReturnItem values (8, 4, 18);