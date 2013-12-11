package residence.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import residence.HomeRole;
import residence.HomeRole.AgentEvent;
import residence.HomeRole.AgentState;
import residence.HomeRole.PartyState;
import Person.PersonAgent;

/**
 * 
 * JUnit test for HomeRole
 *
 */

public class HomeRoleTest extends TestCase {
	
	private PersonAgent myPerson;
	private HomeRole homeRole;
	
	public static Test suite() {
	    return new TestSuite(HomeRoleTest.class);
	}
	
	public void setUp() throws Exception{
		super.setUp();

		myPerson= new PersonAgent("PersonAgent",null);
		homeRole= new HomeRole(myPerson);
		
		}
	
	 public void testOnePayRent() throws InterruptedException {
		 try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 //preconditions
		 assertTrue("Initial State shoud be doingNothing. Instead, state is " + homeRole.state, homeRole.state==AgentState.DoingNothing);
		 assertTrue("No rent should be owed. There is currently " + homeRole.getRentOwed() + " owed.", homeRole.getRentOwed() == 0.00);
		 
		 //step 1
		 homeRole.msgRentDue(5.00, 1);
		 
		 //postconditions
		 assertTrue("Rent owed should be 5.00. It is currently " + homeRole.getRentOwed() + " owed.", homeRole.getRentOwed() == 5.00);
		 
		 
	 }
	 
	 public void testTwoRestockItemScenario() throws InterruptedException {
		 try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 //preconditions
		 assertTrue("Initial State shoud be doingNothing. Instead, state is " + homeRole.state, homeRole.state==AgentState.DoingNothing);
		 assertTrue("Size of inventory should be 3. Instead, size is " + homeRole.getInventory().size(), homeRole.getInventory().size()==3);
		 assertTrue("First item of inventory should be Steak. Instead, is it " + homeRole.getInventory().get(0).name, homeRole.getInventory().get(0).name.equals("Steak"));
		 assertTrue("Quantity of Steak should be 2. Instead, it is " + homeRole.getInventory().get(0).quantity, homeRole.getInventory().get(0).quantity==2);
		 assertTrue("Second item of inventory should be Chicken. Instead, it is " + homeRole.getInventory().get(1).name,homeRole.getInventory().get(1).name.equals("Chicken"));
		 assertTrue("Quantity of Chicken should be 2. Instead, it is " + homeRole.getInventory().get(1).quantity, homeRole.getInventory().get(1).quantity==2);
		 assertTrue("Second item of inventory should be Burger. Instead, it is " + homeRole.getInventory().get(2).name, homeRole.getInventory().get(2).name.equals("Burger"));
		 assertTrue("Quantity of Burger should be 2. Instead, it is " + homeRole.getInventory().get(2).quantity, homeRole.getInventory().get(2).quantity==2);
		 
		 //step 1
		 homeRole.msgRestockItem("Steak", 1);
		 homeRole.msgRestockItem("Chicken", 2);
		 homeRole.msgRestockItem("Burger", 3);
		 
		 //postconditions
		 assertTrue("Size of inventory should be 3. Instead, size is " + homeRole.getInventory().size(), homeRole.getInventory().size()==3);
		 assertTrue("First item of inventory should be Steak. Instead, is it " + homeRole.getInventory().get(0).name, homeRole.getInventory().get(0).name.equals("Steak"));
		 assertTrue("Quantity of Steak should be 3. Instead, it is " + homeRole.getInventory().get(0).quantity, homeRole.getInventory().get(0).quantity==3);
		 assertTrue("Second item of inventory should be Chicken. Instead, it is " + homeRole.getInventory().get(1).name,homeRole.getInventory().get(1).name.equals("Chicken"));
		 assertTrue("Quantity of Chicken should be 4. Instead, it is " + homeRole.getInventory().get(1).quantity, homeRole.getInventory().get(1).quantity==4);
		 assertTrue("Second item of inventory should be Burger. Instead, it is " + homeRole.getInventory().get(2).name, homeRole.getInventory().get(2).name.equals("Burger"));
		 assertTrue("Quantity of Burger should be 5. Instead, it is " + homeRole.getInventory().get(2).quantity, homeRole.getInventory().get(2).quantity==5);
	 }
	 
