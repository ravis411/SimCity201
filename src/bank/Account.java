package bank;

import java.util.*;


public class Account {
	//PersonAgent person;
	bankClientRole client;
	double amount;

	Account(bankClientRole bank, double m){
		client = bank;
		amount = m;
	}	
}
