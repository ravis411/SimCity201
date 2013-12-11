package Person.test.mock;

import gui.Building.ResidenceBuildingPanel;
import interfaces.Person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Person.PersonAgent;
import Person.Role.Role;
import Person.Role.ShiftTime;
import Transportation.test.mock.LoggedEvent;
import Transportation.test.mock.Mock;

public class MockPerson extends Mock implements Person {
	
	private List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public List<Person> friends = new ArrayList<Person>();
	public int numParties = 0;
	
	public MockPerson(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgWeHaveArrived(String currentDestination) {
		log.add(new LoggedEvent("Received message that bus has arrived"));
		
	}

	@Override
	public void msgImHungry() {
		log.add(new LoggedEvent("Gotten hungry enough to go eat"));
		
	}

	@Override
	public void msgINeedMoney(double moneyNeeded) {
		log.add(new LoggedEvent("Used enough money that I need more"));
		
	}

	@Override
	public void msgYouHaveALoan(double loan) {
		log.add(new LoggedEvent("Notified of my outstanding loan"));
		
	}

	@Override
	public void msgReportForWork() {
		log.add(new LoggedEvent("Told to go to work"));
		
	}

	@Override
	public void msgGoToMarket(String item) {
		log.add(new LoggedEvent("Need supplies. Going to market"));
		
	}

	@Override
	public void msgReceiveSalary(double salary) {
		log.add(new LoggedEvent("Received salary"));
		
	}

	@Override
	public void msgPayBackLoanUrgently() {
		log.add(new LoggedEvent("Told of urgent outstanding loan"));
		
	}

	@Override
	public void msgPayBackRentUrgently() {
		log.add(new LoggedEvent("Told of urgent rent debt"));
		
	}

	@Override
	public void msgAddObjectToBackpack(String object, int quantity) {
		log.add(new LoggedEvent("Picked up a new object"));
		
	}

	@Override
	public void msgPartyInvitation(Person p, Calendar rsvpDeadline, Calendar partyTime) {
		log.add(new LoggedEvent("Invited to a party"));
		if(p != null) {
			numParties++;
		}
	}

	@Override
	public void msgIAmComing(Person p) {
		log.add(new LoggedEvent("Received an affirmative RSVP"));
		
	}

	@Override
	public void msgIAmNotComing(Person p) {
		log.add(new LoggedEvent("Received a negative RSVP"));
		
	}

	@Override
	public void startThread() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMoney(double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMoneyNeeded(double d) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void stateChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAge() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMoneyNeeded() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Person> getFriends() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResidenceBuildingPanel getHome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgYouCanLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dateAction(int month, int day, int hour, int minute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivateCurrentRole() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workIsOpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInitialRole(Role roleFromString, String string,
			ShiftTime shift) {
		// TODO Auto-generated method stub
		
	}

	public int getNumParties() {
		// TODO Auto-generated method stub
		return numParties;
	}

	@Override
	public void msgRespondToInviteUrgently(Person myPerson) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPartyOver(Person myPerson) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ShiftTime getCurrentShift() {
		// TODO Auto-generated method stub
		return null;
	}


}
