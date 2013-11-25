package residence.test.mock;

import residence.HomeRole;
import residence.interfaces.*;

/**
 * MockApartmentManager built to unit test a HomeRole
 */

public class MockApartmentManager extends Mock implements ApartmentManager {
	
	public EventLog log = new EventLog();
	
	public HomeRole homeRole;
	public double money = 0;
	public int brokenFeatures = 0;
	
	public MockApartmentManager(String name) {
		super(name);
	}
	
	public void msgRentPaid (Home p, int amount) {
		log.add(new LoggedEvent("Received rent amount of $" + amount + " from resident."));
	}
	public void msgBrokenFeature(String name, Home h) {
		log.add(new LoggedEvent("Resident reporting broken " + name));
		brokenFeatures++;
		homeRole.msgFixedFeature(name);
	}
}