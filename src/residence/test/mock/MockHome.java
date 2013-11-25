package residence.test.mock;

import residence.ApartmentManagerRole;
import residence.HomeRole;
import residence.interfaces.*;

/**
 * MockHomeRole built to unit test a ApartmentManagerRole
 */

public class MockHome extends Mock implements Home {
	
	public EventLog log = new EventLog();
	
	public ApartmentManagerRole landlord;
	public int rentOwed = 0;
	
	public MockHome(String name) {
		super(name);
	}
	
	public void msgRentDue (int amount) {
		log.add(new LoggedEvent("I now owe $" + amount + " in rent."));
	}
	public void msgFixedFeature (String name) {
		log.add(new LoggedEvent(name + " has been fixed."));
	}
}
