package restaurant.gui;


import restaurant.RestaurantCustomerRole;
import restaurant.HostRole;
import restaurant.WaiterAgent;
import restaurant.CashierRole;

import java.awt.*;

public class CashierGui implements Gui {

    private CashierRole agent = null;

    private int xPos = 20, yPos = 100;//default waiter position
    //private int xDestination = -20, yDestination = -20;//default start position

    public CashierGui(CashierRole agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        /*if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable - 50) & (yDestination == yTable - 70)) {
           //agent.msgAtTable();
        }
        else if (xPos == 80 && yPos == 300) {
        	//agent.msgAtKitchen();
        }
        else if (xPos == -20 && yPos == -20) {
        	//agent.msgAtFrontDesk();
        }*/
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(xPos, yPos, 25, 25);
    }

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
