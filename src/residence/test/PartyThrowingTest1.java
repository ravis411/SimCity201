package residence.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import residence.HomeRole;
import residence.HomeRole.AgentState;
import residence.gui.HomeRoleGui;
import residence.test.mock.MockHome;
import Person.PersonAgent;

public class PartyThrowingTest1 extends TestCase {
	
	private PersonAgent myPerson;
	private PersonAgent friend1;
	private PersonAgent friend2;
	private PersonAgent friend3;
	private PersonAgent friend4;
	private PersonAgent friend5;
	private MockHome home;
	private HomeRole homeRole;
	private HomeRoleGui gui;
	
	public static Test suite() {
	    return new TestSuite(PartyThrowingTest1.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		
		home= new MockHome("Mock Home");
		myPerson= new PersonAgent("PersonAgent",null);
		friend1 = new PersonAgent("Friend 1", null);
		friend2 = new PersonAgent("Friend 2", null);
		friend3 = new PersonAgent("Friend 3", null);
		friend4 = new PersonAgent("Friend 4", null);
		friend5 = new PersonAgent("Friend 5", null);
		homeRole= new HomeRole(myPerson);

		myPerson.friends.add(friend1);
		myPerson.friends.add(friend2);
		myPerson.friends.add(friend3);
		myPerson.friends.add(friend4);
		myPerson.friends.add(friend5);
	}
	
	//-------------------ELEMENTARY PRECONDITIONS-----------------//
	public void testHomeRolePreConditions(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEST FOR PRECONDITIONS");
		
		
		assertTrue("Initial State shoud be doingNothing",homeRole.state==AgentState.DoingNothing);
		assertTrue("Size of friends list should be 5",myPerson.friends.size()==5);
		assertTrue("List of parties should be empty",myPerson.getNumParties()==0);
		assertTrue("List of parties should be empty",friend1.getNumParties()==0);
		assertTrue("List of parties should be empty",friend2.getNumParties()==0);
		assertTrue("List of parties should be empty",friend3.getNumParties()==0);
		assertTrue("List of parties should be empty",friend4.getNumParties()==0);
		assertTrue("List of parties should be empty",friend5.getNumParties()==0);
		
		
	}
	//-------------------SENDING INVITES--------------------------//
	public void testHomeRoleMessages(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		homeRole.msgThrowParty();
	}
		
	//-------------------CHECKING POSTCONDITIONS--------------------------//	
		
		public void testHomePostConditions(){
			try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		
		
		assertTrue("List of parties should be empty",myPerson.getNumParties()==0);
		assertTrue("List of parties should be empty",friend5.getNumParties()==0);
		
		
		
		
		
		
	}
}
	