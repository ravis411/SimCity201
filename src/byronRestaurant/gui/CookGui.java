package byronRestaurant.gui;


import java.awt.*;
import java.awt.List;
import java.util.*;

import byronRestaurant.CookRole;
import byronRestaurant.CustomerRole;
import byronRestaurant.WaiterRole;
public class CookGui implements Gui {

	private CookRole agent = null;	
	private int counter;
	private boolean isPresent = false;
	private int xPos = 650, yPos = 60;//default waiter position
	private int xDestination = 650, yDestination = 60;//default start position
	private static int width = 20, length = 20;
	private static int xPlatingArea = 620, yPlatingArea = 40;
	private static int xKitchen = 680, yKitchen = 20;
	private static int xDefault = 650, yDefault = 60;
	private boolean isCooking = false;
	private boolean onPlate = false;
	
	
	public CookGui(CookRole w) {
		this.agent = w;
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
		if ((xPos == xDestination && yPos == yDestination) && (xPos == xPlatingArea) && (yPos == yPlatingArea)){
			agent.msgAtPlatingArea();
		}
		if ((xPos == xDestination && yPos == yDestination) && (xPos == xKitchen) && (yPos == yKitchen)){
			agent.msgAtKitchen();
		}
		if ((xPos == xDestination && yPos == yDestination) && (xPos == xDefault) && (yPos == yDefault)){
			agent.msgAtDefault();
		}

	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, width, length);
		if (onPlate == true){
        	g.setColor(Color.white);
            g.fillOval(600, 20, 15, 15);
            g.setColor(Color.black);
            g.fillOval(602, 22, 10, 10);
		}
		if (isCooking == true){
        	g.setColor(Color.black);
            g.fillOval(700, 20, 10, 10);
		}
	}


	public boolean isPresent() {
		return true;
	}

	public void setPresent(boolean p) {
		isPresent = p; 
	}


	public void doGoToKitchen(){
		xDestination = xKitchen;
		yDestination = yKitchen;
		//    	System.out.println("In doGoToKitchen, new destination " + xDestination + " ," + yDestination);
	}

	public void doGoToPlatingArea(){
		xDestination = xPlatingArea;
		yDestination = yPlatingArea;
		//   	System.out.println("In doGoToPlatingArea, new destination " + xDestination + " ," + yDestination);
	}
	
	public void doGoToDefault(){
		xDestination = xDefault;
		yDestination = yDefault;
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
	
	public void setIsCooking(boolean b){
		isCooking = b;
	}
	
	public void setOnPlate(boolean b){
		onPlate = b;
	}

	public int getCount(){
		return counter;
	}
}
