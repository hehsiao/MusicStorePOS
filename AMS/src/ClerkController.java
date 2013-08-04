import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; 
import java.sql.*;


/*
 * BranchController is a control class that handles action events 
 * on the Branch Admin menu. It also updates the GUI based on 
 * which menu item the user selected. This class contains the following 
 * inner classes: BranchInsertDialog, BranchUpdateDialog, and 
 * BranchDeleteDialog. BranchInsertDialog is a dialog box that allows a 
 * user to insert a branch. BranchUpdateDialog is a dialog box that allows 
 * a user to update the name of a branch. BranchDeleteDialog is a dialog box 
 * that allows a user to delete a branch.
 *
 * BranchController implements the ExceptionListener interface which
 * allows it to be notified of any Exceptions that occur in BranchModel
 * (BranchModel contains the database transaction functions). It is defined
 * in BranchModel.java. The ExceptionListener interface is defined in 
 * ExceptionListener.java. When an Exception occurs in BranchModel, 
 * BranchController will update the status text area of AMSView. 
 */
public class ClerkController implements ActionListener, ExceptionListener
{
	private AMSView AMS = null;
	private ClerkModel clerk = null; 

	// constants used for describing the outcome of an operation
	public static final int OPERATIONSUCCESS = 0;
	public static final int OPERATIONFAILED = 1;
	public static final int VALIDATIONERROR = 2; 

	public ClerkController(AMSView AMS)
	{
		this.AMS = AMS;
		clerk = new ClerkModel();

		// register to receive exception events from branch
		clerk.addExceptionListener(this);
	}


	/*
	 * This event handler gets called when the user makes a menu
	 * item selection.
	 */ 
	public void actionPerformed(ActionEvent e)
	{
		String actionCommand = e.getActionCommand();

		// you cannot use == for string comparisons
		if (actionCommand.equals("Process Purchase"))
		{
			PurchaseDialog iDialog = new PurchaseDialog(AMS);
			System.out.println("BYE");
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("Process Return"))
		{
			//BranchUpdateDialog uDialog = new BranchUpdateDialog(AMS);
			//uDialog.pack();
			//AMS.centerWindow(uDialog);
			//uDialog.setVisible(true);
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

	class PurchaseDialog extends JDialog implements ActionListener
	{
	
			/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public PurchaseDialog(JFrame parent)
		{
			super(parent, "Process Purchase", true);
			setResizable(false);
			System.out.println("HELLO");
			
			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
			new TitledBorder(new EtchedBorder(), "Process Purchase"), 
			new EmptyBorder(5, 5, 5, 5)));
			
			GridBagLayout gb = new GridBagLayout();
			inputPane.setLayout(gb);
		
	
						JPanel buttonPane = new JPanel();
						buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
						buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));

						JButton cashButton = new JButton("CASH");
						cashButton.addActionListener(this);
						cashButton.setActionCommand("CASH");
						
						JButton creditButton = new JButton("CREDIT CARD");
						creditButton.setActionCommand("CREDIT");
						creditButton.addActionListener(this);
						
								
						// add the buttons to buttonPane
						buttonPane.add(Box.createHorizontalGlue());
						buttonPane.add(cashButton);
						buttonPane.add(Box.createRigidArea(new Dimension(10,0)));
						buttonPane.add(creditButton);

						contentPane.add(inputPane, BorderLayout.CENTER);
						contentPane.add(buttonPane, BorderLayout.CENTER);

						addWindowListener(new WindowAdapter() 
						{
							public void windowClosing(WindowEvent e)
							{
								dispose();
							}
						});

		}


		/*
		 * Event handler for the cash button in PurchaseDialog
		 * TODO: 
		 */ 
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("CASH"))
			{
			
			}
		}


	}
	
}


