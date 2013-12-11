package restaurant.interfaces.luca;


public interface LucaWaiter {

	public abstract void msgWaiterSeatCustomer(LucaCustomer c, int Tablenum);

	
	public abstract void msgReadyToOrder(LucaCustomer customer);
	
	
	public abstract void msgHereIsMyChoice(LucaCustomer customer, String choice);
	public abstract void msgWaiterOrderOutOfStock(int tableOriginNumber,	String orderChoice);
		

	public abstract void msgWaiterOrderIsReady(int tableOriginNumber, String orderChoice) ;
	public abstract void msgWaiterReadyForCheck(LucaCustomer customer) ;

	public abstract void msgWaiterHereIsCheck(double d, LucaCustomer customer) ;

	public abstract void msgDoneLeavingTable(LucaCustomer customer) ;
	
	public abstract void msgAtEntrance();
	
	public abstract void msgWaiterReadytoSeat(boolean x);
	
	public abstract void msgWaiterINeedABreak();
	
	public abstract void msgWaiterYouCanBreak();

	public abstract void msgWaiterYouCanNotBreak();
	
	public abstract void msgWaiterIamBackFromBreak();

	
	public abstract void msgAtTable();
	public abstract void msgAtCook();


	public abstract String getName();


	public abstract int getMyCustomerSize();
}
