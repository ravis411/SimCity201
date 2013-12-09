package interfaces;

import interfaces.generic_interfaces.GenericWaiter;
import restaurant.Menu;
//import restaurant.Customer.AgentEvent;
//import restaurant.Customer.AgentState;

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
	public abstract void msgFoodAtTable();
	public abstract void msgOrderOnItsWay();
	public abstract restaurant.RestaurantCustomerRole.AgentState getState();
	public abstract restaurant.RestaurantCustomerRole.AgentEvent getEvent();
	public abstract void msgSitAtTable(Menu menu);
	public abstract void msgWaiterReadyToTakeOrder();
	public abstract void msgWaiterReadyToRetakeOrder();
	public abstract int getWaitingLocX();
	public abstract void setWaitingLocX(int i);
	public abstract void setWaitingLocY(int i);
	public abstract void setWaiter(GenericWaiter waiter);
	public abstract void setTableX(int xCoor);
	public abstract void setTableY(int yCoor);
	public abstract void setTableNum(int table);
	public abstract String getCustomerName();
	public abstract void msgRestaurantFull();

}