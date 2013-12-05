package Transportation.test;

import Transportation.BusStopConstruct;
//import Transportation.BusStopAgent.AgentState;
import Transportation.test.mock.MockBus;
import Transportation.test.mock.MockPassenger;
import junit.framework.*;

public class BusStopTest1 extends TestCase {
	/*
	BusStopAgent stop;
	MockBus bus;
	MockPassenger p1;
	MockPassenger p2;
	
	public void setUp() throws Exception{
		super.setUp();
		stop = new BusStopAgent("stop");
		bus = new MockBus("bus");
		p1 = new MockPassenger("p1");
		p2 = new MockPassenger("p2");
	}
	
	public void testBasicScenarioOnePerson() {
		//Preconditions
		assertEquals("Bus Stop should be idle", stop.state, AgentState.Idle);
		
		//New passenger arrives
		stop.msgWaitingForBus(p1);
		
		//PostConditions
		assertFalse("Nothing has changed yet besides the list so the scheduler should return false", stop.pickAndExecuteAnAction());
		
		//Bus Arrives
		stop.msgAtStop(bus);
		
		//PostConditions
		assertEquals("Stop should now have changed state", stop.state, AgentState.BusInStop);
		
		//Board the passenger
		assertTrue("Stop should successfully put passenger on bus", stop.pickAndExecuteAnAction());
		
		//Postconditions
		assertEquals("Stop should only have one passenger. They should not have been deleted yet", stop.passengerSize(), 1);
		
		//Removing passenger from the list
		stop.msgNewPassenger(p1);
		
		//PostConditions
		assertTrue("Stop should successfully delete passenger from list", stop.pickAndExecuteAnAction());
		assertEquals("Stop should not have passengers.", stop.passengerSize(), 0);
		
		//Bus Leaving
		stop.msgLeavingStop();
		
		//Post Conditions
		assertEquals("Bus stop should return to being idle", stop.state, AgentState.Idle);
		
	}
	*/
}
