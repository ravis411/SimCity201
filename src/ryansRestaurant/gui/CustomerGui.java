package ryansRestaurant.gui;

import ryansRestaurant.RyansCustomerRole;
import ryansRestaurant.RyansHostRole;

import java.awt.*;
import java.util.concurrent.Semaphore;

public class CustomerGui implements Gui{

	private RyansCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	
	private final static int xCashier = 0;
	private final static int yCashier = 300;

	//private RyansHostRole host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToHost, GoToWaitingArea, WaitForCoords, GoToSeat, goingToSeat, GoToCashier, LeaveRestaurant };
	private Command command=Command.noCommand;
	
	//This will hold where the customer is going to sit...Positive coords are valid
	private int xTableCoord  = -40;
	private int yTableCoord = -40;
	
	private String dispName;
	protected Dimension waitingCoords = new Dimension(-40, -40);
	Dimension waitingPosition = null;
	private Semaphore sem = new Semaphore(0, true);
	
	RestaurantLayout layout = null;


	public CustomerGui(RyansCustomerRole c, RestaurantGui gui){ //RyansHostRole m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
		this.gui = gui;
		this.layout = gui.layout;
		dispName = new String("" + agent.getName().charAt(0) + agent.getName().charAt(agent.getName().length() - 1));
	}
	
	public void msgHereAreWaitingCoords(Dimension xyCoords) {
		waitingCoords = xyCoords;
	}
	public void msgHereIsWaitingPosition(Dimension p) {
		waitingPosition = p;
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

		
		if (xPos == xDestination && yPos == yDestination) {
			if(command == Command.GoToHost)
			{
				sem.release();
				command=Command.noCommand;
			}
			else if(command == Command.GoToWaitingArea) {
				sem.release();
				command=Command.noCommand;
			}
			
			else if (command==Command.goingToSeat) 
			{
				agent.msgAnimationFinishedGoToSeat();
				command=Command.noCommand;
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				gui.setCustomerEnabled(agent);
				xDestination = -40;
				yDestination = -40;
				xPos = -40;
				yPos = -40;
				command = Command.noCommand;
			}
			else if(command == Command.GoToCashier) {
				agent.msgAnimationFinishedGoToCashier();
				command=Command.noCommand;
			}
		}
		if (command==Command.GoToSeat) {
			if(xTableCoord > 0 && yTableCoord > 0) {
				xDestination = xTableCoord;
				yDestination = yTableCoord;
				command = Command.goingToSeat;
				layout.removeGui(this);
			}
		}



	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.white);
		g.drawString(dispName, xPos + 1, yPos + 10 );
		int y = yPos - g.getFontMetrics().getHeight();
		for(String line : agent.getActivity().split("\n")) {
			g.drawString(line, xPos, y += g.getFontMetrics().getHeight() );
		}
		
	}

	public boolean isPresent() {
		return true;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	/**
	 * Called from customer to let gui know he/she should be sitting down
	 */
	
	
	public void DoEnterRestaurant() {
		
			xDestination = layout.hostXYCoords.width-25;
			yDestination = layout.hostXYCoords.height;
			command = Command.GoToHost;
			try {
				sem.acquire();
			} catch (InterruptedException e) {
			}
	}
	public void DoWaitInRestaurant() {
		if(gui.layout.addCustomerGui(this))
		{
			xDestination = waitingCoords.width;
			command = Command.GoToWaitingArea;
			yDestination = waitingCoords.height;
			try {
				sem.acquire();
			} catch (InterruptedException e) {
			}
		}
	}
	
	
	public void DoGoToSeat() {
		command = Command.GoToSeat;
	}
	
	
	
	public void DoGoToCoords(Dimension dim) {
		//if(command == Command.WaitForCoords)
		{
			xTableCoord = dim.width;
			yTableCoord = dim.height;
		}
	}
	
	public void DoGoToCashier() {
		xDestination = layout.cashierXYCoords.width;
		yDestination = layout.cashierXYCoords.height + 25;
		command = Command.GoToCashier;
	}

	public void DoImpatientExitRestaurant() {
		layout.removeGui(this);
		xDestination = 0;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void DoExitRestaurant() {
		xDestination = 0;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public Dimension getCurrentCoords() {
		return new Dimension(xPos, yPos);
	}

	

	

	
}
