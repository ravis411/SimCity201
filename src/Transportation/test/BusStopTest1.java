package Transportation.test;

import Transportation.BusStopAgent;
import Transportation.BusStopAgent.AgentState;
import Transportation.test.mock.MockBus;
import Transportation.test.mock.MockPassenger;
import junit.framework.*;

public class BusStopTest1 extends TestCase {

	BusStopAgent stop;
	MockBus bus;
	MockPassenger p1;
	MockPassenger p2;
	
	public void setUp() throws Exception{
		super.setUp();
		stop = new BusStopAgent();
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
		assertFalse("Nothing has changed yet besides the list so the scheduler should return false", )
	}
	
}
