
import java.sql.*; 
import java.util.List;

import javax.swing.event.EventListenerList;

public class ItemModel 
{
	protected PreparedStatement ps = null;
	protected EventListenerList listenerList = new EventListenerList();
	protected Connection con = null; 

	/*
	 * Default constructor
	 * Precondition: The Connection object in AMSOracleConnection must be
	 * a valid database connection.
	 */ 
	public ItemModel()
	{
		con = AMSOracleConnection.getInstance().getConnection();
	}

	public boolean addItem(Integer upc, String title, String type, String category, String company, Integer year, double price, Integer stock)
	{
		try
		{	   
			ps = con.prepareStatement("INSERT INTO item VALUES (?,?,?,?,?,?,?,?)");

			ps.setInt(1, upc.intValue());
			ps.setString(2, title);
			ps.setString(3, type);

			if (category != null){
				ps.setString(4, category);
			}
			else{
				ps.setString(4, null);
			}

			if (company != null){
				ps.setString(5, company);
			}
			else{
				ps.setString(5, null);
			}

			if (year != null) {
				ps.setInt(6, year.intValue());
			}
			else {
				ps.setNull(6, Types.INTEGER);
			}
			ps.setDouble(7, price);
			ps.setInt(8, stock.intValue());
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
	 * Returns true if the item exists; false
	 * otherwise.
	 */ 
	public ResultSet findItemByUPC(Integer upc)
	{
		try
		{	
			ps = con.prepareStatement("SELECT * FROM item WHERE upc = ?");
			ps.setInt(1, upc.intValue());
			ResultSet rs = ps.executeQuery();

			return rs; 

		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return null; 
		}
	}

	/*
	 * Returns true if the item exists; false
	 * otherwise.
	 */ 
	public ResultSet findItemByDescription(List<String> description)
	{
		try
		{	
			String query = "SELECT i.upc, i.title, s.name, i.type, i.category, i.year, i.price, i.stock FROM item i, leadsinger s WHERE s.upc = i.upc";
			if(!description.isEmpty()){
				query += " AND " + queryBuilder(description, " AND ");
			}
			System.out.println(query);
			ps = con.prepareStatement(query);

			ResultSet rs = ps.executeQuery();

			return rs; 

		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return null; 
		}
	}

	/**
	 * return stock level of upc
	 * @param upc
	 * @return stock level of requested stock
	 */
	public Integer getStock(Integer upc)
	{
		try
		{	
			ps = con.prepareStatement("SELECT stock FROM item WHERE upc = ?");
			ps.setInt(1, upc.intValue());
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				return rs.getInt(1); 
			}
			else {
				return -1; 
			}
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return null; 
		}
	}
	
	/**
	 * return price of upc
	 * @param upc
	 * @return stock level of requested stock
	 */
	public Integer getPrice(Integer upc)
	{
		try
		{	
			ps = con.prepareStatement("SELECT price FROM item WHERE upc = ?");
			ps.setInt(1, upc.intValue());
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				return rs.getInt(1); 
			}
			else {
				return -1; 
			}
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return -1; 
		}
	}
	
	/**
	 * restock Item updates the quantity for the upc input
	 * @param upc
	 * @param quantity
	 * @return true if successful
	 */
	public boolean restockItem(int upc, int quantity){
		try
		{	  
			ps = con.prepareStatement("UPDATE Item SET stock =(stock+?)  WHERE upc= ?");
			ps.setInt(1, quantity);
			ps.setInt(2, upc);
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
	 * sellItem decrement the stock level of the UPC product by quantity amount
	 * @param upc
	 * @param quantity
	 * @return true if successfully, false if failed.
	 */
	public boolean sellItem(int upc, int quantity){
		try
		{	  
			ps = con.prepareStatement("UPDATE Item SET stock =(stock-?)  WHERE upc= ?");
			ps.setInt(1, quantity);
			ps.setInt(2, upc);
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
	

	public void sellItem(Integer currReceiptID) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Same as showBranch() except that an updatable result set
	 * is returned.
	 */ 
	public ResultSet editItem()
	{
		try
		{	 
			ps = con.prepareStatement("SELECT i.* FROM item i", 
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
	 * Returns the database connection used by this item model
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
	 * Whenever an exception occurs in ItemModel, an exception event
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

}
