package Transportation.test.mock;

import java.util.ArrayList;
import java.util.List;

import gui.interfaces.Passenger;
import gui.interfaces.Vehicle;

public class MockBus extends Mock implements Vehicle{

	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public MockBus(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void msgGettingOnBus(Passenger p) {
		log.add(new LoggedEvent("New passenger"));
	}
	
	public void msgFreeToLeave() {
		log.add(new LoggedEvent("Given clearance to leave bus stop"));
	}
	
	public void msgArrivedAtStop() {
		log.add(new LoggedEvent("Arrived at the bus stop"));
	}
	
	public void msgGettingOffBus() {
		log.add(new LoggedEvent("Passenger departing bus"));
	}

}
