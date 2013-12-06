package interfaces;

import gui.Building.ResidenceBuildingPanel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Person.PersonAgent;
import Person.PersonAgent.Friend;
import Person.Role.Role;
import bank.BankClientRole;
import bank.BankTellerRole;
import bank.LoanTellerRole;
import building.Building;
import util.DateListener;
import util.MasterTime;

import java.util.Calendar;


public interface Person extends DateListener{
	
	//Transportation functions
	public abstract void msgWeHaveArrived(String currentDestination);
	
	public abstract void msgImHungry();
	
	public abstract void msgINeedMoney(double moneyNeeded);
	
	public abstract void msgYouHaveALoan(double loan);
	
	public abstract void msgReportForWork();
	
	public abstract void msgGoToMarket(String item);
	
	public abstract void msgReceiveSalary(double salary);
	
	public abstract void msgPayBackLoanUrgently();
	
	public abstract void msgPayBackRentUrgently();
	
	public abstract void msgAddObjectToBackpack(String object, int quantity);
	
	public abstract void msgPartyInvitation(Person p, Calendar rsvpDeadline, Calendar partyTime);
	
	public abstract void msgIAmComing(Person p);
	
	public abstract void msgIAmNotComing(Person p);
	
	void msgYouCanLeave();

	public abstract String getName();


	public abstract void startThread();


	public abstract void setMoney(double d);

	public abstract void setMoneyNeeded(double d);

	public abstract double getMoney();

	public abstract void stateChanged();

	public abstract int getAge();

	public abstract double getMoneyNeeded();

	public abstract List<PersonAgent> getFriends();

	public abstract ResidenceBuildingPanel getHome();

	public abstract void setInitialRole(Role roleFromString, String string);
	
	public void dateAction(int month, int day, int hour, int minute);


}
