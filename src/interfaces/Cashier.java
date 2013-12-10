package interfaces;

//import restaurant.CashierAgent.Check;

/**
 * A sample Cashier interface built to unit test
 *
 * @author Dylan Resnik
 *
 */
public interface Cashier {
	public void msgPrepareCheck(Waiter waiter, Customer customer, int mealChoice);
	public void msgPayingCheck(Customer customer, double amountOwed);
	public void msgMarketBill(Market market, double amount);
}