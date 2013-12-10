package mikeRestaurant.interfaces;

import java.text.DecimalFormat;
import java.util.Map;

import mikeRestaurant.WaiterRole;
import mikeRestaurant.CustomerRole.CustomerEvent;
import mikeRestaurant.CustomerRole.CustomerState;


public interface Customer extends AgentInterface{

	/**
	 * Message sent by the cashier that responds to the customer's attempted
	 * transaction.
	 * @param approved true if the transaction was approved, false otherwise
	 * @param change the change from the transaction, only used if the transaction was approved
	 */
	void msgPaymentResponse(boolean approved, Double change);
	
	/**
	 * Message sent by the waiter for the customer to follow him/her to the customer's table
	 * @param menu An array of options to choose from
	 * @param waiter The waiter who sent the message
	 */
	public void msgFollowMeToTable(Map<String, Double> menu, Waiter sender);
	
	/**
	 * Message called by the gui for when the customer has arrived at the table
	 */
	public void msgArrivedAtTable();
	
	/**
	 * Message called by the waiter for when he has arrived at the table and is ready
	 * to take the customer's order
	 */
	public void msgWhatWouldYouLike();
	
	/**
	 * Message called by the Host to notify the customer that the restaurant is full
	 * and the customer can make a decision about it
	 */
	public void msgNotifyRestIsFull();
	
	/**
	 * Message called by the waiter to notify the customer that his food has arrived
	 */
	public void msgHereIsYourFood();
	
	/**
	 * Message called by the waiter when a revised menu is needed by the customer.
	 */
	public void msgHereIsNewMenu(Map<String, Double> menu);
	
	/**
	 * Message for when the waiter has arrived at the table for the customer to order
	 */
	public void msgWaiterAtTable();
	
	/**
	 * Message sent by the gui when the customer has arrived at the cashier
	 */
	public void msgArrivedAtCashier();
	
	/**
	 * Message sent by the gui when the customer has arrived at jail
	 */
	public void msgArrivedAtStart();
	
	/**
	 * Message sent by the WaiterAgent to deliver a check to the customer
	 * @param check the amount owed
	 */
	public void msgHereIsCheck(double check);
}
