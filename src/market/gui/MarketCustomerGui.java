package market.gui;


import interfaces.MarketCustomer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class MarketCustomerGui implements Gui {

    private MarketCustomer role = null;

    private int xPos;
	private int yPos;
    private int yDestination;
    private int xDestination;
    static final int hostWidth = 20, hostHeight = 20;
    private final int xResturantEntrance=400;
	private final int yResturantEntrance= 410;
	private final int xCounterCord;
	private final int yCounterCord= 230;
    public static final int xTable = 200;
    public static final int yTable = 250;
    private int customerNumber;
    private int tableNum;
    private String orderBeingCarried = " ";
    private Boolean atCounter=false;


    public MarketCustomerGui(MarketCustomer marketCustomer, int customerNumber) {
    	this.customerNumber=customerNumber;
        this.role = marketCustomer;
        xCounterCord=395 + 100*(customerNumber%4);
        xDestination=xResturantEntrance;
        yDestination = yResturantEntrance;//default start position
        xPos = xResturantEntrance;
        yPos = yResturantEntrance;
     
    }

    public void updatePosition() {
        if (xPos < xDestination)
            {xPos++;
          
            }
        else if (xPos > xDestination)
            {xPos--;
            }

        if (yPos < yDestination)
            {yPos++;
            }
        else if (yPos > yDestination)
            {yPos--;
            }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xCounterCord) & (yDestination == yCounterCord) && !atCounter) {
        	atCounter=true;
        	role.msgMarketCustomerAtCounter();
        }
        /*
        else if (((xDestination == xCookCord) && (yDestination == yCookCord))&& ((xCookCord == xPos) & (yCookCord == yPos) ))
        {
        	agent.msgAtCook();
        }
        else if (((xDestination == xPlatingCord) && (yDestination == yPlatingCord))&& ((xPlatingCord == xPos) && (yPlatingCord == yPos) ))
        {
        	agent.msgAtCook();
        }
        else if (((xDestination == xResturantEntrance) && (yDestination == yResturantEntrance))&& ((xResturantEntrance == xPos) & (yResturantEntrance == yPos) ))
        	{agent.msgAtEntrance();
        	 agent.msgWaiterReadytoSeat(true);}
        	 */
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.PINK);
        g.fillRect(xPos, yPos, hostWidth, hostHeight);
        g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.PLAIN, 10));
		g.drawString(orderBeingCarried, xPos, yPos +10);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable( int tableN) {
    	tableNum= tableN;
        xDestination = (xTable + 20 + ((tableN-1)*60));
        yDestination = yTable - 20;
    }

	public void DoGoToCounter() {
		xDestination=xCounterCord;
		yDestination=yCounterCord;
	}
    public void DoLeave() {
        xDestination = xResturantEntrance;
        yDestination = yResturantEntrance;
        
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void showOrderInAnimation(String orderChoice, String orderStatus) {
		if (orderChoice == "")
		{
			orderBeingCarried= "";
		}
		else if (orderChoice == "Steak")
		{
			orderBeingCarried= "STK" + orderStatus;
		}
		else if (orderChoice == "Chicken")
		{
			orderBeingCarried= "CHK" + orderStatus;
		}
		
		else if (orderChoice == "Burger")
		{
			orderBeingCarried= "BGR" + orderStatus;
		}
		
	}



}
