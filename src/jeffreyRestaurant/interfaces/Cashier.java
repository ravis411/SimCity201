package jeffreyRestaurant.interfaces;

public interface Cashier {
	//Message to cashier when customer is ready to pay
	public abstract void msgCustomerReadyToPay(Waiter w, String choice, Customer c);
	
	//Message to cashier when customer is paying
	public abstract void msgCustomerPayment(Customer c, Double money);
}
