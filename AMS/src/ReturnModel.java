import java.sql.*; 
import java.util.List;

import javax.swing.event.EventListenerList;
/**
 * ReturnModel contains operations that modifies the Return and ReturnItem.
 */
public class ReturnModel 
{
	protected PreparedStatement ps = null;
	protected EventListenerList listenerList = new EventListenerList();
	protected Connection con = null; 

	/*
	 * Default constructor
	 * Precondition: The Connection object in AMSOracleConnection must be
	 * a valid database connection.
	 */ 
	public ReturnModel()
	{
		con = AMSOracleConnection.getInstance().getConnection();
	}

	/**
	 * Checks and return the last ReceiptID in the Return table.
	 * Returns -1 if error.
	 * @return last receiptID in returns table.
	 */
	public Integer getRetID(){
		try
		{	
			ps = con.prepareStatement("SELECT retID FROM return WHERE retID = (SELECT MAX(retID) from return)");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				return rs.getInt(1);
			}
			return -1; // return negative number if error
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return -1; 
		}
	}

	/**
	 * creates a new return Order
	 * @param currReceiptID 
	 * @return true if successful
	 */
	public Integer createReturnOrder(Integer receiptID){
		try
		{	  
			System.out.println("Creating Return Order");
			ps = con.prepareStatement("INSERT INTO Return(retDate, receiptID) values(SYSDATE, ?)");
			ps.setInt(1, receiptID.intValue());
			ps.executeUpdate();
			con.commit();
			return getRetID();
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			try
			{
				con.rollback();
				return null; 
			}
			catch (SQLException ex2)
			{
				event = new ExceptionEvent(this, ex2.getMessage());
				fireExceptionGenerated(event);
				return 0; 
			}
		}
	}

	/**
	 * addItemToReturn adds item with receiptID to returnItem Table
	 * @param upc
	 * @param quantity
	 * @param receiptID
	 * @return true if successful
	 */
	public boolean addItemToReturn(int upc, int quantity, int retID){
		try
		{	  
			ps = con.prepareStatement("INSERT INTO ReturnItem(retID, upc, quantity) values(?,?,?)");
			ps.setInt(1, retID);
			ps.setInt(2, upc);
			ps.setInt(3, quantity);
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

	public boolean findItemInCart(Integer upc, Integer retID) {
		try
		{	
			ps = con.prepareStatement("SELECT upc FROM ReturnItem WHERE upc = ? AND retID = ?");

			ps.setInt(1, upc);
			ps.setInt(2, retID.intValue());
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

	public boolean updateItemToReturn(Integer upc, Integer quantity,
			Integer retID) {
		try
		{	  
			ps = con.prepareStatement("UPDATE ReturnItem SET quantity = ? WHERE retID = ? AND upc = ?");
			ps.setInt(2, retID);
			ps.setInt(3, upc);
			ps.setInt(1, quantity);
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

	/** 
	 * Return Order retID is cancelled
	 * @param retID
	 * @return true if successful
	 */
	public boolean removeReturn(Integer retID){
		try
		{	 
			ps = con.prepareStatement("DELETE FROM Return WHERE retID = ?");
			ps.setInt(1, retID.intValue());
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
	 * Returns the database connection used by this return model
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
	 * Whenever an exception occurs in ReturnModel, an exception event
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

	/**
	 * Setup Query for WHERE clause.
	 * concatenate a list of criteria with separators in between
	 * @param list of criteria
	 * @param separator
	 * @return
	 */
	public static String queryBuilder(List<String> strings, String separator)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < strings.size(); i++)
		{
			sb.append(strings.get(i));
			if(i < strings.size() - 1)
				sb.append(separator);
		}
		return sb.toString();				
	}

	public double getReturnTotal(Integer retID) {
		try
		{				
			ps = con.prepareStatement("SELECT SUM(i.price*ri.quantity) FROM item i, returnitem ri WHERE ri.upc = i.upc AND ri.retid = ?");
			System.out.println(retID.intValue());
			ps.setInt(1, retID.intValue());
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				return rs.getDouble(1);
			}
			return Double.NaN; // return negative number if error
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return Double.NaN; 
		}
	}

	public ResultSet getReturnItems(Integer retID) {
		try
		{	 
			String query = "SELECT i.upc, i.title, ri.quantity, i.price " +
					"FROM item i, returnitem ri " +
					"WHERE i.upc=ri.upc AND ri.retID = "+ retID;

			ps = con.prepareStatement(query, 
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



}

