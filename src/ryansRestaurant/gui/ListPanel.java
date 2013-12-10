package ryansRestaurant.gui;

import ryansRestaurant.RyansCustomerRole;
import ryansRestaurant.RyansHostRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    private JScrollPane customerPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private JPanel waiterView = new JPanel();
    
    private List<JButton> customerList = new ArrayList<JButton>();
    private JScrollPane waiterPane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private List<JButton> waiterList = new ArrayList<JButton>();
    private JPanel panePanel = new JPanel(); // a panel to hold the  two scroll panes
    
    //Ryan
    //Add components for adding a customer and waiter
    private List<JCheckBox> cBList = new ArrayList<JCheckBox>();//list to hold customer checkboxes
    private List<JButton> wBreakButtonList = new ArrayList<JButton>();//list to hold waiter break buttons
    private JButton addButton = new JButton("Add");
    //private JPanel addPersonPanel = new JPanel();
    private JPanel headerPanel = new JPanel();
    private JPanel inputPanel = new JPanel(); 
    private JTextField addPersonTF = new JTextField();
    private JTextField addWaiterTF = new JTextField();
    private JCheckBox addPersonCB = new JCheckBox();
    private JCheckBox addWaiterCB = new JCheckBox();

    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the ryansRestaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        //headerPanel.setLayout(new BoxLayout((Container) headerPanel, BoxLayout.X_AXIS));
        headerPanel.setLayout(new GridLayout(1, 3, 10, 0));
        headerPanel.add(new JLabel("<html><pre>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<u>Waiters</u></pre></html>"));
        headerPanel.add(addButton);
        headerPanel.add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        //addPersonB.addActionListener(this);
        add(headerPanel);
        addButton.addActionListener(this);
        
        //Ryan
        //Add TF and button to addPersonPanel
       // addPersonPanel.add(addPersonB);
        addPersonTF.setToolTipText("Enter a name, then press add.");
        addWaiterTF.setToolTipText("Enter a name, then press add.");
        Dimension tFSize = new Dimension(75, 25);
        addPersonTF.setMaximumSize(tFSize);
        addPersonTF.setMinimumSize(tFSize);
        addPersonTF.setPreferredSize(tFSize);
        addPersonTF.addActionListener(this);
        addWaiterTF.setMaximumSize(tFSize);
        addWaiterTF.setMinimumSize(tFSize);
        addWaiterTF.setPreferredSize(tFSize);
        addWaiterTF.addActionListener(this);
        //addPersonPanel.setLayout(new BoxLayout((Container) addPersonPanel, BoxLayout.X_AXIS));
        addPersonCB.setText("Hungry?");
        addPersonCB.setSelected(true);
        addWaiterCB.setText("Break?");
        //addPersonPanel.add(addPersonTF);
        //addPersonPanel.add(addPersonCB);
        //addPersonPanel.add(addPersonB);
       // add(addPersonPanel);
        //inputPanel.setLayout(new BoxLayout((Container) inputPanel, BoxLayout.X_AXIS));
        inputPanel.setLayout(new GridLayout(1, 4));
        inputPanel.add(addWaiterTF);
        inputPanel.add(addWaiterCB);
        inputPanel.add(addPersonTF);
        inputPanel.add(addPersonCB);
        add(inputPanel);

        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        waiterView.setLayout(new BoxLayout((Container) waiterView, BoxLayout.Y_AXIS));
        customerPane.setViewportView(view);
        waiterPane.setViewportView(waiterView);
        
        panePanel.setLayout(new BoxLayout((Container)panePanel ,BoxLayout.X_AXIS));
        
        
        Dimension paneSize = new Dimension();
        paneSize.setSize( (190) , 260 );
        customerPane.setPreferredSize(paneSize);
        customerPane.setMaximumSize(paneSize);
        customerPane.setMinimumSize(paneSize);
        waiterPane.setPreferredSize(paneSize);
        waiterPane.setMaximumSize(paneSize);
        waiterPane.setMinimumSize(paneSize);
        
        
        panePanel.add(waiterPane);
        panePanel.add(customerPane);
        
        add(panePanel);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton || e.getSource() == addPersonTF || e.getSource() == addWaiterTF) {
        	//if they both have text, clear them
        	//if(!addPersonTF.getText().isEmpty() && !addWaiterTF.getText().isEmpty()) {
        		//addPersonTF.setText("");
        		//addWaiterTF.setText("");
        	//}
        	//add a person
        	if(!addPersonTF.getText().isEmpty())
        	{
        		List<String> custs = numToCustWaiterList(addPersonTF.getText(), "c");//hack to add multiple custs
        		if(custs == null) {
        			addPerson(addPersonTF.getText(), addPersonCB.isSelected());
        		}
        		else {
        			for(String s : custs) {
        				addPerson(s, addPersonCB.isSelected());
        			}
        		}
        		addPersonTF.setText("");
    			//addPersonCB.setSelected(false);
        	}
        	//add a waiter
        	if(!addWaiterTF.getText().isEmpty()) {
        		List<String> waits = numToCustWaiterList(addWaiterTF.getText(), "w");//hack to add multiple custs
        		if(waits == null) {
        			addWaiter(addWaiterTF.getText(), addWaiterCB.isSelected());
        		}
        		else {
        			for(String s : waits) {
        				addWaiter(s, addWaiterCB.isSelected());
        			}
        		}
        		addWaiterTF.setText("");
        		addWaiterCB.setSelected(false);
        	}
        }
        //Check box was selected
        else if(e.getSource() instanceof JCheckBox)
        {
        	for(JCheckBox temp:cBList){
        		if(e.getSource() == temp)
        		{
        			restPanel.checkBoxed(temp.getName());
        			temp.setEnabled(false);
        		}
        	}
        }
        //A button
        else if(e.getSource() instanceof JButton) {
        	//RyansWaiter Break button
        	for(JButton temp:wBreakButtonList) {
        		if(e.getSource() == temp) {
        			if(temp.getBackground() == Color.green) {
        				restPanel.giveMeABreak(temp.getName());
        			}
        			else if(temp.getBackground() == Color.red || temp.getBackground() == Color.yellow) {
        				temp.setEnabled(false);
        				temp.setBackground(Color.DARK_GRAY);
        				temp.setToolTipText("Canceling break...");
        				restPanel.backToWork(temp.getName());
        			}
        			
        		}
        	}
        	
        	//RyansCustomer info button
        	for(JButton temp : customerList) {
        		if(e.getSource() == temp) {
        			restPanel.gui.ctrlP.showCustomerInfo(temp.getText());
        		}
        	}
        	
        }
    }
    
    /** Hack for customer, waiter name input, if the input is a number this will add that many customers
     * 		or waiters.
     * 
     * @param num			The number of agents to add
     * @param startChar		"c" for customers, "w" for waiters
     * @return				A list of the names of agents to add, null if num is not a number or 0
     */
    private List<String> numToCustWaiterList(String num, String startChar) {
    	int number;
    	try {
			 number = Integer.parseInt(num);
		} catch (NumberFormatException e) {
			return null;
		}
    	
    	if(number == 0) {
    		return null;
    	}
    	
    	String s = new String();
    	int highest = 0;
    	int buff;
    	//it's a number so find the highest c_ in the list
    	List<JButton> list = null;
    	if(startChar.equals("c")) {
    		list = customerList;
    	}
    	else if(startChar.equals("w")) {
    		list = waiterList;
    	}
    	
    	for(JButton j : list) {
    		s = j.getText();
    		if(s.startsWith(startChar)) {
    			try {
					buff = Integer.parseInt(s.substring(1));
					if(buff > highest)
						highest = buff;
				} catch (NumberFormatException e) {
					//The rest of the string was not an int
				}
    		}
    	}
    	
    	highest++;
    	
    	List<String> newCusts = new ArrayList<String>();
    	
    	for(int i = 0; i < number; i++) {
    		newCusts.add(startChar + highest);
    		highest++;
    	}
    	return newCusts;
    }
    

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the ryansRestaurant panel
     * to add a new person.
     *
     * @param name name of new person
     * @param hungry true if the person is hungry false otherwise
     */
    public void addPerson(String name, boolean hungry) {
         	
    	if (name != null) {
    		
    		
    		//If the name exists don't add them.
    		for(JButton c : customerList) {
    			if(c.getText().equals(name)) {
    				return;	}}
            
        	//Panel to hold the button and checkbox
        	JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout((Container) panel, BoxLayout.X_AXIS));
            
            //Check box for the customer
            JCheckBox check = new JCheckBox();
            //check.setText("Hungry?");
            check.setName(name);
            check.addActionListener(this);
            
        	JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = customerPane.getSize();
            Dimension buttonSize = new Dimension( (int)((paneSize.width)*.65),
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            
            
           // button.addActionListener(this);
            customerList.add(button);
            cBList.add(check);
            
            panel.add(button);
            panel.add(check);
            view.add(panel);
            
            restPanel.addPerson(type, name);//puts customer on list
            if(hungry)
            	restPanel.checkBoxed(name);
            
            validate();
        }
    }
    
    public void addWaiter(String name, boolean requestBreak) {
    	if(waiterList.size() >= 8)
    		return;
    	
    	
    	if (name != null) {
            
    		//If the name exists don't add them.
    		for(JButton w : waiterList) {
    			if(w.getText().equals(name)) {
    				return;	}}
    		
    		
    		//Panel to hold the button and checkbox
        	JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout((Container) panel, BoxLayout.X_AXIS));
            
            //Check box for the waiter
            //JCheckBox check = new JCheckBox();
            //check.setText("Hungry?");
            
            //check.addActionListener(this);
            
        	JButton button = new JButton(name);
        	
            button.setBackground(Color.white);

            Dimension paneSize = customerPane.getSize();
            Dimension buttonSize = new Dimension( (int)((paneSize.width)*.4),
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            
            //button for waiter break
            Dimension breakbuttonSize = new Dimension( (int)((paneSize.width)*.6),
                    (int) (paneSize.height / 7.5));
            JButton breakB = new JButton("Break?");
            breakB.setToolTipText("Request Break.");
            breakB.setBackground(Color.green);
            breakB.setBorderPainted(false);
            breakB.addActionListener(this);
            breakB.setPreferredSize(breakbuttonSize);
            breakB.setMinimumSize(breakbuttonSize);
            breakB.setMaximumSize(breakbuttonSize);
            breakB.setName(name);
            wBreakButtonList.add(breakB);
            
            waiterList.add(button);
            
//            cBList.add(check);
            
            panel.add(button);
            panel.add(breakB);
            //panel.add(check);
            waiterView.add(panel);
            
            restPanel.addPerson("Waiters", name);//puts customer on list
            if(requestBreak) {
            	restPanel.giveMeABreak(name);
            }
                        
            validate();
        }
    }

    
    /**Sets the waiter's break button with the given name to color, enabled, and the tooltip.*/
	public void setWaiterBB(String name, Color color, boolean enabled, String text, String tooltip) {
		//Find the button
		for(JButton temp:wBreakButtonList)
		{
			if(temp.getName().equals(name)){
				temp.setBackground(color);
				temp.setEnabled(enabled);
				temp.setText(text);
				temp.setToolTipText(tooltip);
			}
		}
	}
    
    /**Sets the customer's checkbox with the given name to selected and enabled*/
	public void setCustomerCBox(String name, boolean selected, boolean enabled) {
		//Find the checkbox
		for(JCheckBox temp:cBList)
		{
			if(temp.getName().equals(name)){
				temp.setSelected(selected);
				temp.setEnabled(enabled);
			}
		}
	}
	
	
}
