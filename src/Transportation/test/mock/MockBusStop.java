package Transportation.test.mock;

import interfaces.Bus;
import interfaces.BusStop;
import interfaces.Passenger;
import interfaces.Person;

import java.util.ArrayList;
import java.util.List;

import Transportation.BusAgent;

public class MockBusStop extends Mock implements BusStop{
	
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public MockBusStop(String name) {
		super(name);
	}

	@Override
	public void msgArrivedAtStop(Bus bus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtBusStop(Person person, String destinationStop) {
		// TODO Auto-generated method stub
		
	}

}
