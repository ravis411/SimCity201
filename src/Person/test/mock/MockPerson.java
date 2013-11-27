package Person.test.mock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import building.Building;
import interfaces.Person;
import Person.PersonAgent;
import Transportation.test.mock.Mock;
import Transportation.test.mock.LoggedEvent;

public class MockPerson extends Mock implements Person {
	
	private List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
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
	public void msgReportForWork(String role) {
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
	public Calendar getRealTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PersonAgent> getFriends() {
		// TODO Auto-generated method stub
		return null;
	}


}
