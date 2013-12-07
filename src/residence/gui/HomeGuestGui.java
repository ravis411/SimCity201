package residence.gui;

import residence.HomeGuestRole;
import residence.HomeRole;

import java.awt.*;

import building.BuildingList;

public class HomeGuestGui implements Gui {

    private HomeGuestRole agent = null;

    private int xPos = 801, yPos = 150;//default position
    private int xDestination = 801, yDestination = 150;//default start position

    public HomeGuestGui(HomeGuestRole agent) {
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

        if (xPos == 805 && yPos == 150) {
            agent.msgAtFrontDoor();
        }
        if (xPos == 375 && yPos == 150) {
        	agent.msgAtCenter();
        }
    }

    public void draw(Graphics2D g) {
    	
        g.setColor(Color.GREEN);
        g.fillRect(xPos, yPos, 20, 20);
        if(xPos == 375 && yPos == 65) {
        	g.setColor(Color.black);
            g.fillOval(372, 47, 11, 11);
        }
    }
    
    public void DoGoToCenter() {
    	xDestination = 375;
    	yDestination = 150;
    }
    
    public void DoGoToFrontDoor() {
    	xDestination = 805;
    	yDestination = 150;
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