	 public void testThreeBrokenFeatureScenario() throws InterruptedException {
		 try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 //preconditions
		 assertTrue("Initial State shoud be doingNothing. Instead, state is " + homeRole.state, homeRole.state==AgentState.DoingNothing);
		 assertTrue("Size of features list should be 3. Instead, size is " + homeRole.getHomeFeatures().size(), homeRole.getHomeFeatures().size()==3);	 
		 assertTrue("First item of feature list should be Sink. Instead, it is " + homeRole.getHomeFeatures().get(0).name, homeRole.getHomeFeatures().get(0).name.equals("Sink"));
		 assertTrue("Sink should be working. It is currently not working.", homeRole.getHomeFeatures().get(0).working==true);
		 assertTrue("There should be no work order filed to fix Sink. There currently is.", homeRole.getHomeFeatures().get(0).workOrderFiled==false);
		 assertTrue("Second item of feature list should be Stove. Instead, it is " + homeRole.getHomeFeatures().get(1).name, homeRole.getHomeFeatures().get(1).name.equals("Stove"));
		 assertTrue("Stove should be working. It is currently not working.", homeRole.getHomeFeatures().get(1).working==true);
		 assertTrue("There should be no work order filed to fix Stove. There currently is.", homeRole.getHomeFeatures().get(1).workOrderFiled==false);
		 assertTrue("Third item of feature list should be Refrigerator. Instead, it is " + homeRole.getHomeFeatures().get(2).name, homeRole.getHomeFeatures().get(2).name.equals("Refrigerator"));
		 assertTrue("Refrigerator should be working. It is currently not working.", homeRole.getHomeFeatures().get(2).working==true);
		 assertTrue("There should be no work order filed to fix Refrigerator. There currently is.", homeRole.getHomeFeatures().get(2).workOrderFiled==false);
		 
		 //step 1
		 homeRole.getHomeFeatures().get(0).working = false;
		 homeRole.getHomeFeatures().get(0).workOrderFiled = true;
		 
		 //postconditions for step 1, preconditions for step 2
		 assertTrue("First item of feature list should be Sink. Instead, it is " + homeRole.getHomeFeatures().get(0).name, homeRole.getHomeFeatures().get(0).name.equals("Sink"));
		 assertTrue("Sink should not be working. It currently is working.", homeRole.getHomeFeatures().get(0).working==false);
		 assertTrue("There should be a work order filed to fix Sink. There currently is not.", homeRole.getHomeFeatures().get(0).workOrderFiled==true);
		 
		 //step 2
		 homeRole.msgFixedFeature();
		 
		 //postconditions for step 2
		 assertTrue("First item of feature list should be Sink. Instead, it is " + homeRole.getHomeFeatures().get(0).name, homeRole.getHomeFeatures().get(0).name.equals("Sink"));
		 assertTrue("Sink should be working. It is currently not working.", homeRole.getHomeFeatures().get(0).working==true);
		 assertTrue("There should not be work order filed to fix Sink. There currently is.", homeRole.getHomeFeatures().get(0).workOrderFiled==false);
		 
	 }
	 
	 public void testFourStateChanges() throws InterruptedException {
		 try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 //preconditions
		 assertTrue("Initial State shoud be doingNothing. Instead, state is " + homeRole.state, homeRole.state==AgentState.DoingNothing);
		 assertTrue("Initial EventState shoud be none. Instead, state is " + homeRole.event, homeRole.event==AgentEvent.none);
		 assertTrue("Initial PartyState shoud be none. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.none);
		 
		 //step 1
		 homeRole.msgTired();
		 assertTrue("State shoud be Sleeping. Instead, state is " + homeRole.state, homeRole.state==AgentState.Sleeping);
		 //step 2
		 homeRole.msgMakeFood();
		 assertTrue("State shoud be Cooking. Instead, state is " + homeRole.state, homeRole.state==AgentState.Cooking);
		 //step 3
		 homeRole.msgLeaveBuilding();
		 assertTrue("EventState shoud be leaving. Instead, state is " + homeRole.event, homeRole.event==AgentEvent.leaving);
		 //step 4
		 homeRole.msgEnterBuilding();
		 assertTrue("EventState shoud be none. Instead, state is " + homeRole.event, homeRole.event==AgentEvent.none);
		 //step 5
		 homeRole.msgThrowParty();
		 assertTrue("PartyState shoud be send invites. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.sendInvites);
		 //step 6
		 homeRole.msgResendInvites();
		 assertTrue("PartyState shoud be resend invites. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.resendInvites);
		 //step 7
		 homeRole.msgHostParty();
		 assertTrue("PartyState shoud be host. Instead, state is " + homeRole.partyState, homeRole.partyState==PartyState.host);
	 }
	 
}

