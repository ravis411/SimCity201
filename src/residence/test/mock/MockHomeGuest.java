package residence.test.mock;

import interfaces.HomeGuest;

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
