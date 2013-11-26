package ryansRestaurant.interfaces;


public interface Host {
	public abstract void msgIWantFood(Customer cust);
	
	//called if a customer decides to leave before being seated.
	public abstract void msgIWantToLeave(Customer cust);
	
	public abstract void msgTableFree(Waiter waiter, int tableNumber);
	
	/** A message for the waiter to go on break
	 * 
	 * @param waiter The waiter
	 */
	public abstract void msgWantToGoOnBreak(Waiter waiter);
	
	/** A waiter calls this function when they are done with their break
	 * 
	 * @param waiter The waiter
	 */
	public abstract void msgBreakOver(Waiter waiter);
	
	
	/**msgAddWaiter
	 * @param waiter the waiter to add
	 */
	public abstract void msgAddWaiter(Waiter waiter);
	
	
	/**msgAddTable sent from GUI when a table is added.
	 * 
	 * @param tableNumber must be a tableNumber that does not currently exist or the function will terminate
	 * @param numSeats currently a value 1-4 for the number of seats
	 */
	public abstract void msgAddTable(int tableNumber, int numSeats);
}
