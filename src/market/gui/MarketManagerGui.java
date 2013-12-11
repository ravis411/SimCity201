package market.gui;


import interfaces.MarketManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import MarketEmployee.MarketManagerRole;

public class MarketManagerGui implements Gui {

    private MarketManager role = null;

    private int xPos;
	private int yPos;
    private int yDestination;
    private int xDestination;
    static final int hostWidth = 20, hostHeight = 20;
    private int xResturantEntrance=380;
	private final static int yResturantEntrance= 375;
	private final int xCounterEntranceCord= 700;
	private final int yCounterEntranceCord= 180;
	private final int xManagerOfficeDoor= 180;
	private final int yManagerOfficeDoor= 140;
	private final int xManagerOfficeDesk= 90;
	private final int yManagerOfficeDesk= 180;
	private final int xTruck= 20;
	private final int yTruck= 20;
	private final static int xPlatingCord= 450;
	private final static int yPlatingCord= 10;
    public static final int xTable = 200;
    public static final int yTable = 250;
    private int waiterNumber=0;
    private int tableNum;
    private String orderBeingCarried = " ";
    public MarketManagerGui(MarketManagerRole marketManagereRole) {
        this.role = marketManagereRole;
        xDestination=xCounterEntranceCord;
        yDestination = yCounterEntranceCord+80;//default start position
        xPos = xResturantEntrance;
        yPos = yResturantEntrance;
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

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xCounterEntranceCord & (yDestination == yCounterEntranceCord+80))) {
        	xDestination= xCounterEntranceCord;
        	yDestination= yCounterEntranceCord;
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xCounterEntranceCord & (yDestination == yCounterEntranceCord))) {
        	xDestination= xManagerOfficeDoor;
        	yDestination= yManagerOfficeDoor;
        	role.msgMarketEmployeeAtDesk();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination ==  xManagerOfficeDoor+60 & (yDestination ==  yManagerOfficeDoor))) {
        	xDestination= xManagerOfficeDoor+60;
        	yDestination= yManagerOfficeDoor-100;
        	
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination ==  xManagerOfficeDoor+60 & (yDestination ==  yManagerOfficeDoor-100))) {
        	xDestination= xTruck;
        	yDestination= yTruck;
        	
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTruck & (yDestination == yTruck))) {
        	
        	xDestination= xManagerOfficeDoor+70;
        	yDestination= yManagerOfficeDoor-110;
        	role.msgMarketEmployeeAtTruck();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xManagerOfficeDoor+70 & (yDestination == yManagerOfficeDoor-110))) {
        	
        	xDestination= xManagerOfficeDoor+50;
        	yDestination= yManagerOfficeDoor-1;
        	
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xManagerOfficeDoor+50 & (yDestination == yManagerOfficeDoor-1))) {
        	
        	xDestination= xManagerOfficeDoor-20;
        	yDestination= yManagerOfficeDoor-1;
        	
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination ==  xManagerOfficeDoor-20 & (yDestination ==  yManagerOfficeDoor-1))) {
        	xDestination= xManagerOfficeDesk;
        	yDestination= yManagerOfficeDesk;
        	role.msgMarketEmployeeAtDeskRelease();
        	
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xManagerOfficeDoor & (yDestination == yManagerOfficeDoor))) {
        	
        	xDestination= xManagerOfficeDesk;
        	yDestination= yManagerOfficeDesk;
        	role.msgMarketEmployeeAtDeskRelease();
        	
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

    public void DoGoToDeliveryTruck() {
    	xDestination = xManagerOfficeDoor+60;
	    yDestination = yManagerOfficeDoor;		
	}
    

	public void DoGoToDoor() {
		 xDestination = xManagerOfficeDoor;
	     yDestination = yManagerOfficeDoor;
		
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
