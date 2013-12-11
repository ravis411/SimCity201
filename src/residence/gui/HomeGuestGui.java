package residence.gui;

import residence.HomeGuestRole;
import residence.HomeRole;

import java.awt.*;
import java.util.Random;

import building.BuildingList;

public class HomeGuestGui implements Gui {

    private HomeGuestRole agent = null;
    public boolean leaveParty = false;

    private int xPos = 801, yPos = 150;//default position
    private int xDestination = 801, yDestination = 150;//default start position
    private int xRand = -1, yRand = -1;

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
        if (leaveParty == true) {
        	DoGoToFrontDoor();
        }
        else {
	        if (xPos == 600 && yPos == 150) {
	        	agent.msgAtCenter();
	        	DoMingle();
	        }
	        if (xPos == xRand && yPos == yRand) {
	        	DoGoToCenter();
	        }
        }
    }

    public void draw(Graphics2D g) {
    	
        g.setColor(Color.GREEN);
        g.fillRect(xPos, yPos, 20, 20);
    }
    
    public void DoGoToCenter() {
    	xDestination = 600;
    	yDestination = 150;
    }
    
    public void DoGoToFrontDoor() {
    	xDestination = 805;
    	yDestination = 150;
    }
    
    public void DoMingle() {
    	 Random rand = new Random();
    	 xRand = rand.nextInt((600 - 475) + 1) + 475;
    	 yRand = rand.nextInt((275 - 200) + 1) + 200;
    	 xDestination = xRand;
    	 yDestination = yRand;
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