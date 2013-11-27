package Transportation.test;

import Person.test.mock.MockPerson;
import Transportation.BusStopAgent;
import Transportation.BusStopAnimationPanel;
import Transportation.test.mock.MockBus;
import junit.framework.TestCase;

public class BusStopTest2 extends TestCase {
	
	BusStopAgent stop_1, stop_2;	
	MockPerson p1;
	MockPerson p2;
	MockBus bus;
	BusStopAnimationPanel dummyPanel;
	
	public void setUp() throws Exception {
		dummyPanel = new BusStopAnimationPanel();
		stop_1 = new BusStopAgent("Stop_1", dummyPanel); 
		stop_2 = new BusStopAgent("Stop_2", dummyPanel);
		p1 = new MockPerson("p1");
		p2 = new MockPerson("p2");
		bus = new MockBus("bus");
		BusStopAgent.addStop("Stop_1", stop_1);
		BusStopAgent.addStop("Stop_2", stop_2);
	}

	public void testNormativeScenario() {
		//Prerequisites
		assertEquals("Bus map did not populate correctly", BusStopAgent.stops.get("Stop_2"), stop_2);
		assertEquals("Bus map did not populate correctly", BusStopAgent.stops.get("Stop_1"), stop_1);
		
		//New person arrives
		stop_1.msgAtBusStop(p1, "Stop_1");
		
		//Postconditions
		assertEquals("Bus did not correctly add the passenger", stop_1.passengerSize(), 1);
		assertEquals("Did not add the passenger to the correct stop", stop_2.passengerSize(), 0);
		
		//Bus arries at stop
		stop_1.msgArrivedAtStop(bus);
		
		//Postconditions
		assertEquals("Bus stop did not correctly clear waiting passenger list", stop_1.passengerSize(), 0);
		
	}
	
	
}
