package Transportation.test;

import Person.test.mock.MockPerson;
import Transportation.BusStopAgent;
import Transportation.test.mock.MockBus;
import junit.framework.TestCase;

public class BusStopTest2 extends TestCase {
	
	BusStopAgent stop_1, stop_2;	
	MockPerson p1;
	MockPerson p2;
	MockBus bus;
	
	public void setUp() throws Exception {
		stop_1 = new BusStopAgent("Stop_1", null); //May break because referencing null animation panel
		stop_2 = new BusStopAgent("Stop_2", null);
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
		
		
	}
	
	
}
