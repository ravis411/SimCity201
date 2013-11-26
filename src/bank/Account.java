package bank;


import bank.interfaces.BankClient;


/**
 * 
 * @author Byron Choy
 *
 */

public class Account {
	public BankClient client;
	public double amount;

	public Account(BankClient bank, double m){
		client = bank;
		amount = m;
	}	 
}