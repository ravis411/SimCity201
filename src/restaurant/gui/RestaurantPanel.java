package restaurant.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.MarketAgent;
import restaurant.OldWaiterAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.Waiter;
import Person.PersonAgent;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel implements ActionListener {
	private RestaurantGui gui; //reference to main gui
	
    //Host, cook, waiters and customers
	private PersonAgent person1 = new PersonAgent("Person 1",null);
	private PersonAgent person2 = new PersonAgent("Person 2",null);
	private PersonAgent person3 = new PersonAgent("Person 3",null);
	private PersonAgent person4 = new PersonAgent("Person 4",null);
	private PersonAgent person5 = new PersonAgent("Person 5",null);
	private PersonAgent person6 = new PersonAgent("Person 6",null);
	
    private HostAgent host = new HostAgent();
    private HostGui hostGui = new HostGui(host);
    private CookAgent cook = new CookAgent();
    private CookGui cookGui = new CookGui(cook);
    private CashierAgent cashier = new CashierAgent("Cashier");
    private CashierGui cashierGui = new CashierGui(cashier);
    private OldWaiterAgent waiter = new OldWaiterAgent();
    private WaiterGui waiterGui = new WaiterGui(waiter,gui,150,10);
    private CustomerAgent customer = new CustomerAgent();
    private CustomerGui customerGui = new CustomerGui(customer,gui);
    
    
    
    private MarketAgent market1 = new MarketAgent("Market 1", cook, cashier);
    private MarketAgent market2 = new MarketAgent("Market 2", cook, cashier);
    private MarketAgent market3 = new MarketAgent("Market 3", cook, cashier);

    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    private Vector<MarketAgent> markets = new Vector<MarketAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    
    private JPanel waiterGroup = new JPanel();
    private JPanel customerGroup = new JPanel();
    
    private JButton pauseButton = new JButton("Pause");
    private JButton resumeButton = new JButton("Resume");
    
    private int waiterCount = 1;
    
    public ListPanel getCustomerPanel() {
    	return customerPanel;
    }
    
    public ListPanel getWaiterPanel() {
    	return waiterPanel;
    }
    
    public RestaurantGui getRestaurantGui() {
    	return gui;
    }
    
    public void clearCooksInventory() {
    	cook.clearInventory();
    }
    
    public void emptyRegister() {
    	cashier.emptyRegister();
    }
    
    public void fillRegister() {
    	cashier.fillRegister();
    }

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        //host.setGui(hostGui);
        
        customer.setHost(host);
        customer.setCashier(cashier);
        host.addWaiter(waiter);
        
        waiter.waiterGui = waiterGui;
        customer.customerGui = customerGui;
        
        person1.addRole(host);
        person2.addRole(cook);
        person3.addRole(cashier);
        person4.addRole(waiter);
        person5.addRole(customer);
        
        person1.roles.get(0).activate();
        person2.roles.get(0).activate();
        person3.roles.get(0).activate();
        person4.roles.get(0).activate();
        person5.roles.get(0).activate();
        
       customer.gotHungry();
        
        
        markets.add(market1);
        markets.add(market2);
        markets.add(market3);
        
        for(MarketAgent market: markets) {
        	cook.addMarket(market);
    	}
        
        //gui.animationPanel.addGui(hostGui);
        gui.animationPanel.addGui(cashierGui);
        gui.animationPanel.addGui(cookGui);
        gui.animationPanel.addGui(customerGui);
        gui.animationPanel.addGui(waiterGui);
        
        cook.cookGui = cookGui;
        person1.startThread();
        person2.startThread();
        person3.startThread();
        person4.startThread();
        person5.startThread();
        //cashier.startThread();
        for(MarketAgent market: markets) {
        	market.startThread();
    	}
        
        pauseButton.addActionListener(this);
        resumeButton.addActionListener(this);
        
        resumeButton.setEnabled(false);
        
        customerPanel.enableAddButton(false);

        setLayout(new GridLayout(1, 2, 5, 20));
        
        waiterGroup.setLayout(new BoxLayout(waiterGroup, BoxLayout.Y_AXIS));
        waiterGroup.add(waiterPanel);
        waiterGroup.add(pauseButton);
        
        customerGroup.setLayout(new BoxLayout(customerGroup, BoxLayout.Y_AXIS));
        customerGroup.add(customerPanel);
        customerGroup.add(resumeButton);
        
        
        initRestLabel();
        add(restLabel);
        //add(group);
        add(waiterGroup);
        add(customerGroup);
        //add(pauseResumeGroup);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pauseButton) {
        	System.out.println("Simulation paused. Please allow agents to finish current actions in scheduler.");
        	pauseButton.setEnabled(false);
        	resumeButton.setEnabled(true);
        	//pauseThread();
        }
        else if (e.getSource() == resumeButton) {
        	System.out.println("Simulation resumed.");
        	pauseButton.setEnabled(true);
        	resumeButton.setEnabled(false);
        	//resumeThread();
        }
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        //restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>Host:</td><td>Host</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }
    
    public void makeHungry(String name, int index) {
    	customerPanel.disableCheckBoxAt(index);
    	for (int i = 0; i < customers.size(); i++) {
            CustomerAgent temp = customers.get(i);
            if (temp.getName() == name)
                gui.Hungerize(temp);
        }
    }
    
    public void requestBreak(String name, int index) {
    	waiterPanel.disableCheckBoxAt(index);
    	for (int i = 0; i < waiters.size(); i++) {
            WaiterAgent temp = waiters.get(i);
            if (temp.getName() == name)
                gui.breakRequest(temp);
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    /*public void addPerson(String type, String name) {
    	
    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    	else if (type.equals("Waiters")) {
    		OldWaiterAgent waiter = new OldWaiterAgent(name);
    	    WaiterGui waiterGui = new WaiterGui(waiter, gui, waiterCount*50+120, 0);
    	    
    	    gui.animationPanel.addGui(waiterGui);
    	    waiter.setHost(host);
    	    waiter.setCashier(cashier);
    	    waiter.setGui(waiterGui);
    	    waiterCount++;
    	    host.addWaiter(waiter); //add waiter to host's waiter vector
            waiter.setCook(cook); //add cook to waiter
            waiters.add(waiter);
    	    waiter.startThread();
    	    
    	    customerPanel.enableAddButton(true);
    	}
    }
    
    public void addHungryPerson(String type, String name, int CBState) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    		if (CBState == 1) {
    			gui.initializeHungry(CBState, c);
    		}
    	}
    }
    
    public void enableCheckBox(CustomerAgent c) {
    	for (int i = 0; i < customers.size(); i++) {
            CustomerAgent temp = customers.get(i);
            if (temp.getName() == c.getCustomerName())
                customerPanel.enableCheckBoxAt(i);
        }
    }
    
    public void enableCheckBox(Waiter w) {
    	for (int i = 0; i < waiters.size(); i++) {
            WaiterAgent temp = waiters.get(i);
            if (temp.getName() == w.getName())
                waiterPanel.enableCheckBoxAt(i);
        }
    }
    
    public void pauseThread() {
    	host.pauseThread();
    	cook.pauseThread();
    	cashier.pauseThread();
    	for(WaiterAgent waiter: waiters) {
    		waiter.pauseThread();
    	}
    	for(CustomerAgent customer: customers) {
    		customer.pauseThread();
    	}
    	for(MarketAgent market: markets) {
    		market.pauseThread();
    	}
    }
    
    public void resumeThread() {
    	host.resumeThread();
    	
    	cook.resumeThread();
    	cashier.resumeThread();
    	for(WaiterAgent waiter: waiters) {
    		waiter.resumeThread();
    	}
    	for(CustomerAgent customer: customers) {
    		customer.resumeThread();
    	}
    	for(MarketAgent market: markets) {
    		market.resumeThread();
    	}
    }*/

}
