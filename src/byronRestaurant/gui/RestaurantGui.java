package byronRestaurant.gui; 

import javax.swing.*;

import byronRestaurant.CustomerRole;
import byronRestaurant.WaiterRole;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
//	JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel;
    private JCheckBox stateCB; //part of infoLabel
    private JPanel idPanel;
    private JLabel idLabel;

    private Object currentPerson; /* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    /**
     * 
     */
    /**
     * 
     */
    public RestaurantGui() {
        int WINDOWX = 450;
        int WINDOWY = 300;
        int BOUNDX = 800;
        int BOUNDY = 600;
        int ORIGINX = 50;
        int ORIGINY = 50;
//        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        animationFrame.setBounds(250+WINDOWX, ORIGINY , WINDOWX, WINDOWY);
//        animationFrame.setVisible(true);
//    	animationFrame.add(animationPanel); 
    	
    	setBounds(ORIGINX, ORIGINY, BOUNDX, BOUNDY);

        setLayout(new BorderLayout(20,20));

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel, BorderLayout.PAGE_START);
        //setting up Identification panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add(infoPanel, BorderLayout.PAGE_END);
/*
        idPanel = new JPanel();
        idPanel.setPreferredSize(infoDim);
        idPanel.setMinimumSize(infoDim);
        idPanel.setMaximumSize(infoDim);
        idPanel.setBorder(BorderFactory.createTitledBorder("My Information"));
        idPanel.setLayout(new BorderLayout());
        
        ImageIcon image = new ImageIcon("C:/users/Hiro/workspace/agents/resources/tennis-ball.png");
        JLabel imageLabel = new JLabel(image);
        idPanel.add(imageLabel, BorderLayout.CENTER);
        
        idLabel = new JLabel();
        idLabel.setText("Byron Choy");
        idPanel.add(idLabel, BorderLayout.WEST);
        add(idPanel, BorderLayout.PAGE_END);

*/
        add(animationPanel, BorderLayout.CENTER);

    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        currentPerson = person;

        if (person instanceof CustomerRole) {
            stateCB.setVisible(true);
            CustomerRole customer = (CustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if (person instanceof WaiterRole){
        	stateCB.setVisible(false);
        	WaiterRole waiter = (WaiterRole) person;
        	infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
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
            if (currentPerson instanceof CustomerRole) {
                CustomerRole c = (CustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
        if (e.getSource() == restPanel.customerPanel.hungry){
        	if (currentPerson instanceof CustomerRole){
        		CustomerRole c = (CustomerRole) currentPerson;
        		c.getGui().setHungry();
        		stateCB.setEnabled(false);
        		stateCB.setSelected(true);
        	}
        }
    }	
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerRole c) {
        if (currentPerson instanceof CustomerRole) {
            CustomerRole cust = (CustomerRole) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        } 
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
