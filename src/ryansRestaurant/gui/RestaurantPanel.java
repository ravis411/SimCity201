package ryansRestaurant.gui;

import interfaces.generic_interfaces.GenericWaiter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ryansRestaurant.RyansCashierRole;
import ryansRestaurant.RyansCookRole;
import ryansRestaurant.RyansCustomerRole;
import ryansRestaurant.RyansMarketRole;
import ryansRestaurant.RyansOldWaiterRole;
import ryansRestaurant.RyansWaiterRole;
import astar.AStarTraversal;

/**
 * Panel in frame that contains all the ryansRestaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //RyansHost, cook, waiters a
    

    protected Vector<RyansCustomerRole> customers = new Vector<RyansCustomerRole>();
    private Vector<RyansWaiterRole> waiters = new Vector<RyansWaiterRole>();
    private Vector<RyansMarketRole> markets = new Vector<RyansMarketRole>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();

    protected RestaurantGui gui; //reference to main gui
    

    protected RestaurantLayout restLayout = null;
    //A*********************
    Semaphore[][] grid;// = new Semaphore[restLayout.GRID_SIZEX + 1][restLayout.GRID_SIZEY + 1];
    
    
    
    
    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        restLayout = gui.layout;
        grid = restLayout.addAndInitializeMainGrid(grid);
        
        
       // host.startThread();
       // cashier.startThread();
        
        
//        cook = new RyansCookRole("Ryan's Restaurant");
//        
//        CookGui cookGui = new CookGui(cook, gui);
//        cook.setGui(cookGui);
//        gui.animationPanel.addGui(cookGui);
//        
//        for(int i = 1; i <=3; i++) {
//        	RyansMarketRole m = new RyansMarketRole("RyansMarket" + i);
//        	m.startThread();
//        	cook.addMarket(m);
//        	markets.add(m);
//        }        
        //cook.startThread();
//        gui.animationPanel.setHost(host);
//        
//        markets.get(0).addToInventory("Oreo Cookie", 5);
//        markets.get(0).addToInventory("Cookies n Cream", 15);
//        markets.get(1).addToInventory("Oreo Cookie", 15);
//        markets.get(1).addToInventory("Oreo Milkshake", 20);
//        markets.get(2).addToInventory("Oreo Cake", 10);
//        markets.get(2).addToInventory("Dirt n Worms", 20);

        //Add a waiter
       /* waiters.add(new RyansWaiterRole("Mary", host, cook));
        waiters.get(0).startThread();
        WaiterGui wGui = new WaiterGui(waiters.get(0));
        waiters.get(0).setGui(wGui);
        host.msgAddWaiter(waiters.firstElement());
        
        gui.animationPanel.addGui(wGui);*/
        
        
        
        //setLayout(new GridLayout(1, 2, 20, 20));
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS ));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);

        initRestLabel();
        add(restLabel);
        Dimension dim = new Dimension(150, 350);
        restLabel.setMaximumSize(dim);
        restLabel.setMinimumSize(dim);
        restLabel.setPreferredSize(dim);
        
        add(group);
    }

    /**
     * Sets up the ryansRestaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + "Host" + "</td></tr><tr><td>cook:</td><td>" + "Cook" + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Oreo Cake</td><td>$15.99</td></tr><tr><td>Oreo Milkshake</td><td>$10.99</td></tr><tr><td>CookiesnCream</td><td>$5.99</td></tr><tr><td>DirtnWorms</td><td>$8.99</td></tr><tr><td>Oreo Cookie</td><td>$2.00</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("       "), BorderLayout.EAST);
        restLabel.add(new JLabel("       "), BorderLayout.WEST);
       
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
//    public void showInfo(String type, String name) {
//
//        if (type.equals("Customers")) {
//
//            for (int i = 0; i < customers.size(); i++) {
//                RyansCustomerRole temp = customers.get(i);
//                if (temp.getName() == name)
//                    gui.updateInfoPanel(temp);
//            }
//        }
//    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		RyansCustomerRole c = new RyansCustomerRole();	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		//c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	}
    	else if(type.equals("Waiters")) {
    		if(waiters.size() >= 10)
    			return;
    		
    		RyansWaiterRole w = new RyansOldWaiterRole("Ryan's Restaurant");
    		
    		AStarTraversal aStar = new AStarTraversal(grid);
    		
    		WaiterGui g = new WaiterGui(w, gui, restLayout, aStar);
    		
            w.setGui(g);
            gui.animationPanel.addGui(g);
            
            //host.addWaiter((GenericWaiter) w);
            waiters.add(w);
    		//w.startThread();
    	}
    }
    
    
    
    /**Sets the customer's checkbox with the given name to selected and enabled*/
    public void setCustomerBox(String name, boolean selected, boolean enabled)
    {
    	customerPanel.setCustomerCBox(name, selected, enabled);
    }
    
    /** Called when a customer's checkbox is selected*/
    public void checkBoxed(String name)
    {
    	for(RyansCustomerRole temp:customers)
    	{
    		if(temp.getName() == name)
    		{
    			temp.getGui().setHungry();
    			customerPanel.setCustomerCBox(name, true, false);
    		}
    	}
    }
    
    public void setWaiterBB(String name, Color color, boolean enabled, String text, String tooltip) {
    	customerPanel.setWaiterBB(name, color, enabled, text, tooltip);
    }
    
    public void giveMeABreak(String name) {
    	for(RyansWaiterRole w:waiters) {
    		if(w.getName().equals(name)) {
    			customerPanel.setWaiterBB(name, Color.gray, false, "Negotiating...", "Requesting a break...");
    			w.msgRequestABreak();
    			return;
    		}
    	}
    }
    
    /** called from list panel when button is pressed again
     * 
     * @param name
     */
    public void backToWork(String name) {
    	for(RyansWaiterRole w:waiters) {
    		if(w.getName().equals(name)) {
    			w.msgBackToWork();
    			return;
    		}
    	}
    }
    
    public void pause()
    {
    	//calls all the agents pause()
    	//host.pauseThread();
    	//cook.pauseThread();
    	for(RyansCustomerRole cust : customers) {
    	//	cust.pauseThread();
    	}
    	for(RyansWaiterRole waiter : waiters) {
    	//	waiter.pauseThread();
    	}
    }
    public void resume(){
    	//calls all the agents resume()
    //	host.resumeThread();
    	//cook.resumeThread();
    	for(RyansCustomerRole cust : customers) {
    	//	cust.resumeThread();
    	}
    	for(RyansWaiterRole waiter : waiters) {
    	//	waiter.resumeThread();
    	}
    }
    
    public Vector<RyansMarketRole> getMarkets() {
    	return markets;
    }
    
   public RyansCookRole getCook() {
    	return cook;
    }
    RyansCashierRole cashier = null;
    public RyansCashierRole getCashier(){
    	return cashier;
    }
    public void setRyansCashierRole(RyansCashierRole cashier){
    	this.cashier = cashier;
    }

    RyansCookRole cook = null;
	public void setRyansCookRole(RyansCookRole cook) {
		this.cook = cook;
	}

}
