package restaurant.interfaces;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;

import restaurant.Menu;
//import restaurant.Customer.AgentEvent;
//import restaurant.Customer.AgentState;
import restaurant.gui.CustomerGui;

/**
 * A sample Customer interface built to unit test Cashier
 *
 * @author Dylan Resnik
 *
 */
public interface Customer {
	/*public abstract void gotHungry();
	public abstract void msgRestaurantFull();
	public abstract void msgSitAtTable(Menu m);
	public abstract void msgWaiterReadyToTakeOrder();
	public abstract void msgWaiterReadyToRetakeOrder();
	public abstract void msgOrderOnItsWay();
	public abstract void msgFoodAtTable();*/
	public abstract void msgCheckAtTable(double amount);
	public abstract void msgCheckPayed();
	public abstract void msgCheckNotPayed();
	//public abstract void msgAnimationFinishedGoToSeat();
	//public abstract void msgAnimationFinishedPaying();
	//public abstract void msgAnimationFinishedLeaveRestaurant();
	public abstract int getTableX();
	public abstract int getTableY();
	public abstract int getTableNum();
	public abstract String getName();
	public abstract double getMoney();

}