package jeffreyRestaurant.Gui;

import java.awt.*;

import jeffreyRestaurant.CustomerAgent;
import jeffreyRestaurant.HostAgent;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int squareDim = 20;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, GoPay, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable1 = 200;//kludge
	public static final int yTable1 = 250;
	
	public static final int xTable2 = 100;
	public static final int yTable2 = 250;
	
	public static final int xTable3 = 200;
	public static final int yTable3 = 150;

	public CustomerGui(CustomerAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = 40;
		yDestination = 60;
		//maitreD = m;
		this.gui = gui;
	}
	
	public CustomerGui(CustomerAgent c) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = 40;
		yDestination = 60;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos+= 5;
		else if (xPos > xDestination)
			xPos-= 5;

		if (yPos < yDestination)
			yPos+= 5;
		else if (yPos > yDestination)
			yPos-= 5;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			if (command==Command.GoPay) {
				agent.msgDoneAnimation();
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, squareDim, squareDim);
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

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		if (seatnumber == 1) {
			xDestination = xTable1;
			yDestination = yTable1;
		}
		else if (seatnumber == 2){
			xDestination = xTable2;
			yDestination = yTable2;
		}
		else if (seatnumber == 3){
			xDestination = xTable3;
			yDestination = yTable3;
		}
		command = Command.GoToSeat;
	}
	public void DoGoPay() {
		xDestination = 10;
		yDestination = 220;
		command = Command.GoPay;
	}
	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
