package kushrestaurant.interfaces;

import kushrestaurant.HostRole.Table;

public interface Host {

	public abstract String getName();
	public abstract void msgIWantFood(Customer c);
	public abstract void msgTableAvailable(Customer c);
	public abstract void seatCustomerMsg(Customer c,Table t);
	public abstract void msgAskForBreak(Waiter w);
	public abstract void msgBreakDone(Waiter w);
	
}
