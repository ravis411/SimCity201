package bank;

import java.util.*;
import Person.PersonAgent;

public class Account {
	BankClientRole client;
	double amount;

	Account(BankClientRole bank, double m){
		client = bank;
		amount = m;
	}	 
}
