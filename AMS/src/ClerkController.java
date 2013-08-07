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
	private PurchaseModel purchase = null;


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
			AMS.buttonPane.setVisible(false);
			PurchaseDialog iDialog = new PurchaseDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("Process Return"))
		{
			AMS.buttonPane.setVisible(false);
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
				CashDialog iDialog = new CashDialog(AMS);
				iDialog.pack();
				AMS.centerWindow(iDialog);
				iDialog.setVisible(true);
				return; 
			}
		}


	}
	class CashDialog extends JDialog implements ActionListener
	{
		JTextField itemUPC = new JTextField(10);
		JTextField itemQuantity = new JTextField(10);


		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public CashDialog(JFrame parent)
		{
			super(parent, "CASH", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Item Fields"), 
					new EmptyBorder(5, 5, 5, 5)));

			// add the text field labels and text fields to inputPane
			// using the GridBag layout manager

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			// create and place upc label
			JLabel label= new JLabel("Item UPC: ", SwingConstants.RIGHT);	    
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(0, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place upc field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(0, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemUPC, c);
			inputPane.add(itemUPC);



			// create and place quantity label
			label = new JLabel("Quantity: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place Quantity field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemQuantity, c);
			inputPane.add(itemQuantity);



			// when the return key is pressed in the last field
			// of this form, the action performed by the ok button
			// is executed
			itemQuantity.addActionListener(this);
			itemQuantity.setActionCommand("OK");

			// panel for the OK and cancel buttons
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));

			JButton OKButton = new JButton("OK");
			JButton cancelButton = new JButton("Cancel");
			OKButton.addActionListener(this);
			OKButton.setActionCommand("OK");
			cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					dispose();
				}
			});

			// add the buttons to buttonPane
			buttonPane.add(Box.createHorizontalGlue());
			buttonPane.add(OKButton);
			buttonPane.add(Box.createRigidArea(new Dimension(10,0)));
			buttonPane.add(cancelButton);

			contentPane.add(inputPane, BorderLayout.CENTER);
			contentPane.add(buttonPane, BorderLayout.SOUTH);

			addWindowListener(new WindowAdapter() 
			{
				public void windowClosing(WindowEvent e)
				{
					dispose();
				}
			});
		}


		/*
		 * Event handler for the OK button in BranchInsertDialog
		 */ 
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("OK"))
			{
				if (validateCASH() != VALIDATIONERROR)
				{
					dispose();
				}
				else
				{
					Toolkit.getDefaultToolkit().beep();

					// display a popup to inform the user of the validation error
					JOptionPane errorPopup = new JOptionPane();
					errorPopup.showMessageDialog(this, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
				}	
			}
		}


		/*
		 * Validates the text fields in BranchInsertDialog and then
		 * calls branch.insertBranch() if the fields are valid.
		 * Returns the operation status, which is one of OPERATIONSUCCESS, 
		 * OPERATIONFAILED, VALIDATIONERROR.
		 */ 
		private int validateCASH()
		{
			try
			{
				Integer rID;
				Integer upc;
				Integer quantity;

				if (itemUPC.getText().trim().length() != 0)
				{
					upc = Integer.valueOf(itemUPC.getText().trim());

					// check for duplicates
					if (clerk.findItem(upc.intValue())){

					}
				}

				else
				{
					return VALIDATIONERROR; 

				}


				if (itemQuantity.getText().trim().length() != 0)
				{
					quantity = Integer.valueOf(itemQuantity.getText().trim());
				}
				else
				{
					return VALIDATIONERROR; 
				}


				AMS.updateStatusBar("Processing Cash Purchase...");
                  rID = purchase.createCashPurchaseOrder();
                  System.out.println(rID.toString());
				if (purchase.addItemToPurchase(upc,quantity,rID))
				{

					AMS.updateStatusBar("Operation successful.");

					showPurchase(rID);

					return OPERATIONSUCCESS; 
				}
				else
				{
					Toolkit.getDefaultToolkit().beep();
					AMS.updateStatusBar("Operation failed.");
					return OPERATIONFAILED; 
				}
			}
			catch (NumberFormatException ex)
			{
				// this exception is thrown when a string 
				// cannot be converted to a number
				return VALIDATIONERROR; 
			}
		}
	}


		/*
		 * This method displays all purchases in a non-editable JTable
		 */
		private void showPurchase(Integer receiptID)
		{
			ResultSet rs = purchase.getPurchaseItems(receiptID);
	
			// CustomTableModel maintains the result set's data, e.g., if  
			// the result set is updatable, it will update the database
			// when the table's data is modified.  
			CustomTableModel model = new CustomTableModel(clerk.getConnection(), rs);
			CustomTable data = new CustomTable(model);
	
			// register to be notified of any exceptions that occur in the model and table
			model.addExceptionListener(this);
			data.addExceptionListener(this);
	
			// Adds the table to the scrollpane.
			// By default, a JTable does not have scroll bars.
			AMS.addTable(data);
		}


}


