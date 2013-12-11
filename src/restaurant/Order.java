package restaurant;

import interfaces.Customer;
import interfaces.Waiter;
import restaurant.Menu;
import restaurant.Menu.Dish;

public class Order
{
	Waiter waiter;
	Customer customer;
	int choice;
	//Dish choice;
	int table;
	public enum orderStatus {pending, cooking, cooked}
	public orderStatus status = orderStatus.pending;
	int cookTime;

	public Order (Waiter waiter, int choice, int table, Customer customer) {
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
	
	public Waiter getWaiter() {
		return waiter;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public int getTableNum() {
		return table;
	}

}