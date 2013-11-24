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

        if (xPos == xDestination && yPos == yDestination) {
           //agent.msgAtTable();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }
    
    public void DoGoToBed() {
    	/*boolean inBedroom = false;
    	xDestination = 200;
    	yDestination = 150;
    	while(xPos > 201) {
    		inBedroom = false;
    		System.out.println("asdad");
    	}
    	inBedroom = true;
    	System.out.println("DONE");
    	if(inBedroom == true) {*/
    		xDestination = 50;
    		yDestination = 110;
    	//}
    }
    
    public void DoGoToFrontDoor() {
    	xDestination = 750;
    	yDestination = 205;
    }
    
    public void DoGoToKitchen() {
    	xDestination = 377;
    	yDestination = 365;
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