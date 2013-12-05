package kushrestaurant.interfaces;

import kushrestaurant.HostRole;
import kushrestaurant.HostRole.Table;

public interface Waiter {

	public abstract String getName();
	public abstract void msgLeavingTable(Customer cust);
	public abstract void setHost(HostRole host);
	public abstract void setCook(Cook cook);
	public abstract void msgSeatCustomer(Customer c, Table t);
	public abstract void msgReadytoOrder(Customer c);
	public abstract void msgHereisChoice(Customer c, String choice);
	public abstract void msgGetFoodFromCook();
	public abstract void msgDoneEating(Customer c);
	public abstract void msgDontHaveThis(String food, Customer c);
	public abstract void msgAtTable();
	public abstract void msgYouCantGoOnBreak();
	public abstract void msgGoOnBreak();
	public abstract void msgIWantCheck(Customer c);
	public abstract void msgHereIsCheck(Customer c, double check);
	public abstract void changeBreakEvent();
	public abstract void changeBreakEvent2();
	public abstract boolean isAtBreak();
	
}
