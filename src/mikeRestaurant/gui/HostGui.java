package mikeRestaurant.gui;


import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import mikeRestaurant.CustomerRole;
import mikeRestaurant.HostRole;

public class HostGui implements Gui {

    private HostRole agent = null;
    private boolean movingToTable;

    private ImageIcon icon;
    
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -60, yDestination = -60;//default start position

    private static final int X_INITIAL = -60;
    private static final int Y_INITIAL = -60;
    
    private boolean readyToSeatCustomer;

    public HostGui(HostRole agent) {
        this.agent = agent;
        icon = new ImageIcon("res/waitress.png");
        movingToTable = false;
        readyToSeatCustomer = false;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (movingToTable) ) {
           //agent.msgAtTable();
           movingToTable = false;
        }
        
        if(xPos == X_INITIAL && yPos == Y_INITIAL && !readyToSeatCustomer){
        	//agent.msgReadyToSeatCustomer();
        	readyToSeatCustomer = true;
        }
    }

    public void draw(Graphics2D g) {
        icon.paintIcon(new JLabel("Host"), g, xPos, yPos);
    }

    public boolean isPresent() {
        return true;
    }
    
    public void hasLeftToSeatCustomer(){
    	readyToSeatCustomer = false;
    }

    public void DoBringToTable(CustomerRole customer, int table) {
    	int col = (table-1) % MikeAnimationPanel.NUM_COLUMNS;
    	int row = (table-1) / MikeAnimationPanel.NUM_COLUMNS;
        xDestination = MikeAnimationPanel.TABLE_X + col*MikeAnimationPanel.TABLE_WIDTH*2 + icon.getIconWidth();
        yDestination = MikeAnimationPanel.TABLE_Y + row*MikeAnimationPanel.TABLE_HEIGHT*2 - icon.getIconHeight();
        movingToTable = true;
    }

    public void DoLeaveCustomer() {
        xDestination = X_INITIAL;
        yDestination = Y_INITIAL;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
