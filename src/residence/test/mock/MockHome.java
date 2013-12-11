package residence.test.mock;

import interfaces.Home;

/**
 * MockHomeRole built to unit test a ApartmentManagerRole
 */

public class MockHome extends Mock implements Home {
	
	public EventLog log = new EventLog();
	
	
	public int rentOwed = 0;
	
	public MockHome(String name) {
		super(name);
	}

	@Override
	public void msgRentDue(double amount, int date) {
		log.add(new LoggedEvent("I now owe $" + amount + " in rent."));
	}

	@Override
	public void msgFixedFeature() {
		log.add(new LoggedEvent("Feature has been fixed."));
	}
}
