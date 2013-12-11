package Transportation.test.mock;

import interfaces.Bus;
import interfaces.Passenger;

import java.util.ArrayList;
import java.util.List;

import Transportation.myBusPassenger;

public class MockBus extends Mock implements Bus{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public MockBus(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereArePassengers(List<myBusPassenger> passengers) {
		log.add(new LoggedEvent("Received a list of passengers from the bus stop"));
	}
	
	public void addBusStop() {
		log.add(new LoggedEvent("Notified of a new bus stop"));
	}

}
