package interfaces;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Person.PersonAgent;
import bank.BankClientRole;
import bank.BankTellerRole;
import bank.LoanTellerRole;
import building.Building;


public interface Person {
	
	//Transportation functions
	public abstract void msgWeHaveArrived(String currentDestination);

	public abstract String getName();


	public abstract void startThread();


	public abstract void setMoney(double d);

	public abstract void setMoneyNeeded(double d);

	public abstract double getMoney();

	public abstract void stateChanged();

	public abstract int getAge();

	public abstract double getMoneyNeeded();

	public abstract Calendar getRealTime();


	public abstract List<PersonAgent> getFriends();


}
