package interfaces;

import restaurant.CashierRole;
import restaurant.CookRole;
import restaurant.HostRole;
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
//	public void setHost(HostRole hr);
//	public void setCook(CookRole cr);
//	public void setCashier(CashierRole cr);
	
	void msgAtFrontDesk();
	void msgAtWaitingArea();
	void msgAtPlatingArea();
	void msgAtCookingArea();
	void msgAtTable();
	public boolean getBreakStatus();
	public int getNumberOfCustomers();
	public void msgGoTakeOrder();
	public void msgTakeOrder(Customer customerAgent, int mealChoice);
	public void msgSeatCustomer(Customer customer);
	public void msgLeavingTable(Customer customerAgent);
}