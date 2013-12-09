package ryansRestaurant.gui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.TabExpander;

import ryansRestaurant.RyansCashierRole;
import ryansRestaurant.RyansCookRole;
import ryansRestaurant.RyansCustomerRole;
import ryansRestaurant.RyansMarketRole;
import ryansRestaurant.RyansHostRole.aTable;

public class ControlPanel extends JPanel implements ActionListener {

	public static final Vector<Integer> numSeats = new Vector<Integer>() 
			{{add(1);
			add(2);
			add(3);
			add(4);}};
	
	private RestaurantGui gui = null;
	
	
	private JButton addTableButton = new JButton("Add Table");//button for adding a table
	private JButton addAllTables = new JButton("Add All");
	JComboBox<Integer> tablePosCombo; // ComboBox for table pos/number
	JComboBox<Integer> numSeatsCB;	//comboBox for table size/ number of seats
	
	private JButton cancelButton = new JButton("Cancel");
	private JButton pauseButton = new JButton("Pause");
	
	private JButton marketButton = new JButton("Markets");
	private List<JButton> markets = new ArrayList<>();
	
	private JButton cookButton = new JButton("Cook Info");
	private JButton cashierButton = new JButton("Ryan's Cashier Info");
	
	//sets the state of the panel
	private enum GUIState {none, addTable, marketsPanel, cookPanel, cutomerPanel, cashierPanel};
	private GUIState state = GUIState.none;
	
	
	
	/**Constructor for controlPanel */
	public ControlPanel(RestaurantGui gui)
	{
		this.gui = gui;
		
		//add action listener for addTableButton
		addTableButton.addActionListener(this);
		addAllTables.addActionListener(this);
		cancelButton.addActionListener(this);
		pauseButton.addActionListener(this);
		marketButton.addActionListener(this);
		cookButton.addActionListener(this);
		cashierButton.addActionListener(this);
				
		showCtrlPanel();
		
	}
	
	
	
	
	
	
	
	
	
	private void showCtrlPanel()
	{
		this.removeAll();
		this.setLayout(new FlowLayout());
		
		addTableButton.setText("Click to add Table");
		
		add(addTableButton);
		//add(pauseButton);
		//add(marketButton);
		add(cookButton);
		add(cashierButton);
		add(cancelButton);
		this.repaint();
		validate();
	}
	
	
	public void showCustomerInfo(String name) {
		for( RyansCustomerRole cust : gui.restPanel.customers) {
			if(cust.getName().equals(name)) {
				this.removeAll();
				state = GUIState.cutomerPanel;
				
				add(new CustomerInfo(cust));
				
				this.repaint();
				validate();
				return;
			}
		}
	}
	
	public void showCashierInfo() {
		this.removeAll();
		state = GUIState.cashierPanel;
		add(new CashierInfo(gui.restPanel.getCashier()));
		this.repaint();
		validate();
	}
	
	
	private void showCookPanel() {
		this.removeAll();
		state = GUIState.cookPanel;
        
		add(new CookInfo(gui.restPanel.getCook()));
		
		this.repaint();
		validate();
	}
	
	
	
