package bank;


import restaurant.CashierRole;
import bank.interfaces.BankClient;



/**
 * 
 * @author Byron Choy
 *
 */

public class Account {
	public BankClient client = null;
	public CashierRole business = null;
	public double amount = 0;

	public Account(BankClient bank, double m){
		client = bank;
		amount = m;
	}
	
	public Account(CashierRole b, double m){
		business = b;
		amount = m;
	}
}