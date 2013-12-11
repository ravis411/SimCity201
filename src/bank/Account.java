package bank;


import interfaces.BankClient;
import interfaces.generic_interfaces.GenericCashier;
import restaurant.CashierRole;



/**
 * 
 * @author Byron Choy
 *
 */

public class Account {
	public BankClient client = null;
	public GenericCashier business = null;
	public double amount = 0;

	public Account(BankClient bank, double m){
		client = bank;
		amount = m;
	}
	
	public Account(GenericCashier b, double m){
		business = b;
		amount = m;
	}

}