package restaurant.gui;

import restaurant.RestaurantCustomerRole;
import restaurant.HostRole;

import java.awt.*;
import java.util.ArrayList;

public class CustomerGui implements Gui{

	private RestaurantCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	//RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, PayCheck, LeaveRestaurant};
	private Command command=Command.noCommand;

	//public static final int xTable = 200;
	//public static final int yTable = 250;

	public CustomerGui(RestaurantCustomerRole c){ //HostAgent m) {
		agent = c;
//		xPos = -10;
//		yPos = -20;
		
		xPos = 20;
		yPos = 20;
		xDestination = 40;
		yDestination = 20;
		//maitreD = m;
		//this.gui = gui;
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
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.PayCheck) {
				agent.msgAnimationFinishedPaying();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
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

	public void waitingAreaPositionX(int xCoor) {
		xDestination = xCoor;
	}
	
	public void waitingAreaPositionY(int yCoor) {
		xDestination = yCoor;
	}
	
	public void DoGoToSeat(int table) {//later you will map seatnumber to table coordinates.
		//ArrayList array = (ArrayList) agent.getHost().tables;
		xDestination = agent.getHost().getTableX(table);
		yDestination = agent.getHost().getTableY(table);
		command = Command.GoToSeat;
	}
	
	public void DoGoPay() {
		xDestination = 20;
		yDestination = 100;
		command = Command.PayCheck;
	}

	public void DoExitRestaurant() {
		xDestination = -20;
		yDestination = -20;
		command = Command.LeaveRestaurant;
	}
}
