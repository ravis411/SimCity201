package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JPanel animationFrame = new JPanel();
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JPanel userPanel;
    private JLabel infoLabel; //part of infoPanel
    private JLabel userLabel; //part of userPanel
    private JLabel picLabel; //part of userPanel
    private JCheckBox stateCB;//part of infoLabel
    
    private JPanel hackPanel; //for testing different scenarios
    private JButton clearInventory; //clears cook's inventory
    private JButton drainMoney; //makes cashier's money balance go to zero
    private JButton giveMoney; //makes cashier's money balance go to two hundred
    
    private ImageIcon mypic;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 450;
        int WINDOWY = 400;

        //animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        //animationFrame.setSize(400,300);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); 
    	
    	setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));
        
        //setLayout(new FlowLayout());
        
    	//setLayout(new GridLayout());
        
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .54));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .15));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout());
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        //infoPanel.add(stateCB);
        add(infoPanel);
        
        //Creation of own info panel
        userPanel = new JPanel();
        userPanel.setPreferredSize(infoDim);
        userPanel.setMinimumSize(infoDim);
        userPanel.setMaximumSize(infoDim);
        userPanel.setBorder(BorderFactory.createTitledBorder("Developer Info"));
        
        userLabel = new JLabel(); 
        userLabel.setText("<html><pre>Dylan Resnik</pre></html>");
        userPanel.add(userLabel);
        
        mypic = new ImageIcon("happy-face2.jpg");
        picLabel = new JLabel(mypic);
        userPanel.add(picLabel);
        
        add(userPanel);
        
        hackPanel = new JPanel();
        hackPanel.setPreferredSize(infoDim);
        hackPanel.setMinimumSize(infoDim);
        hackPanel.setMaximumSize(infoDim);
        hackPanel.setBorder(BorderFactory.createTitledBorder("Hacks for scenario testing"));
        
        clearInventory = new JButton("Clear inventory");
        clearInventory.addActionListener(this);
        hackPanel.add(clearInventory);
        
        drainMoney = new JButton("Empty register");
        drainMoney.addActionListener(this);
        giveMoney = new JButton("Fill register");
        giveMoney.addActionListener(this);
        hackPanel.add(drainMoney);
        hackPanel.add(giveMoney);
        
        add(hackPanel);
        
        add(animationPanel);
        
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
        infoPanel.validate();
    }
    
    public void initializeHungry(int state, CustomerAgent c){
    	if (state == 1) {
    		 c.getGui().setHungry();
             stateCB.setEnabled(false);
    	}
    }
    
    public void Hungerize(CustomerAgent c){
    	c.getGui().setHungry();
        //stateCB.setEnabled(false);
    }
    
    public void breakRequest(WaiterAgent w){
    	w.msgRequestBreak();
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
                //restPanel.getCustomerPanel().hungry.setEnabled(false);
                //restPanel.getCustomerPanel().hungry.setSelected(true);
            }
        }
        if (e.getSource() == clearInventory) {
        	restPanel.clearCooksInventory();
        }
        if (e.getSource() == drainMoney) {
        	restPanel.emptyRegister();
        }
        if (e.getSource() == giveMoney) {
        	restPanel.fillRegister();
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
       // if (currentPerson instanceof CustomerAgent) {
       //     CustomerAgent cust = (CustomerAgent) currentPerson;
       //     if (c.equals(cust)) {
                //stateCB.setEnabled(true);
                //stateCB.setSelected(false);
            	restPanel.enableCheckBox(c);
            //}
        //}
    }
    
    
    public void setWaiterEnabled(WaiterAgent w) {
    	restPanel.enableCheckBox(w);
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setSize(480, 670);
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
