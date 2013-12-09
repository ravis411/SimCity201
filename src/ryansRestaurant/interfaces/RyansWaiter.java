package ryansRestaurant.interfaces;


public interface RyansWaiter {
	public void msgSitAtTable(RyansCustomer cust, int tableNumber);

	public void msgReadyToOrder(RyansCustomer customer);
	
	public void msgHereIsMyChoice(RyansCustomer customer, String choice);
	
	public void msgOutOfOrder(int tableNumber, String choice);
	
	public void msgOrderReady(int tableNumber, int grillNumber);
	
	public void msgHereIsCheck(RyansCustomer cust, double total);
	
	public void msgDoneEatingLeaving(RyansCustomer customer);
	
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
