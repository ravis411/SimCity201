package restaurant.gui.luca;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import restaurant.luca.LucaRestaurantCustomerRole;


public class CustomerGui implements Gui{

	private LucaRestaurantCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable = 200;
	public static final int yTable = 250;
	static final int customerWidth = 20, customerHeight = 20;
	static final int xResturantEntrance= -40;
	static final int yResturantEntrance= -40;
	private String orderText = " ";
	public CustomerGui(LucaRestaurantCustomerRole c, int waitingRoomNumber){ //HostAgent m) {
		agent = c;
		xPos = 0;
		yPos = 0;
		xDestination = 5*(waitingRoomNumber%4) + 20*(waitingRoomNumber%4);
		yDestination =0;

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

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xResturantEntrance) & (yDestination == yResturantEntrance)) {
        	agent.msgLeftRestaurant();
        }
		
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;

			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, customerWidth, customerHeight);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.PLAIN, 10));
		g.drawString(orderText, xPos, yPos +10);
		
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

	public void DoGoToSeat(int seatnumber, int tableNum) {//later you will map seatnumber to table coordinates.
		xDestination = (xTable+ ((tableNum-1)*60));
		yDestination = yTable;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = xResturantEntrance;
		yDestination = yResturantEntrance;
		command = Command.LeaveRestaurant;
	}

	public void showMyOrderInAnimation(String orderChoice, String orderStatus) {
		if (orderChoice == "")
		{
			orderText= "";
		}
		else if (orderChoice == "Steak")
		{
			orderText= "STK" + orderStatus;
		}
		else if (orderChoice == "Chicken")
		{
			orderText= "CHK" + orderStatus;
		}
		
		else if (orderChoice == "Burger")
		{
			orderText= "BGR" + orderStatus;
		}
	}
}
