
import java.sql.*; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.EventListenerList;

import oracle.sql.DATE;

public class ManagerModel 
{
	protected PreparedStatement ps = null;
	protected EventListenerList listenerList = new EventListenerList();
	protected Connection con = null; 

	/*
	 * Default constructor
	 * Precondition: The Connection object in AMSOracleConnection must be
	 * a valid database connection.
	 */ 
	public ManagerModel()
	{
		con = AMSOracleConnection.getInstance().getConnection();
	}

	/*
	 * Inserts a tuple into the items table. 
	 * price can be null
	 */ 
	public boolean insertItem(Integer upc, Integer price, Integer quantity)
	{
	  if (upc==null|| quantity==null) return false;
			
			try
			{	
			
				if (price==0){
				
			ps = con.prepareStatement("UPDATE Item SET price = (price+?), stock =(stock+?)  WHERE upc= ?");
			
			
			}
			else {
				ps = con.prepareStatement("UPDATE Item SET price = ?, stock =(stock+?)  WHERE upc= ?");
		
			}
			    ps.setInt(1,price.intValue());
				
				
				//ps = con.prepareStatement("UPDATE Item SET price = (price+?), stock =(stock+?)  WHERE upc= ?");
				//ps = con.prepareStatement("UPDATE Item SET stock =100  WHERE upc= 2");
					
			
			ps.setInt(1,price.intValue());
			ps.setInt(2, quantity.intValue());
		    ps.setInt(3, upc.intValue());
				
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

	public boolean setDeliveryDate(Integer rID)
	{
		try
		{	
			ps = con.prepareStatement("UPDATE Purchase SET deliveredDate = (sysdate) WHERE receiptID = ?");

		
			ps.setInt(1, rID.intValue());

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

	public boolean findRID(int rid)
	{
		try
		{	
			ps = con.prepareStatement("SELECT receiptID FROM Purchase WHERE receiptID = ?");

			ps.setInt(1, rid);

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

	public ResultSet showResultSet(java.sql.Date date)
	{
		try
		{	 
		//category totalunit totalvalue
			ps = con.prepareStatement("(SELECT i.upc, i.category, i.price as Unit_Price, sum(pi.quantity) as " +
					"Units_Sold,sum(pi.quantity)*(i.price) as Total_Value FROM Item i, Purchase p, PurchaseItem pi " +
					"WHERE pi.upc=i.upc and pi.receiptID=p.receiptID and p.pdate=? group by i.category) UNION " +
					
					"(SELECT i.category, sum(sum(pi.quantity)) as Total_Units_Sold, sum(sum(pi.quantity)*(i.price)) as Total Value" +
					"FROM Item i, Purchase p, PurchaseItem pi WHERE pi.upc=i.upc and pi.receiptID=p.receiptID and p.pdate=? ", 
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
			
			ps.setDate(1,date);
			ps.setDate(2,date);
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
	
	public String findInfo(Integer rID, String info)
	{
		try
		{	 
			if (info.equals("dop")){
				ps = con.prepareStatement("SELECT p.pdate FROM Purchase p WHERE p.receiptID=?");
				ps.setInt(1, rID);
				
				ResultSet rs = ps.executeQuery();
				String dop = rs.getString("pdate");
				if (!rs.next()){
					return null;
				}
				return dop;	
			}
			
			if (info.equals("cname")){
				ps = con.prepareStatement("SELECT c.name FROM Purchase p, Customer C WHERE p.receiptID=? AND p.cid=c.cid");
				ps.setInt(1, rID);	
				ResultSet rs = ps.executeQuery();
				String cname = rs.getString("name");
				if (!rs.next()){
					return null;
				}
				return cname;	
			}
			else return null;
			
     		
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);
			// no need to commit or rollback since it is only a query

			return null; 
		}
	}
	
	
	
	public String[] findArray(String what)
	{
		try
		{	 
			 List<String> resultList = new ArrayList<String>();
			if (what.equals("RIDwithoutDD")){
			
				ps = con.prepareStatement("SELECT p.receiptID FROM Purchase p where (p.deliveredDate IS NULL)");

				ResultSet rs = ps.executeQuery();

				while (rs.next()){
			
				    String em = rs.getString("receiptID");  
				    System.out.println(em);
				  resultList.add(em);
					}
				
		}
			
			String[] resultArray = new String[resultList.size()];
			resultList.toArray(resultArray);
		return resultArray;
		
		
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
	public boolean findDate(java.sql.Date date)
	{
		try
		{	
			ps = con.prepareStatement("SELECT date FROM Purchase WHERE date = ?");

					
			ps.setDate(1,date);

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
	 * Returns the database connection used by this manager model
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
	 * Whenever an exception occurs in ManagerModel, an exception event
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