	private void showMarketInfo(JButton button) {
		this.removeAll();
		this.setLayout(new FlowLayout());
		//find the market
		RyansMarketRole market = null;
		
		for(RyansMarketRole m : gui.restPanel.getMarkets()) {
			if(m.getName().equals(button.getText())) {
				market = m;
			}
		}
		
		add(new MarketInfo(market));
		this.repaint();
		validate();
	}
	
	
	private void showMarketPanel() {
		this.removeAll();
		state = GUIState.marketsPanel;
		
		JScrollPane marketListPane =
	            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    JPanel marketView = new JPanel();
	    marketView.setLayout(new BoxLayout((Container) marketView, BoxLayout.Y_AXIS));
	    marketListPane.setViewportView(marketView);
        Dimension paneSize = new Dimension();
        paneSize.setSize( (150) , 260 );
        marketListPane.setPreferredSize(paneSize);
        marketListPane.setMaximumSize(paneSize);
        marketListPane.setMinimumSize(paneSize);
        
        markets.clear();
        for(RyansMarketRole m : gui.restPanel.getMarkets()) {
        	JButton button = new JButton(m.getName());
            button.setBackground(Color.white);
            Dimension buttonSize = new Dimension( (int)((paneSize.width) *.9),
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            
        	markets.add(button);
        	button.addActionListener(this);
        	marketView.add(button);
        }
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(marketListPane);
        add(cancelButton);
        
        
		
		this.repaint();
		validate();
	}
	
	
	private void showAddTablePanel()
	{
		this.removeAll();
		
		//Add panel for the table number which sets the location
		JPanel tablNumberP = new JPanel();
		JLabel tableNumberL = new JLabel();
		tableNumberL.setText("Select Table Number/Location:");
		tablePosCombo = new JComboBox<>(getAvailableTableLocations());
		tablNumberP.add(tableNumberL);
		tablNumberP.add(tablePosCombo);
		Dimension tableNumberPDim = new Dimension(this.getSize().width, 30);
		tablNumberP.setMinimumSize(tableNumberPDim);
		tablNumberP.setMaximumSize(tableNumberPDim);
		tablNumberP.setPreferredSize(tableNumberPDim);
		
		//Set number of seats
		JPanel numSeatsPanel = new JPanel();
		JLabel numSeatsLabel = new JLabel("Select the number of seats:");
		numSeatsCB = new JComboBox<Integer>(numSeats);
		numSeatsCB.setSelectedIndex(3);//Set the last size as default
		numSeatsPanel.add(numSeatsLabel);
		numSeatsPanel.add(numSeatsCB);
		numSeatsPanel.setPreferredSize(tableNumberPDim);
		numSeatsPanel.setMaximumSize(tableNumberPDim);
		numSeatsPanel.setMinimumSize(tableNumberPDim);
		
		if(!getAvailableTableLocations().isEmpty())
		{
			add(tablNumberP);
			add(numSeatsPanel);
			add(addTableButton);
			add(addAllTables);
			add(cancelButton);
		}
		else{
			tableNumberL.setText("Max Number of Tables reached.");
			add(tableNumberL);
			add(cancelButton);
		}
		
		state = GUIState.addTable;
		
		this.repaint();
		validate();
	}
	
	public Vector<Integer> getAvailableTableLocations()
	{
		
		//get a list of the current tables
		List<aTable> tables = gui.animationPanel.host.getTableNumbers();
	
		Vector<Integer> tableNumbers = new Vector<Integer>();
		
		for(int i = 1; i <= gui.animationPanel.tableMap.size(); i++)
			tableNumbers.add(i);

		for(aTable table:tables)
			tableNumbers.remove( (Integer)table.getTableNumber() );
		
		return tableNumbers;
	}
	
	public void pause()
	{
		if(pauseButton.getText().equals("Pause"))
		{
			pauseButton.setText("Resume");
			gui.restPanel.pause();
			
		}
		else
		{
			pauseButton.setText("Pause");
			gui.restPanel.resume();
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(state == GUIState.none && e.getSource() == addTableButton)
		{
			showAddTablePanel();
		}
		else if(state == GUIState.none && e.getSource() == marketButton) {
			showMarketPanel();
		}
		else if(state == GUIState.none && e.getSource() == cancelButton) {
			gui.showInfoPanel(false);
		}
		else if(e.getSource() == pauseButton)
		{
			pause();
		}
		
		else if(e.getSource() == cookButton) {
			showCookPanel();
		}
		
		else if(e.getSource() == cashierButton) {
			showCashierInfo();
		}
		
		//if adding a table and the addTableButton is pressed, add the table
		else if(state == GUIState.addTable && e.getSource() == addTableButton)
		{
			gui.animationPanel.host.msgAddTable((int)tablePosCombo.getSelectedItem(), (int)numSeatsCB.getSelectedItem());
			state = GUIState.none;
			showCtrlPanel();
		}
		else if(state == GUIState.addTable && e.getSource() == addAllTables){
			Vector<Integer> tableNumbers = getAvailableTableLocations();
			if(!tableNumbers.isEmpty()){
				for(Integer i : tableNumbers){
					gui.animationPanel.host.msgAddTable((int)i, (int)numSeatsCB.getSelectedItem());
				}
			}
			state = GUIState.none;
			showCtrlPanel();
		}
		else if( (state == GUIState.addTable || state == GUIState.marketsPanel || state==GUIState.cutomerPanel || state == GUIState.cookPanel || state == GUIState.cashierPanel) && e.getSource() == cancelButton)
		{
			state = GUIState.none;
			showCtrlPanel();
		}
		
		else if(state == GUIState.marketsPanel && e.getSource() instanceof JButton) {
			for(JButton b : markets) {
				if(e.getSource() == b) {
					showMarketInfo(b);
				}
			}
		}
		
	}
	
	
	
	
	
	/**
	 * RyansCustomer Info Class
	 * @author
	 *
	 */
	private class CustomerInfo extends JPanel implements ActionListener{

		private JLabel label;
		private RyansCustomerRole customer;
		private JButton saveB = new JButton("Save");
		private JButton refreshB = new JButton("Refresh");
		private JButton popB = new JButton("Pop-out");
		JPanel popCancelP = new JPanel();
		
		private JTextField walletTF = new JTextField();
		private JCheckBox flakeCB = new JCheckBox("Flake");
		private JCheckBox leavesCB = new JCheckBox("Leaves");
		
		public CustomerInfo(RyansCustomerRole customer) {
			this.customer = customer;
			
			setLayout(new GridLayout(6, 0));
			
			add(new JLabel("<html><h2> Name: " + customer.getName() + " </h2></html>" ));
			
			JPanel panel = new JPanel();			
			JTextField textF = new JTextField();
			
			panel.add(new JLabel("Wallet: $"));
			panel.add(walletTF);
			panel.setLayout(new GridLayout(0,2));
			add(panel);
			
			
			add(flakeCB);
			add(leavesCB);
			
			
			
			walletTF.addActionListener(this);
			flakeCB.addActionListener(this);
			leavesCB.addActionListener(this);
			saveB.addActionListener(this);
			refreshB.addActionListener(this);
			popB.addActionListener(this);
			
			panel = new JPanel();
			panel.add(saveB);
			panel.add(refreshB);
			add(panel);
			
			popCancelP.add(popB);
			popCancelP.add(cancelButton);
			add(popCancelP);
			
			refresh();
			
			
		}
		
		private void refresh() {
			walletTF.setText("" + customer.getWallet());
			flakeCB.setSelected(customer.getFlake());
			leavesCB.setSelected(customer.getLeaves());
		}
		
		private void saveInfo() {
			customer.setFlake(flakeCB.isSelected());
			try {
				customer.setWallet( Double.parseDouble(walletTF.getText()) );
			} catch (NumberFormatException e) {	}
			
			customer.setLeaves(leavesCB.isSelected());
			
			refresh();
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == refreshB) {
				refresh();
				return;
			}
			else if(e.getSource() == popB) {
				JFrame frame = new JFrame(customer.getName());
				
				remove(popCancelP);
				
				Point point = this.getLocationOnScreen();
				
				frame.add(this);
				frame.setVisible(true);
				frame.setBounds(point.x, point.y, 250, 250);
				cancelButton.doClick();
				return;
			}
			
			saveInfo();


		}
		
	}
	
	
	
	
	
	private class CookInfo extends JPanel implements ActionListener{

		private JLabel foodL;
		private JTextField foodAmountTF;
		private List<JTextField> fAtextFields = new ArrayList<>();
		private JTextField foodLowTF;
		private List<JTextField> foodLOWTFields = new ArrayList<>();
		private JTextField foodCapTF;
		private List<JTextField> foodCAPTFields = new ArrayList<>();
		private JTextField foodOrderedTF;
		private List<JTextField> foodORDEREDTFields = new ArrayList<>();
		
		
		private JButton saveB = new JButton("Save");
		private JButton popB = new JButton("Pop-out");
		private JPanel popCancelP = new JPanel();
		private JButton refreshB = new JButton("Refresh");
		public RyansCookRole cook = null;
		
				
		
		
		public CookInfo(RyansCookRole cook) {
			this.cook = cook;
			JPanel panel = new JPanel();
			//panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.setLayout(new GridLayout(1,0));
			
			
			//this.setLayout(new GridLayout(0,4));
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			
			panel.add(new JLabel("Item"));
			panel.add(new JLabel("Amount"));
			panel.add(new JLabel("Low"));
			panel.add(new JLabel("Cap."));
			panel.add(new JLabel("Ordered"));
			
			
			
			
			
			add(panel);
			
			
			Dimension dim = new Dimension( 5 , 5);
			
			
			
			
			for(String s : cook.Defaultmenu) {
				panel = new JPanel();
				panel.setLayout(new GridLayout(1,0));
				
				foodL = new JLabel(s);
				panel.add(foodL);
				
				foodAmountTF = new JTextField();
				foodAmountTF.setName(s);
				foodAmountTF.addActionListener(this);
				panel.add(foodAmountTF);
				fAtextFields.add(foodAmountTF);
				
				foodLowTF = new JTextField();
				foodLowTF.setName(s);
				foodLowTF.addActionListener(this);
				panel.add(foodLowTF);
				foodLOWTFields.add(foodLowTF);
				
				foodCapTF = new JTextField();
				foodCapTF.setName(s);
				foodCapTF.addActionListener(this);
				panel.add(foodCapTF);
				foodCAPTFields.add(foodCapTF);
				
				foodOrderedTF = new JTextField();
				foodOrderedTF.setName(s);
				foodOrderedTF.setEditable(false);
				panel.add(foodOrderedTF);
				foodORDEREDTFields.add(foodOrderedTF);
				
				
				add(panel);
				
			}
			
			updateInfo();
			panel = new JPanel();
			panel.add(saveB);
			panel.add(refreshB);			
			add(panel);

			panel = popCancelP;
			panel.add(popB);
			panel.add(cancelButton);
			add(panel);
			refreshB.addActionListener(this);
			popB.addActionListener(this);
			saveB.addActionListener(this);
			
		}
		
		private void updateInfo() {
			for(JTextField t : fAtextFields) {
				t.setText("" + cook.getInventory(t.getName(), "AMOUNT"));
			}
			
			for(JTextField t : foodLOWTFields) {
				t.setText("" + cook.getInventory(t.getName(), "LOW") );
			}
			
			for(JTextField t : foodCAPTFields) {
				t.setText("" + cook.getInventory(t.getName(), "CAP") );
			}
			for(JTextField t : foodORDEREDTFields) {
				t.setText("" + cook.getInventory(t.getName(), "ORDERED") );
			}
			
			
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == refreshB) {
				updateInfo();
				return;
			}
			else if(e.getSource() == popB) {
				JFrame frame = new JFrame("Cook");
				
				remove(popCancelP);
				
				Point point = this.getLocationOnScreen();
				
				frame.add(this);
				frame.setVisible(true);
				frame.setBounds(point.x, point.y, 200, 200);
				cancelButton.doClick();
				return;
			}
			
			
			for(JTextField t : fAtextFields) {
				try {
					cook.setInventory(t.getName(), Integer.parseInt(t.getText()), "AMOUNT" );
				} catch (NumberFormatException arg) {	}
			}
			
			for(JTextField t : foodLOWTFields) {
				try {
					cook.setInventory(t.getName(), Integer.parseInt(t.getText()), "LOW" );
				} catch (NumberFormatException arg) {	}
			}
			
			for(JTextField t : foodCAPTFields) {
				try {
					cook.setInventory(t.getName(), Integer.parseInt(t.getText()), "CAP" );
				} catch (NumberFormatException arg) {	}
			}			
			
			
			updateInfo();
			cook.checkInventory();
		}	
					
	}	
	
