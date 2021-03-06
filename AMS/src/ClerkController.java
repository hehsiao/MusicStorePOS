
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*; 

import java.sql.*;
import java.util.ArrayList;

public class ClerkController implements ActionListener, ExceptionListener
{
	private AMSView AMS = null;
	private ClerkModel clerk = null;
	private ItemModel item = null;
	private PurchaseModel purchase = null;
	private ReturnModel ret = null;

	private Integer currReceiptID = 0;		// Receipt ID
	private Integer currRetID = 0;			// Return Receipt ID

	private JButton cashButton;
	// constants used for describing the outcome of an operation
	public static final int OPERATIONSUCCESS = 0;
	public static final int OPERATIONFAILED = 1;
	public static final int VALIDATIONERROR = 2; 

	public ClerkController(AMSView AMS)
	{
		this.AMS = AMS;
		clerk = new ClerkModel();
		item = new ItemModel();
		purchase = new PurchaseModel();
		ret = new ReturnModel();
		AMS.clearStatusBar();

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

		if (actionCommand.equals("Process Purchase"))
		{
			AMS.buttonPaneReturn.setVisible(false);
			AMS.buttonPane.setVisible(false);
			AMS.buttonPaneStore.setVisible(true);
			currReceiptID  = purchase.createPurchaseOrder();
			AMS.clearStatusBar();
			AMS.updateStatusBar("Hi Clerk, What would your customer like to purchase today?");
			return; 
		}

		if (actionCommand.equals("Process Return"))
		{
			AMS.buttonPane.setVisible(false);
			AMS.buttonPaneStore.setVisible(false);
			AMS.buttonPaneReturn.setVisible(true);
			// ENABLE BUTTONS AFTER RECEIPT ID IS ENTERED
			AMS.addToReturn.setEnabled(false);
			AMS.checkoutReturn.setEnabled(false);
			AMS.clearStatusBar();
			AMS.updateStatusBar("Please enter Reciept ID for return by pressing Search Return");
			return; 
		}

		if (actionCommand.equals("SearchItems")){
			ItemSearchDialog iDialog = new ItemSearchDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("addToCart")){
			ItemInsertDialog iDialog = new ItemInsertDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("checkout"))
		{
			CheckoutDialog iDialog = new CheckoutDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return;
		}

		if (actionCommand.equals("cancelOrder")){
			AMS.buttonPaneStore.setVisible(false);
			System.out.println("Cancel Order: " + currReceiptID);
			if(currReceiptID > 0)
				if(purchase.removePurchase(currReceiptID))
					AMS.updateStatusBar("Order has been cancelled.");

			return; 
		}

		if (actionCommand.equals("displayItem")){
			DisplayItemDialog iDialog = new DisplayItemDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("addToReturn")){
			ItemInsertRetDialog iDialog = new ItemInsertRetDialog(AMS);
			iDialog.pack();
			AMS.centerWindow(iDialog);
			iDialog.setVisible(true);
			return; 
		}

		if (actionCommand.equals("checkoutReturn"))
		{
			if(purchase.getCreditCard(currReceiptID) == null)
				AMS.updateStatusBar(ret.getReturnTotal(currRetID) + " are refunded in Cash.");
			else
				AMS.updateStatusBar(ret.getReturnTotal(currRetID) + " are refunded to Credit Card ending in " + purchase.getCreditCard(currReceiptID));
			AMS.buttonPaneReturn.setVisible(false);
			return;
		}

		if (actionCommand.equals("cancelReturn")){
			AMS.buttonPaneReturn.setVisible(false);
			System.out.println("Cancel Return: " + currRetID);
			if(currRetID > 0)
				if(ret.removeReturn(currRetID))
					AMS.updateStatusBar("Return has been cancelled.");


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
	 * This class creates a dialog box for registering a clerk account.
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
			super(parent, "Purchase Search", true);
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
		 * calls clerk.insertClerk() if the fields are valid.
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

				String text = "Searching for item that";
				if(upc != null){
					searchCriteria.add("i.upc = " + upc.toString());
					text += " has UPC " + upc.toString();
				}
				if(title.length() != 0){
					searchCriteria.add("title = \'" + title + "\'");

					text += " the title is " + title;
				}
				if(singer.trim().length() != 0){
					searchCriteria.add("name = \'" + singer +"\'");
					text += " sang by " + singer;
				}
				if(category.length() != 0){
					searchCriteria.add("category = \'" + category +"\'");
					text += " in the " + category + " category";
				}
				if(searchCriteria.isEmpty()){
					text = "Showing all Items";
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
	 * This class creates a dialog box for view cart dialog.
	 */
	class CheckoutDialog extends JDialog implements ActionListener
	{

		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public CheckoutDialog(JFrame parent)
		{
			super(parent, "Checkout", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// TODO: ADD TOTAL PRICE text and prompt user for payment method
			// OR if nothing is there, prompt to add more items first. disable buttons?

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));

			JButton creditButton = new JButton("Credit Card");
			creditButton.addActionListener(this);
			creditButton.setActionCommand("creditcard");

			JButton cashButton = new JButton("Cash");
			cashButton.addActionListener(this);
			cashButton.setActionCommand("cash");

			JButton shoppingButton = new JButton("Continue Shopping");
			shoppingButton.setActionCommand("Continue");
			shoppingButton.addActionListener(this);

			// add the buttons to buttonPane
			buttonPane.add(cashButton, BorderLayout.WEST);
			buttonPane.add(creditButton, BorderLayout.EAST);
			buttonPane.add(shoppingButton, BorderLayout.SOUTH);

			contentPane.add(buttonPane, BorderLayout.CENTER);

			addWindowListener(new WindowAdapter() 
			{
				public void windowClosing(WindowEvent e)
				{
					dispose();
				}
			});
		}

		public void actionPerformed(ActionEvent e) 
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("creditcard"))
			{
				dispose();
				CreditCardDialog iDialog = new CreditCardDialog(AMS);
				iDialog.pack();
				AMS.centerWindow(iDialog);
				iDialog.setVisible(true);
				return; 
			}

			if (actionCommand.equals("cash"))
			{
				dispose();
				// TODO: add printout on screen
				AMS.updateStatusBar("Please pay " + purchase.getPurchaseTotal(currReceiptID) + " in cash. Thank you for Shopping with us.");
				AMS.buttonPaneStore.setVisible(false);
				return; 
			}


			if (actionCommand.equals("Continue"))
			{
				dispose();
			}
		}
	}	// end CheckoutDialog

