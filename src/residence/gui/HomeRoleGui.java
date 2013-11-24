package residence.gui;

import residence.HomeRole;

import java.awt.*;

public class HomeRoleGui implements Gui {

    private HomeRole agent = null;

    private int xPos = 700, yPos = 200;//default waiter position
    private int xDestination = 700, yDestination = 200;//default start position

    public HomeRoleGui(HomeRole agent) {
        this.agent = agent;
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

        if (xPos == 50 && yPos == 110) {
           agent.msgAtBed();
        }
        /*if (xPos == 50 && yPos == 110) {
            agent.msgAtBedroom();
        }*/
        if (xPos == 375 && yPos == 65) {
            agent.msgAtKitchen();
        }
        if (xPos == 750 && yPos == 205) {
            agent.msgAtFrontDoor();
        }
        if (xPos == 370 && yPos == 80) {
        	agent.msgAtTable();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        if(xPos == 375 && yPos == 65) {
        	g.setColor(Color.black);
            g.fillOval(372, 47, 11, 11);
        }
    }
    
    public void DoGoToBed() {
    	xDestination = 50;
    	yDestination = 110;
    }
    
    public void DoGoToFrontDoor() {
    	xDestination = 750;
    	yDestination = 205;
    }
    
    public void DoGoToKitchen() {
    	xDestination = 375;
    	yDestination = 65;
    }
    
    public void DoGoToTable() {
    	xDestination = 370;
    	yDestination = 80;
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