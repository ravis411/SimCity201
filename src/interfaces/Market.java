package interfaces;

//import restaurant.CashierAgent.Check;

/**
 * A sample Cashier interface built to unit test
 *
 * @author Dylan Resnik
 *
 */
public interface Market {
	public abstract void msgNeedFood(int ingredientNum, int quantity);
	public abstract void msgReceivePayment(double amount);
	public abstract String getName();
}