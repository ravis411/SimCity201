package restaurant.interfaces.luca;

import java.util.Collection;
import java.util.List;

public interface LucaHost {

	public abstract String getMaitreDName();

	public abstract String getName();
	public abstract List getWaitingCustomers();
	public abstract Collection getTables();
	public abstract void msgIWantFood(LucaCustomer cust);
	
	public abstract void msgHostTableFree(int tableNum);
	
	public abstract void msgHostCanIGoOnBreak(LucaWaiter waiter);

}
