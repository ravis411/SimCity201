package mikeRestaurant.interfaces;

import java.util.Set;

import mikeRestaurant.CustomerRole;
import mikeRestaurant.Table;
import mikeRestaurant.WaiterRole;

public interface Waiter extends AgentInterface{

	/**
	 * Message received from the Cashier with the amount owed by a Customer
	 * @param sender
	 * @param price
	 */
	void msgHereIsCheck(Customer sender, double price);
	
	/**
	 * Message sent by the HostAgent to seat a particular customer waiting
	 * to eat at the restauraunt
	 * @param customer the CustomerAgent to seat
	 * @param table the table the customer is to sit at
	 */
	public void msgSitAtTable(Customer customer, Table table);
	
	/**
	 * Message sent by the CustomerAgent when he/she is ready for the check
	 * from his/her waiter
	 * @param sender the CustomerAgent who wants his/her check
	 */
	public void msgReadyForCheck(Customer sender);
	
	/**
	 * Message sent by the CustomerAgent to signal the waiter for ordering
	 * @param sender the CustomerAgent sending the message
	 */
	public void msgImReadyToOrder(Customer sender);
	
	/**
	 * Message sent by the CustomerAgent to give a waiter his/her order
	 * @param choice A String representation of the choice
	 * @param sender the CustomerAgent sending the message
	 */
	public void msgHereIsMyChoice(String choice, Customer sender);
	
	/**
	 * Message sent by the CookAgent that a particular order is ready for delivery
	 * @param order the order that is ready
	 */
	public void msgOrderIsReady(Waiter waiter, String choice, Table table, int grillPosition);
	
	/**
	 * Message sent by the CookAgent to inform the waiter that he is 
	 * out of food for a particular order
	 * @param order the order for which the cook does not have sufficient food
	 */
	public void msgOutOfFoodForOrder(Waiter waiter, String choice, Table table, Set<String> remainingFood);
	
	/**
	 * Message sent by the CustomerAgent that he/she is done eating and leaving the restaurant
	 * @param sender the CustomerAgent sending the message
	 */
	public void msgDoneEatingAndPaying(Customer sender);

	/**
	 * Message Sent by the WaiterGui that it has arrived at the table
	 */
	public void msgGuiAtTable();
	
	/**
	 * Message sent by the WaiterGui that it has arrived at the cook
	 */
	public void msgGuiAtCook();
	
	/**
	 * Message sent by the WaiterGui that it has arrived at the starting location
	 * presumably to pickup a new customer
	 */
	public void msgGuiAtStart();
	
	/**
	 * Message sent by the WaiterGui that it has arrived at the cashier
	 */
	public void msgGuiAtCashier();
	
	/**
	 * Message sent by the GUI to request a break;
	 */
	public void goOnBreak();
	
	/**
	 * Message sent by the HostAgent telling the waiter if he/she
	 * can go on break
	 * @param canGoOnBreak true if the waiter can go on break, false otherwise
	 */
	public void msgBreakReply(boolean canGoOnBreak);
}
