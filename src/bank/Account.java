package bank;


import restaurant.interfaces.Cashier;
import bank.interfaces.BankClient;



/**
 * 
 * @author Byron Choy
 *
 */

public class Account {
	public BankClient client = null;
	public Cashier business = null;
	public double amount = 0;

	public Account(BankClient bank, double m){
		client = bank;
		amount = m;
	}
	
	public Account(Cashier b, double m){
		business = b;
		amount = m;
	}
}