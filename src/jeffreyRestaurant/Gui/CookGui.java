package jeffreyRestaurant.Gui;


import java.awt.*;

import jeffreyRestaurant.CookAgent;
import jeffreyRestaurant.CustomerAgent;
import jeffreyRestaurant.HostAgent;
import jeffreyRestaurant.WaiterAgent;
import jeffreyRestaurant.interfaces.Cook;

public class CookGui implements Gui {

    private Cook agent = null;

    private int xPos = 160, yPos = 5;//default cook position
    private int xDestination = 160, yDestination = 5;//default start position
    private int xHome = 180, yHome = 5;

	
    public CookGui(Cook agent) {
        this.agent = agent;
        xPos = xHome;
    }
    

    public void updatePosition() {
        if (xPos < xDestination)
            xPos+= 5;
        else if (xPos > xDestination)
            xPos-= 5;

        if (yPos < yDestination)
            yPos+= 5;
        else if (yPos > yDestination)
            yPos-= 5;
        
        if (xPos == xDestination && xDestination == 200) 
        	agent.msgAnimationDone();
        if (xPos == xDestination && xDestination == 260)
        	agent.msgAnimationDone();
        
    }

    public void goHome() {
    	xDestination = xHome;
    	yDestination = yHome;
    }
    public void DoCookFood() {
    	xDestination = 200;
    }
    public void DoPlateFood() {
    	xDestination = 260;
    }
    public void draw(Graphics2D g) {
        g.setColor(Color.PINK);
        g.fillRect(xPos, yPos, 20, 20);
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
