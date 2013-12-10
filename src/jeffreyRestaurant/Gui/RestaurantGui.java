package jeffreyRestaurant.Gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

import jeffreyRestaurant.CustomerAgent;
import jeffreyRestaurant.WaiterAgent;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    //private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JButton pause;

    private JPanel credPanel;
    private JLabel credLabel;
    private JLabel credLabelPic;
    
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
    
    private Boolean isPaused = false;

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 1500;
        int WINDOWY = 750;

        /*animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX, WINDOWY);
        animationFrame.setVisible(true);
    	animationFrame.*/add(animationPanel); 
    	
    	setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new GridLayout(2,4));

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
       // restPanel.setPreferredSize(restDim);
       // restPanel.setMinimumSize(restDim);
       // restPanel.setMaximumSize(restDim);
       // add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        pause = new JButton("Pause");
        pause.addActionListener(this);
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Enter a name</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        infoPanel.add(pause);
        add(infoPanel);
        
        //Added Creator panel
        Dimension credDim = new Dimension(WINDOWX, (int) (WINDOWY*.2));
        credPanel = new JPanel();
        credPanel.setPreferredSize(credDim);
        credPanel.setMinimumSize(credDim);
        credPanel.setMaximumSize(credDim);
        credPanel.setBorder(BorderFactory.createTitledBorder("Me"));
        
        credPanel.setLayout(new FlowLayout());
        
        credLabel = new JLabel();
        credLabel.setText("<html><pre>Welcome the the Katu Cafe!</pre></html>");
        credPanel.add(credLabel);
        
        credLabelPic = new JLabel(new ImageIcon("katu.png"));
        credPanel.add(credLabelPic);
        
        add(credPanel);
        
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if (person instanceof WaiterAgent) {
        	WaiterAgent waiter = (WaiterAgent) person;
        	stateCB.setText("Break?");
        	stateCB.setEnabled(true);
        	stateCB.setSelected(waiter.isOnBreak());
        	//stateCB.setEnabled(!waiter.isOnBreak());
        	infoLabel.setText(
        		"<html><pre>	Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            if (currentPerson instanceof WaiterAgent) {
            	WaiterAgent w = (WaiterAgent) currentPerson;
            	if (stateCB.isSelected()) {
            		w.setWantsBreak(stateCB.isSelected());
            	}
            	else if (!stateCB.isSelected()) {
            		w.setOffBreak();
            	}
            }
        }
        //Pause functionality
        //As of now obsolete so just commented it out. 
        /*
        if (e.getSource() == pause) {
        	Vector<CustomerAgent> customers = restPanel.getCustomers();
        	for (CustomerAgent c : customers) {
        		if (!isPaused) {
        			c.pause();
        			isPaused = true;
        		}
        		else {
        			c.resume();
        			isPaused = false;
        		}
        	}
        	Vector<WaiterAgent> waiters = restPanel.getWaiters();
        	for (WaiterAgent w : waiters) {
        		if (!isPaused) {
        			w.pause();
        			isPaused = true;
        		}
        		else {
        			w.resume();
        			isPaused = false;
        		}
        	}
        }
        */
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
//    /**
//     * Main routine to get gui started
//     */
//    public static void main(String[] args) {
//        RestaurantGui gui = new RestaurantGui();
//        gui.setTitle("csci201 Restaurant");
//        gui.setVisible(true);
//        gui.setResizable(false);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
}
