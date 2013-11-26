package Transportation.test.mock;

import java.util.ArrayList;
import java.util.List;

import gui.interfaces.Passenger;
import gui.interfaces.Bus;

public class MockBus extends Mock implements Bus{

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

	@Override
	public void msgGettingOffBus(Passenger p) {
		log.add(new LoggedEvent("Passenger leaving bus"));
		
	}

}
