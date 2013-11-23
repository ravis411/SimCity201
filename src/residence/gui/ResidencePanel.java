package residence.gui;

import javax.swing.*;

import residence.HomeRole;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class ResidencePanel extends JPanel implements ActionListener {

    private HomeRole homeRole = new HomeRole("Test");

    private ResidenceGui gui; //reference to main gui
    
    public ResidenceGui getRestaurantGui() {
    	return gui;
    }
    
    public ResidencePanel(ResidenceGui gui) {
        this.gui = gui;
        
        //gui.animationPanel.addGui(homeRole);
        homeRole.startThread();
    }
    
    public void actionPerformed(ActionEvent e) {
        
    }
    
    public void tired() {
    	homeRole.msgTired();
    }

}
