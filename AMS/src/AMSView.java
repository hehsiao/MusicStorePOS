import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/*
 * AMSView allows a user to view and manipulate the database system
 */ 
public class AMSView extends JFrame
{
	// initial position of the main frame
	private int framePositionX;
	private int framePositionY;

	// initial size of main frame
	private Rectangle frameBounds = null;

	// the status text area for displaying error messages
	private JTextArea statusField = new JTextArea(5,0);

	// the scrollpane that will hold the table of database data
	private JScrollPane tableScrPane = new JScrollPane();

	// the manager menu
	private JMenu manager;

	// the clerk menu
	private JMenu clerk;

	// the customer menu
	private JMenu customer;

	// the debug menu
	private JMenu debug;
	
	public JPanel southPane = new JPanel();

	// command buttons for purchasing
	GridLayout buttonLayout = new GridLayout(1,4);
	public JPanel buttonPane = new JPanel();
	
	// Buttons for Returns
	GridLayout buttonLayoutReturn = new GridLayout(1,4);
	public JPanel buttonPaneReturn = new JPanel();

	GridLayout buttonLayoutStore = new GridLayout(1,4);
	public JPanel buttonPaneStore = new JPanel();
	
	public JButton addToReturn;
	public JButton checkoutReturn;
	public JButton cancelReturn;
	public JButton checkout;

