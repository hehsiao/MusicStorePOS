
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; 
import java.sql.*;

public class CustomerController implements ActionListener, ExceptionListener
{
	private AMSView AMS = null;
	private CustomerModel customer = null;

	// constants used for describing the outcome of an operation
	public static final int OPERATIONSUCCESS = 0;
	public static final int OPERATIONFAILED = 1;
	public static final int VALIDATIONERROR = 2; 

	public CustomerController(AMSView AMS)
	{
		this.AMS = AMS;
		customer = new CustomerModel();

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
}
