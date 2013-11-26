package bank;

import java.util.*;
import Person.PersonAgent;

/**
 * 
 * @author Byron Choy
 *
 */

public class Account {
	BankClientRole client;
	double amount;

	Account(BankClientRole bank, double m){
		client = bank;
		amount = m;
	}	 
}
