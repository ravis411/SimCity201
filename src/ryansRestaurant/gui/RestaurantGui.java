package ryansRestaurant.gui;

import ryansRestaurant.CustomerAgent;
import ryansRestaurant.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class RestaurantGui extends JFrame implements ActionListener {
    
	RestaurantLayout layout = new RestaurantLayout();
	AnimationPanel animationPanel = new AnimationPanel(layout);
	
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    RestaurantPanel restPanel = new RestaurantPanel(this);
    
    // Holds the components that aren't the animation.
    private JPanel restInfoControlPanel;
    
    //A Frame for the controlPanel ;)
   // JFrame controlFram = new JFrame();
    ControlPanel ctrlP = new ControlPanel(this);
    //JButton controlB = new JButton("Control Panel");
    
    
     /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 450;
        int WINDOWY = 350;

       
        animationPanel.setBounds(new Rectangle(WINDOWX, (int)(WINDOWY*1.5)) );
    	    	
    	setBounds(50, 50, WINDOWX + WINDOWX , WINDOWY + (int)(1.5 * WINDOWY));

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));
        
        restInfoControlPanel = new JPanel();
        restInfoControlPanel.setLayout(new BoxLayout((Container) restInfoControlPanel,BoxLayout.X_AXIS));
        

        Dimension restDim = new Dimension(WINDOWX + (int)(WINDOWX * .2), (int) (WINDOWY  *.9 ));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        
        
        //restInfoControlPanel.add(controlB);
        //controlB.addActionListener(this);
             
        Dimension ctrlPDim = new Dimension((int)(WINDOWX * .6), WINDOWY);
        ctrlP.setMinimumSize(ctrlPDim);
        ctrlP.setPreferredSize(ctrlPDim);
        ctrlP.setMaximumSize(ctrlPDim);
        
        restInfoControlPanel.add(ctrlP);
        restInfoControlPanel.add(restPanel);
        add(restInfoControlPanel);
        
        add(animationPanel);
        
        
        //controlFram.setBounds(50, WINDOWY + 100, WINDOWX, WINDOWY);
        //controlFram.add(ctrlP);
        //controlFram.setVisible(false);
      
        
    }
   
    
    @Override
	public void actionPerformed(ActionEvent e) {
		
    	//if(e.getSource() == controlB){
    		//controlFram.setVisible(true);
    	//}
		
	}

    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        restPanel.setCustomerBox(c.getName(), false, true);
    }
    
    public void setWaiterBreakStatus(WaiterAgent w, String status) {
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
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant - Ryan Davis");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


	
}
