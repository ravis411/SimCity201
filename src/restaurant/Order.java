package restaurant;

import restaurant.Menu;
import restaurant.Menu.Dish;

public class Order
{
	WaiterAgent waiter;
	CustomerAgent customer;
	int choice;
	//Dish choice;
	int table;
	public enum orderStatus {pending, cooking, cooked}
	private orderStatus status = orderStatus.pending;
	int cookTime;

	public Order (WaiterAgent waiter, int choice, int table, CustomerAgent customer) {
		this.waiter = waiter;
		this.choice = choice;
		this.table = table;
		this.customer = customer;
	}

	public void setCooking() {
		status = orderStatus.cooking;
	}
	
	public void setCooked() {
		status = orderStatus.cooked;
	}
	
	public orderStatus getStatus() {
		return status;
	}
	
	public WaiterAgent getWaiter() {
		return waiter;
	}
	
	public CustomerAgent getCustomer() {
		return customer;
	}
	
	public int getTableNum() {
		return table;
	}

}