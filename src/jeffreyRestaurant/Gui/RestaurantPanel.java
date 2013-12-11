package jeffreyRestaurant.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import jeffreyRestaurant.CashierAgent;
import jeffreyRestaurant.CookAgent;
import jeffreyRestaurant.CustomerAgent;
import jeffreyRestaurant.HostAgent;
import jeffreyRestaurant.MarketAgent;
import jeffreyRestaurant.WaiterAgent;

/**
 * Old Restaurant GUI panel for the restaurant project. 
 * Obsolete file in SimCity201.
 */
public class RestaurantPanel extends JPanel {/*
	private int ROWS = 1;
	private int COLLUMNS = 2;
	private int vgap1 = 10;
	private int vgap2 = 20;
	private int hgap1 = 10;
	private int hgap2 = 20;
    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah");
    //private WaiterAgent waiter = new WaiterAgent("Breanna"); //Hard coded, one waiter
    //private HostGui hostGui = new HostGui(waiter);
    private CookAgent cook = new CookAgent("Bob");
    private CashierAgent cashier = new CashierAgent("Cashier");
    private MarketAgent m1 = new MarketAgent("m1", cook, cashier, 1, 10, 10, 10);
    private MarketAgent m2 = new MarketAgent("m2", cook, cashier, 10, 10, 10, 10);
    private MarketAgent m3 = new MarketAgent("m3", cook, cashier, 10, 10, 10, 10);
    private CookGui cookGui = new CookGui(cook);
    private int waiterCount = 0;
    
    

    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
    	//host.addWaiter(waiter);//Debug
    	//waiter.setHost(host);
        this.gui = gui;
        //waiter.setGui(hostGui);
        //waiter.setCook(cook);
        cook.setGui(cookGui);
        gui.animationPanel.addGui(cookGui);
        //host.startThread();
        //waiter.startThread();
        //cook.startThread();
        cook.addMarket(m1);
        cook.addMarket(m2);
        cook.addMarket(m3);
        //m1.startThread();
        //m2.startThread();
        //m3.startThread();
        //cashier.startThread();

        setLayout(new GridLayout(ROWS, COLLUMNS, hgap2, vgap2));
        group.setLayout(new GridLayout(ROWS, COLLUMNS, hgap1, vgap1));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     *//*
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr><tr><td>cook:</td><td>" + cook.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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
     *//*
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        //if type = waiters
        if (type.equals("Waiters")) {
        	for (int j = 0; j < waiters.size(); j++) {
        		WaiterAgent tempWaiter = waiters.get(j);
        		if (tempWaiter.getName() == name)
        			gui.updateInfoPanel(tempWaiter);
        	}
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
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    	if (type.equals("Waiters")) {
    		WaiterAgent w = new WaiterAgent(name);
    		HostGui wg = new HostGui(w);
    		
    		gui.animationPanel.addGui(wg);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setGui(wg);
    		waiters.add(w);
    		w.startThread();
    		
    		host.addWaiter(w);
    	}
    }*//*
    public void addPerson(String type, String name, Boolean isHungry) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		//c.setWaiter(waiter);//change to correct waiters
    		c.setGui(g);
    		if (isHungry){
    			g.setHungry();
    		}
    		customers.add(c);
    		//c.startThread();
    	}
    	if (type.equals("Waiters")) {
    		WaiterAgent w = new WaiterAgent(name);
    		++waiterCount;
    		HostGui wg = new HostGui(w, waiterCount);
    		
    		gui.animationPanel.addGui(wg);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		w.setGui(wg);
    		waiters.add(w);
    		//w.startThread();
    		
    		host.addWaiter(w);
    	}
    }
    public Vector<CustomerAgent> getCustomers() {
    	return customers;
    }
    public Vector<WaiterAgent> getWaiters() {
    	return waiters;
    }*/
}
