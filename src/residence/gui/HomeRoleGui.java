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
        if (xPos == 200 && yPos == 150) {
            agent.msgAtBedroom();
        }
        if (xPos == 375 && yPos == 65) {
            agent.msgAtKitchen();
        }
        if (xPos == 805 && yPos == 200) {
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
        if(xPos > 700 && xPos < 800 && yPos > 150 && yPos < 250) { //front door
        	g.setColor(Color.orange);
            g.fillRect(760, 160, 45, 5);
            g.fillRect(760, 250, 45, 5);
        }
        else {
        	g.setColor(Color.orange);
            g.fillRect(760, 160, 5, 45);
            g.fillRect(760, 205, 5, 45);
            g.setColor(Color.black);
            g.fillRect(760, 203, 5, 2);
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
    	yDestination = 200;
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