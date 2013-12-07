package mikeRestaurant.interfaces;

import mikeRestaurant.CustomerRole;
import mikeRestaurant.Table;
import mikeRestaurant.WaiterRole;

public interface Host extends AgentInterface {

	/**
	 * Message sent to the host by the gui panel to add a new table
	 */
	public void msgAddTable();
	
//	/**
//	 * Message sent to the host from the creation panel to add a waiter
//	 * @param waiter the waiter to add
//	 */
//	public void addWaiter(Waiter waiter);
	
	/**
	 * Message sent to the host by the gui panel when a customer is initially hungry
	 * @param cust the customer who is hungry
	 */
	public void msgIWantToEat(Customer cust);

	/**
	 * Message sent to the host by the waiter agent when a table is free
	 * @param table the table now free
	 */
	public void msgTableIsFree(Table table);
	
	/**
	 * Mesage sent by the Waiter when he wants to go on break
	 * @param wtr
	 */
	public void msgWantToGoOnBreak(Waiter wtr);
	
	/**
	 * Message sent by the Waiter when he comes off break
	 * @param wtr
	 */
	public void msgOffBreak(Waiter wtr);
	
	/**
	 * Message sent by a customer when he is waiting in line
	 * at a full restaurant and would prefer to leave
	 * @param sender the Customer who is leaving
	 */
	public void msgIWontWait(Customer sender);
	
}
