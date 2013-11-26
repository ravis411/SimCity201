package Transportation.test;

import junit.framework.*;
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
	
	
	public void testSinglePassengerSingleStop() {
		//Preconditions
		assertEquals("Bus Agent should have an empty list of passengers", bus.getPassengers().size(), 0);
		assertFalse("Scheduler should return false", bus.pickAndExecuteAnAction());
		
		//Load first passenger
		bus.msgGettingOnBus(mp1);
		
		//Postconditions
		assertEquals("Bus Agent should have only a single passenger", bus.getPassengers().size(), 1);
		assertFalse("Scheduler should still return false", bus.pickAndExecuteAnAction());
		
		//Move to new location
		bus.msgFreeToLeave();
		
		//Postconditions
		//assertEquals("Bus should be in next location", bus.location, "Bus Stop 3");
		
		//Arrived at next stop
		bus.msgArrivedAtStop();
		
		//Postconditions
		assertTrue("Scheduler should now have a task", bus.pickAndExecuteAnAction());
		assertEquals("List of passengers should still be size one because passenger hasn't left yet", bus.getPassengers().size(), 1);
		
		//Passenger decides this is his stop
		bus.msgGettingOffBus(mp1);
		
		//Postconditions
		assertTrue("Scheduler should now have to delete passenger", bus.pickAndExecuteAnAction());
		//assertEquals("Passenger list should now be empty", bus.getPassengers().size(), 0);
		
	}
}
