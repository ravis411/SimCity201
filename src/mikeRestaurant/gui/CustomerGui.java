package mikeRestaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.ImageIcon;

import mikeRestaurant.CustomerRole;
import mikeRestaurant.WaiterRole;

public class CustomerGui implements Gui{

	private CustomerRole agent = null;
	private WaiterRole wtrAgent = null;
	
	private boolean isPresent = false;
	private boolean isHungry = false;

	private ImageIcon icon;
	
	//private HostAgent host;
	MikeAnimationPanel gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private boolean moving;
	
	private final int INITIAL_X;
	private final int INITIAL_Y;
	
	private final int LEAVE_X = -100;
	private final int LEAVE_Y = -100;
	
	private final int HOME_X = -50;
	private final int HOME_Y = -50;
	
	private final int ANCHOR_X = 10;
	private final int ANCHOR_Y = 15;
	
	private static final int CASHIER_X = 0;
	private static final int CASHIER_Y = 200;
	
	private static final int JAIL_X = 400;
	private static final int JAIL_Y = 0;
	
	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	
	private static final int PADDING = 10;
	
	private static final int NUM_COLUMNS = 2;
	
	private static final String IMAGE_PATH = "/mikeRestaurant/res/diner.png";
	
	private static int idCounter = 0;
	private final int ID;

	public CustomerGui(CustomerRole c, MikeAnimationPanel gui){
		
		agent = c;
		
		icon = new ImageIcon(this.getClass().getResource(IMAGE_PATH));
		
		ID = idCounter++;
		
		int row = ID / NUM_COLUMNS;
		int col = ID % NUM_COLUMNS;
		
		INITIAL_X = ANCHOR_X + (WIDTH+PADDING)*col;
		INITIAL_Y = ANCHOR_Y + (HEIGHT+PADDING)*row;
		
		//provides offscreen starting location
		xPos = HOME_X;
		yPos = HOME_Y;
		xDestination = INITIAL_X;
		yDestination = INITIAL_Y;
		
		moving = true;
		//maitreD = m;
		//this.gui = gui;
	}
	
	public Point getHomePosition(){
		return new Point(INITIAL_X, INITIAL_Y);
	}

	public void updatePosition() {
		if(moving){
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
	
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
	
			if (xPos == xDestination && yPos == yDestination) {
				//if leaving the restaurant
				if (xPos == INITIAL_X){
					isHungry = false;
					//gui.setCustomerEnabled(agent);
				//if moving to table
				}else if(xPos == CASHIER_X){
					agent.msgArrivedAtCashier();
				}else if(xPos == JAIL_X){
					
				}else if (xPos == LEAVE_X){
					agent.msgArrivedAtLeave();
				}else{
					agent.msgArrivedAtTable();
				}

				moving = false;
			}
		}
	}

	public void draw(Graphics2D g) {
		icon.paintIcon(gui, g, xPos, yPos);
		
//		g.setColor(Color.BLUE);
//		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
	}

	public boolean isPresent() {
		return true;
	}
	public void setHungry() {
		isHungry = true;
		//agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	/**
	 * Sets the specified location as the new destination
	 * @param xDest new x destination
	 * @param yDest new y destination
	 */
	public void DoGoToLocation(int xDest, int yDest) {
		//System.out.println("Customer Moving to Seat#: "+seatnumber);
		xDestination = xDest;
		yDestination = yDest;
		moving = true;
	}
	
	public void DoGoToCashier(){
		xDestination = CASHIER_X;
		yDestination = CASHIER_Y;
		moving = true;
	}
	
	public void DoGoToJail(){
		xDestination = JAIL_X;
		yDestination = JAIL_Y;
		moving = true;
	}

	/**
	 * Sets the new destination outside the restauraunt so the customergui will leave
	 */
	public void DoExitRestaurant() {
		xDestination = LEAVE_X;
		yDestination = LEAVE_Y;
		moving = true;
		//gui.enableCustomer(agent.getName());
	}
}
