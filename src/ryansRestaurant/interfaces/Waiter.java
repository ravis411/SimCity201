package ryansRestaurant.interfaces;


public interface Waiter {
	public void msgSitAtTable(Customer cust, int tableNumber);

	public void msgReadyToOrder(Customer customer);
	
	public void msgHereIsMyChoice(Customer customer, String choice);
	
	public void msgOutOfOrder(int tableNumber, String choice);
	
	public void msgOrderReady(int tableNumber, int grillNumber);
	
	public void msgHereIsCheck(Customer cust, double total);
	
	public void msgDoneEatingLeaving(Customer customer);
	
	/**Msg from host to allow waiter to go on a break
	 * 
	 * @param b true if break is allowed, false otherwise
	 */
	public void msgFromHostGoOnBreak(boolean b);
	/**
	 * From animation so waiter can request a break
	 */
	public abstract void msgRequestABreak();
	
}
