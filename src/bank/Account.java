package bank;


import bank.interfaces.BankClient;


/**
 * 
 * @author Byron Choy
 *
 */

public class Account {
	BankClient client;
	double amount;

	Account(BankClient bank, double m){
		client = bank;
		amount = m;
	}	 
}