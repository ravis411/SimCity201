package residence.test.mock;

import residence.ApartmentManagerRole;
import residence.HomeRole;
import residence.interfaces.*;

/**
 * MockHomeGuestRole built to unit test
 */

public class MockHomeGuest extends Mock implements HomeGuest {
	
	public EventLog log = new EventLog();
	
	public MockHomeGuest(String name) {
		super(name);
	}

	@Override
	public void msgComeIn() {
		log.add(new LoggedEvent("Entering party."));
	}

	@Override
	public void msgPartyOver() {
		log.add(new LoggedEvent("Leaving party."));
	}
	
	
}
