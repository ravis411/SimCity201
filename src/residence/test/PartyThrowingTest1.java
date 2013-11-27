package residence.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import residence.HomeRole;
import residence.HomeRole.AgentEvent;
import residence.HomeRole.AgentState;
import residence.gui.HomeRoleGui;
import residence.test.mock.MockApartmentManager;
import residence.test.mock.MockHome;
import trace.AlertLog;
import Person.PersonAgent;
import Person.test.mock.MockRole;

public class PartyThrowingTest1 extends TestCase {
	
	private PersonAgent myPerson;
	private MockApartmentManager manager;
	private MockHome home;
	private HomeRole homeRole;
	private HomeRoleGui gui;
	
	public static Test suite() {
	    return new TestSuite(PartyThrowingTest1.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		
		manager=new MockApartmentManager("Mock Manager");
		home= new MockHome("Mock Home");
		myPerson= new PersonAgent("PersonAgent",null);
		homeRole= new HomeRole(myPerson);
		homeRole.setLandlord(manager);
		manager.homeRole=homeRole;
		
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
		assertTrue("Size of inventory should be 2",homeRole.getInventory().size()==2);
		assertTrue("Size of features list should be 1",homeRole.getHomeFeatures().size()==1);
		assertTrue("First item of inventory should be Cooking Ingredient and it should have 2 of those",homeRole.getInventory().get(0).name.equals("Cooking Ingredient"));
		assertTrue("First item of inventory should be Cooking Ingredient and it should have 2 of those",homeRole.getInventory().get(0).quantity==2);
		assertTrue("First item of inventory should be Cleaning supply and it should have 2 of those",homeRole.getInventory().get(1).name.equals("Cleaning supply"));
		assertTrue("First item of inventory should be Cleaning supply and it should have 2 of those",homeRole.getInventory().get(1).quantity==2);
		assertTrue("First item of feature list should be sink and it should be working",homeRole.getHomeFeatures().get(0).name.equals("Sink"));
		assertTrue("First item of feature list should be sink and it should be working",homeRole.getHomeFeatures().get(0).working==true);
		
		
		
		
	}
	//-------------------MESSAGE CHECKING--------------------------//
	public void testHomeRoleMessages(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("TEST FOR MESSAGES AND STATECHANGE OPERATIONS");
		
		assertTrue("The initial event state should be none",homeRole.event==AgentEvent.none);
		homeRole.msgEnterBuilding();
		assertTrue("Now the event should be none",homeRole.event==AgentEvent.none);
		assertTrue("Now the state should be Cooking",homeRole.enterHome==true);
		homeRole.enterHome=false;
		homeRole.msgRentDue(100);
		
		assertTrue("Amount of rentOwed should be 100",homeRole.getRentOwed()==100);
		homeRole.pickAndExecuteAction();
		assertTrue("Amount of rentOwed now should be 0",homeRole.getRentOwed()==0);
		homeRole.msgTired();
		assertTrue("Now the state should be Sleeping",homeRole.state==AgentState.Sleeping);
		homeRole.msgMakeFood();
		assertTrue("Now the state should be Cooking",homeRole.state==AgentState.Cooking);
		homeRole.msgRestockItem ("Cleaning supply", 2);
		homeRole.msgRestockItem("Cooking Ingredient",2);
		assertTrue("Size of inventory should still be 2",homeRole.getInventory().size()==2);
		assertTrue("First item of inventory should be Cooking Ingredient and it should have 4 of those",homeRole.getInventory().get(0).name.equals("Cooking Ingredient"));
		assertTrue("First item of inventory should be Cooking Ingredient and it should have 4 of those",homeRole.getInventory().get(0).quantity==4);
		assertTrue("First item of inventory should be Cleaning supply and it should have 4 of those",homeRole.getInventory().get(1).name.equals("Cleaning supply"));
		assertTrue("First item of inventory should be Cleaning supply and it should have 4 of those",homeRole.getInventory().get(1).quantity==4);
		//Setting the sink functionality to false then going to test maintenance
		homeRole.getHomeFeatures().get(0).working=false ;//doesnt work now
	    manager.msgBrokenFeature(homeRole.getHomeFeatures().get(0).name, home);
		assertTrue("First item of feature list should be sink and it should be working after repair",homeRole.getHomeFeatures().get(0).working==true);
		homeRole.msgLeaveBuilding();
		assertTrue("Now the event should be none",homeRole.event==AgentEvent.leaving);
		assertTrue("Now the leaveBuilding boolean should be true",homeRole.leaveHome==true);
		assertTrue("Now the enterHome boolean should be false",homeRole.enterHome==false);
		
		
		
		
		
		
		
		
		
		
		
	}
}
	