	private class MarketInfo extends JPanel implements ActionListener{

		private JLabel steakL = new JLabel("Steak");
		private JLabel chickenL = new JLabel("Chicken");
		private JLabel saladL = new JLabel("Salad");
		private JLabel pizzaL = new JLabel("Pizza");
		private JLabel cookieL = new JLabel("Cookie");
		
		private JTextField secondsTF = new JTextField();
		private JTextField steakTF = new JTextField();
		private JTextField chickenTF = new JTextField();
		private JTextField saladTF = new JTextField();
		private JTextField pizzaTF = new JTextField();
		private JTextField cookieTF = new JTextField();
		
		private JButton saveB = new JButton("Save");
		private JButton maximizeB = new JButton("Pop-out");
		private JButton refreshB = new JButton("Refresh");
		public RyansMarketRole market = null;
		
		
		public MarketInfo(RyansMarketRole market) {
			this.market = market;
			
			updateInfo();
			
			this.setLayout(new GridLayout(0,2));
			
			
			add(new JLabel("Name: "));
			add(new JLabel("" + market.getName()));
			add(new JLabel("T to complete (sec)"));
			add(secondsTF);
			add(steakL);
			add(steakTF);
			add(chickenL);
			add(chickenTF);
			add(saladL);
			add(saladTF);
			add(pizzaL);
			add(pizzaTF);
			add(cookieL);
			add(cookieTF);
			
			add(saveB);add(cancelButton);
			add(maximizeB);add(refreshB);
			
			maximizeB.addActionListener(this);
			saveB.addActionListener(this);
			refreshB.addActionListener(this);
			
		}
		
