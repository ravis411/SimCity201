package byronRestaurant.gui;


import java.awt.*;
import java.awt.List;
import java.util.*;

import byronRestaurant.CustomerAgent;
import byronRestaurant.WaiterAgent;
public class WaiterGui implements Gui {

	private WaiterAgent agent = null;	
	private int counter;
	private boolean isPresent = false;
	private int xPos = 0, yPos = 60;//default waiter position
	private int xDestination = 0, yDestination = 60;//default start position
	private static int width = 20, length = 20;
	public static final int xTable = 200;
	public static final int yTable = 200;
	public static int xLobby = 180, yLobby = 40;
	private static int xKitchen = 580, yKitchen = 20;
	private RestaurantGui gui;
	private ArrayList<Integer> tableNums = new ArrayList<Integer>();

	public WaiterGui(WaiterAgent w, RestaurantGui gui, int c) {
		this.agent = w;
		this.gui = gui;
		counter = c;
		yPos = 60 + (20*c);
		yDestination = 60 + (20*c);
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
		for (int i = 0; i < tableNums.size(); i++){
			if (xPos == xDestination && yPos == yDestination
					& (xDestination == xTable + (100*(tableNums.get(i)-1)) + 20) & (yDestination == yTable - 20)) {
				agent.msgAtTable();
			}
		}
		if ((xPos == xDestination && yPos == yDestination) && (xPos == xKitchen) && (yPos == yKitchen)){
			agent.msgAtKitchen();
		}
		if ((xPos == xDestination && yPos == yDestination) && (xPos == xLobby) && (yPos == yLobby)){
			agent.msgAtLobby();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, width, length);
	}


	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p; 
	}

	public void DoBringToTable(CustomerAgent customer, int n) {
		tableNums.add(n);
		xDestination = xTable + (100*(n-1)) + 20;
		yDestination = yTable - 20;
		//        System.out.println("In DoBringToTable, new destination " + xDestination + " ," + yDestination);
	}

	public void doGoToTable(int n){
		xDestination = xTable + (100*(n-1)) + 20;
		yDestination = yTable - 20;
		//        System.out.println("In doGoToTable, new destination " + xDestination + " ," + yDestination);

	}
	public void doGoToLobby(){
		xDestination = xLobby;
		yDestination = yLobby;
		//    	System.out.println("In doGoToLobby, new destination " + xDestination + " ," + yDestination);
	}

	public void DoLeaveCustomer() {
		xDestination = 0;
		yDestination = 60 + (20*counter);
		//       System.out.println("In DoLeaveCustomer, new destination " + xDestination + " ," + yDestination);
	}

	public void doGoToKitchen(){
		xDestination = xKitchen;
		yDestination = yKitchen;
		//   	System.out.println("In doGoToKitchen, new destination " + xDestination + " ," + yDestination);
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public String getName(){
		return agent.getName();
	}

	public int getCount(){
		return counter;
	}
}
