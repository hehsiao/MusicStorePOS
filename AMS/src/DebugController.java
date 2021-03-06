
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; 
import java.sql.*;

public class DebugController implements ActionListener, ExceptionListener
{
	private AMSView AMS = null;
	private DebugModel debug = null;

	// constants used for describing the outcome of an operation
	public static final int OPERATIONSUCCESS = 0;
	public static final int OPERATIONFAILED = 1;
	public static final int VALIDATIONERROR = 2; 

	public DebugController(AMSView AMS)
	{
		this.AMS = AMS;
		debug = new DebugModel();

		// register to receive exception events from branch
		debug.addExceptionListener(this);
	}

	/*
	 * This event handler gets called when the user makes a menu
	 * item selection.
	 */ 
	public void actionPerformed(ActionEvent e)
	{
		String actionCommand = e.getActionCommand();
		AMS.clearStatusBar();
		if (actionCommand.equals("Item"))
		{
			AMS.updateStatusBar("Displaying Item Table");
			editTables("Item");
			return; 
		}

		else if (actionCommand.equals("LeadSinger"))
		{
			AMS.updateStatusBar("Displaying Lead Singer Table");
			showTables("LeadSinger");
			return; 
		}

		else if (actionCommand.equals("HasSong"))
		{
			AMS.updateStatusBar("Displaying HasSong Table");
			showTables("HasSong");
			return; 
		}

		else if (actionCommand.equals("Purchase"))
		{			
			AMS.updateStatusBar("Displaying Purchase Table");
			showTables("Purchase");
			return; 
		}

		else if (actionCommand.equals("PurchaseItem"))
		{
			AMS.updateStatusBar("Displaying Purchase Item Table");
			showTables("PurchaseItem");
			return; 
		}

		else if (actionCommand.equals("Customer"))
		{
			AMS.updateStatusBar("Displaying Customer Table");
			showTables("Customer");
			return; 
		}

		else if (actionCommand.equals("Return"))
		{
			AMS.updateStatusBar("Displaying Return Table");
			showTables("Return");
			return; 
		}

		else if (actionCommand.equals("ReturnItem"))
		{
			AMS.updateStatusBar("Displaying Return Item Table");
			showTables("ReturnItem");
			return; 
		}
	}

	/*
	 * This event handler gets called when an exception event 
	 * is generated. It displays the exception message on the status 
	 * text area of the main GUI.
	 */ 
	public void exceptionGenerated(ExceptionEvent ex)
	{
		String message = ex.getMessage();

		// annoying beep sound
		Toolkit.getDefaultToolkit().beep();

		if (message != null)
		{	
			AMS.updateStatusBar(ex.getMessage());
		}
		else
		{
			AMS.updateStatusBar("An exception occurred!");
		}
	}    

	/*
	 * This method displays all Items in a non-editable JTable
	 */
	private void showTables(String table_name)
	{
		ResultSet rs = debug.showTable(table_name);

		// CustomTableModel maintains the result set's data, e.g., if  
		// the result set is updatable, it will update the database
		// when the table's data is modified.  
		CustomTableModel model = new CustomTableModel(debug.getConnection(), rs);
		CustomTable data = new CustomTable(model);

		// register to be notified of any exceptions that occur in the model and table
		model.addExceptionListener(this);
		data.addExceptionListener(this);

		// Adds the table to the scrollpane.
		// By default, a JTable does not have scroll bars.
		AMS.addTable(data);
	}

	/*
	 * This method displays all branches in an editable JTable
	 */
	private void editTables(String table_name)
	{
		ResultSet rs = debug.editTable(table_name);

		CustomTableModel model = new CustomTableModel(debug.getConnection(), rs);
		CustomTable data = new CustomTable(model);

		model.addExceptionListener(this);
		data.addExceptionListener(this);

		AMS.addTable(data);
	}


}
