
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

		if (actionCommand.equals("Show Items"))
		{
			showAllItems();
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
	private void showAllItems()
	{
		ResultSet rs = debug.showItem();

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


}
