package ryansRestaurant;

public class RyansCookWaiterOrder {

	public RyansWaiterRole waiter = null;
	public String choice = null;
	public int tableNumber;
	
	
	public RyansCookWaiterOrder(RyansWaiterRole w, String choice,
			int tableNumber) {
		this.waiter = w;
		this.choice = choice;
		this.tableNumber = tableNumber;
	}

}
