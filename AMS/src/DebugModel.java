
import java.sql.*; 

import javax.swing.event.EventListenerList;

public class DebugModel 
{
	protected PreparedStatement ps = null;
	protected EventListenerList listenerList = new EventListenerList();
	protected Connection con = null; 
	private String query;

	/*
	 * Default constructor
	 * Precondition: The Connection object in AMSOracleConnection must be
	 * a valid database connection.
	 */ 
	public DebugModel()
	{
		con = AMSOracleConnection.getInstance().getConnection();
	}

	/*
	 * Returns a ResultSet containing all items. The ResultSet is
	 * scroll insensitive and read only. If there is an error, null
	 * is returned.
	 */ 
	public ResultSet showTable(String table)
	{
		try
		{	 
			query = "SELECT * FROM " + table;
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
	
	/*
	 * Same as editTable() except that an updatable result set
	 * is returned.
	 */ 
	public ResultSet editTable(String table)
	{
		try
		{	 
			System.out.println("EDIT");
			query = "SELECT * FROM " + table;
			ps = con.prepareStatement(query, 
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
	 * Returns the database connection used by this debug model
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
	 * Whenever an exception occurs in DebugModel, an exception event
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
