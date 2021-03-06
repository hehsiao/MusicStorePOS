// File: BranchModel.java

import java.sql.*; 

import javax.swing.event.EventListenerList;

import oracle.sql.DATE;


/*
 * BranchModel is a database interface class that provides methods to 
 * insert a branch, update the name of a branch, delete a branch, return 
 * a ResultSet containing all branches, determine the existence of a particular 
 * branch, and return the database connection used by this class. 
 *
 * Each insert, update, and delete method is treated as a transaction, i.e., they 
 * commit the change. If you want to use those methods as helper functions for a 
 * larger transaction, you would have to write non commit() versions of those methods, 
 * so that you can treat the larger transaction as a single transaction. 
 * 
 * The database connection in AMSOracleConnection must be set before any database
 * access methods can be used. (This was done when connect() was called by the 
 * LoginWindow). This class is defined in AMSOracleConnection.java. 
 *
 * BranchModel allows components to register for exception events when 
 * an exception occurs in the model.  Exceptions are part of the 
 * Java 2 API, but exception events aren't.  We are creating our own 
 * event/listener. The BranchController class registers itself as
 * a listener to BranchModel so that it can display the exception 
 * messages.  The file ExceptionEvent.java contains the class 
 * definition of ExceptionEvent.  The file ExceptionListener.java
 * contains the interface that components must implement in order 
 * to receive exception events.   
 */ 
public class ClerkModel
{
	protected PreparedStatement ps = null;
	protected EventListenerList listenerList = new EventListenerList();
	protected Connection con = null; 
	private PurchaseModel purchase;


	/*
	 * Default constructor
	 * Precondition: The Connection object in AMSOracleConnection must be
	 * a valid database connection.
	 */ 
	public ClerkModel()
	{
		con = AMSOracleConnection.getInstance().getConnection();
	}


	/*
	 * stock--,
	 * daily sales report**
	 * 
	 * 
create table Purchase
(receiptId integer not null PRIMARY KEY,
date date,
cid integer FOREIGN KEY REFERENCES Customer,
cardnum integer,
expiryDate integer,
expectedDate integer,
deliveredDate integer);

create table PurchaseItem
(receiptId integer not null PRIMARY KEY,
upc integer not null PRIMARY KEY,
quantity integer
Foreign Key (receiptId) REFERENCES Purchase
Foreign Key (upc) REFERENCES Item);

	 */ 
	public boolean reduceStock(Integer upc, Integer quantity)
	{
		try
		{	   
			Integer receiptID = null;
			if (upc==null) return false;	
			//TODO: If stock < 0, stay 0	

			ps = con.prepareStatement("UPDATE Item SET stock =(stock-?)  WHERE upc= ?");
			ps.setInt(1, quantity.intValue());
			ps.setInt(2, upc.intValue());
			ps.executeUpdate();
			con.commit();
//			
//			System.out.println("1");
//			ps = con.prepareStatement("INSERT INTO Purchase(pdate) values(SYSDATE)");
//			ps.executeUpdate();
//			con.commit();
//			
//			ps = con.prepareStatement("SELECT receiptID FROM purchase WHERE receiptID = (SELECT MAX(receiptID) from purchase)");
//			ResultSet rs = ps.executeQuery();
//			if (rs.next())
//			{
//				receiptID = rs.getInt(1);
//			}
//			
//			ps = con.prepareStatement("INSERT INTO PurchaseItem(receiptID, upc,quantity) values(?, ?,?)");
//			ps.setInt(1, receiptID.intValue());
//			ps.setInt(2, upc.intValue());
//			ps.setInt(3, quantity.intValue());
//			ps.executeUpdate();
//			con.commit();
//
//			System.out.println("3");

			return true; 
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			try
			{
				con.rollback();
				return false; 
			}
			catch (SQLException ex2)
			{
				event = new ExceptionEvent(this, ex2.getMessage());
				fireExceptionGenerated(event);
				return false; 
			}
		}
	}


	public Integer createCreditPurchaseOrder(Integer cardNumber){
		try
		{	  
			ps = con.prepareStatement("INSERT INTO Purchase(pdate, cardnum, expiryDate) values(SYSDATE, ?, SYSDATE+15)");
			ps.setInt(1, cardNumber.intValue());

			ps.executeUpdate();
			con.commit();
			return purchase.getReceiptID();
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			try
			{
				con.rollback();
				return -1; 
			}
			catch (SQLException ex2)
			{
				event = new ExceptionEvent(this, ex2.getMessage());
				fireExceptionGenerated(event);
				return 0; 
			}
		}
	}
	
	/*
	 * Returns true if the item exists; false
	 * otherwise.
	 */ 
	public boolean findItem(int upc)
	{
		try
		{	
			ps = con.prepareStatement("SELECT upc FROM Item WHERE upc = ?");

			ps.setInt(1, upc);

			ResultSet rs = ps.executeQuery();

			if (rs.next())
			{
				return true; 
			}
			else
			{
				return false; 
			}
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return false; 
		}
}


	/*
	 * Returns the database connection used by this branch model
	 */
	public Connection getConnection()
	{
		return con; 
	}


	/*
	 * This method allows members of this class to clean up after itself 
	 * before it is garbage collected. It is called by the garbage collector.
	 */ 
	protected void finalize() throws Throwable
	{
		if (ps != null)
		{
			ps.close();
		}

		// finalize() must call super.finalize() as the last thing it does
		super.finalize();	
	}


	/******************************************************************************
	 * Below are the methods to add and remove ExceptionListeners.
	 * 
	 * Whenever an exception occurs in BranchModel, an exception event
	 * is sent to all registered ExceptionListeners.
	 ******************************************************************************/ 

	public void addExceptionListener(ExceptionListener l) 
	{
		listenerList.add(ExceptionListener.class, l);
	}


	public void removeExceptionListener(ExceptionListener l) 
	{
		listenerList.remove(ExceptionListener.class, l);
	}


	/*
	 * This method notifies all registered ExceptionListeners.
	 * The code below is similar to the example in the Java 2 API
	 * documentation for the EventListenerList class.
	 */ 
	public void fireExceptionGenerated(ExceptionEvent ex) 
	{
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event.
		// I have no idea why the for loop counts backwards by 2
		// and the array indices are the way they are.
		for (int i = listeners.length-2; i>=0; i-=2) 
		{
			if (listeners[i]==ExceptionListener.class) 
			{
				((ExceptionListener)listeners[i+1]).exceptionGenerated(ex);
			}
		}
	}
}
