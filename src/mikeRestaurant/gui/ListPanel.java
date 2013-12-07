package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    private RestaurantPanel restPanel;
    private String type;
    
    private JPanel typePanel;
    private JTextField typeNameField;
    private JButton typeSubmit;
    
    private ArrayList<UserPanel> customerUserPanels;
    private ArrayList<UserPanel> waiterUserPanels;
    
    private JPanel containerPanel;
    
    private JCheckBox hungerBox;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
 
        restPanel = rp;
        this.type = type;
        setLayout(new BorderLayout());

        typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
        typePanel.setBorder(BorderFactory.createTitledBorder(type.toUpperCase()+" Information"));
        typePanel.add(new JLabel("Name: "));
        //create namefield
        typeNameField = new JTextField(10);
        typeNameField.setEnabled(true); //disable it initially
        typePanel.add(typeNameField);

        //panel for adding new objects
        JPanel additionPanel = new JPanel();
        additionPanel.setLayout(new BoxLayout(additionPanel, BoxLayout.X_AXIS));
        typeSubmit = new JButton("Add");
        //typeSubmit.setEnabled(false);
        typeSubmit.addActionListener(this);
        additionPanel.add(typeSubmit);
        //only customers get a hungerbox
        if(type.equals("customer")){
        	hungerBox = new JCheckBox("Hungry?");
        	additionPanel.add(hungerBox);
        }
        typePanel.add(additionPanel);
        
        add(typePanel, BorderLayout.NORTH);
        
        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBorder(BorderFactory.createTitledBorder(type.toUpperCase()+" List"));
        containerPanel.setMinimumSize(new Dimension(200, 200));
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        add(scrollPane, BorderLayout.CENTER);
      
        //initialize lists for keeping track of what already exists
        customerUserPanels = new ArrayList<UserPanel>();
        waiterUserPanels = new ArrayList<UserPanel>();
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == typeSubmit && !typeNameField.getText().isEmpty()) {
        	
        	//if the named person already exists, don't allow it break out here
        	if(type.equals("customer")){
        		for(UserPanel panel : customerUserPanels){
        			if(panel.getUserName().equals(typeNameField.getText()))
        				return;
        		}
        	}else if(type.equals("waiter")){
        		for(UserPanel panel : waiterUserPanels){
        			if(panel.getUserName().equals(typeNameField.getText()))
        				return;
        		}
        	}
        	
        	//unreachable code if the named person already exists
        	boolean hungry;
        	if(type.equals("customer")){
        		hungry = hungerBox.isSelected();
        	}else{
        		hungry = false;
        	}
        	//create the agent and start its thread
        	restPanel.addPerson(type, typeNameField.getText());
        	//add the person to the gui
        	addPersonToList(typeNameField.getText(), hungry);
        	
        	validate();
        	
        }
    }
    
    /**
     * Adds a gui component of the person to the list
     * @param name the name of the thing to add
     * @param hungry whether or not the person is hungry, irrelevant if type.equals("waiter")
     */
    public void addPersonToList(String name, boolean hungry){
    	switch(type){
    		//two different types of creation for two different types of lists
    		case "customer":
    	    	UserPanel userPanel = new UserPanel(name, hungry, "customer");
	        	customerUserPanels.add(userPanel);
	        	containerPanel.add(userPanel);
	        	validate();
    			break;
    	
    		case "waiter":
    			UserPanel waiterPanel = new UserPanel(name, false, "waiter");
    			containerPanel.add(waiterPanel);
    			waiterUserPanels.add(waiterPanel);
    			validate();
    			break;
    	}

    }
    
    /**
     * Reenables the use of the checkbox of the parameter-customer
     * @param name the name of the customer to enable
     */
    public void enableCustomer(String name){
    	for(int i = 0; i < customerUserPanels.size(); i++){
    		if(customerUserPanels.get(i).getUserName().equals(name)){
    			customerUserPanels.get(i).enableCheckBox();
    			return;
    		}
    	}
    }
    
    /**
     * Make a gui representation of a awiter not going on break
     * @param name the waiter who cannot go on break
     */
	public void waiterCannotGoOnBreak(String name){
		if(type.equals("waiter")){
			for(UserPanel panel : waiterUserPanels){
				if(panel.getUserName().equals(name)){
					panel.deselectCheckbox();
					panel.checkBox.setText("Break?");
				}
			}
		}
	}
    
    /**
     * Panel that links a customer name to its checkbox
     *
     */
    class UserPanel extends JPanel implements ActionListener{

		private String userName;
		private String type;
    	private JCheckBox checkBox;
    	
    	/**
    	 * Constructor
    	 * @param name name of customer
    	 * @param hungry whether or not they are hungry initially
    	 */
    	public UserPanel(String name, boolean hungry, String type){
    		userName = name;
    		this.type = type;
    		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    		add(new JLabel(userName));
    		
    		if(type.equals("customer")){
        		checkBox = new JCheckBox("Hungry? ");
    		}else{
    			checkBox = new JCheckBox("Break? ");
    		}
    		checkBox.addActionListener(this);
    		add(checkBox);
    		
    		//if initially hungry, message the restPanel to message the host
    		if(hungry){
    			checkBox.setSelected(true);
    			actionPerformed(null);
    		}
    	}
    	
		@Override
		/**
		 * Method for the ActionListener interface that will respond to checking boxes
		 * @param e The event that fired this method
		 */
		public void actionPerformed(ActionEvent e) {
			if(checkBox.isSelected()){
				restPanel.sendPersonWithName(userName, type);
				if(type.equals("customer"))
					checkBox.setEnabled(false);
				if(type.equals("waiter")){
					checkBox.setText("Back-to-Work?");
				}
			}else if(!checkBox.isSelected()){
				restPanel.takeWaiterOffBreak(userName);
				if(type.equals("waiter")){
					checkBox.setText("Break?");
				}
			}
		}
	
		
		/**
		 * Accessor for customers name
		 * @return the customers name
		 */
		public String getUserName(){
			return userName;
		}
		
		/**
		 * Reenables check box
		 */
		public void enableCheckBox(){
			checkBox.setEnabled(true);
			checkBox.setSelected(false);
		}
		
		public void deselectCheckbox(){
			checkBox.setSelected(false);
		}
    	
    }
}
