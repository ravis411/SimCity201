package byronRestaurant.gui;

import java.awt.*;
import java.util.*;

import byronRestaurant.CustomerRole;

public class CustomerGui implements Gui{

	private CustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostRole host;
	private int count;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private int width, length;
	private enum Command {noCommand, GoToWaitingArea, GoToSeat, GoToCashier, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable = 200;
	public static final int yTable = 200;
	
	public AnimationPanel animationPanel;
	
	public Map<Integer, Dimension> tables = new HashMap<Integer, Dimension>(){{
		put(1, new Dimension(xTable, yTable));
		put(2, new Dimension(xTable+100, yTable));
		put(3, new Dimension(xTable+200, yTable));
	}};

	public CustomerGui(CustomerRole c, int i){ 
		agent = c;
		xPos = 40 + (15*i);
		yPos = 20;
		xDestination = 40 + (15*i);
		yDestination = 20;
		//maitreD = m;
		width = 20;
		length = 20;
		count = i;
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
			if (command==Command.GoToWaitingArea){
				agent.msgAtWaitingArea();
			}
			if (command==Command.GoToSeat) {
				agent.msgAtTable();
			}
			else if (command==Command.GoToCashier){
				agent.msgAtCashier();
			}
			else if (command==Command.LeaveRestaurant) {
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, width, length);
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
		xDestination = tables.get(seatnumber).width;
		yDestination = tables.get(seatnumber).height;
		command = Command.GoToSeat;
	}
	
	public void DoGoToWaitingArea(){
		xDestination = 0;
		yDestination = 0;
		command = Command.GoToWaitingArea;
	}
	public void DoGoToLobby(){
		xDestination = 20;
		yDestination = 20;
		command = Command.GoToCashier;
	}
	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		count = 0;
		command = Command.LeaveRestaurant;
	}
    public String getName(){
    	return agent.getName();
    }
    public int getCount(){
    	return count;
    }

	@Override
	public int getXPos() {
		return xPos;
	}

	@Override
	public int getYPos() {
		return yPos;
	}
}
