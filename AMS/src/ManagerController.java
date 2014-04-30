
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; 
import java.util.Date;
import java.util.TimeZone;

import oracle.sql.DATE;


import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ManagerController implements ActionListener, ExceptionListener
{
	private AMSView AMS = null;
	private ManagerModel manager = null;
	private ItemModel item = null;

	// constants used for describing the outcome of an operation
	public static final int OPERATIONSUCCESS = 0;
	public static final int OPERATIONFAILED = 1;
	public static final int VALIDATIONERROR = 2; 

	public ManagerController(AMSView AMS)
	{
		this.AMS = AMS;
		manager = new ManagerModel();
		item = new ItemModel();

		// register to receive exception events from branch
		manager.addExceptionListener(this);
		item.addExceptionListener(this);
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
			AMS.buttonPane.setVisible(false);
			ItemInsertDialog iDialog = new ItemInsertDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("Edit Items"))
		{
			AMS.buttonPane.setVisible(false);
			editAllItems();
			return; 
		}

		if (actionCommand.equals("Process Delivery"))
		{
			AMS.buttonPane.setVisible(false);
			ProcessDeliveryDialog iDialog = new ProcessDeliveryDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("View Daily Sales Report"))
		{
			AMS.buttonPane.setVisible(false);
			DailySalesReportDialog iDialog = new DailySalesReportDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("View Top Selling Items"))
		{
			AMS.buttonPane.setVisible(false);
			TopSalesItemsReportDialog iDialog = new TopSalesItemsReportDialog(AMS);
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
	 * This method displays all branches in an editable JTable
	 */
	private void editAllItems()
	{
		ResultSet rs = item.editItem();

		CustomTableModel model = new CustomTableModel(item.getConnection(), rs);
		CustomTable data = new CustomTable(model);

		model.addExceptionListener(this);
		data.addExceptionListener(this);

		AMS.addTable(data);
	}


	/*
	 * This class creates a dialog box for adding an item to inventory.
	 */
	class ItemInsertDialog extends JDialog implements ActionListener
	{

		//JFormattedTextField itemUPC = new JFormattedTextField(numForm);
		//JFormattedTextField itemQuantity = new JFormattedTextField(numForm);
		//JFormattedTextField itemPrice = new JFormattedTextField(numForm);

		JTextField itemUPC = new JTextField(10);
		JTextField itemQuantity = new JTextField(10);
		JTextField itemPrice = new JTextField(10);


		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public ItemInsertDialog(JFrame parent)
		{
			super(parent, "Add Items", true);
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


			// create and place price label
			label = new JLabel("Price(Optional): ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place price field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(itemPrice, c);
			inputPane.add(itemPrice);

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
				if (validateInsert() != VALIDATIONERROR)
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
		private int validateInsert()
		{
			try
			{
				Integer upc;
				double price;
				Integer quantity;

				if (itemUPC.getText().trim().length() != 0)
				{
					upc = Integer.valueOf(itemUPC.getText().trim());

					// check for duplicates
					if (!manager.findItem(upc.intValue()))
					{
						AMS.updateStatusBar("UPC not Found!");
						return VALIDATIONERROR;
					}
				}
				else
				{
					//or operationfailed
					return VALIDATIONERROR; 
				}

				if (itemPrice.getText().trim().length() != 0)
				{
					price = Double.valueOf(itemPrice.getText().trim());
				}
				else
				{
					price =0;
				}


				if (itemQuantity.getText().trim().length() != 0)
				{
					quantity = Integer.valueOf(itemQuantity.getText().trim());
				}
				else
				{
					return VALIDATIONERROR; 
				}

				if (manager.insertItem(upc, price, quantity))
				{
					AMS.updateStatusBar("Item Updated");
					displayUpdateItem(upc);
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

	private void displayUpdateItem(int upc)
	{
		ResultSet rs = item.findItemByUPC(upc);

		// CustomTableModel maintains the result set's data, e.g., if  
		// the result set is updatable, it will update the database
		// when the table's data is modified.  
		CustomTableModel model = new CustomTableModel(item.getConnection(), rs);
		CustomTable data = new CustomTable(model);

		// register to be notified of any exceptions that occur in the model and table
		model.addExceptionListener(this);
		data.addExceptionListener(this);

		// Adds the table to the scrollpane.
		// By default, a JTable does not have scroll bars.
		AMS.addTable(data);
	}


	class ProcessDeliveryDialog extends JDialog implements ActionListener
	{
		String[] receiptID = manager.findArray("RIDwithoutDD");	// Use SQL to pull receiptIDs that currently don't have a delivered date.
		private JComboBox purchaseReceiptID = new JComboBox(receiptID);



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

			// panel for the OK and cancel buttons
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));

			JButton OKButton = new JButton("Process Delivery");
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
		 * Event handler for the OK button in ProcessDeliveryDialogue
		 */ 
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("OK"))
			{
				if (validateInsert() != VALIDATIONERROR)
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


		private int validateInsert()
		{
			try
			{
				int id= Integer.parseInt((String)purchaseReceiptID.getSelectedItem());



//				// check for duplicates
//				if (manager.findRID(id))
//				{								
//
//					//	Toolkit.getDefaultToolkit().beep();
//					//	return OPERATIONFAILED; 
//				}
//
//
//				else
//				{
//					return VALIDATIONERROR; 
//				}


				AMS.updateStatusBar("Processing Delivery of Order...");

				if (manager.setDeliveryDate( Integer.valueOf(id)))
				{
					AMS.updateStatusBar("Purchase ID " + Integer.valueOf(id) + " is set to delivered");
					
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
	}	// end ProcessDeliveryDialog

	/*
	 * This class creates a dialog box for viewing a daily sales report.
	 */
	class DailySalesReportDialog extends JDialog implements ActionListener
	{
		private JTextField purchaseDate = new JTextField(10);

		// Given the purchase date this field automatically retrieves all the information for the sales report
		private JTextArea dailySalesReport = new JTextArea(25, 50);

		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public DailySalesReportDialog(JFrame parent)
		{
			super(parent, "View Daily Sales Report", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Daily Sales Report"), 
					new EmptyBorder(5, 5, 5, 5)));

			// add the text field labels and text fields to inputPane
			// using the GridBag layout manager

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			// create and place purchase date label
			JLabel label = new JLabel("Sales Date(yy-MM-dd): ", SwingConstants.RIGHT);	    
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(0, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place purchase date field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(0, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(purchaseDate, c);
			inputPane.add(purchaseDate);

			// place daily sales report field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			dailySalesReport.setBackground(Color.lightGray);
			dailySalesReport.setEditable(false);
			gb.setConstraints(dailySalesReport, c);
			inputPane.add(dailySalesReport);

			// when the return key is pressed in the last field
			// of this form, the action performed by the OK button
			// is executed
			purchaseDate.addActionListener(this);
			purchaseDate.setActionCommand("OK");

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

		private void showDailySalesReport(String date)
		{
			ResultSet rs = manager.selectDailyView(date);
			//ResultSet rs1 = manager.selectDSView(date);
			//ResultSet rs = manager.selectTotalCategoryView();
			
			if (rs!=null) {
			
			// CustomTableModel maintains the result set's data, e.g., if  
			// the result set is updatable, it will update the database
			// when the table's data is modified.  
			CustomTableModel model = new CustomTableModel(manager.getConnection(), rs);
			CustomTable data = new CustomTable(model);
			//CustomTableModel model1 =  new CustomTableModel(manager.getConnection(), rs1);
			//CustomTable data1 = new CustomTable(model1);

			// register to be notified of any exceptions that occur in the model and table
			//	model.addExceptionListener(this);
			//	data.addExceptionListener(this);

			// Adds the table to the scrollpane.
			// By default, a JTable does not have scroll bars.
			AMS.addTable(data);
			//AMS.addTable(data1);
			}
			else {
				AMS.updateStatusBar("Unable to show daily sales report..");
				
				
			}
		}

		/*
		 * Event handler for the OK button in AddItemsDialog
		 */ 
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("OK"))
			{
				try {
					if (validateInsert() != VALIDATIONERROR)
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
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}	
			}
		}


		/*
		 * Validates the text fields in AddItemsDialog and then
		 * calls manager.addItem() if the fields are valid.
		 * Returns the operation status, which is one of OPERATIONSUCCESS, 
		 * OPERATIONFAILED, VALIDATIONERROR.
		 */ 
		@SuppressWarnings("deprecation")
		private int validateInsert() throws ParseException
		{
			try
			{
				Date utilDate = new Date();
				 java.sql.Date sqlDate;
				String dateString;
				
				if (purchaseDate.getText().trim().length() != 0)
				{
					String pd = purchaseDate.getText().trim();
					dateString = "20"+pd;
					System.out.println(pd);

					if (pd.length() != 8) {
						return VALIDATIONERROR;
					}
					
					
					//date=DATE.fromText(arg0, arg1, arg2);

					DateFormat format = new SimpleDateFormat("yy-MM-dd");
					format.setTimeZone(TimeZone.getTimeZone("GMT-7"));
					System.out.println("Current Time: "+utilDate);
				    utilDate = format.parse(pd);//if wrong format, "invalid format"
					System.out.println("Current Time: "+utilDate);
				  			
				
				    sqlDate= new java.sql.Date(utilDate.getTime());
				    System.out.println("utilDate:" + utilDate);
				    System.out.println("sqlDate:" + sqlDate);
				   
					// check for duplicates
					if (manager.findDate(sqlDate))
					{
						System.out.println("BYE");
					}
				}
				else
				{
					return VALIDATIONERROR; 
				}

				AMS.updateStatusBar("Creating Daily Sales Report...");
				showDailySalesReport(dateString);

				return OPERATIONSUCCESS;


				//						if (manager.findRID(1))//stub
				//						{
				//							AMS.updateStatusBar("Operation successful.");
				//							return OPERATIONSUCCESS; 
				//						}
				//						else
				//						{
				//							Toolkit.getDefaultToolkit().beep();
				//							AMS.updateStatusBar("Operation failed.");
				//							return OPERATIONFAILED; 
				//						}

			}
			catch (NumberFormatException ex)
			{
				// this exception is thrown when a string 
				// cannot be converted to a number
				return VALIDATIONERROR; 
			}

		}
	}	// end DailySalesReportDialog

	/*
	 * This class creates a dialog box for viewing the top selling items report.
	 */
	class TopSalesItemsReportDialog extends JDialog implements ActionListener
	{
		private JTextField purchaseDate = new JTextField(10);

		private JTextField topSalesItem = new JTextField(5);

		// Given the purchase date and number of top sales items this field automatically retrieves all the information for the sales report
		private JTextArea topSalesItemReport = new JTextArea(25, 50);

		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public TopSalesItemsReportDialog(JFrame parent)
		{
			super(parent, "View Top Selling Items", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Top Selling Items Report"), 
					new EmptyBorder(5, 5, 5, 5)));

			// add the text field labels and text fields to inputPane
			// using the GridBag layout manager

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			// create and place purchase date label
			JLabel label = new JLabel("Sales Date(YY-MM-DD): ", SwingConstants.RIGHT);	    
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(0, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place purchase date field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(0, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(purchaseDate, c);
			inputPane.add(purchaseDate);

			// create and place top sales item label
			label = new JLabel("Top # Sales Item: ", SwingConstants.RIGHT);	    
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(0, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place top sales item field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(0, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(topSalesItem, c);
			inputPane.add(topSalesItem);

			// place top selling items report field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			topSalesItemReport.setBackground(Color.lightGray);
			topSalesItemReport.setEditable(false);
			gb.setConstraints(topSalesItemReport, c);
			inputPane.add(topSalesItemReport);

			// when the return key is pressed in the last field
			// of this form, the action performed by the OK button
			// is executed
			topSalesItem.addActionListener(this);
			topSalesItem.setActionCommand("OK");

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
				try {
					if (validateInsert() != VALIDATIONERROR)
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
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}	
			}
		}
		
		private void showTopSalesItemsReport(int num, java.sql.Date date)
		{
			
		//	ResultSet rs = manager.selectDSView(date);
			ResultSet rs = manager.selectTOPView(num);
	
			
			if (rs!=null) {
				
				// CustomTableModel maintains the result set's data, e.g., if  
				// the result set is updatable, it will update the database
				// when the table's data is modified.  
				CustomTableModel model = new CustomTableModel(manager.getConnection(), rs);
				CustomTable data = new CustomTable(model);
				//CustomTableModel model1 =  new CustomTableModel(manager.getConnection(), rs1);
				//CustomTable data1 = new CustomTable(model1);

				// register to be notified of any exceptions that occur in the model and table
				//	model.addExceptionListener(this);
				//	data.addExceptionListener(this);

				// Adds the table to the scrollpane.
				// By default, a JTable does not have scroll bars.
				AMS.addTable(data);
				//AMS.addTable(data1);
				}
				else {
					AMS.updateStatusBar("Unable to show top sales report..");
					
					
				}
			
		}

		/** VALIDATION REQUIRED: SETTING UP UI FIRST **/
		//		/*
		//		 * Validates the text fields in TopSalesItemsReportDialog and then
		//		 * calls manager.() if the fields are valid.
		//		 * Returns the operation status, which is one of OPERATIONSUCCESS, 
		//		 * OPERATIONFAILED, VALIDATIONERROR.
		//		 */ 
				private int validateInsert() throws ParseException
				{
						try
						{
							Date utilDate = new Date();
							 java.sql.Date sqlDate = null;
							 int topnumber;

							if (purchaseDate.getText().trim().length() != 0)
							{
								String pd = purchaseDate.getText().trim();
								System.out.println(pd);

								if (pd.length() != 8) {
									return VALIDATIONERROR;
								}
								
								
								//date=DATE.fromText(arg0, arg1, arg2);

								DateFormat format = new SimpleDateFormat("yy-MM-dd");
								format.setTimeZone(TimeZone.getTimeZone("GMT-7"));
								System.out.println("Current Time: "+utilDate);
							    utilDate = format.parse(pd);//if wrong format, "invalid format"
								System.out.println("Current Time: "+utilDate);
							  			
							
							    sqlDate= new java.sql.Date(utilDate.getTime());
							    System.out.println("utilDate:" + utilDate);
							    System.out.println("sqlDate:" + sqlDate);
							   
								// check for duplicates
								if (manager.findDate(sqlDate))
								{
									System.out.println("BYE");
								}
							}
							
							if (topSalesItem.getText().trim().length()!=0)
							{
								topnumber = Integer.parseInt(topSalesItem.getText());
							}
							
							else
							{
								return VALIDATIONERROR; 
							}

							AMS.updateStatusBar("Creating Top Sales Items Report...");
					
							showTopSalesItemsReport(topnumber,sqlDate);
							return OPERATIONSUCCESS;



						}
						catch (NumberFormatException ex)
						{
							// this exception is thrown when a string 
							// cannot be converted to a number
							return VALIDATIONERROR; 
						}

				
				}
	}	// end TopSalesItemsReportDialog
}

