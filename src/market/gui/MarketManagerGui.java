package market.gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Person.PersonAgent;

public class MarketManagerGui implements Gui {

    private PersonAgent agent = null;

    private int xPos;
	private int yPos;
    private int yDestination;
    private int xDestination;
    static final int hostWidth = 20, hostHeight = 20;
    private int xResturantEntrance=380;
	private final static int yResturantEntrance= 750;
	private final static int xCookCord= 430;
	private final static int yCookCord= 60;
	private final static int xPlatingCord= 450;
	private final static int yPlatingCord= 100;
    public static final int xTable = 200;
    public static final int yTable = 250;
    private int waiterNumber=0;
    private int tableNum;
    private String orderBeingCarried = " ";
    MarketGui gui;

    public MarketManagerGui(PersonAgent agent, MarketGui gui) {
        this.agent = agent;
        xDestination=100;
        yDestination = 380;//default start position
        xPos = 400;
        yPos = 750;
        this.gui = gui;
        //this.waiterNumber=waiterNumber;
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
/*
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + 20 + ((tableNum-1)*60)) & (yDestination == yTable - 20)) {
           agent.msgAtTable();
        }
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
        g.setColor(Color.RED);
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
    
    public void DoGoToCook() {
    	xDestination = xCookCord;
        yDestination = yCookCord;
		
	}
	public void DoGoToPlatingArea() {
		xDestination=xPlatingCord;
		yDestination=yPlatingCord;
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
