package kushrestaurant.interfaces;

import java.util.ArrayList;

import kushrestaurant.CashierRole.Bill;

public interface Cashier {

	public abstract void msgProduceCheck(Customer c, String choice, Waiter w);
	public abstract void msgHereIsPayment(Customer c, double check, double cash);
	public abstract void msgHereIsBill(Market m, int bill);
	public abstract double getBill();
	public abstract double getMoney();
	public abstract ArrayList<Bill> getListOfBills();
}
