package restaurant.interfaces;

import restaurant.Order;

//import restaurant.WaiterAgent.Check;

/**
 * A sample Waiter interface built to unit test Cashier
 *
 * @author Dylan Resnik
 *
 */
public interface Waiter {
	public void msgCheckReady(Customer customer, double amount);
	public String getName();
	public void msgBringFoodToTable(Order o);
	public void msgOutOfFood(int choice, Customer customer);
	public void msgBreakReply(boolean b);
	
	void msgAtFrontDesk();
	void msgAtWaitingArea();
	void msgAtPlatingArea();
	void msgAtCookingArea();
	void msgAtTable();
}