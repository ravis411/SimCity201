package residence.test;

import Person.PersonAgent;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import residence.HomeGuestRole;
import residence.HomeGuestRole.AgentState;

/**
 * 
 * JUnit test for HomeGuestRole
 *
 */

public class HomeGuestTest extends TestCase {

	private PersonAgent myPerson;
	private HomeGuestRole homeGuestRole;
	
	public static Test suite() {
	    return new TestSuite(HomeGuestTest.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();

		myPerson= new PersonAgent("PersonAgent",null);
		homeGuestRole= new HomeGuestRole();
		homeGuestRole.setPerson(myPerson);
	}
	
	 public void testOneStates() throws InterruptedException {
		 try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 //preconditions
		 assertTrue("Initial State shoud be doingNothing. Instead, state is " + homeGuestRole.state, homeGuestRole.state==AgentState.DoingNothing);
		 
		 //step 1
		 homeGuestRole.msgComeIn();
		 assertTrue("Initial State shoud be doingNothing. Instead, state is " + homeGuestRole.state, homeGuestRole.state==AgentState.WalkingIn);
		 //step 2
		 homeGuestRole.msgPartyOver();
		 assertTrue("Initial State shoud be doingNothing. Instead, state is " + homeGuestRole.state, homeGuestRole.state==AgentState.Leaving);

	 }
}