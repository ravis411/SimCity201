
package residence.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import residence.HomeRole;
import residence.HomeRole.AgentEvent;
import residence.HomeRole.AgentState;
import residence.HomeRole.PartyState;
import residence.gui.HomeRoleGui;
import residence.test.mock.MockApartmentManager;
import residence.test.mock.MockHome;
import trace.AlertLog;
import Person.PersonAgent;
import Person.test.mock.MockRole;

public class PartyThrowingTest1 extends TestCase {
	
	private PersonAgent myPerson;
	private PersonAgent friend1;
	private PersonAgent friend2;
	
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
		
		homeRole= new HomeRole(myPerson);

		myPerson.friends.add(friend1);
		myPerson.friends.add(friend2);
		friend1.friends.add(myPerson);
		
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
	    myPerson.homeThrowParty();
	    assertTrue("partyState should be send invites", homeRole.partyState==PartyState.sendInvites);
	    myPerson.stateChanged();
	    assertTrue("Size of the party invitees list should be 5", homeRole.partyInvitees.size()==5);
	    assertTrue("partyState should be set up", homeRole.partyState==PartyState.setUp);
	    assertTrue("Size of the party list in every person should be 1", friend1.getNumParties()==1);
	    assertTrue("Size of the party list in every person should be 1", friend2.getNumParties()==1);
	    friend1.rsvpYes(myPerson,friend1.parties.get(0));
	    friend2.rsvpNo(myPerson, friend2.parties.get(0));
	    assertTrue("friend1 should have partyState of the one party as going", friend1.parties.get(0).partyState==Person.PersonAgent.PartyState.GoingToParty);
	    assertTrue("friend2 should have partyState of the one party as not going", friend2.parties.get(0).partyState==Person.PersonAgent.PartyState.NotGoingToParty);
	    //cant run the dateListeners in the test so making the friends go to the party arbritarily
	    homeRole.msgHostParty();
	    myPerson.stateChanged();
	    friend1.GoToParty(myPerson.getHome().getName());
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
		assertTrue("List of parties should be empty",friend1.getNumParties()==0);
		
		
		
		
		
		
	}
}
	
=======
//package residence.test;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import residence.HomeRole;
//import residence.HomeRole.AgentEvent;
//import residence.HomeRole.AgentState;
//import residence.HomeRole.PartyState;
//import residence.gui.HomeRoleGui;
//import residence.test.mock.MockApartmentManager;
//import residence.test.mock.MockHome;
//import trace.AlertLog;
//import Person.PersonAgent;
//import Person.test.mock.MockRole;
//
//public class PartyThrowingTest1 extends TestCase {
//	
//	private PersonAgent myPerson;
//	private PersonAgent friend1;
//	private PersonAgent friend2;
//	private PersonAgent friend3;
//	private PersonAgent friend4;
//	private PersonAgent friend5;
//	private MockHome home;
//	private HomeRole homeRole;
//	private HomeRoleGui gui;
//	
//	public static Test suite() {
//	    return new TestSuite(PartyThrowingTest1.class);
//	}
//	
//	public void setUp() throws Exception{
//		super.setUp();
//		
//		home= new MockHome("Mock Home");
//		myPerson= new PersonAgent("PersonAgent",null);
//		friend1 = new PersonAgent("Friend 1", null);
//		friend2 = new PersonAgent("Friend 2", null);
//		friend3 = new PersonAgent("Friend 3", null);
//		friend4 = new PersonAgent("Friend 4", null);
//		friend5 = new PersonAgent("Friend 5", null);
//		homeRole= new HomeRole(myPerson);
//
//		myPerson.friends.add(friend1);
//		myPerson.friends.add(friend2);
//		myPerson.friends.add(friend3);
//		myPerson.friends.add(friend4);
//		myPerson.friends.add(friend5);
//	}
//	
//	//-------------------ELEMENTARY PRECONDITIONS-----------------//
//	public void testHomeRolePreConditions(){
//		try {
//			setUp();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("TEST FOR PRECONDITIONS");
//		
//		
//		assertTrue("Initial State shoud be doingNothing",homeRole.state==AgentState.DoingNothing);
//		assertTrue("Size of friends list should be 5",myPerson.friends.size()==5);
//		assertTrue("List of parties should be empty",myPerson.getNumParties()==0);
//		assertTrue("List of parties should be empty",friend1.getNumParties()==0);
//		assertTrue("List of parties should be empty",friend2.getNumParties()==0);
//		assertTrue("List of parties should be empty",friend3.getNumParties()==0);
//		assertTrue("List of parties should be empty",friend4.getNumParties()==0);
//		assertTrue("List of parties should be empty",friend5.getNumParties()==0);
//		
//		
//	}
//	//-------------------SENDING INVITES--------------------------//
//	public void testHomeRoleMessages(){
//		try {
//			setUp();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		homeRole.msgThrowParty();
//	}
//		
//	//-------------------CHECKING POSTCONDITIONS--------------------------//	
//		
//		public void testHomePostConditions(){
//			try {
//				setUp();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}	
//		
//		
//		assertTrue("List of parties should be empty",myPerson.getNumParties()==0);
//		assertTrue("List of parties should be empty",friend5.getNumParties()==0);
//		
//		
//		
//		
//		
//		
//	}
//}
//	

