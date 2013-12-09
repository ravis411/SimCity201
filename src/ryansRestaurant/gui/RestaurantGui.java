package ryansRestaurant.gui;

import interfaces.GuiPanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ryansRestaurant.RyansCashierRole;
import ryansRestaurant.RyansCookRole;
import ryansRestaurant.RyansCustomerRole;
import ryansRestaurant.RyansHostRole;
import ryansRestaurant.RyansWaiterRole;
import Person.Role.Role;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class RestaurantGui extends JPanel {
    
	RestaurantLayout layout = new RestaurantLayout();
	RyansRestaurantAnimationPanel animationPanel = new RyansRestaurantAnimationPanel(layout, this);
	
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked RyansCustomer (created just below)
     */    
    RestaurantPanel restPanel = new RestaurantPanel(this);
    
    // Holds the components that aren't the animation.
    private JPanel restInfoControlPanel;
    
    //A Frame for the controlPanel ;)
   // JFrame controlFram = new JFrame();
    ControlPanel ctrlP = new ControlPanel(this);
    //JButton controlB = new JButton("Control Panel");
    
    CardLayout cardLayout = new CardLayout();
    
    
     /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 800;
        int WINDOWY = 400;

       
        animationPanel.setBounds(new Rectangle(WINDOWX, 400) );
    	    	
    	

       // setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(cardLayout);
        
        restInfoControlPanel = new JPanel();
        restInfoControlPanel.setLayout(new BoxLayout((Container) restInfoControlPanel,BoxLayout.X_AXIS));
        

        Dimension restDim = new Dimension((int)(WINDOWX *.7), 350);
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        
        
        //restInfoControlPanel.add(controlB);
        //controlB.addActionListener(this);
             
        Dimension ctrlPDim = new Dimension((int)(WINDOWX * .3), 400);
        ctrlP.setMinimumSize(ctrlPDim);
        ctrlP.setPreferredSize(ctrlPDim);
        ctrlP.setMaximumSize(ctrlPDim);
        
        restInfoControlPanel.add(ctrlP);
        restInfoControlPanel.add(restPanel);
       
        add(animationPanel, "Animation Panel");
       // add(animationPanel);
        add(restInfoControlPanel, "Info Panel");
     
        
        //controlFram.setBounds(50, WINDOWY + 100, WINDOWX, WINDOWY);
        //controlFram.add(ctrlP);
        //controlFram.setVisible(false);
      
        
    }
    
    
    public RyansRestaurantAnimationPanel getAnimationPanel(){
    	return this.animationPanel;
    }
   
    
 
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(RyansCustomerRole c) {
        restPanel.setCustomerBox(c.getName(), false, true);
    }
    
    public void setWaiterBreakStatus(RyansWaiterRole w, String status) {
    	if(status.equalsIgnoreCase("none")) {
    		restPanel.setWaiterBB(w.getName(), Color.green, true, "Break?", "Working. Request a Break?");
    	}
    	else if(status.equalsIgnoreCase("preparingForBreak")) {
    		restPanel.setWaiterBB(w.getName(), Color.yellow, true, "back-to-work", "Preparing to go on break...back-to-work?");
    	}
    	else if(status.equalsIgnoreCase("onbreak")) {
    		restPanel.setWaiterBB(w.getName(), Color.red, true, "back-to-work", "On break. Back-to-work?");
    	}
    }
    
    
    public void showInfoPanel(boolean visible){
    	if(visible)
    			cardLayout.show(this, "Info Panel");
    	else
    		cardLayout.show(this, "Animation Panel");
    }
  

	
}
