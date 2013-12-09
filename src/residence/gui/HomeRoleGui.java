package residence.gui;

import residence.HomeRole;

import java.awt.*;

import building.BuildingList;

public class HomeRoleGui implements Gui {

    private HomeRole agent = null;
    public boolean hostingParty = false;

    private int xPos = 801, yPos = 150;//default waiter position
    private int xDestination = 375, yDestination = 150;//default start position

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
        if (xPos == 200 && yPos == 150) {
            agent.msgAtBedroom();
        }
        if (xPos == 375 && yPos == 65) {
            agent.msgAtKitchen();
        }
        if (xPos == 805 && yPos == 150) {
            agent.msgAtFrontDoor();
        }
        if (xPos == 371 && yPos == 195) {
        	agent.msgAtTable();
        }
        if (xPos == 375 && yPos == 150) {
        	agent.msgAtCenter();
        }
    }

    public void draw(Graphics2D g) {
    	
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        if(xPos == 375 && yPos == 65) {
        	g.setColor(Color.black);
            g.fillOval(372, 47, 11, 11);
            g.fillRect(377, 58, 2, 4);
        }
        if(xPos > 150 && xPos < 250 && yPos > 100 && yPos < 200) { //bedroom door
        	g.setColor(Color.orange);
            g.fillRect(200, 110, 45, 5);
            g.fillRect(200, 200, 45, 5);
        }
        else {
        	g.setColor(Color.orange);
            g.fillRect(200, 110, 5, 45);
            g.fillRect(200, 155, 5, 45);
            g.setColor(Color.black);
            g.fillRect(200, 153, 5, 2);
        }
        if((xPos > 700 && xPos < 800 && yPos > 110 && yPos < 210) || hostingParty) { //front door
        	g.setColor(Color.orange);
            g.fillRect(760, 120, 45, 5);
            g.fillRect(760, 210, 45, 5);
        }
        else {
        	g.setColor(Color.orange);
            g.fillRect(760, 120, 5, 45);
            g.fillRect(760, 165, 5, 45);
            g.setColor(Color.black);
            g.fillRect(760, 163, 5, 2);
        }
    }
    
    public void DoGoToCenter() {
    	xDestination = 375;
    	yDestination = 150;
    }
    
    public void DoGoToBed() {
    	xDestination = 50;
    	yDestination = 110;
    }
    
    public void DoGoToBedroom() {
    	xDestination = 200;
    	yDestination = 150;
    }
    
    public void DoGoToFrontDoor() {
    	xDestination = 805;
    	yDestination = 150;
    }
    
    public void DoGoToKitchen() {
    	xDestination = 375;
    	yDestination = 65;
    }
    
    public void DoGoToTable() {
    	xDestination = 371;
    	yDestination = 195;
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