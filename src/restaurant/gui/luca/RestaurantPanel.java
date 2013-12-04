package restaurant.gui.luca;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant.luca.LucaCashierAgent;
import restaurant.luca.LucaCookAgent;
import restaurant.luca.LucaCustomerAgent;
import restaurant.luca.LucaHostAgent;
import restaurant.luca.LucaMarketAgent;
import restaurant.luca.LucaWaiterAgent;
import restaurant.interfaces.luca.LucaWaiter;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
	private LucaHostAgent host = new LucaHostAgent("Cary");
	private LucaCookAgent cook = new LucaCookAgent("Harry");
	private LucaCashierAgent cashier = new LucaCashierAgent("Sarah");
	private Vector<LucaMarketAgent> markets = new Vector<LucaMarketAgent>();
	
    private Vector<LucaCustomerAgent> customers = new Vector<LucaCustomerAgent>();
    private Vector<LucaWaiterAgent> waiters = new Vector<LucaWaiterAgent>();
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui
    private int customerNumber=0;
    private int waiterNumber=0;

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        CookGui c = new CookGui(cook, gui);
    	

		gui.animationPanel.addGui(c);// dw

		cook.setGui(c);
		//cook.setGui(gui);
        
        LucaMarketAgent marketStopAndShop = new LucaMarketAgent("Stop & Shop");	
        marketStopAndShop.setCook(cook);
        marketStopAndShop.setCashier(cashier);
		markets.add(marketStopAndShop);
		LucaMarketAgent marketShaws = new LucaMarketAgent("Shaws");	
        marketShaws.setCook(cook);
        marketShaws.setCashier(cashier);
		markets.add(marketShaws);
		LucaMarketAgent marketRalphs = new LucaMarketAgent("Ralphs");	
		marketRalphs.setCook(cook);
		marketRalphs.setCashier(cashier);
		markets.add(marketRalphs);
		
		
		marketStopAndShop.startThread();
		marketShaws.startThread();
		marketRalphs.startThread();
        cook.startThread();
        host.startThread();
        cashier.addMarkets(markets);
        cashier.startThread();
        cook.msgAddMarket(marketStopAndShop);
        cook.msgAddMarket(marketShaws);
        cook.msgAddMarket(marketRalphs);

        
        setLayout(new GridLayout(1, 2, 5, 0));
        group.setLayout(new GridLayout(1, 2, 1, 0));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "<tr><td>cook:</td><td>" + cook.getName() + "<tr><td>cashier:</td><td>" + cashier.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$20.00</td></tr><tr><td>Chicken</td><td>$10.00</td></tr><tr><td>Burger</td><td>$15.00</td></tr></table><br></html>");

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
		if (type == "Customers"){
			for (int i=0; i<customers.size(); i++){
				if (customers.get(i).getName() == name)
					gui.updateInfoPanel(customers.get(i));
			}
		}
		if (type == "Waiters"){
			for (int i=0; i<waiters.size(); i++){
				if (waiters.get(i).getName() == name)
					gui.updateInfoPanel(waiters.get(i));
			}
		}
	}
	


    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean hungry) {

    	if (type.equals("Customers")) {
    		
    		LucaCustomerAgent c = new LucaCustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui,customerNumber);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		if (hungry){
    			c.setHungry(hungry);
    		}
    		c.startThread();
    		customerNumber++;
    	}	
    	else if (type.equals("Waiters")) {
    		LucaWaiterAgent w = new LucaWaiterAgent(name);
    		WaiterGui x = new WaiterGui(w, gui,waiterNumber);
    	

    		gui.animationPanel.addGui(x);// dw
    		w.setCook(cook);
    		w.setHost(host);
    		w.setCashier(cashier);
    		w.setGui(x);
    		w.setGui(gui);
    		waiters.add(w);
    		w.startThread();
    		waiterNumber++;
    		w.tellHostIExist();
    	}	
    }
/*
	public void restartProgram() {
		host.restartAgent();
		cook.restartAgent();
		cashier.restartAgent();
		for (int i=0; i<waiters.size(); i++){
			if (waiters.get(i) != null)
				waiters.get(i).restartAgent();
		}
		for (int i=0; i<markets.size(); i++){
			if (markets.get(i) != null)
				markets.get(i).restartAgent();
		}
	}

	public void pauseProgram() {
		host.pauseAgent();
		cook.pauseAgent();
		cashier.pauseAgent();
		for (int i=0; i<waiters.size(); i++){
			if (waiters.get(i) != null)
				waiters.get(i).pauseAgent();
		}
		for (int i=0; i<markets.size(); i++){
			if (markets.get(i) != null)
				markets.get(i).pauseAgent();
		}
	}
*/

    
}
