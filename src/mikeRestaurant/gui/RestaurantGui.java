package mikeRestaurant.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mikeRestaurant.CustomerAgent;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame{
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	
	AnimationPanel animationPanel = new AnimationPanel();
	RestaurantPanel restPanel;

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 1000;
        int WINDOWY = 500;
       
        //setBounds(0, 0, WINDOWX, WINDOWY);
        this.setMinimumSize(new Dimension(WINDOWX, WINDOWY));
    	
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
    	add(animationPanel);
    	restPanel = new RestaurantPanel(this);
    	add(restPanel);
        setVisible(true);
    }

    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
//        if (currentPerson instanceof CustomerAgent) {
//            CustomerAgent cust = (CustomerAgent) currentPerson;
//            if (c.equals(cust)) {
//                stateCB.setEnabled(true);
//                stateCB.setSelected(false);
//            }
//        }
    }
    
    public void enableCustomer(String name){
    	restPanel.enableCustomer(name);
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
