package Person.test.mock;

import java.util.ArrayList;
import java.util.List;

import interfaces.Person;
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
	
}
