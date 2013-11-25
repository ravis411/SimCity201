package bank.gui;

import bank.bankTellerRole;
import java.awt.Color;
import java.awt.Graphics2D;


public class TellerGui implements Gui {

	private bankTellerRole role = null;

	private int xPos;
	private int yPos;
	private int yDestination;
	private int xDestination;
	static final int hostWidth = 20, hostHeight = 20;
	private int xBankEntrance= 750;
	private final static int yBankEntrance= 0;
	private final static int xIntermediateEntrance = 680;
	private final static int yIntermediateEntrance = 180;
	private final int xTellerDesk= 195;
	private final int yTellerDesk= 180;
	private final static int xBreakRoom= 450;
	private final static int yBreakRoom= 10;
	private int lane;
	BankGui gui;
	private int xcounter = 0;
	private int ycounter = 0;
	public TellerGui(bankTellerRole tellerRole, BankGui gui, int laneNum) {
		this.role = tellerRole;
		lane = laneNum;
		xPos = xBankEntrance;
		yPos = yBankEntrance-50;
		xDestination = xIntermediateEntrance;
		yDestination = yIntermediateEntrance;
		this.gui = gui;
	}

	public void updatePosition() {
		if (xPos < xDestination){
			xcounter++;
			if (xcounter == 2){
				xPos++;
				xcounter = 0;
			}
		} 
		else if (xPos > xDestination){
			xcounter++;
			if (xcounter == 2){
				xPos--;
				xcounter = 0;
			}
		}
		if (yPos < yDestination){
			ycounter++;
			if (ycounter == 2){
				yPos++;
				ycounter = 0;
			}
		}
		else if (yPos > yDestination){
			ycounter++;
			if (ycounter == 2){
				yPos--;
				ycounter = 0;
			}
		}
		if (xPos == xDestination && yPos == yDestination && xDestination == xIntermediateEntrance && yDestination == yIntermediateEntrance){
			role.msgAtIntermediate();
		}

		if (xPos == xDestination && yPos == yDestination
				&& (xDestination == xTellerDesk + (100*(lane-1)) & (yDestination == yTellerDesk))) {
			role.msgAtStation();
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
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, hostWidth, hostHeight);
	}

	public boolean isPresent() {
		return true;
	}

	public void DoGoToStation(){
		xDestination=xTellerDesk + (100*(lane-1));
		yDestination = yTellerDesk;//default start position
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