		private void updateInfo() {
			secondsTF.setText("" + market.getSecondsToFullfillOrder());
			steakTF.setText( "" + market.getInventory("Steak"));
			chickenTF.setText( "" + market.getInventory("Chicken"));
			saladTF.setText( "" + market.getInventory("Salad"));
			pizzaTF.setText( "" + market.getInventory("Pizza"));
			cookieTF.setText( "" + market.getInventory("Cookie"));
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == saveB) {
				try {
					market.setSecondsToFullfillOrder(Double.parseDouble(secondsTF.getText()) );
				} catch (NumberFormatException e1) {

				}
				try {
					market.setInventory("Steak", Integer.parseInt(steakTF.getText()) );
				} catch (NumberFormatException e1) {

				}
				try {
					market.setInventory("Chicken", Integer.parseInt(chickenTF.getText()) );
				} catch (NumberFormatException e1) {

				}
				try {
					market.setInventory("Salad", Integer.parseInt(saladTF.getText()) );
				} catch (NumberFormatException e1) {

				}
				try {
					market.setInventory("Pizza", Integer.parseInt(pizzaTF.getText()) );
				} catch (NumberFormatException e1) {

				}
				try {
					market.setInventory("Cookie", Integer.parseInt(cookieTF.getText()) );
				} catch (NumberFormatException e1) {

				}
				updateInfo();
			}
			else if(e.getSource() == refreshB) {
				updateInfo();
			}
			else if(e.getSource() == maximizeB) {
				JFrame frame = new JFrame(market.getName());
				
				this.remove(cancelButton);
				this.remove(maximizeB);
				
				Point point = this.getLocationOnScreen();
				
				frame.add(this);
				frame.setVisible(true);
				frame.setBounds(point.x, point.y, 200, 200);
				
				cancelButton.doClick();
				
			}

			
		}
		
					
	}	

	
	private class CashierInfo extends JPanel implements ActionListener{

		
		
		private JTextField moneyTF = new JTextField();
		
		
		private JButton saveB = new JButton("Save");
		private JButton maximizeB = new JButton("Pop-out");
		private JButton refreshB = new JButton("Refresh");
		public RyansCashierRole cashier = null;
		
		
		public CashierInfo(RyansCashierRole cashier) {
			this.cashier = cashier;
			
			updateInfo();
			
			this.setLayout(new GridLayout(0,2));
			
			
			add(new JLabel("Name: "));
			add(new JLabel("" + cashier.getName()));
			add(new JLabel("Money $"));
			add(moneyTF);
			
			
			add(saveB);add(cancelButton);
			add(maximizeB);add(refreshB);
			
			maximizeB.addActionListener(this);
			saveB.addActionListener(this);
			refreshB.addActionListener(this);
			
		}
		
		private void updateInfo() {
			moneyTF.setText("" + cashier.getMoney());
			
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == saveB) {
				try {
					cashier.setMoney(Double.parseDouble(moneyTF.getText()) );
				} catch (NumberFormatException e1) {

				}
				updateInfo();
			}
			else if(e.getSource() == refreshB) {
				updateInfo();
			}
			else if(e.getSource() == maximizeB) {
				JFrame frame = new JFrame(cashier.getName());
				
				this.remove(cancelButton);
				this.remove(maximizeB);
				
				Point point = this.getLocationOnScreen();
				
				frame.add(this);
				frame.setVisible(true);
				frame.setBounds(point.x, point.y, 200, 200);
				
				cancelButton.doClick();
				
			}

			
		}
		
					
	}	
}
