

import java.sql.*; 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import oracle.sql.DATE;
/**
 * PurchaseModel contains operations that modifies the Purchase and PurchaseItem.
 */
public class PurchaseModel 
{
	protected PreparedStatement ps = null;
	protected EventListenerList listenerList = new EventListenerList();
	protected Connection con = null; 

	class purchaseDetail{
		int receiptID;
		String pdate;
		int cid;
		String cardNum;
		String expiryDate;
		String expectedDate;
		String deliveredDate;
	}

	/*
	 * Default constructor
	 * Precondition: The Connection object in AMSOracleConnection must be
	 * a valid database connection.
	 */ 
	public PurchaseModel()
	{
		con = AMSOracleConnection.getInstance().getConnection();
	}
	
	public int getOutstandingOrder(){
		try
		{	
			ps = con.prepareStatement("select count(*) from purchase where expecteddate IS NOT NULL and delivereddate IS NULL");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				System.out.println("Outstanding orders " + rs.getInt(1));
				return rs.getInt(1);
			}
			return 0; // return negative number if error
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return 0; 
		}
		
	}
	
	/**
	 * Checks and return the last ReceiptID in the Purchase table.
	 * Returns -1 if error.
	 * @return last receiptID in purchases table.
	 */
	public Integer getReceiptID(){
		try
		{	
			ps = con.prepareStatement("SELECT receiptID FROM purchase WHERE receiptID = (SELECT MAX(receiptID) from purchase)");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				// TODO: remove
				System.out.println("Current Order: " + rs.getInt(1));
				return rs.getInt(1);
			}
			return null; // return negative number if error
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return null; 
		}
	}

	/**
	 * Checks and return the last ReceiptID in the Purchase table.
	 * Returns -1 if error.
	 * @return last receiptID in purchases table.
	 */
	public Integer getRID(int id){
		try
		{	
			ps = con.prepareStatement("SELECT receiptID FROM purchase WHERE receiptID = (SELECT MAX(receiptID) from purchase)");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				return rs.getInt(1);
			}
			return null; // return null if error
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return null; 
		}
	}
	
	/**
	 * getCreditCard returns the last 4 digits of creditcard used
	 * @param recieptID
	 * @return last 4 digits of creditcard for return verifications.
	 */
	public String getCreditCard(int receiptID){
		try
		{	
			ps = con.prepareStatement("SELECT cardNum FROM purchase WHERE receiptID = ?");
			ps.setInt(1, receiptID);
			ResultSet rs = ps.executeQuery();
			if (rs.next() && rs.getString(1) != null)
			{
				return rs.getString(1).substring(12); // returns the last four digits
			}
			return null; // return null if error
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return null; 
		}
	}
	/**
	 * creates a new purchase Order for online purchases purchases
	 * @return true if successful
	 */
	public Integer createOnlinePurchaseOrder(String cid){
		try
		{	  
			ps = con.prepareStatement("INSERT INTO Purchase(pdate, cid) values(SYSDATE, ?)");
			ps.setString(1, cid);
			ps.executeUpdate();
			con.commit();
			return getReceiptID();
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
	 * creates a new purchase Order
	 * @return Integer receiptID if successful
	 */
	public Integer createPurchaseOrder(){
		try
		{	  
			System.out.println("Creating Purchase Order");
			ps = con.prepareStatement("INSERT INTO Purchase(pdate) values(SYSDATE)");
			ps.executeUpdate();
			con.commit();
			return getReceiptID();
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
	 * CustomerPayNow updates a customer purchase with receiptID to purchaseItem Table
	 * @param cid
	 * @param string
	 * @param string2
	 * @return true if successful
	 */
	public boolean customerPayNowCredit(int receiptID, String ccNumber, String ccExpiry){
		try
		{	  
			ps = con.prepareStatement("UPDATE Purchase SET cardnum = ?, expiryDate = ? WHERE receiptId = ?");
			ps.setString(1, ccNumber);
			ps.setString(2, ccExpiry);
			ps.setInt(3,  receiptID);
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

	

	public boolean customerOnlinePayNowCredit(int receiptID, String ccNumber, String ccExpiry, String date) throws ParseException {
		try
		{	  
			 SimpleDateFormat fm = new SimpleDateFormat("yy/MM/dd");
			  java.util.Date utilDate = fm.parse(date);
			  java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			ps = con.prepareStatement("UPDATE Purchase SET cardnum = ?, expiryDate = ?, expectedDate = ? WHERE receiptId = ?");
			ps.setString(1, ccNumber);
			ps.setString(2, ccExpiry);
			ps.setInt(4,  receiptID);
			ps.setDate(3, sqlDate);

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
	public boolean findItemInCart(int upc, Integer receiptID)
	{
		try
		{	
			ps = con.prepareStatement("SELECT upc FROM PurchaseItem WHERE upc = ? AND receiptID = ?");

			ps.setInt(1, upc);
			ps.setInt(2, receiptID.intValue());
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

	/**
	 * addItemToPurchase adds item with receiptID to purchaseItem Table
	 * @param upc
	 * @param quantity
	 * @param receiptID
	 * @return true if successful
	 */
	public boolean addItemToPurchase(int upc, int quantity, int receiptID){
		try
		{	  
			ps = con.prepareStatement("INSERT INTO PurchaseItem(receiptID, upc, quantity) values(?,?,?)");
			ps.setInt(1, receiptID);
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

	/**
	 * updateItemToPurchase updates item with receiptID to purchaseItem Table
	 * @param upc
	 * @param quantity
	 * @param receiptID
	 * @return true if successful
	 */
	public boolean updateItemToPurchase(int upc, int quantity, int receiptID){
		try
		{	  
			ps = con.prepareStatement("UPDATE PurchaseItem SET quantity = ? WHERE receiptID = ? AND upc = ?");
			ps.setInt(2, receiptID);
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
	 * getPurchaseDetail returns the items purchased in the receiptID provided
	 * @param receiptID
	 * @return ResultSet [receiptID, UPC, Title, Quantity, Price]
	 */
	public ResultSet getPurchaseItems(Integer receiptID){
		try
		{	 
			String query = "SELECT i.upc, i.title, pi.quantity, i.price " +
					"FROM item i, purchase p, purchaseitem pi " +
					"WHERE i.upc=pi.upc AND p.receiptID = pi.receiptID AND p.receiptID = " +receiptID;

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

	/**
	 * getPurchaseTotal returns the total price of the Purchase based on the receipt ID
	 * @param receiptID
	 * @return Total price of purchase
	 */
	public double getPurchaseTotal(Integer receiptID){
		try
		{				
			ps = con.prepareStatement("SELECT SUM(i.price*pi.quantity) FROM item i, purchase p, purchaseitem pi WHERE pi.upc = i.upc AND pi.receiptid = p.receiptid AND p.receiptid = ?");
			System.out.println(receiptID.intValue());
			ps.setInt(1, receiptID.intValue());
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				return rs.getDouble(1);
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
	

	public Integer getPurchasedQuantity(Integer upc, Integer receiptID)
	{
		try
		{	
			ps = con.prepareStatement("SELECT quantity FROM purchaseitem WHERE upc = ? and receiptID = ?");
			ps.setInt(1, upc.intValue());
			ps.setInt(2, receiptID.intValue());
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				return rs.getInt(1); 
			}
			else {
				return null; 
			}
		}
		catch (SQLException ex)
		{
			ExceptionEvent event = new ExceptionEvent(this, ex.getMessage());
			fireExceptionGenerated(event);

			return null; 
		}
	}
	

	public boolean removePurchase(Integer receiptID){
		try
		{	 

			ps = con.prepareStatement("DELETE FROM Purchase WHERE receiptID = ?");
			ps.setInt(1, receiptID.intValue());
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
	 * 	Sets the delivery date for a Purchase
	 * @param id
	 * @return
	 */
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
	/**
	 * Finds info on a purchase given the id and a string, either customer name or date of purchase
	 * @param id
	 * @param string
	 * @return
	 */

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

			if (info.equals("items")) {
				ps = con.prepareStatement("SELECT p.receiptID, i.upc, i.title, pi.quantity, i.price " +
						"FROM item i, purchase p, purchaseitem pi " +
						"WHERE i.upc=pi.upc AND p.receiptID = pi.receiptID AND p.receiptID = ");
				ps.setInt(1, rID);

				ResultSet rs = ps.executeQuery();
				String items = rs.getString("receiptID, upc, title, quantity, price");
				if (!rs.next()){
					return null;
				}
				return items;
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
	 * Returns the database connection used by this purchase model
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
	 * Whenever an exception occurs in PurchaseModel, an exception event
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

	public boolean getPurchase(Integer receiptID) {
		try
		{	
			ps = con.prepareStatement("SELECT * FROM Purchase WHERE receiptID = ?");

			ps.setInt(1, receiptID.intValue());
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

}
