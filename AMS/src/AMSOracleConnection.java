import java.sql.*; 


/*
 * This class is a singleton class that provides methods 
 * to connect to an Oracle database, return the connection, 
 * set the connection, and determine whether or not the Oracle
 * JDBC driver has been loaded. To obtain a reference to an
 * instance of this class, use the getInstance() method.
 */ 
public class AMSOracleConnection
{
	private static AMSOracleConnection _AMS = null;
	protected Connection con = null;
	protected boolean driverLoaded = false;


	/*
	 * The constructor is declared protected so that only subclasses
	 * can access it.
	 */ 
	protected AMSOracleConnection()
	{
		// empty
	}


	/*
	 * Returns an instance of AMSOracleConnection
	 */ 
	public static AMSOracleConnection getInstance()
	{
		if (_AMS == null)
		{
			_AMS = new AMSOracleConnection(); 
		}

		return _AMS;
	}


	/* 
	 * Loads the Oracle JDBC driver and connects to the database named ug using 
	 * the given username and password.
	 * Returns true if the connection is successful; false otherwise.
	 */ 
	public boolean connect(String username, String password)
	{
		try
		{
			// change the url if the branch table is located somewhere else
			String url = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";

			if (!driverLoaded)
			{
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				driverLoaded = true; 
			}

			con = DriverManager.getConnection(url, username, password);

			con.setAutoCommit(false);

			return true; 
		}
		catch (SQLException ex)
		{
			return false; 
		}
	}


	/*
	 * Returns the connection
	 */
	public Connection getConnection()
	{
		return con; 
	}


	/*
	 * Sets the connection
	 */
	public void setConnection(Connection connect)
	{
		con = connect; 
	}


	/*
	 * Returns true if the driver is loaded; false otherwise
	 */ 
	public boolean isDriverLoaded()
	{
		return driverLoaded; 
	}


	/*
	 * This method allows members of this class to clean up after itself 
	 * before it is garbage collected. It is called by the garbage collector.
	 */ 
	protected void finalize() throws Throwable
	{		
		if (con != null)
		{
			con.close();
		}

		// finalize() must call super.finalize() as the last thing it does
		super.finalize();	
	}
}
