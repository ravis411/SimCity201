package restaurant.gui;


import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.RestaurantCustomerRole;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class WaiterGui implements Gui {

    private Waiter agent = null;
    
    RestaurantGui gui;

    private int xTable = 200;
    private int yTable = 0;
    private int xHome = -1;
    private int yHome = -1;
    
    private int xPos = 200, yPos = 0;//default waiter position
    private int xDestination = 200, yDestination = 0;//default start position

    public WaiterGui(Waiter agent,int xHome, int yHome) {
        this.agent = agent;
        this.gui = gui;
        this.xHome = xHome;
        this.yHome = yHome;
        xPos = xHome;
        yPos = yHome;
        xDestination = xHome;
        yDestination = yHome;
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
        		& (xDestination == xTable - 50) & (yDestination == yTable - 70)) {
           agent.msgAtTable();
        }
        else if (xPos == 350 && yPos == 50) {
        	agent.msgAtCookingArea();
        }
        else if (xPos == 350 && yPos == 160) {
        	agent.msgAtPlatingArea();
        }
        else if (xPos == 60 && yPos == 40) {
        	agent.msgAtWaitingArea();
        }
        else if (xPos == xHome && yPos == yHome) {
        	agent.msgAtFrontDesk();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToIdle() {
    	xDestination = xHome;
    	yDestination = yHome;
    }
    
    public void DoGoGetCustomer() {
    	xDestination = 60;
    	yDestination = 40;
    }
    
    public void DoBringToTable(Customer customer) {
        xDestination = customer.getTableX() - 50;
        yDestination = customer.getTableY() - 70;
        xTable = customer.getTableX();
        yTable = customer.getTableY();
    }

    public void DoLeaveCustomer() {
        xDestination = xHome;
        yDestination = yHome;
    }
    
    public void DoGoTakeOrder(Customer customer) {
    	//xDestination = customer.getTableX() + 20;
        //yDestination = customer.getTableY() - 20;
        xDestination = customer.getTableX() - 50;
        yDestination = customer.getTableY() - 70;
        xTable = customer.getTableX();
        yTable = customer.getTableY();
    }
    
    public void DoGoToCookingArea() {
    	xDestination = 350;
    	yDestination = 50;
    }
    
    public void DoGoToPlatingArea() {
    	xDestination = 350;
    	yDestination = 160;
    }
    
    public void DoTakeFoodToTable(Customer customer) {
    	DoGoTakeOrder(customer);
    }
    
    public void DoTakeCheckToTable(Customer customer) {
    	DoGoTakeOrder(customer);
    }
    
    public void DoGoOnBreak() {
    	xDestination = 200;
    	yDestination = -50;
    }
    
    public void DoBackFromBreak() {
    	gui.setWaiterEnabled(agent);
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
