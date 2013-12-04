package kushrestaurant.gui;

import kushrestaurant.CookAgent;
import kushrestaurant.MarketAgent;
import kushrestaurant.CustomerAgent;
import kushrestaurant.HostAgent;
import kushrestaurant.CashierAgent;

import agent.Agent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

import kushrestaurant.WaiterAgent;
/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah");
    private CashierAgent cashier = new CashierAgent("TheCashier");
    private CookAgent cook;
    private CookGui cookGui;
    private Vector<WaiterAgent> Waiters = new Vector<WaiterAgent>();
    private MarketAgent market1= new MarketAgent("M1",cashier);
    private MarketAgent market2= new MarketAgent("M2",cashier);
    private MarketAgent market3= new MarketAgent("M3",cashier);
    public ArrayList<WaiterGui> waiterGuis = new ArrayList<WaiterGui>();
    public int customerPosition=1;
    int i=0;
    public int waiterPosition=1;
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
  private Vector<Agent> agents= new Vector<Agent>();
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        cook=new CookAgent("Cook", this.gui);
        cookGui=new CookGui(cook,this.gui);
        cook.setGui(cookGui);
        gui.animationPanel.addGui(cookGui);
        agents.add(host);
        agents.add(cook);
        agents.add(cashier);
        agents.add(market1);
        cook.addMarket(market1);
        agents.add(market2);
        cook.addMarket(market2);
        agents.add(market3);
        cook.addMarket(market3);
        //waiter.setGui(hostGui);
        host.startThread();
        cook.startThread();
        cashier.startThread();
        market1.startThread();
        market2.startThread();
        market3.startThread();
        
        //System.out.println(cook.name);
        

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    public void pauseAgents()
    {
    	 for(Agent a: agents)
    	 {
    		 a.pauseThread();
    	 }
    }
    public void resumeAgents()
    {
    	 for(Agent a: agents)
    	 {
    		 a.resumeThread();
    	 }
    }
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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
                   // customerPanel.disableCheckBox(temp);
                    
            }
        }
        if(type.equals("Waiters")){

            for (int i = 0; i < Waiters.size(); i++) {
                WaiterAgent temp = Waiters.get(i);
                if (temp.getName() == name)
                    if(temp.isAtBreak()){
                    	customerPanel.disableCheckBox(temp);
                    }
                   // customerPanel.disableCheckBox(temp);
        	
        }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name,i++);	
    		CustomerGui g = new CustomerGui(c, gui,customerPosition++);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		//c.setWaiter(waiters.get(0));
    		c.setGui(g);
    		customers.add(c);
    		agents.add(c);
    		c.startThread();
    		
    	}
    }
    public void addWaiter(String Name){
    	WaiterAgent w = new WaiterAgent(Name,host,cook,gui);
    	WaiterGui g = new WaiterGui(w,waiterPosition++);
    	
    	gui.animationPanel.addGui(g);
		w.setHost(host);
		w.setCook(cook);
		w.setCashier(cashier);
		w.setGui(g);
		agents.add(w);
		Waiters.add(w);
		host.addWaiter(w);
		w.startThread();
    }
    public void addPerson(String type, String name, boolean hungry) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name,i++);	
    		CustomerGui g = new CustomerGui(c, gui,customerPosition++);
    		
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		//c.setWaiter(waiters.get(0));
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		agents.add(c);
    		c.startThread();
    		if(hungry){ g.setHungry(); }
    	}
    }
  //  public CustomerAgent specifiedCustomer(int n){
   // 	return customers.get(n);
    //}
    public WaiterAgent specifiedWaiter(int n){
    	     return Waiters.get(n);
    	   }
    public int getHungerLevel2(int n){
    	return customers.get(n).getHungerLevel();
    }

}
