package Transportation.test;

import junit.framework.*;
import Transportation.BusAgent;
//import Transportation.BusAgent.AgentState;
import Transportation.test.mock.MockBusStop;
import Transportation.test.mock.MockPassenger;

public class BusTest1 extends TestCase {
	/*
	BusAgent bus;
	MockPassenger mp1;
	MockPassenger mp2;
	MockBusStop stop;
	
	public void setUp() throws Exception {
		super.setUp();
		//will break. need to add support for gui pointers
		bus = new BusAgent("Bus_1");
		bus.addBusStop(1, "Stop_1", null);
		bus.addBusStop(2, "Stop_2", null);
		bus.addBusStop(3, "Stop_3", null);
		mp1 = new MockPassenger("MP_1");
		mp2 = new MockPassenger("MP_2");
		stop = new MockBusStop("Stop");
	}
	
	
	public void testSinglePassengerSingleStop() {
		//Preconditions
		assertEquals("Bus Agent should have an empty list of passengers", bus.getPassengers().size(), 0);
		assertFalse("Scheduler should return false", bus.pickAndExecuteAnAction());
		
		//Load first passenger
		bus.msgArrivedAtStop(); //Prelim debug to set initial state
		bus.msgGettingOnBus(mp1);
		
		//Postconditions
		assertEquals("Bus Agent should have only a single passenger", bus.getPassengers().size(), 1);
		assertFalse("Scheduler should return false", bus.pickAndExecuteAnAction());
		
		assertEquals("State should not be inTransit", bus.state, AgentState.loading);
		
		//Move to new location
		bus.msgFreeToLeave();

		
		//Postconditions
		assertTrue("Scheduler should have a new task", bus.pickAndExecuteAnAction());
		assertEquals("Bus should be in next location", bus.location, "Stop_2");
		
		//Arrived at next stop
		bus.msgArrivedAtStop();
		//Passenger decides this is his stop
		bus.msgGettingOffBus(mp1); //Needs to be sent before. need to pass next stop to passenger
		
		//Postconditions
		assertEquals("Bus should now be unloading", bus.state, AgentState.unloading);
		assertTrue("Scheduler should now have a task", bus.pickAndExecuteAnAction());
		assertEquals("Bus should still be unloading", bus.state, AgentState.unloading);
		
		
	}
	*/
}
