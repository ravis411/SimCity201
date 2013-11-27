package bank.gui;

import bank.BankClientRole;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * 
 * @author Byron Choy
 *
 */

public class ClientGui implements Gui {

	private BankClientRole role = null;

	private int xPos;
	private int yPos;
	private int yDestination;
	private int xDestination;
	static final int hostWidth = 20, hostHeight = 20;
	private int xBankEntrance= 750;
	private final static int yBankEntrance= 400;
	private final int xWaitingArea= new Random().nextInt(120);
	private final int yWaitingArea= new Random().nextInt(160) + 220;
	private final static int xTellerDesk= 195;
	private final static int yTellerDesk= 230;
	private int line;
	BankAnimationPanel gui;

	public ClientGui(BankClientRole clientRole, BankAnimationPanel bankAnimationPanel) {
		this.role = clientRole;
		xDestination=xWaitingArea+50;
		yDestination = yWaitingArea;//default start position
		xPos = xBankEntrance;
		yPos = yBankEntrance-50;
		this.gui = bankAnimationPanel;
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
				& (xDestination == xWaitingArea) & (yDestination == yWaitingArea)) {
				role.msgAtWaitingArea();
		}
		if (xPos == xDestination && yPos == yDestination && 
				(xDestination == xTellerDesk + (100*(line-1))) && (yDestination == yTellerDesk)) {
			role.msgAtLine();
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
        else if (((xDestination == xTellerDesk) && (yDestination == yTellerDesk))&& ((xTellerDesk == xPos) && (yTellerDesk == yPos) ))
        {
                agent.msgAtCook();
        }
        else if (((xDestination == xBankEntrance) && (yDestination == yBankEntrance))&& ((xBankEntrance == xPos) & (yBankEntrance == yPos) ))
                {agent.msgAtEntrance();
                 agent.msgWaiterReadytoSeat(true);}
		 */
	}


	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, hostWidth, hostHeight);
	}

	public boolean isPresent() {
		return true;
	}

	public void doGoToLine(int n){
		line = n;
		xDestination = xTellerDesk + (100*(n-1));
		yDestination = yTellerDesk;

	}
	
	public void doGoToWaitingArea(){
		xDestination = xWaitingArea;
		yDestination = yWaitingArea;
		
	}
	public void DoLeave() {
		xDestination = xBankEntrance;
		yDestination = yBankEntrance;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}