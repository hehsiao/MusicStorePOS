
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; 
import java.sql.*;

public class ManagerController implements ActionListener, ExceptionListener
{
	private AMSView AMS = null;
	private ManagerModel manager = null;

	// constants used for describing the outcome of an operation
	public static final int OPERATIONSUCCESS = 0;
	public static final int OPERATIONFAILED = 1;
	public static final int VALIDATIONERROR = 2; 

	public ManagerController(AMSView AMS)
	{
		this.AMS = AMS;
		manager = new ManagerModel();

		// register to receive exception events from branch
		manager.addExceptionListener(this);
	}

	/*
	 * This event handler gets called when the user makes a menu
	 * item selection.
	 */ 
	public void actionPerformed(ActionEvent e)
	{
		String actionCommand = e.getActionCommand();

		if (actionCommand.equals("Add Items"))
		{
			AddItemsDialog iDialog = new AddItemsDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("Process Delivery"))
		{
			ProcessDeliveryDialog iDialog = new ProcessDeliveryDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
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
	 * This class creates a dialog box for adding an item to inventory.
	 */
	class AddItemsDialog extends JDialog implements ActionListener
	{
		private JTextField itemUPC = new JTextField(12);
		private JTextField itemTitle = new JTextField(15);
		private JTextField itemType = new JTextField(10);
		private JTextField itemCategory = new JTextField(10);
		private JTextField itemCompany = new JTextField(15);
		private JTextField itemYear = new JTextField(4);
		private JTextField itemPrice = new JTextField(8);
		private JTextField itemStock = new JTextField(4);

		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public AddItemsDialog(JFrame parent)
		{
			super(parent, "Add Item", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Add Item to Inventory"), 
					new EmptyBorder(5, 5, 5, 5)));

			// add the text field labels and text fields to inputPane
			// using the GridBag layout manager

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			// create and place UPC label
			JLabel label = new JLabel("UPC: ", SwingConstants.RIGHT);	    
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(0, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place UPC field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(0, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemUPC, c);
			inputPane.add(itemUPC);

			// create and place item title label
			label = new JLabel("Product Name: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place item title field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemTitle, c);
			inputPane.add(itemTitle);

			// create and place item type label
			label = new JLabel("Type: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place item type field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemType, c);
			inputPane.add(itemType);

			// create and place item category label
			label = new JLabel("Category: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place item category field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemCategory, c);
			inputPane.add(itemCategory);

			// create and place item company label
			label = new JLabel("Manufacturer: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place item company field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemCompany, c);
			inputPane.add(itemCompany);

			// create and place item year label
			label = new JLabel("Product Year: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place item year field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemYear, c);
			inputPane.add(itemYear);

			// create and place item price label
			label = new JLabel("Price: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place item price field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemPrice, c);
			inputPane.add(itemPrice);

			// create and place item stock label
			label = new JLabel("Stock: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place item stock field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemStock, c);
			inputPane.add(itemStock);

			// when the return key is pressed in the last field
			// of this form, the action performed by the OK button
			// is executed
			itemStock.addActionListener(this);
			itemStock.setActionCommand("OK");

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
		 * Event handler for the OK button in AddItemsDialog
		 */ 
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("OK"))
			{
				//				if (validateInsert() != VALIDATIONERROR)
				//				{
				//					dispose();
				//				}
				//				else
				{
					Toolkit.getDefaultToolkit().beep();

					// display a popup to inform the user of the validation error
					JOptionPane errorPopup = new JOptionPane();
					errorPopup.showMessageDialog(this, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
				}	
			}
		}

		/** VALIDATION REQUIRED: SETTING UP UI FIRST **/
		//		/*
		//		 * Validates the text fields in AddItemsDialog and then
		//		 * calls manager.addItem() if the fields are valid.
		//		 * Returns the operation status, which is one of OPERATIONSUCCESS, 
		//		 * OPERATIONFAILED, VALIDATIONERROR.
		//		 */ 
		//		private int validateInsert()
		//		{
		//			try
		//			{
		//				Integer cid;
		//				String password;
		//				String name;
		//				String address;
		//				Integer phone;
		//
		//				if (itemUPC.getText().trim().length() != 0)
		//				{
		//					cid = Integer.valueOf(itemUPC.getText().trim());
		//
		//					// check for duplicates
		//					if (manager.findItem(cid.intValue()))
		//					{
		//						Toolkit.getDefaultToolkit().beep();
		//						AMS.updateStatusBar("Manager " + cid.toString() + " already exists!");
		//						return OPERATIONFAILED; 
		//					}
		//				}
		//				else
		//				{
		//					return VALIDATIONERROR; 
		//				}
		//
		//				if (itemTitle.getText().trim().length() != 0)
		//				{
		//					password = itemTitle.getText().trim();
		//				}
		//				else
		//				{
		//					return VALIDATIONERROR; 
		//				}
		//
		//				if (itemType.getText().trim().length() != 0)
		//				{
		//					name = itemType.getText().trim();
		//				}
		//				else
		//				{
		//					return VALIDATIONERROR; 
		//				}
		//
		//				if (itemCategory.getText().trim().length() != 0)
		//				{
		//					address = itemCategory.getText().trim();
		//				}
		//				else
		//				{
		//					address = null; 
		//				}
		//
		//				if (itemCompany.getText().trim().length() != 0)
		//				{
		//					phone = Integer.valueOf(itemCompany.getText().trim());
		//				}
		//				else
		//				{
		//					phone = null; 
		//				}
		//
		//				AMS.updateStatusBar("Creating Account...");
		//
		//				if (manager.addItem(cid, password, name, address, phone))
		//				{
		//					AMS.updateStatusBar("Operation successful.");
		//					return OPERATIONSUCCESS; 
		//				}
		//				else
		//				{
		//					Toolkit.getDefaultToolkit().beep();
		//					AMS.updateStatusBar("Operation failed.");
		//					return OPERATIONFAILED; 
		//				}
		//			}
		//			catch (NumberFormatException ex)
		//			{
		//				// this exception is thrown when a string 
		//				// cannot be converted to a number
		//				return VALIDATIONERROR; 
		//			}
		//		}
	}	// end AddItemsDialog

	/*
	 * This class creates a dialog box for processing a delivery.
	 */
	class ProcessDeliveryDialog extends JDialog implements ActionListener
	{
		String[] receiptID = {"Risa", "is", "racist"};	// Use SQL to pull receiptIDs that currently don't have a delivered date.
		private JComboBox purchaseReceiptID = new JComboBox(receiptID);

		private JTextField purchaseDeliveredDate = new JTextField(10);

		// Given the receiptID this field automatically retrieves the date of the order
		private JTextField purchaseDate = new JTextField(10);

		// Given the receiptID this field automatically retrieves the name of the customer
		private JTextField customerName = new JTextField(15);

		// Given the receiptID this field automatically retrieves all the items in the order
		private JTextArea purchaseItem = new JTextArea(30, 30);

		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public ProcessDeliveryDialog(JFrame parent)
		{
			super(parent, "Process Delivery", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Process Delivery"), 
					new EmptyBorder(5, 5, 5, 5)));

			// add the text field labels and text fields to inputPane
			// using the GridBag layout manager

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			// create and place receipt ID label
			JLabel label = new JLabel("Receipt ID: ", SwingConstants.RIGHT);	    
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(0, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place receipt ID field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(0, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(purchaseReceiptID, c);
			inputPane.add(purchaseReceiptID);

			// create and place purchase delivered date label
			label = new JLabel("Date of Delivery: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place purchase delivered date field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(purchaseDeliveredDate, c);
			inputPane.add(purchaseDeliveredDate);

			// create and place purchase date label
			label = new JLabel("Date of Purchase: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place purchase date field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			purchaseDate.setEditable(false);
			purchaseDate.setBackground(Color.lightGray);
			gb.setConstraints(purchaseDate, c);
			inputPane.add(purchaseDate);

			// create and place customer name label
			label = new JLabel("Customer Name: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place customer name field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			customerName.setBackground(Color.lightGray);
			customerName.setEditable(false);
			gb.setConstraints(customerName, c);
			inputPane.add(customerName);

			// create and place purchase item label
			label = new JLabel("Item(s) Purchased: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place purchase item field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			purchaseItem.setBackground(Color.lightGray);
			purchaseItem.setEditable(false);
			gb.setConstraints(purchaseItem, c);
			inputPane.add(purchaseItem);

			// when the return key is pressed in the last field
			// of this form, the action performed by the OK button
			// is executed
			purchaseDeliveredDate.addActionListener(this);
			purchaseDeliveredDate.setActionCommand("OK");

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
		 * Event handler for the OK button in AddItemsDialog
		 */ 
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("OK"))
			{
				//				if (validateInsert() != VALIDATIONERROR)
				//				{
				//					dispose();
				//				}
				//				else
				{
					Toolkit.getDefaultToolkit().beep();

					// display a popup to inform the user of the validation error
					JOptionPane errorPopup = new JOptionPane();
					errorPopup.showMessageDialog(this, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
				}	
			}
		}

		/** VALIDATION REQUIRED: SETTING UP UI FIRST **/
		//		/*
		//		 * Validates the text fields in AddItemsDialog and then
		//		 * calls manager.addItem() if the fields are valid.
		//		 * Returns the operation status, which is one of OPERATIONSUCCESS, 
		//		 * OPERATIONFAILED, VALIDATIONERROR.
		//		 */ 
		//		private int validateInsert()
		//		{
		//			try
		//			{
		//				Integer cid;
		//				String password;
		//				String name;
		//				String address;
		//				Integer phone;
		//
		//				if (itemUPC.getText().trim().length() != 0)
		//				{
		//					cid = Integer.valueOf(itemUPC.getText().trim());
		//
		//					// check for duplicates
		//					if (manager.findItem(cid.intValue()))
		//					{
		//						Toolkit.getDefaultToolkit().beep();
		//						AMS.updateStatusBar("Manager " + cid.toString() + " already exists!");
		//						return OPERATIONFAILED; 
		//					}
		//				}
		//				else
		//				{
		//					return VALIDATIONERROR; 
		//				}
		//
		//				if (itemTitle.getText().trim().length() != 0)
		//				{
		//					password = itemTitle.getText().trim();
		//				}
		//				else
		//				{
		//					return VALIDATIONERROR; 
		//				}
		//
		//				if (itemType.getText().trim().length() != 0)
		//				{
		//					name = itemType.getText().trim();
		//				}
		//				else
		//				{
		//					return VALIDATIONERROR; 
		//				}
		//
		//				if (itemCategory.getText().trim().length() != 0)
		//				{
		//					address = itemCategory.getText().trim();
		//				}
		//				else
		//				{
		//					address = null; 
		//				}
		//
		//				if (itemCompany.getText().trim().length() != 0)
		//				{
		//					phone = Integer.valueOf(itemCompany.getText().trim());
		//				}
		//				else
		//				{
		//					phone = null; 
		//				}
		//
		//				AMS.updateStatusBar("Creating Account...");
		//
		//				if (manager.addItem(cid, password, name, address, phone))
		//				{
		//					AMS.updateStatusBar("Operation successful.");
		//					return OPERATIONSUCCESS; 
		//				}
		//				else
		//				{
		//					Toolkit.getDefaultToolkit().beep();
		//					AMS.updateStatusBar("Operation failed.");
		//					return OPERATIONFAILED; 
		//				}
		//			}
		//			catch (NumberFormatException ex)
		//			{
		//				// this exception is thrown when a string 
		//				// cannot be converted to a number
		//				return VALIDATIONERROR; 
		//			}
		//		}
	}	// end ProcessDeliveryDialog
}
