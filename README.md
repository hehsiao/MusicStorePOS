AMS Music Store Point of Sale System
===============

Project Description

The database system that you are to implement in this project is for a large music retail company with multiple stores and an on-line outlet (like Virgin or Music World), called herein Allegro Music Store (or AMS), or simply "the company". The rest of this section provides a detailed description of the basic operations which the users of the database system expect to perform. 

The main function of AMS is to sell music to its customers. AMS has  a number of stores in a number of cities in Canada and also has an online outlet. A store is uniquely identified by a name (like Vancouver-Main ), and  has an address, city and phone associated with it. There is a special store called "Warehouse" that only services the online orders. Each store keeps two types of music:  audio CD's and DVD's with music videos. The stores have also music books and  sheet music. Each item is uniquely identified by a universal product code (UPC). For each audio CD and  DVD we need to record their title, the leading singer(s) or group, the production company and  year,  and the titles of the songs which are  contained in it.  Each CD and DVD belongs to one of the following music categories: rock, pop, rap, country, classical, new age and instrumental.  Each book has a title, one or more authors,  a publisher and a year. Finally a music score has a title, one or more composers and a publishing company.  

Every item has a price, an indication whether it is taxable, and is supplied by one or more suppliers. For each supplier the company would like to record their name (which is unique), their address, their city, and their status, which can be "active" or "inactive".

To buy an item, an AMS customer can either visit a store and make a regular purchase or can access the on-line AMS site and place an order.  When customers shop in a regular store, they can pick up the items and pay at a check-out counter. A customer may pay for their purchase either by cash, or by one of the acceptable credit cards. The company currently accepts Master Card, Visa and American Express cards. When the customer requests a credit card charge, the clerk requests authorization from the credit card company. If authorization fails the customer is asked to pay cash. If the customer refuses, the purchase is cancelled.  When the payment is completed the system prints a receipt (with a unique receipt number ) for the purchase. The company does not keep records for its customers who shop at the regular stores, but when a customer pays by a credit card, the card number and expiry date are recorded with the purchase. 

Alternatively, customers can buy goods from the company's on-line site. When at the site, customers can identify themselves  by typing their customer id and password.  Customers who access the online store for the first time, will be asked to register by providing their personal information, including their name, address, city, phone number, an id and password.   If the id is already in the system, they will be asked to provide another one.  To shop at the site, registered customers will be given a virtual shopping basket and asked to specify the  item they want to buy. The customer will describe the category,  the item title (optional)  and the quantity. If the information is not enough to define a single item, the system will display all the items that match the input and ask the customer to select one.  When an item is selected, it will be added to the customer shopping cart. Each time an item is selected the systems has to make sure that there is enough quantity in the Warehouse to complete the purchase. Otherwise the system will ask the customer to accept the existing quantity.   The customer can repeat the same process for any number of items.   When the customer is ready to check-out, the system will produce a bill with the items and the total amount. The client has to provide a credit card number and expiry date to complete the transaction.  After that,  the system will create an order for the Warehouse and inform the customer about the number of days it will take to receive the goods.  This number is estimated by the number of outstanding orders and  the maximum number of orders that can be delivered in a day (which is fixed).   

Customers can return merchandises to any store within 15 days from the purchase date. When an item is returned, the system verifies the receipt and issues a refund in the same  payment form that was used for the original purchase. That is,  if cash was used for the original purchase, the refund is in cash. Otherwise, the credit card  is credited.

In addition to customer orders and deliveries, the system should keep track of the in-store purchases, the returns, and the supply shipments.  A  store may order a number of goods from a supplier. Each order is processed manually and is outside the scope of this system.  When a supply shipment comes from a supplier to a store, the store personnel has to record the shipment and update the inventory.  

The system should perform a variety of transactions. The most frequent operations that we need to implement in the first release of the system include: 
- process a purchase of  items at a conventional store
- register customers for online purchase
- process an online purchase of items 
- record the delivery for an online order
- process the return of an item for a refund
- add and remove suppliers,
- process a shipment from a supplier to a store 
- print a variety of reports.

At the end of each day, the company wants to create a report with all items sold during the day, grouped by store and item category, and showing the subtotal sales for each category, each store, and the grand total for the day. Every week, the company would like to produce reports showing for each store the items that are low in stock (i.e. have less than 10 pieces on the shelves), or  just showing the low stock items in a specific store.

Finally there are a number of simple queries the system users should be able to ask. They should be able to get all the relevant information for a given store and  item (by providing the store's name and the item title), including 
- the stock for that item get the suppliers for a given item
- show the top 5 (in terms of quantity)  items and the total quantity that the company sold in a given time period
- get the titles and the stock of the CD's for a given artist (leading singer)
- get the titles of the DVDs that are produced by a given producer.

The system you design will be used by three types of user:
- customer: anyone who buys items online
- clerk: a typical check-out  counter clerk who performs in-store purchases and returns,
- manager: a user  who performs all the rest 
