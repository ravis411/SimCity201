package Transportation.test;

import junit.framework.TestCase;
import Transportation.BusAgent;
import Transportation.test.mock.MockBusStop;
import Transportation.test.mock.MockPassenger;

public class TransportationTest1 extends TestCase {
	BusAgent bus;
	MockPassenger mp1;
	MockPassenger mp2;
	MockBusStop stop;
	
	public void setUp() throws Exception {
		super.setUp();
		bus = new BusAgent("Bus_1");
		mp1 = new MockPassenger("MP_1");
		mp2 = new MockPassenger("MP_2");
		stop = new MockBusStop("Stop");
	}
	
	
	public void standardBusRouteScenarioOne() {
		//Preconditions
		assertEquals("Bus Agent should have an empty list of passengers", bus.getPassengers().size(), 0);
		
		//Step 1
		bus.msgGettingOnBus(mp1);
		
		//Postconditions
		assertEquals("Bus Agent should have only a single passenger", bus.getPassengers().size(), 0);
	}
	
}
