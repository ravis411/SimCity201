package ryansRestaurant.interfaces;


public interface Cashier {
	public abstract void msgComputeBill(Waiter waiter, String choice, Customer customer);
	public abstract void msgHereIsPayment(double cash, Customer cust);
	public abstract void msgMarketBill(Market market, double total);
	
	public String toString();
}