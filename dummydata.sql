-- DUMMY DATA 
INSERT into Item values (1, 'Levels', 'CD', 'House', 'Universal', 2011, '19.99', '10');
INSERT into LeadSinger values (1, 'Avicii');
INSERT into HasSong values (1, 'Levels (original version)');
INSERT into Item values (2, 'Random Access Memories', 'CD', 'Electric', 'Daft Life', 2012, '19.99', '10');
INSERT into LeadSinger values (2, 'Daft Punk');
INSERT into HasSong values (2, 'Get Lucky');
INSERT into HasSong values (2, 'Instant Crush');
INSERT into HasSong values (2, 'Contact');
INSERT into Customer(cid,password,name,address,phone) values (1,'1234', 'Matthew', '100 Hastings', '6045551234' );
INSERT into Customer(cid,password,name,address,phone) values (2, '2345', 'Risa', '100 Main', '6045551235' );
INSERT into Customer(cid,password,name,address,phone) values (3, '1334', 'Henry', '100 Vancouver', '6045551254' );
INSERT into Customer(cid,password,name,address,phone) values (4, '2234', 'Shanifer', '100 Burnaby', '604555444' );
INSERT into Purchase (receiptId, pdate, cid) values (1, sysdate, 1);
INSERT into PurchaseItem (1, 1, 2);
INSERT into Purchase (receiptid, pdate, cid) values (2, sysdate, 3);
INSERT into PurchaseItem (2, 2, 1);
INSERT into Item values (3, 'Tubthumper', 'CD', 'Post-punk', 'Universal', 1997, '9.99', '20');
INSERT into LeadSinger values (3, 'Chumbawamba');
INSERT into HasSong values (3, 'Tubthumping');
INSERT into HasSong values (3, 'I Want More');
INSERT into HasSong values (3, 'Amnesia');
INSERT into Item values (4, 'Plans', 'CD', 'Indie Rock', 'Atlantic', 2005, '10.99' 30);
INSERT into LeadSinger values (4, 'Death Cab for Cutie');
INSERT into HasSong values (4, 'Soul Meets Body');
INSERT into HasSong values (4, 'Crooked Teeth');
INSERT into HasSong values (4, 'I Will Follow You into the Dark');
INSERT into Customer(cid,password,name,address,phone) values (5, '3456', 'Josh', '300 Main Mall', '6048228444');
INSERT into Customer(cid,password,name,address,phone) values (6, '5678', 'Neema', '400 Seattle', '2063607777');
INSERT into Purchase (receiptId, pdate, cid) values (3, sysdate, 4);
INSERT into Purchase (receiptId, pdate, cid) values (4, sysdate, 5);
INSERT into PurchaseItem (2, 2, 3);
INSERT into PurchaseItem (4, 4, 5);
INSERT into Item values (4, 'Tommy', 'DVD', 'Rock', 'Columbia', 1975, '9.99', '15');
INSERT into LeadSinger values (4, 'The Who');
INSERT into HasSong values (4, 'The Acid Queen');
INSERT into HasSong values (4, 'Pinball Wizard');
INSERT into HasSong values (4, 'Eyesight to the Blind');
INSERT into Item values (5, 'Phantom of the Opera', 'DVD', 'Musical', 'Warner Bros.', 2004, '9.99', '50');
INSERT into LeadSinger values (5, 'Andrew Lloyd Webber');
INSERT into HasSong values (5, 'Music of the Night');
INSERT into HasSong values (5, 'Point of No Return');
INSERT into HasSong values (5, 'Angel of Music');
INSERT into Item values (6, 'Les Miserables', 'DVD', 'Musical', 'Universal', 2012, '15.99', '40');
INSERT into LeadSinger values (6, 'Herbery Kretzmer');
INSERT into HasSong values (6, 'I Dreamed a Dream');
INSERT into HasSong values (6, 'The Confrontation');
INSERT into HasSong values (6, 'One Day More');
INSERT into Item values (7, 'Moulin Rouge', 'DVD', 'Musical', '20th Century Fox', 2001, '9.99', '60');
INSERT into LeadSinger values (7, 'Baz Luhrmann');
INSERT into HasSong values (7, 'Your Song');
INSERT into HasSong values (7, 'Come What May');
INSERT into HasSong values (7, 'Hindi Sad Diamonds');
INSERT into ReturnItem values (1, 1, 3);
INSERT into ReturnItem values (2, 2, 3);
INSERT into ReturnItem values (3, 3, 3);
INSERT into Return values (1, '900809', 1);
INSERT into Return values (2, '880704', 2);
INSERT into Return values (3, '130808', 3);