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
	public boolean processCash(Integer upc, Integer quantity)
	{
		try
		{	   
		
		if (upc==null) return false;	
			
			ps = con.prepareStatement("UPDATE Item SET stock =(stock-?)  WHERE upc= ?");
			ps.setInt(1, quantity.intValue());
			ps.setInt(2, upc.intValue());
			ps.executeUpdate();
			con.commit();
			
			System.out.println("1");
			//ps = con.prepareStatement("INSERT INTO Purchase(date) values(SYSDATE)");
			ps = con.prepareStatement("INSERT INTO Item(upc,price,stock) values(2,3,5)");
			System.out.println("Hello");
			ps.executeUpdate();
			System.out.println("YO");
			con.commit();
			System.out.println("2");
			ps = con.prepareStatement("INSERT INTO PurchaseItem(upc,quantity) values(?,?)");
			ps.setInt(1, upc.intValue());
			ps.setInt(2, quantity.intValue());
			ps.executeUpdate();
			con.commit();

			System.out.println("3");

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



	/*
	 * Updates the name of a branch
	 * Returns true if the update is successful; false otherwise.
	 *
	 * bname cannot be null.
	 */
	public boolean updateBranch(int bid, String bname)
	{
		try
		{	
			ps = con.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");

			if (bname != null)
			{
				ps.setString(1, bname);
			}
			else
			{
				return false; 
			}

			ps.setInt(2, bid);

			ps.executeUpdate();

			con.commit();

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


	/*
	 * Deletes a branch.
	 * Returns true if the delete is successful; false otherwise.
	 */
	public boolean deleteBranch(int bid)
	{
		try
		{	  
			ps = con.prepareStatement("DELETE FROM branch WHERE branch_id = ?");

			ps.setInt(1, bid);

			ps.executeUpdate();

			con.commit();

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


	/*
	 * Returns a ResultSet containing all branches. The ResultSet is
	 * scroll insensitive and read only. If there is an error, null
	 * is returned.
	 */ 
	public ResultSet showPurchases()
	{
		try
		{	 
			ps = con.prepareStatement("SELECT b.* FROM branch b", 
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = ps.executeQuery();

			return rs; 
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);
			// no need to commit or rollback since it is only a query

			return null; 
		}
	}


	/*
	 * Same as showBranch() except that an updatable result set
	 * is returned.
	 */ 
	public ResultSet editBranch()
	{
		try
		{	 
			ps = con.prepareStatement("SELECT b.* FROM branch b", 
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = ps.executeQuery();

			return rs; 
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);
			// no need to commit or rollback since it is only a query

			return null; 
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