	/*
	 * This class creates a dialog box for credit card dialog
	 */
	class CreditCardDialog extends JDialog implements ActionListener
	{
		private JTextField ccNumber = new JTextField(16);
		private JTextField ccExpiry = new JTextField(4);

		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public CreditCardDialog(JFrame parent)
		{
			super(parent, "Credit Card Information", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Enter Payment Information"), 
					new EmptyBorder(5, 5, 5, 5)));


			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			/** Credit Card Number **/
			JLabel label = null;
			labelTextField("Credit Card Number", ccNumber, label, inputPane, gb, c);

			/** Expiry Date **/
			labelTextField("Expiry Date", ccExpiry, label, inputPane, gb, c);

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));

			JButton submitButton = new JButton("Submit Order");
			submitButton.addActionListener(this);
			submitButton.setActionCommand("Submit Order");

			JButton continueButton = new JButton("Add more Item");
			continueButton.setActionCommand("Continue");
			continueButton.addActionListener(this);

			cashButton = new JButton("Pay Cash");
			cashButton.setActionCommand("cash");
			cashButton.addActionListener(this);
			cashButton.setVisible(false);
			
			// add the buttons to buttonPane
			buttonPane.add(submitButton, BorderLayout.WEST);
			buttonPane.add(continueButton, BorderLayout.EAST);
			buttonPane.add(cashButton, BorderLayout.CENTER);

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

		public void actionPerformed(ActionEvent e) 
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("Submit Order"))
			{
				if(ccNumber.getText().trim().length() != 16 && ccExpiry.getText().trim().length() != 5){
					Toolkit.getDefaultToolkit().beep();

					// display a popup to inform the user of the validation error
					JOptionPane errorPopup = new JOptionPane();
					errorPopup.showMessageDialog(this, "Invalid CreditCard Number (16 digits) & Expiry Date (MM/YY). Try again or please pay by Cash", "Error", JOptionPane.ERROR_MESSAGE);
					cashButton.setVisible(true);
				}
				else{
					purchase.customerPayNowCredit(currReceiptID, ccNumber.getText().trim(), ccExpiry.getText().trim());
					AMS.updateStatusBar("Received payment for Purchase ID " + currReceiptID + " from credit card ending in " + ccNumber.getText().trim().substring(12));
					AMS.buttonPaneStore.setVisible(false);
					dispose();

				}
			}

			if (actionCommand.equals("cash"))
			{
				dispose();
				// TODO: add printout on screen
				AMS.updateStatusBar("Please pay " + purchase.getPurchaseTotal(currReceiptID) + " in cash. Thank you for Shopping with us.");
				AMS.buttonPaneStore.setVisible(false);
				return; 
			}
			
			if (actionCommand.equals("Continue"))
			{
				dispose();
			}
		}
	}	// end CreditCardtDialog

	/*
	 * This class creates a dialog box for adding an item to inventory.
	 */
	class ItemInsertDialog extends JDialog implements ActionListener
	{

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
				Integer quantity;

				if (itemUPC.getText().trim().length() != 0)
				{
					upc = Integer.valueOf(itemUPC.getText().trim());

				}
				else
				{
					//or operationfailed
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

				addToCart(upc, quantity);
			}
			catch (NumberFormatException ex)
			{
				// this exception is thrown when a string 
				// cannot be converted to a number
				return VALIDATIONERROR; 
			}
			return 0;
		}
	}

	/*
	 * This class creates a dialog box for credit card dialog
	 */
	class DisplayItemDialog extends JDialog implements ActionListener
	{
		private JTextField receiptIDtf = new JTextField(15);

		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public DisplayItemDialog(JFrame parent)
		{
			super(parent, "Receipt ID", true);
			setResizable(false);

			JPanel contentPane = new JPanel(new BorderLayout());
			setContentPane(contentPane);
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// this panel will contain the text field labels and the text fields.
			JPanel inputPane = new JPanel();
			inputPane.setBorder(BorderFactory.createCompoundBorder(
					new TitledBorder(new EtchedBorder(), "Enter Previous Receipt ID"), 
					new EmptyBorder(5, 5, 5, 5)));


			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			inputPane.setLayout(gb);

			/** Reciept ID**/
			JLabel label = null;
			labelTextField("Reciept ID", receiptIDtf, label, inputPane, gb, c);

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));

			JButton submitButton = new JButton("Search for ID");
			submitButton.addActionListener(this);
			submitButton.setActionCommand("searchReceiptID");

			JButton continueButton = new JButton("Cancel Return");
			continueButton.setActionCommand("continue");
			continueButton.addActionListener(this);

			// add the buttons to buttonPane
			buttonPane.add(Box.createHorizontalGlue());
			buttonPane.add(submitButton);
			buttonPane.add(Box.createRigidArea(new Dimension(10,0)));
			buttonPane.add(continueButton);

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

		public void actionPerformed(ActionEvent e) 
		{
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("searchReceiptID"))
			{
				System.out.println("I am here");
				if(receiptIDtf.getText().trim().length() > 0){
					try {
						currReceiptID = Integer.parseInt(receiptIDtf.getText().trim());
						if(!purchase.getPurchase(currReceiptID)){
							Toolkit.getDefaultToolkit().beep();

							// display a popup to inform the user of the validation error
							JOptionPane errorPopup = new JOptionPane();
							errorPopup.showMessageDialog(this, "Receipt does not exist", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} catch (NumberFormatException nfe){
						Toolkit.getDefaultToolkit().beep();

						// display a popup to inform the user of the validation error
						JOptionPane errorPopup = new JOptionPane();
						errorPopup.showMessageDialog(this, "Invalid Character entered", "Error", JOptionPane.ERROR_MESSAGE);
					}

				}
				currRetID = ret.createReturnOrder(currReceiptID);
				AMS.updateStatusBar("Receipt " + currReceiptID + " is found.  Which of the following item would you like to return?");
				displayPurchaseDetail(currReceiptID);
				AMS.addToReturn.setEnabled(true);
				AMS.checkoutReturn.setEnabled(true);
				dispose();
			}

			if (actionCommand.equals("continue"))
			{
				dispose();
			}
		}
	}	// end DisplayItemtDialog

	/*
	 * This class creates a dialog box for adding an item to inventory.
	 */
	class ItemInsertRetDialog extends JDialog implements ActionListener
	{

		JTextField itemUPC = new JTextField(10);
		JTextField itemQuantity = new JTextField(10);

		/*
		 * Constructor. Creates the dialog's GUI.
		 */
		public ItemInsertRetDialog(JFrame parent)
		{
			super(parent, "Return Item", true);
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
				Integer quantity;

				if (itemUPC.getText().trim().length() != 0)
				{
					upc = Integer.valueOf(itemUPC.getText().trim());
				}
				else
				{
					//or operationfailed
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

				addToReturn(upc, quantity, currReceiptID);
				displayReturnItem(currRetID);
				dispose();
			}
			catch (NumberFormatException ex)
			{
				// this exception is thrown when a string 
				// cannot be converted to a number
				return VALIDATIONERROR; 

			}
			dispose();
			return 0;
		}


		private void addToReturn(Integer upc, Integer quantity, Integer retReceiptID) {
			int purchased = purchase.getPurchasedQuantity(upc, retReceiptID);

			// Check if we have enough stock, set currentStock as quantity if quantity wanted is more than currentStock
			if(quantity > purchased){
				quantity = purchased;
				AMS.updateStatusBar("Exceeded original purchased amount.");
			}

			// Add or Update the quantity to purchaseItem.
			if(!ret.findItemInCart(upc,currRetID))
				ret.addItemToReturn(upc, quantity, currRetID);
			else
				ret.updateItemToReturn(upc, quantity, currRetID);
			item.restockItem(upc, quantity);
			return;
		}



	}

	/*
	 * This method displays all branches in an editable JTable
	 */
	public void searchResults(ArrayList<String> description)
	{
		ResultSet rs = item.findItemByDescription(description);
		if(rs != null){
			CustomTableModel model = new CustomTableModel(item.getConnection(), rs);
			CustomTable data = new CustomTable(model);

			model.addExceptionListener(this);
			data.addExceptionListener(this);

			AMS.addTable(data);
		}
		else {
			AMS.updateStatusBar("Unable to find item by Descriptions. Please try other terms, or leave all criteria empty.");
		}
	}

	/*
	 * This method displays all Items in a non-editable JTable
	 */
	private void displayPurchaseDetail(int receiptID)
	{
		AMS.updateStatusBar("Your order has been updated.");
		AMS.updateStatusBar("Total Price for current order: " + purchase.getPurchaseTotal(currReceiptID));

		ResultSet rs = purchase.getPurchaseItems(receiptID);

		// CustomTableModel maintains the result set's data, e.g., if  
		// the result set is updatable, it will update the database
		// when the table's data is modified.  
		CustomTableModel model = new CustomTableModel(purchase.getConnection(), rs);
		CustomTable data = new CustomTable(model);

		// register to be notified of any exceptions that occur in the model and table
		model.addExceptionListener(this);
		data.addExceptionListener(this);

		// Adds the table to the scrollpane.
		// By default, a JTable does not have scroll bars.
		AMS.addTable(data);
	}

	/*
	 * This method adds the item upc and quantity into the virtualcart
	 */
	public void addToCart(Integer upc, Integer quantity){

		int currentStock = item.getStock(upc);

		// Check if we have enough stock, set currentStock as quantity if quantity wanted is more than currentStock
		if(currentStock <= 0){
			AMS.updateStatusBar("Sorry, the last item with upc "+ upc +" is purchased by another customer");
			return;
		}
		else if (quantity > currentStock){
			quantity = currentStock;
			AMS.updateStatusBar("We only have " + currentStock + " available and added those to your cart.");
		}
		// Add or Update the quantity to purchaseItem.
		if(!purchase.findItemInCart(upc,currReceiptID))
			purchase.addItemToPurchase(upc, quantity, currReceiptID);
		else
			purchase.updateItemToPurchase(upc, quantity, currReceiptID);
		item.sellItem(upc, quantity);
		AMS.checkout.setEnabled(true);
		displayPurchaseDetail(currReceiptID);
		return;
	}

	public void displayReturnItem(Integer retID) {
		AMS.updateStatusBar("Your return has been updated.");
		AMS.updateStatusBar("Total Refund: " + ret.getReturnTotal(retID));
		ResultSet rs = ret.getReturnItems(retID);

		// CustomTableModel maintains the result set's data, e.g., if  
		// the result set is updatable, it will update the database
		// when the table's data is modified.  
		CustomTableModel model = new CustomTableModel(ret.getConnection(), rs);
		CustomTable data = new CustomTable(model);

		// register to be notified of any exceptions that occur in the model and table
		model.addExceptionListener(this);
		data.addExceptionListener(this);

		// Adds the table to the scrollpane.
		// By default, a JTable does not have scroll bars.
		AMS.addTable(data);
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
