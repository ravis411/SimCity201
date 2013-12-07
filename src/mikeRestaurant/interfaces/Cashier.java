package mikeRestaurant.interfaces;

import mikeRestaurant.CashierRole.Bill;

public interface Cashier extends AgentInterface {

	/**
	 * Message sent to the cashier by the WaiterAgent to compute a bill for a particular customer
	 * @param choice the item that the customer ordered
	 * @param cust the customer who ordered
	 * @param wtr the waiter handling the order
	 */
	public void msgComputeBill(String choice, Customer cust, Waiter wtr);
	
	/**
	 * Message sent to the cashier by a CustomerAgent when he/she is trying to leave
	 * and trying to pay a bill
	 * @param check the amount owed
	 * @param cash the amount that the customer has to pay
	 * @param cust the customer who is paying
	 */
	public void msgHereIsPayment(double check, double cash, Customer cust);
	
	/**
	 * Message from the MarketAgent requesting payment for specific goods
	 * @param name name of good to buy
	 * @param quantity amount of goods
	 * @param price price of goods
	 * @param market market who requested payment
	 */
	public void msgAskForPayment(String name, int quantity, double price, Market market);
	
}
