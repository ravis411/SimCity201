package restaurant.interfaces;

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
}