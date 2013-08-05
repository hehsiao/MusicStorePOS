
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; 
import java.sql.*;
import java.util.ArrayList;

public class CustomerController implements ActionListener, ExceptionListener
{
	private AMSView AMS = null;
	private CustomerModel customer = null;
	private ItemModel item = null;
	
	// Virtual Cart
	private class CartItem{
		int upc;
		int quantity;
	}
	private ArrayList<CartItem> vCart = new ArrayList<CartItem>();

	// constants used for describing the outcome of an operation
	public static final int OPERATIONSUCCESS = 0;
	public static final int OPERATIONFAILED = 1;
	public static final int VALIDATIONERROR = 2; 

	public CustomerController(AMSView AMS)
	{
		this.AMS = AMS;
		customer = new CustomerModel();
		item = new ItemModel();

		// register to receive exception events from branch
		customer.addExceptionListener(this);
	}

	/*
	 * This event handler gets called when the user makes a menu
	 * item selection.
	 */ 
	public void actionPerformed(ActionEvent e)
	{
		String actionCommand = e.getActionCommand();

		if (actionCommand.equals("Register Account"))
		{
			RegisterAccountInsertDialog iDialog = new RegisterAccountInsertDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("Purchase Items"))
		{
			CustomerLoginDialog iDialog = new CustomerLoginDialog(AMS);
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
	 * This class creates a dialog box for registering a customer account.
	 */
	class RegisterAccountInsertDialog extends JDialog implements ActionListener
	{
		private JTextField customerID = new JTextField(4);
		private JPasswordField customerPW = new JPasswordField(15);
		private JTextField customerName = new JTextField(10);
		private JTextField customerAddress = new JTextField(15);
		private JTextField customerPhone = new JTextField(10);

		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public RegisterAccountInsertDialog(JFrame parent)
		{
			super(parent, "Register Account", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Customer Account Registration"), 
					new EmptyBorder(5, 5, 5, 5)));

			// add the text field labels and text fields to inputPane
			// using the GridBag layout manager

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			/** CUSTOMER ID **/
			// create and place customer id label
			JLabel label = new JLabel("Customer ID: ", SwingConstants.RIGHT);	    
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(0, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place customer id field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(0, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(customerID, c);
			inputPane.add(customerID);

			/** CUSTOMER PASSWORD **/
			// create and place customer password label
			label = new JLabel("Password: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place customer password field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(customerPW, c);
			inputPane.add(customerPW);

			/** CUSTOMER NAME **/
			// create and place customer name label
			label = new JLabel("Name: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place customer name field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(customerName, c);
			inputPane.add(customerName);

			/** CUSTOMER ADDRESS **/
			// create and place customer address label
			label = new JLabel("Address: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place customer address field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(customerAddress, c);
			inputPane.add(customerAddress);

			/** CUSTOMER PHONE **/
			// create and place customer phone label
			label = new JLabel("Phone Number: ", SwingConstants.RIGHT);
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.insets = new Insets(5, 0, 0, 5);
			c.anchor = GridBagConstraints.EAST;
			gb.setConstraints(label, c);
			inputPane.add(label);

			// place customer phone field
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.insets = new Insets(5, 0, 0, 0);
			c.anchor = GridBagConstraints.WEST;
			gb.setConstraints(customerPhone, c);
			inputPane.add(customerPhone);

			// when the return key is pressed in the last field
			// of this form, the action performed by the OK button
			// is executed
			customerPhone.addActionListener(this);
			customerPhone.setActionCommand("OK");

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
		 * Event handler for the OK button in RegisterAccountInsertDialog
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
		 * Validates the text fields in RegisterAccountInsertDialog and then
		 * calls customer.insertCustomer() if the fields are valid.
		 * Returns the operation status, which is one of OPERATIONSUCCESS, 
		 * OPERATIONFAILED, VALIDATIONERROR.
		 */ 
		private int validateInsert()
		{
			try
			{
				Integer cid;
				String password;
				String name;
				String address;
				Integer phone;

				if (customerID.getText().trim().length() != 0)
				{
					cid = Integer.valueOf(customerID.getText().trim());

					// check for duplicates
					if (customer.findCustomer(cid.intValue()))
					{
						Toolkit.getDefaultToolkit().beep();
						AMS.updateStatusBar("Customer " + cid.toString() + " already exists!");
						return OPERATIONFAILED; 
					}
				}
				else
				{
					return VALIDATIONERROR; 
				}

				if (customerPW.getText().trim().length() != 0)
				{
					password = customerPW.getText().trim();
				}
				else
				{
					return VALIDATIONERROR; 
				}

				if (customerName.getText().trim().length() != 0)
				{
					name = customerName.getText().trim();
				}
				else
				{
					return VALIDATIONERROR; 
				}

				if (customerAddress.getText().trim().length() != 0)
				{
					address = customerAddress.getText().trim();
				}
				else
				{
					address = null; 
				}

				if (customerPhone.getText().trim().length() != 0)
				{
					phone = Integer.valueOf(customerPhone.getText().trim());
				}
				else
				{
					phone = null; 
				}

				AMS.updateStatusBar("Creating Account...");

				if (customer.insertCustomer(cid, password, name, address, phone))
				{
					AMS.updateStatusBar("Operation successful.");
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
	 * This class creates a dialog box for registering a customer account.
	 */
	class CustomerLoginDialog extends JDialog implements ActionListener
	{
		private JTextField customerID = new JTextField(4);
		private JPasswordField customerPW = new JPasswordField(15);
		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public CustomerLoginDialog(JFrame parent)
		{
			super(parent, "Online Purchase", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Customer Login"), 
					new EmptyBorder(5, 5, 5, 5)));

			// add the text field labels and text fields to inputPane
			// using the GridBag layout manager

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			/** CUSTOMER ID **/
			JLabel label = null;
			labelTextField("Customer ID", customerID, label, inputPane, gb, c);

			/** CUSTOMER PASSWORD **/
			// create and place customer password label
			labelTextField("Password", customerPW, label, inputPane, gb, c);

			// when the return key is pressed in the last field
			// of this form, the action performed by the OK button
			// is executed
			customerPW.addActionListener(this);
			customerPW.setActionCommand("Login");

			// panel for the OK and cancel buttons
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));

			JButton LoginButton = new JButton("Login");
			JButton cancelButton = new JButton("Cancel");
			LoginButton.addActionListener(this);
			LoginButton.setActionCommand("Login");
			cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					dispose();
				}
			});

			// add the buttons to buttonPane
			buttonPane.add(Box.createHorizontalGlue());
			buttonPane.add(LoginButton);
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
		 * Event handler for the OK button in RegisterAccountInsertDialog
		 */ 
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("Login"))
			{
				if (validateLogin() != VALIDATIONERROR)
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
		 * Validates the text fields in RegisterAccountInsertDialog and then
		 * calls customer.insertCustomer() if the fields are valid.
		 * Returns the operation status, which is one of OPERATIONSUCCESS, 
		 * OPERATIONFAILED, VALIDATIONERROR.
		 * 
		 * Verifies that customer exists, and that the password entered is correct.
		 */ 
		private int validateLogin()
		{
			try
			{
				Integer cid;
				String password;

				if (customerID.getText().trim().length() == 0 && customerPW.getText().trim().length() == 0){
					// If customerID or password was not entered, return validation error.

					return VALIDATIONERROR;
				}
				else {
					cid = Integer.valueOf(customerID.getText().trim());
					password = customerPW.getText().trim();

					if(!customer.findCustomer(cid.intValue())){
						AMS.updateStatusBar("Customer " + cid.toString() + " is not found." );
						return OPERATIONFAILED;
					}

					// check for account 
					if (customer.authenticateCustomer(cid.intValue(), password))
					{
						AMS.updateStatusBar("Customer " + cid.toString() + " login successfully!");
						// calls describe item dialog
						ItemSearchDialog iDialog = new ItemSearchDialog(AMS);
						iDialog.pack();
						AMS.centerWindow(iDialog);
						iDialog.setVisible(true);
						return OPERATIONSUCCESS;
					}
					else {
						Toolkit.getDefaultToolkit().beep();
						AMS.updateStatusBar("Customer " + cid.toString() + " might have a wrong id/password combinations!");
						return OPERATIONFAILED; 

					}

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
	 * This class creates a dialog box for registering a customer account.
	 */
	class ItemSearchDialog extends JDialog implements ActionListener
	{
		private JTextField itemUPC = new JTextField(4);
		private JTextField itemTitle = new JTextField(20);
		
		// CHANGE THE CATEGORY AND SINGER TO A LIST LATER?
		private JTextField categoryName = new JTextField(20);
		private JTextField singerName = new JTextField(20);
		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public ItemSearchDialog(JFrame parent)
		{
			super(parent, "Online Purchase", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Product Search by UPC"), 
					new EmptyBorder(5, 5, 5, 5)));

			// add the text field labels and text fields to inputPane
			// using the GridBag layout manager

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			/** Item UPC **/
			// search for Item UPC
			JLabel label = null;
			labelTextField("UPC", itemUPC, label, inputPane, gb, c);
			
			// this panel will contain the text field labels and the text fields.
			JPanel inputPane2 = new JPanel();
			inputPane2.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Product Search by Description"), 
					new EmptyBorder(5, 5, 5, 5)));

			// add the text field labels and text fields to inputPane
			// using the GridBag layout manager

			GridBagLayout gb2 = new GridBagLayout();
			GridBagConstraints c2 = new GridBagConstraints();
			inputPane2.setLayout(gb2);


			/** Item Title **/
			// search for Item Name
			labelTextField("Title", itemTitle, label, inputPane2, gb2, c2);

			/** Item Category **/
			// search for Item Name
			labelTextField("Category", categoryName, label, inputPane2, gb2, c2);

			/** Item Title **/
			// search for Item Name
			labelTextField("Lead Singer", singerName, label, inputPane2, gb2, c2);

			// when the return key is pressed in the last field
			// of this form, the action performed by the OK button
			// is executed
			itemTitle.addActionListener(this);
			itemTitle.setActionCommand("Search");

			// panel for the OK and cancel buttons
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));

			JButton SearchButton = new JButton("Search");
			JButton cancelButton = new JButton("Cancel");
			SearchButton.addActionListener(this);
			SearchButton.setActionCommand("Search");
			cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					dispose();
				}
			});

			// add the buttons to buttonPane
			buttonPane.add(Box.createHorizontalGlue());
			buttonPane.add(SearchButton);
			buttonPane.add(Box.createRigidArea(new Dimension(10,0)));
			buttonPane.add(cancelButton);

			contentPane.add(inputPane, BorderLayout.NORTH);
			contentPane.add(inputPane2, BorderLayout.CENTER);
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
		 * Event handler for the OK button in RegisterAccountInsertDialog
		 */ 
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("Search"))
			{
				if (validateInput() != VALIDATIONERROR)
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
		 * Validates the text fields in RegisterAccountInsertDialog and then
		 * calls customer.insertCustomer() if the fields are valid.
		 * Returns the operation status, which is one of OPERATIONSUCCESS, 
		 * OPERATIONFAILED, VALIDATIONERROR.
		 * 
		 */ 
		private int validateInput()
		{
			try
			{
				Integer upc = null;
				if(itemUPC.getText().trim().length() != 0){
					 upc = Integer.valueOf(itemUPC.getText().trim());
				}
				
				String title = itemTitle.getText().trim();
				String singer = singerName.getText().trim();
				String category = categoryName.getText().trim();
				
				ArrayList<String> searchCriteria = new ArrayList<String>();
				// If all fields were empty, show all product.
				if (upc == null && title.length() == 0
						&& singer.length() == 0 && category.length() == 0){
					return VALIDATIONERROR;
				}
				
				String text = "Searching for item that";
				if(upc != null){
					searchCriteria.add("upc = " + upc.toString());
					text += " has UPC " + upc.toString();
				}
				if(title.length() != 0){
					searchCriteria.add("title = \'" + title + "\'");
					
					text += " the title is " + title;
				}
				if(singer.trim().length() != 0){
					searchCriteria.add("singer = \'" + singer +"\'");
					text += " sang by " + singer;
				}
				if(category.length() != 0){
					searchCriteria.add("category = \'" + category +"\'");
					text += " in the " + category + " category";
				}
				AMS.updateStatusBar(text);
				
				

				// PROCESS QUERY AND LIST RESULTS 
				searchResults(searchCriteria);
				
				return OPERATIONSUCCESS;




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
	 * This method displays all branches in an editable JTable
	 */
	public void searchResults(ArrayList<String> description)
	{
		ResultSet rs = item.findItemByDescription(description);

		CustomTableModel model = new CustomTableModel(item.getConnection(), rs);
		CustomTable data = new CustomTable(model);

		model.addExceptionListener(this);
		data.addExceptionListener(this);

		AMS.addTable(data);
	}
	
	/*
	 * This method adds the item upc and quantity into the virtualcart
	 */
	public void addToCart(Integer upc, Integer quantity){
		CartItem item = new CartItem();
		item.upc = upc;
		item.quantity = quantity;
		
		vCart.add(item);
		AMS.updateStatusBar(quantity + " of Item " + upc + " is added to the cart.");
	}
	
	private void labelTextField(String labelName, JTextField fieldName, JLabel label,  JPanel inputPane, GridBagLayout gb,
			GridBagConstraints c) {
		label = new JLabel(labelName + ": ", SwingConstants.RIGHT);	    
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 0, 0, 5);
		c.anchor = GridBagConstraints.EAST;
		gb.setConstraints(label, c);
		inputPane.add(label);

		// place Item UPC field
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		gb.setConstraints(fieldName, c);
		inputPane.add(fieldName);
	}


}
