package ryansRestaurant.interfaces;


public interface RyansCashier {
	public abstract void msgComputeBill(RyansWaiter waiter, String choice, RyansCustomer customer);
	public abstract void msgHereIsPayment(double cash, RyansCustomer cust);
	public abstract void msgMarketBill(RyansMarket market, double total);
	
	public String toString();
	public abstract void msgWakeUp();
}