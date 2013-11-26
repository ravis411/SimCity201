package Transportation.test.mock;

import java.util.ArrayList;
import java.util.List;

import Transportation.BusAgent;
import gui.interfaces.BusStop;
import gui.interfaces.Passenger;

public class MockBusStop extends Mock implements BusStop{
	
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public MockBusStop(String name) {
		super(name);
	}

	@Override
	public void msgWaitingForBus(Passenger p) {
		log.add(new LoggedEvent("New passenger at this stop"));
	}

	@Override
	public void msgAtStop(BusAgent bus) {
		log.add(new LoggedEvent("Bus has arrived at this stop"));
	}

	@Override
	public void msgNewPassenger(Passenger p) {
		log.add(new LoggedEvent("Current bus has acquired a passenger"));
	}

	@Override
	public void msgLeavingStop() {
		log.add(new LoggedEvent("Current bus is leaving stop"));
	}

}
