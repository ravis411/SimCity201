package restaurant.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
import restaurant.WaiterAgent;
import agent.Agent;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    private final HostAgent host;
    private final CookAgent cook;
    private final CookGui cookGui;
    private final CashierAgent cashier;
    
    //list of all agents
    private List<Agent> agents = new ArrayList<Agent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel;
    private ListPanel wtrPanel;
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui
    
    public RestaurantPanel(RestaurantGui gui) {
    	
    	this.gui = gui;
    	
    	setPreferredSize(new Dimension(gui.getWidth()/2, gui.getHeight()));
    	
    	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    	
    	host = new HostAgent("Sarah");
    	cashier = new CashierAgent();
    	cook = new CookAgent(cashier);
    	cookGui = new CookGui(cook, gui);
    	cook.setGui(cookGui);
    	gui.animationPanel.addGui(cookGui);
    	
    	agents.add(host);
    	agents.add(cook);
    	agents.add(cashier);
    	
    	setBorder(BorderFactory.createTitledBorder("Restaurant Panel"));
    	host.startThread();
    	cook.startThread();
    	cashier.startThread();
    	
    	//left-side panel of the rest panel
    	JPanel leftGroup = new JPanel();
    	leftGroup.setLayout(new BoxLayout(leftGroup, BoxLayout.Y_AXIS));
    	//buttons
    	JButton pause = new JButton("Pause");
    	pause.addActionListener(new PauseSimulationListener());
    	
    	JButton addTable = new JButton("Add Table");
    	addTable.addActionListener(new AddTableListener());
    	
    	/*JButton addMarket = new JButton("Add Market");
    	addMarket.addActionListener(new AddMarketListener());*/
    	initRestLabel();
    	leftGroup.add(restLabel);
    	leftGroup.add(pause);
    	leftGroup.add(addTable);
    	//leftGroup.add(addMarket);
    	
    	//list panels
    	JPanel panelGroup = new JPanel();
    	panelGroup.setLayout(new BoxLayout(panelGroup, BoxLayout.Y_AXIS));
    	
    	customerPanel = new ListPanel(this, "customer");
    	panelGroup.add(customerPanel);
    	
    	wtrPanel = new ListPanel(this, "waiter");
    	panelGroup.add(wtrPanel);
    	
    	add(leftGroup);
    	add(panelGroup);
    	
    	setVisible(true);
    }

    /**
     * Sets up the restaurant label tha't includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //label.setSize(200, 200);
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
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("customer")) {
    		CustomerAgent c = new CustomerAgent(name, cashier, host);	
    		CustomerGui g = new CustomerGui(c, gui);
    		
    		if(name.equalsIgnoreCase("kill_cook")){
    			cook.clearFoodStores();
    		}else if(name.equalsIgnoreCase("kill_market")){
    			cook.clearMarkets();
    		}
    		
    		gui.animationPanel.addGui(g);// dw
    		c.setGui(g);
    		
    		c.startThread();
    		
    		agents.add(c);
    	}else if(type.equals("waiter")){
    		WaiterAgent w = new WaiterAgent(name, host, cook, cashier);
        	WaiterGui wtrGui = new WaiterGui(w, cookGui, gui);
        	gui.animationPanel.addGui(wtrGui);
        	w.setGui(wtrGui);
        	w.startThread();
        	
        	host.msgAddWaiter(w);
        	
        	agents.add(w);
    	}
    }
    
    /**
     * Method to send the initial msg to the host for a hungry customer--
     * sent by the customer creation panel checkbox
     * @param name Name of the customer
     */
    public void sendPersonWithName(String name, String type){
    	for(Agent agent : agents){
    		if(type.equals("customer") && agent instanceof CustomerAgent){
    			CustomerAgent cust = (CustomerAgent) agent;
        		if(cust.getName().equals(name)){
        			host.msgIWantToEat(cust);
        			return;
        		}
    		}else if(type.equals("waiter") && agent instanceof WaiterAgent){
    			WaiterAgent wtr = (WaiterAgent) agent;
    			if(wtr.getName().equals(name)){
    				host.msgWantToGoOnBreak(wtr);
    				return;
    			}
    		}
    	}
    }

    /**
     * Takes the waiter of the specified name off break
     * @param name name of waiter to take off break
     */
    public void takeWaiterOffBreak(String name){
    	for(Agent agent : agents){
    		if(agent instanceof WaiterAgent){
    			WaiterAgent wtr = (WaiterAgent) agent;
    			if(wtr.getName().equals(name)){
    				host.msgOffBreak(wtr);
    				return;
    			}
    		}
    	}
    }
    
    /**
     * Make a gui response of the named waiter not going on break
     * @param name the name of the waiter who cannot go on break
     */
    public void waiterCannotGoOnBreak(String name){
    	wtrPanel.waiterCannotGoOnBreak(name);
    }
    
    /**
     * Called when the CustomerAgent has left the restaurant to reenable the 
     * customer's check box in the creation panel
     * @param name the customer's name
     */
    public void enableCustomer(String name){
    	customerPanel.enableCustomer(name);
    }

    /**
     * Private class whose existence is only as an ActionListener
     * to add new tables to the animationPanel
     */
    private class AddTableListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			host.msgAddTable();
			gui.animationPanel.addTable();
		}
    	
    }
    
    /**
     * Private class whose existence is only as an ActionListnere
     * to pause the simulation if the user so chooses
     *
     */
    private class PauseSimulationListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton sender = (JButton) e.getSource();
			//if we should be pausing
			if(sender.getText().equals("Pause")){
				sender.setText("Restart");
				//pause messaging in the Agent base methods
				for(Agent agent: agents){
					agent.pause();
				}
				//pause the animations as well
				gui.animationPanel.pause();
			}else{ //if instead we should be restarting everything
				sender.setText("Pause");
				
				//restart messaging in the Agent base methods
				for(Agent agent: agents){
					agent.restart();
				}
				//restart the animations
				gui.animationPanel.restart();
			}
		}
    	
    }
    
    private class AddMarketListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			MarketAgent mkt = new MarketAgent(cook, cashier);
			mkt.startThread();
			cook.addMarket(mkt);
		}
    	
    	
    }

}
