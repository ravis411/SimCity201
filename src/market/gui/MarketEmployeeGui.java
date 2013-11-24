package market.gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import market.interfaces.MarketEmployee;

public class MarketEmployeeGui implements Gui {

    private MarketEmployee role = null;

    private int xPos;
	private int yPos;
    private int yDestination;
    private int xDestination;
    static final int hostWidth = 20, hostHeight = 20;
    private int xResturantEntrance=380;
	private final int yResturantEntrance= 375;
	private final int xFood1Cord= 450;
	private final int yFood1Cord= 50;
	private final int xFood2Cord= 550;
	private final int yFood2Cord= 50;
	private final int xFood3Cord= 650;
	private final int yFood3Cord= 50;
	private final int xCounterEntranceCord= 700;
	private final int yCounterEntranceCord= 180;
	private int xCounter;
	private int yCounter= 180;
    public final int xTable = 200;
    public final int yTable = 250;
    private int waiterNumber;
    private int tableNum;
    private String orderBeingCarried = " ";
    private boolean atCounter=false;
    MarketGui gui;

    public MarketEmployeeGui(MarketEmployee marketEmployee, MarketGui gui, int waiterNumber) {
        this.role = marketEmployee;
        xDestination=xCounterEntranceCord;
        yDestination = yCounterEntranceCord+100;
        xCounter=395 + 100*(waiterNumber%4);
        xPos = xResturantEntrance;//default start position
        yPos = yResturantEntrance;
        this.gui = gui;
        this.waiterNumber=waiterNumber;
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
        		& (xDestination == xCounterEntranceCord & (yDestination == yCounterEntranceCord+100))) {
        	xDestination= xCounterEntranceCord;
        	yDestination= yCounterEntranceCord;
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFood1Cord & (yDestination == yFood1Cord+69))) {
        	xDestination= xFood1Cord;
        	yDestination= yFood1Cord;

        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFood1Cord & (yDestination == yFood1Cord))) {
        	xDestination= xFood1Cord;
        	yDestination= yFood1Cord+75;
        	role.msgMarketEmployeeAtFood1();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFood1Cord & (yDestination == yFood1Cord+75))) {
        	xDestination= xFood2Cord;
        	yDestination= yFood2Cord+75;
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFood2Cord & (yDestination == yFood2Cord+75))) {
        	xDestination= xFood2Cord;
        	yDestination= yFood2Cord;
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFood2Cord & (yDestination == yFood2Cord))) {
        	xDestination= xFood2Cord+10;
        	yDestination= yFood2Cord+75;
        	role.msgMarketEmployeeAtFood2();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFood2Cord+10 & (yDestination == yFood2Cord+75))) {
        	xDestination= xFood3Cord;
        	yDestination= yFood3Cord+75;
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFood3Cord & (yDestination == yFood3Cord+75))) {
        	xDestination= xFood3Cord;
        	yDestination= yFood3Cord;
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFood3Cord & (yDestination == yFood3Cord))) {
        	atCounter=false;
        	xDestination= xFood3Cord;
        	yDestination= yFood3Cord+76;
        	role.msgMarketEmployeeAtFood3();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFood3Cord & (yDestination == yFood3Cord+76))) {
        	xDestination= xCounter;
        	yDestination= yCounter;
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xCounterEntranceCord & (yDestination == yCounterEntranceCord))) {
        	xDestination= xCounter;
        	yDestination= yCounter;
        }
        if (xPos == xDestination && yPos == yDestination
				& (xDestination == xCounter & (yDestination == yCounter) ) && !atCounter) {
        	atCounter=true;
        	role.msgMarketEmployeeAtCounter();
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
    

	public void goCollectOrRestockFoodOrder() {
		xDestination=xFood1Cord;
		yDestination=yFood1Cord+69;
		
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
