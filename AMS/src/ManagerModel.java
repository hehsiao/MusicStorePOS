
import java.sql.*; 

import javax.swing.event.EventListenerList;

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

	public boolean addItem(Integer cid, String password, String name, String address, Integer phone)
	{
		try
		{	   
			ps = con.prepareStatement("INSERT INTO manager VALUES (?,?,?,?,?)");

			ps.setInt(1, cid.intValue());

			ps.setString(2, password);

			ps.setString(3, name);

			if (address != null)
			{
				ps.setString(4, address);
			}
			else
			{
				ps.setString(4, null);
			}

			if (phone != null)
			{
				ps.setInt(5, phone.intValue());
			}
			else
			{
				ps.setNull(5, Types.INTEGER);
			}

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
	 * Returns true if the manager exists; false
	 * otherwise.
	 */ 
	public boolean findItem(int cid)
	{
		try
		{	
			ps = con.prepareStatement("SELECT cid FROM manager WHERE cid = ?");

			ps.setInt(1, cid);

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
