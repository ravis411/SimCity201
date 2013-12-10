package ryansRestaurant;

import interfaces.generic_interfaces.GenericWaiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RyansRevolvingStand {
	private List<RyansCookWaiterOrder> orders = Collections.synchronizedList(new ArrayList<RyansCookWaiterOrder>());
	
	/**
	 * Adds an order to the Stand based on the necessary parameters
	 * @param w the GenericWaiter
	 * @param choice the choice
	 * @param t the Table
	 */
	public synchronized void addOrder(GenericWaiter w, String choice, int tableNumber){
		orders.add(new RyansCookWaiterOrder((RyansWaiterRole) w, choice, tableNumber));
	}
	
	/**
	 * Will try and take the last order, if it exists,
	 * removing it from the list if successful.
	 * @return the final order in the list
	 */
	public synchronized RyansCookWaiterOrder getLastOrder(){
		if(orders.size() != 0){
			return orders.remove(0);
		}
		return null;
	}
	
	/**
	 * Returns true if there are no orders in the list, false otherwise
	 * @return true if there are no orders in the list, false otherwise
	 */
	public synchronized boolean isEmpty(){
		return orders.isEmpty();
	}
}
