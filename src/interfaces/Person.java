package interfaces;

import java.util.Calendar;

public interface Person {
	
	//Transportation functions
	public abstract void msgWeHaveArrived(String currentDestination);
	
	public abstract void msgImHungry();
	
	public abstract void msgINeedMoney(double moneyNeeded);
	
	public abstract void msgYouHaveALoan(double loan);
	
	public abstract void msgReportForWork(String role);
	
	public abstract void msgGoToMarket(String item);
	
	public abstract void msgReceiveSalary(double salary);
	
	public abstract void msgPayBackLoanUrgently();
	
	public abstract void msgPayBackRentUrgently();
	
	public abstract void msgAddObjectToBackpack(String object, int quantity);
	
	public abstract void msgPartyInvitation(Person p, Calendar rsvpDeadline, Calendar partyTime);
	
	public abstract void msgIAmComing(Person p);
	
	public abstract void msgIAmNotComing(Person p);

	public abstract String getName();
	

}
