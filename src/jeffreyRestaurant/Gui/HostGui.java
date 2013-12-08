package jeffreyRestaurant.Gui;


import java.awt.*;

import jeffreyRestaurant.CustomerAgent;
import jeffreyRestaurant.HostAgent;
import jeffreyRestaurant.WaiterAgent;

public class HostGui implements Gui {

    private WaiterAgent agent = null;

    private int xPos = 80, yPos = 60;//default waiter position
    private int xDestination = 20, yDestination = 20;//default start position
    private int xHome = 80, yHome = 60;

	public static final int xTable1 = 200;//kludge
	public static final int yTable1 = 250;
	
	public static final int xTable2 = 100;
	public static final int yTable2 = 250;
	
	public static final int xTable3 = 200;
	public static final int yTable3 = 150;
	
	public static final int xCook = 260;
	public static final int yCook = 50;
	
    public HostGui(WaiterAgent agent, int homeNumber) {
        this.agent = agent;
        xHome = 50 + 30*homeNumber;
        xPos = xHome;
    }
    
    public HostGui(WaiterAgent agent) {
    	this.agent = agent;
    	xHome = 50;
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

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable1 + 20) & (yDestination == yTable1 - 20)) {
        	//System.out.println(xPos + "_" + yPos + "_" + xDestination + "_" + yDestination);
           agent.msgAtDestination();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable2 + 20) & (yDestination == yTable2 - 20)) {
        	//System.out.println(xPos + "_" + yPos + "_" + xDestination + "_" + yDestination);
           agent.msgAtDestination();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable3 + 20) & (yDestination == yTable3 - 20)) {
        	//System.out.println(xPos + "_" + yPos + "_" + xDestination + "_" + yDestination);
           agent.msgAtDestination();
        }
        if (xPos == xDestination && yPos == yDestination 
        	& (xDestination == xCook) & (yDestination == yCook)) {
        	agent.msgAtDestination();
        }
        
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(CustomerAgent customer, int table) {
    	if (table == 1) {
    		xDestination = xTable1 + 20;
    		yDestination = yTable1 - 20;
    		
    	}
    	else if (table == 2){
    		xDestination = xTable2 + 20;
            yDestination = yTable2 - 20;
    	}
    	else if (table == 3){
    		xDestination = xTable3 + 20;
            yDestination = yTable3 - 20;
    	}
    }
    
    public void DoGoToTable(int table) {
    	if (table == 1) {
    		xDestination = xTable1 + 20;
    		yDestination = yTable1 - 20;
    		
    	}
    	else if (table == 2){
    		xDestination = xTable2 + 20;
            yDestination = yTable2 - 20;
    	}
    	else if (table == 3){
    		xDestination = xTable3 + 20;
            yDestination = yTable3 - 20;
    	}
    }
    
    public void DoGoToCook() {
    	xDestination = xCook;
    	yDestination = yCook;
    }
    
    public void DoLeaveCustomer() {
    	//System.out.println("Going Home");
        xDestination = xHome;
        yDestination = yHome;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
