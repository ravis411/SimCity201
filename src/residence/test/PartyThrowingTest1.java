package residence.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import residence.HomeRole;

import residence.HomeRole.AgentState;
import residence.HomeRole.PartyState;
import Person.PersonAgent;
import Person.test.mock.MockPerson;

public class PartyThrowingTest1 extends TestCase {
	
	private PersonAgent myPerson;
	private MockPerson friend1;
	private MockPerson friend2;
	private MockPerson friend3;
	private MockPerson friend4;
	private HomeRole homeRole;
	
	public static Test suite() {
	    return new TestSuite(PartyThrowingTest1.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		
		myPerson = new PersonAgent("PersonAgent",null);
		friend1 = new MockPerson("Friend 1");
		friend2 = new MockPerson("Friend 2");
		friend3 = new MockPerson("Friend 3");
		friend4 = new MockPerson("Friend 4");
		homeRole = new HomeRole(myPerson);

		myPerson.friends.add(friend1);
		myPerson.friends.add(friend2);
		myPerson.friends.add(friend3);
		myPerson.friends.add(friend4);
		
		friend1.friends.add(myPerson);
		friend2.friends.add(myPerson);
	}
	
	public void testOneThrowPartyNormative(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//preconditions
		assertTrue("Initial State shoud be doingNothing", homeRole.state==AgentState.DoingNothing);
		assertTrue("Size of friends list should be 4", myPerson.friends.size()==4);
		assertTrue("List of parties should be empty", myPerson.getNumParties()==0);
		assertTrue("List of parties should be empty", friend1.getNumParties()==0);
		assertTrue("List of parties should be empty", friend2.getNumParties()==0);
		assertTrue("List of parties should be empty", friend3.getNumParties()==0);
		assertTrue("List of parties should be empty", friend4.getNumParties()==0);
		
		//step 1
		homeRole.msgThrowParty();
		friend1.msgPartyInvitation(myPerson, null, null);
		friend2.msgPartyInvitation(myPerson, null, null);
		
		//postconditions
		assertTrue("List of parties should be empty", myPerson.getNumParties()==0);
		assertTrue("PartyState shoud be send invites. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.sendInvites);
		assertTrue("There should be one party in the list", friend1.getNumParties()==1);
		assertTrue("There should be one party in the list", friend2.getNumParties()==1);
		assertTrue("List of parties should be empty", friend3.getNumParties()==0);
		assertTrue("List of parties should be empty", friend4.getNumParties()==0);
		
		//step 2
		homeRole.msgHostParty();
		
		//postconditions
		assertTrue("PartyState shoud be host. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.host);
		
	}
	
	public void testTwoThrowPartyOneAttendee(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	
		//preconditions
		assertTrue("Initial State shoud be doingNothing", homeRole.state==AgentState.DoingNothing);
		assertTrue("Size of friends list should be 4", myPerson.friends.size()==4);
		assertTrue("List of parties should be empty", myPerson.getNumParties()==0);
		assertTrue("List of parties should be empty", friend1.getNumParties()==0);
		assertTrue("List of parties should be empty", friend2.getNumParties()==0);
		assertTrue("List of parties should be empty", friend3.getNumParties()==0);
		assertTrue("List of parties should be empty", friend4.getNumParties()==0);
		
		//step 1
		homeRole.msgThrowParty();
		friend2.msgPartyInvitation(myPerson, null, null);
		friend3.msgPartyInvitation(null, null, null);
		
		//postconditions
		assertTrue("List of parties should be empty", myPerson.getNumParties()==0);
		assertTrue("PartyState shoud be send invites. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.sendInvites);
		assertTrue("There should be one party in the list", friend1.getNumParties()==0);
		assertTrue("There should be one party in the list", friend2.getNumParties()==1);
		assertTrue("List of parties should be empty", friend3.getNumParties()==0);
		assertTrue("List of parties should be empty", friend4.getNumParties()==0);
		
		//step 2
		homeRole.msgResendInvites();
		
		//postconditions
		assertTrue("PartyState shoud be resend invites. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.resendInvites);
		
		//step 3
		homeRole.msgHostParty();
		
		//postconditions
		assertTrue("PartyState shoud be host. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.host);
		
	}
	
	public void testThreeThrowPartyNoAttendees(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//preconditions
		assertTrue("Initial State shoud be doingNothing", homeRole.state==AgentState.DoingNothing);
		assertTrue("Size of friends list should be 4", myPerson.friends.size()==4);
		assertTrue("List of parties should be empty", myPerson.getNumParties()==0);
		assertTrue("List of parties should be empty", friend1.getNumParties()==0);
		assertTrue("List of parties should be empty", friend2.getNumParties()==0);
		assertTrue("List of parties should be empty", friend3.getNumParties()==0);
		assertTrue("List of parties should be empty", friend4.getNumParties()==0);
		
		//step 1
		homeRole.msgThrowParty();
		friend1.msgPartyInvitation(null, null, null);
		friend2.msgPartyInvitation(null, null, null);
		friend3.msgPartyInvitation(null, null, null);
		friend4.msgPartyInvitation(null, null, null);
		
		//postconditions
		assertTrue("List of parties should be empty", myPerson.getNumParties()==0);
		assertTrue("PartyState shoud be send invites. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.sendInvites);
		assertTrue("There should be one party in the list", friend1.getNumParties()==0);
		assertTrue("There should be one party in the list", friend2.getNumParties()==0);
		assertTrue("List of parties should be empty", friend3.getNumParties()==0);
		assertTrue("List of parties should be empty", friend4.getNumParties()==0);
		
	}
}
	
