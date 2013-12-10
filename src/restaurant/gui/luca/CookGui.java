package restaurant.gui.luca;


import interfaces.MarketManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import restaurant.interfaces.luca.LucaCook;
import restaurant.luca.LucaCookRole;
import Person.Role.Employee;
import building.BuildingList;

public class CookGui implements Gui {

    private LucaCook agent = null;

    private int xPos = 700;//default waiter position

	private int yPos = 60;
    private int xDestination = 700, yDestination = 60;//default start position
    static final int hostWidth = 20, hostHeight = 20;
    private final static int xCookDefault= 700;
	private final static int yCookDefault= 60;
	private final static int xPlatingCord= 700;
	private final static int yPlatingCord= 100;
	private final static int xRefrigerator= 580;
	private final static int yRefrigerator= 20;
    public static final int xGrill = 450;
    public static final int yGrill = 20;
    private String orderBeingCarried = " ";


    public CookGui(LucaCookRole cook) {
        this.agent = cook;
        xPos = 700;
        yPos = 60;

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
        else if (((xDestination == xRefrigerator) && (yDestination == yRefrigerator))&& ((xRefrigerator == xPos) && (yRefrigerator == yPos) ))
        {
        	agent.msgAtRefrigerator();
        }
        if (xPos == xDestination && yPos == yDestination & (xDestination == xGrill) & (yDestination == yGrill)) {
        	agent.msgAtGrill();

        }
        else if (((xDestination == xPlatingCord) && (yDestination == yPlatingCord))&& ((xPlatingCord == xPos) && (yPlatingCord == yPos) ))
        {
        	agent.msgAtPlatingArea();
        }
        else if (((xDestination == xCookDefault) && (yDestination == yCookDefault))&& ((xCookDefault == xPos) && (yCookDefault == yPos) ))
        	{
        	agent.msgAtDefaultPosition();
        	 }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fillRect(xPos, yPos, hostWidth, hostHeight);
        g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.PLAIN, 10));
		g.drawString(orderBeingCarried, xPos, yPos +10);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToGrill() {

        xDestination = xGrill;
        yDestination = yGrill;
    }
    
    public void DoGoToPlatingArea() {
    	xDestination = xPlatingCord;
        yDestination = yPlatingCord;
		
	}
    public void DoGoToRefrigerator() {
    	xDestination = xRefrigerator;
        yDestination = yRefrigerator;
		
	}
    public void DoGoToDefault() {
    	xDestination = xCookDefault;
        yDestination = yCookDefault;
		
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
