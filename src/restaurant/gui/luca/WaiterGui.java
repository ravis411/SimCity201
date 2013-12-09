package restaurant.gui.luca;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import restaurant.interfaces.luca.LucaWaiter;
import restaurant.luca.LucaWaiterRole;

public class WaiterGui implements Gui {

    private LucaWaiter agent = null;

    private int xPos = -20;//default waiter position

	private int yPos = -20;
    private int yDestination = 20;//default start position
    private int xDestination;
    static final int hostWidth = 20, hostHeight = 20;
    private int xResturantEntrance;
	private final static int yResturantEntrance= 20;
	private final static int xCookCord= 430;
	private final static int yCookCord= 60;
	private final static int xPlatingCord= 450;
	private final static int yPlatingCord= 100;
    public static final int xTable = 200;
    public static final int yTable = 250;
    private int waiterNumber;
    private int tableNum;
    private String orderBeingCarried = " ";

    public WaiterGui(LucaWaiter agent, int waiterNumber) {
        this.agent = agent;
        xResturantEntrance= 5*(waiterNumber%4) + 20*(waiterNumber%4);
        xDestination=5*(waiterNumber%4) + 20*(waiterNumber%4);
        xPos = 5*(waiterNumber%4) + 20*(waiterNumber%4);
        yPos = 20;
        this.waiterNumber=waiterNumber;
    }


	public void updatePosition() {
        if (xPos < xDestination)
            {xPos++;
            agent.msgWaiterReadytoSeat(false);
            }
        else if (xPos > xDestination)
            {xPos--;
            agent.msgWaiterReadytoSeat(false);
            }

        if (yPos < yDestination)
            {yPos++;
            agent.msgWaiterReadytoSeat(false);
            }
        else if (yPos > yDestination)
            {yPos--;
            agent.msgWaiterReadytoSeat(false);
            }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + 20 + ((tableNum-1)*60)) & (yDestination == yTable - 20)) {
           agent.msgAtTable();
           agent.msgWaiterReadytoSeat(false);
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
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
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
