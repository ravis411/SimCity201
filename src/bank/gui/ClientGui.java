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
	private int interimCounter = 0, waitingAreaCounter = 0, deskCounter = 0;
	static final int hostWidth = 20, hostHeight = 20;
	private int xBankEntrance= 750;
	private final static int yBankEntrance= 400;
	private final int xWaitingArea= new Random().nextInt(130);
	private final int yWaitingArea= new Random().nextInt(130) + 250;
	private final static int xTellerDesk= 195;
	private final static int yTellerDesk= 230;
	private final static int xExit = 800;
	private final static int yExit = 400;
	private final static int xInterim = 400;
	private final static int yInterim = 275;
	private int line;
	BankAnimationPanel gui;
	private boolean isRobber = false;
	private boolean maskOn = false;

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
				& (xDestination == xWaitingArea) & (yDestination == yWaitingArea) && waitingAreaCounter == 0) {
			waitingAreaCounter++;
			role.msgAtWaitingArea();
		}
		if (xPos == xDestination && yPos == yDestination
				& (xDestination == xInterim) & (yDestination == yInterim) && interimCounter == 0) {
			role.msgAtInterim();
			if (isRobber == true){
				maskOn = true;
			}
			interimCounter++;
		}
		if (xPos == xDestination && yPos == yDestination && 
				(xDestination == xTellerDesk + (100*(line-1))) && (yDestination == yTellerDesk) && deskCounter == 0) {
			role.msgAtLine();
			deskCounter++;
		}
		if (xPos == xDestination && yPos == yDestination && 
				(xDestination == xExit) && (yDestination == yExit)) {
			role.msgAtExit();
			waitingAreaCounter=0;
			interimCounter=0;
			deskCounter=0;
			maskOn = false;
			isRobber = false;
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
		if (maskOn == false){
			g.setColor(Color.RED);
			g.fillRect(xPos, yPos, hostWidth, hostHeight);
		}
		else {
			g.setColor(Color.WHITE);
			g.fillOval(xPos, yPos, hostWidth, hostHeight);
			g.setColor(Color.BLACK);
			g.fillOval(xPos+2, yPos+4, hostWidth/3, hostHeight/3);
			g.fillOval(xPos+10, yPos+4, hostWidth/3, hostHeight/3);

		}

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

	public void doGoToInterim(boolean iR){
		xDestination = xInterim;
		yDestination = yInterim;
		isRobber = iR;
	}
	public void DoLeave() {
		xDestination = xExit;
		yDestination = yExit;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}