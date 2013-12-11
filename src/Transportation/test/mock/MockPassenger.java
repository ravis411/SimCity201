package Transportation.test.mock;

import interfaces.Bus;
import interfaces.Passenger;

import java.util.ArrayList;
import java.util.List;

public class MockPassenger extends Mock implements Passenger {

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public MockPassenger(String name) {
		super(name);
	}
	
	@Override
	public void msgBusIsHere(Bus bus) {
		log.add(new LoggedEvent("Received msgBusIsHere from bus stop"));
	}
	
	@Override
	public void msgNextDestination(String location) {
		log.add(new LoggedEvent("Received msgArrivedAtDestination from bus"));
		
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