	/*
	 * Default constructor. Constructs the main window.
	 */ 
	public AMSView()
	{
		// should call the constructor of the superclass first
		super("AMS");
		setSize(650,450);

		// the content pane;
		// components will be spaced vertically 10 pixels apart
		JPanel contentPane = new JPanel(new BorderLayout(0, 10));
		setContentPane(contentPane);

		// leave some space around the content pane
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// setup the menubar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// indent first menu
		menuBar.add(Box.createRigidArea(new Dimension(10,0)));

		// sets up the manager, clerk, and customer menu and adds it to the menu bar
		setupManagerMenu(menuBar);
		setupClerkMenu(menuBar);
		setupCustomerMenu(menuBar);
		setupDebugMenu(menuBar);

		// the scrollpane for the status text field
		JScrollPane statusScrPane = new JScrollPane(statusField);
		statusScrPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		statusScrPane.setBorder(BorderFactory.createLoweredBevelBorder());

		// set status field properties
		statusField.setEditable(false);
		statusField.setLineWrap(true);
		statusField.setWrapStyleWord(true);

		southPane.add(buttonPane, BorderLayout.CENTER);
		southPane.add(buttonPaneStore, BorderLayout.WEST);
		southPane.add(buttonPaneReturn, BorderLayout.EAST);
		
		// add the panes to the content pane
		contentPane.add(tableScrPane, BorderLayout.CENTER);
		contentPane.add(statusScrPane, BorderLayout.NORTH);
		contentPane.add(southPane, BorderLayout.SOUTH);

		// center the main window
		Dimension screenSize = getToolkit().getScreenSize();
		frameBounds = getBounds();
		framePositionX = (screenSize.width - frameBounds.width)/2;
		framePositionY = (screenSize.height - frameBounds.height)/2;
		setLocation(framePositionX, framePositionY);

		// anonymous inner class to terminate program
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});	
	}

	/*
	 * Adds menu items to the Manager menu and then
	 * adds the menu to the menubar
	 */ 
	private void setupManagerMenu(JMenuBar mb)
	{
		manager = new JMenu("Manager");

		// when alt-a is pressed on the keyboard, the menu will appear
		manager.setMnemonic(KeyEvent.VK_M);

		createMenuItem(manager, "Add Items", 
				KeyEvent.VK_I, "Add Items");

		createMenuItem(manager, "Edit Items", 
				KeyEvent.VK_I, "Edit Items");
		
		createMenuItem(manager, "Process Delivery", 
				KeyEvent.VK_D, "Process Delivery");

		createMenuItem(manager, "View Daily Sales Report", 
				KeyEvent.VK_S, "View Daily Sales Report");

		createMenuItem(manager, "View Top Selling Items", 
				KeyEvent.VK_T, "View Top Selling Items");

		mb.add(manager);
	}

	/*
	 * Adds menu items to the Clerk menu and then
	 * adds the menu to the menubar
	 */ 
	private void setupClerkMenu(JMenuBar mb)
	{
		clerk = new JMenu("Clerk");

		// when alt-e is pressed on the keyboard, the menu will appear
		clerk.setMnemonic(KeyEvent.VK_E);

		createMenuItem(clerk, "Process Purchase", 
				KeyEvent.VK_P, "Process Purchase");

		createMenuItem(clerk, "Process Return", 
				KeyEvent.VK_R, "Process Return");

		mb.add(clerk);
	}

	/*
	 * Adds menu items to the Customer menu and then
	 * adds the menu to the menubar
	 */ 
	private void setupCustomerMenu(JMenuBar mb)
	{
		customer = new JMenu("Customer");

		// when alt-c is pressed on the keyboard, the menu will appear
		customer.setMnemonic(KeyEvent.VK_C);

		createMenuItem(customer, "Register Account", 
				KeyEvent.VK_N, "Register Account");

		createMenuItem(customer, "Purchase Items", 
				KeyEvent.VK_I, "Purchase Items");

		mb.add(customer);
	}

	/*
	 * Adds menu items to the Debug menu and then
	 * adds the menu to the menubar
	 */ 
	private void setupDebugMenu(JMenuBar mb)
	{
		debug = new JMenu("Debug");

		// when alt-a is pressed on the keyboard, the menu will appear
		debug.setMnemonic(KeyEvent.VK_D);

		createMenuItem(debug, "Show Items", 
				-1, "Item");

		createMenuItem(debug, "Show Lead Singer", 
				-1, "LeadSinger");

		createMenuItem(debug, "Show Songs", 
				-1, "HasSong");

		createMenuItem(debug, "Show Purchases", 
				-1, "Purchase");

		createMenuItem(debug, "Show Purchase Items", 
				-1, "PurchaseItem");

		createMenuItem(debug, "Show Customer", 
				-1, "Customer");

		createMenuItem(debug, "Show Return", 
				-1, "Return");

		createMenuItem(debug, "Show Return Item", 
				-1, "ReturnItem");


		mb.add(debug);
	}

	/*
	 * Creates a menu item and adds it to the given menu.  If the menu item 
	 * has no mnemonic, set mnemonicKey to a negative integer. If it has no
	 * action command, set actionCommand to the empty string "". By setting 
	 * the menu item's action command, the event handler can determine which 
	 * menu item was selected by the user. This method returns the menu item.
	 */ 
	private JMenuItem createMenuItem(JMenu menu, String label, int mnemonicKey, String actionCommand)
	{
		JMenuItem menuItem = new JMenuItem(label);

		if (mnemonicKey > 0)
		{
			menuItem.setMnemonic(mnemonicKey);
		}

		if (actionCommand.length() > 0)
		{
			menuItem.setActionCommand(actionCommand);
		}

		menu.add(menuItem);

		return menuItem;
	}

	/*
	 * Places the given window approximately at the center of the screen
	 */ 
	public void centerWindow(Window w)
	{
		Rectangle winBounds = w.getBounds();
		w.setLocation(framePositionX + (frameBounds.width - winBounds.width)/2, 
				framePositionY + (frameBounds.height - winBounds.height)/2);
	}

	/*
	 * This method adds the given string to the status text area 
	 */
	public void updateStatusBar(String s)
	{
		// trim() removes whitespace and control characters at both ends of the string
		statusField.append(s.trim() + "\n"); 

		// This informs the scroll pane to update itself and its scroll bars.
		// The scroll pane does not always automatically scroll to the message that was
		// just added to the text area. This line guarantees that it does.
		statusField.revalidate();
	}

	public void clearStatusBar(){
		statusField.setText(null);
	}

	/*
	 * This method adds the given JTable into tableScrPane
	 */
	public void addTable(JTable data)
	{
		tableScrPane.setViewportView(data);
	}

	/*
	 * This method registers the controllers for all items in each menu. This
	 * method should only be executed once.
	 */ 
	public void registerControllers()
	{
		JMenuItem menuItem; 

		//		TO DO: EACH CONTROLLER NEEDS CODE FOR THIS PART TO WORK. LOOK AT EACH RESPECTIVE
		//		JAVA FILE

		// ManagerController handles events on the Manager Admin menu items (i.e. when they are clicked)
		ManagerController managerControl = new ManagerController(this);

		for (int i = 0; i < manager.getItemCount(); i++)
		{
			menuItem = manager.getItem(i);
			menuItem.addActionListener(managerControl);
		}

		// ClerkController handles events on the Clerk menu items (i.e. when they are clicked)
		ClerkController clerkControl = new ClerkController(this);

		for (int i = 0; i < clerk.getItemCount(); i++)
		{
			menuItem = clerk.getItem(i);
			menuItem.addActionListener(clerkControl);
		}

		// CustomerController handles events on the Customer menu items (i.e. when they are clicked)
		CustomerController customerControl = new CustomerController(this);

		for (int i = 0; i < customer.getItemCount(); i++)
		{
			menuItem = customer.getItem(i);
			menuItem.addActionListener(customerControl);
		}
		
		/**
		 * Purchasing buttons
		 */
		// command buttons for purchasing
		JButton searchItems = new JButton("Search for Items");
		JButton addToCart = new JButton("Add Item to Cart");
		checkout = new JButton("Checkout");
		JButton cancelOrder = new JButton("Cancel Order");

		searchItems.addActionListener(customerControl);
		searchItems.setActionCommand("SearchItems");
		addToCart.addActionListener(customerControl);
		addToCart.setActionCommand("addToCart");
		checkout.addActionListener(customerControl);
		checkout.setActionCommand("checkout");
		cancelOrder.addActionListener(customerControl);
		cancelOrder.setActionCommand("cancelOrder");

		// add the command buttons for purchasing and hide them until we need them
		buttonPane.setLayout(buttonLayout);
		buttonPane.add(searchItems);
		buttonPane.add(addToCart);
		buttonPane.add(checkout);
		buttonPane.add(cancelOrder);
		buttonPane.setVisible(false);

		/**
		 * InStore Purchasing buttons
		 */
		// command buttons for purchasing
		JButton searchItemsIS = new JButton("Search for Items");
		JButton addToCartIS = new JButton("Add Item to Cart");
		JButton checkoutIS = new JButton("Checkout");
		JButton cancelOrderIS = new JButton("Cancel Order");

		searchItemsIS.addActionListener(clerkControl);
		searchItemsIS.setActionCommand("SearchItems");
		addToCartIS.addActionListener(clerkControl);
		addToCartIS.setActionCommand("addToCart");
		checkoutIS.addActionListener(clerkControl);
		checkoutIS.setActionCommand("checkout");
		cancelOrderIS.addActionListener(clerkControl);
		cancelOrderIS.setActionCommand("cancelOrder");

		// add the command buttons for purchasing and hide them until we need them
		buttonPaneStore.setLayout(buttonLayout);
		buttonPaneStore.add(searchItemsIS);
		buttonPaneStore.add(addToCartIS);
		buttonPaneStore.add(checkoutIS);
		buttonPaneStore.add(cancelOrderIS);
		buttonPaneStore.setVisible(false);

		/**
		 * Return Transactions buttons
		 */
		// command buttons for purchasing
		JButton displayPurchasedItem = new JButton("Display Purchases");
		 addToReturn = new JButton("Return Item");
		 checkoutReturn = new JButton ("Checkout");
		 cancelReturn = new JButton("Cancel Return");

		displayPurchasedItem.addActionListener(clerkControl);
		displayPurchasedItem.setActionCommand("displayItem");
		addToReturn.addActionListener(clerkControl);
		addToReturn.setActionCommand("addToReturn");
		checkoutReturn.addActionListener(clerkControl);
		checkoutReturn.setActionCommand("checkoutReturn");
		cancelReturn.addActionListener(clerkControl);
		cancelReturn.setActionCommand("cancelReturn");

		// add the command buttons for purchasing and hide them until we need them
		buttonPaneReturn.setLayout(buttonLayout);
		buttonPaneReturn.add(displayPurchasedItem);
		buttonPaneReturn.add(addToReturn);
		buttonPaneReturn.add(checkoutReturn);
		buttonPaneReturn.add(cancelReturn);
		buttonPaneReturn.setVisible(false);
		
		
		// CustomerController handles events on the Customer menu items (i.e. when they are clicked)
		DebugController debugControl = new DebugController(this);

		for (int i = 0; i < debug.getItemCount(); i++)
		{
			menuItem = debug.getItem(i);
			menuItem.addActionListener(debugControl);
		}
	}

	public static void main(String[] args) 
	{
		AMSView AMS = new AMSView();

		// we will not call pack() on the main frame 
		// because the size set by setSize() will be ignored
		AMS.setVisible(true);

		// create the login window
		LoginWindow lw = new LoginWindow(AMS);	      

		lw.addWindowListener(new ControllerRegister(AMS));	

		// pack() has to be called before centerWindow() 
		// and setVisible()
		lw.pack();

		AMS.centerWindow(lw);

		lw.setVisible(true); 
		
	}
}

/*
 * Event handler for login window. After the user logs in (after login
 * window closes), the controllers that handle events on the menu items
 * are created. The controllers cannot be created before the user logs 
 * in because the database connection is not valid at that time. The 
 * models that are created by the controllers require a valid database 
 * connection.
 */ 
class ControllerRegister extends WindowAdapter
{
	private AMSView AMS; 

	public ControllerRegister(AMSView AMS)
	{
		this.AMS = AMS;
	}

	public void windowClosed(WindowEvent e)
	{	
		AMS.registerControllers();
	}
} 
