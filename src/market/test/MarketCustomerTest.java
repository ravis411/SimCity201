package market.test;

import junit.framework.TestCase;
import market.gui.MarketCustomerGui;
import market.gui.MarketGui;
import market.test.mock.MockMarketEmployee;
import MarketEmployee.MarketCustomerRole;
import Person.PersonAgent;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class MarketCustomerTest extends TestCase
{
	
	//these are instantiated for each test separately via the setUp() method.
	MarketCustomerRole customer;
	MockMarketEmployee employee;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		customer = new MarketCustomerRole();
		customerPerson= new MockPerson("fd");
		customer.setPerson(customerPerson);
	
		employee = new MockMarketEmployee("employee");
		customer.marketEmployee=employee;
		MarketGui gui= new MarketGui();
		customer.gui=new MarketCustomerGui(customer, 1);
		gui.animationPanel.addGui(customer.gui);
		gui.setVisible(true);
        gui.setResizable(false);
		//PersonAgent customerPerson =new PersonAgent("Bob");
	//	customer.setPerson(customerPerson);
		//customerPerson.startThread();
		
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenarioWithFoodInstock()
	{
		//setUp() runs first before this test!
		
		
		
		assertEquals(
				"customer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertTrue("Customer's scheduler should have returned true (goToMarketEmployeeToOrder action should be done).", customer.pickAndExecuteAction());
		assertTrue("customer should have logged \"Recieved Entered Market" +"\" but didn't. His log reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Entered Market"));
		assertTrue("Employee should have logged \"Recieved msgMarketEmployeetTellMeWhenICanGiveOrder" +"\" but didn't. His log reads instead: " 
				+ employee.log.getLastLoggedEvent().toString(), employee.log.containsString("msgMarketEmployeetTellMeWhenICanGiveOrder"));
		customer.msgMarketCustomerReadyToTakeOrder();
		assertTrue("Customer's scheduler should have returned true (giveEmployeeOrder action should be done).", customer.pickAndExecuteAction());
		assertTrue("Employee should have logged \"Recieved msgMarketEmployeeOrder" +"\" but didn't. His log reads instead: " 
				+ employee.log.getLastLoggedEvent().toString(), employee.log.containsString("msgMarketEmployeeOrder"));
		//at this point the employee has recieved an order.
		customer.msgMarketCustomerHereIsOrder("Burger",1);
		assertTrue("Customer's scheduler should have returned true (payAndLeaveMarket action should be done).", customer.pickAndExecuteAction());
		assertTrue("Customer's state should now equal none after leaving", customer.state==MarketCustomerRole.MarketCustomerState.none);
		assertTrue("Employee should have logged \"Recieved msgMarketEmployeePayment" +"\" but didn't. His log reads instead: " 
				+ employee.log.getLastLoggedEvent().toString(), employee.log.containsString("msgMarketEmployeePayment"));
		
			
	}//end one normal customer scenario
	
	public void testTwoCustomerScenarioWithPartialOrderInstockAndPartialOrderNOTAcceptable()
	{
		//setUp() runs first before this test!
		customer.willTakePartialOrder=false;
		
		
		assertEquals(
				"customer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertTrue("Customer's scheduler should have returned true (goToMarketEmployeeToOrder action should be done).", customer.pickAndExecuteAction());
		assertTrue("customer should have logged \"Recieved Entered Market" +"\" but didn't. His log reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Entered Market"));
		assertTrue("Employee should have logged \"Recieved msgMarketEmployeetTellMeWhenICanGiveOrder" +"\" but didn't. His log reads instead: " 
				+ employee.log.getLastLoggedEvent().toString(), employee.log.containsString("msgMarketEmployeetTellMeWhenICanGiveOrder"));
		customer.msgMarketCustomerReadyToTakeOrder();
		assertTrue("Customer's scheduler should have returned true (giveEmployeeOrder action should be done).", customer.pickAndExecuteAction());
		assertTrue("Employee should have logged \"Recieved msgMarketEmployeeOrder" +"\" but didn't. His log reads instead: " 
				+ employee.log.getLastLoggedEvent().toString(), employee.log.containsString("msgMarketEmployeeOrder"));
		//at this point the employee has recieved an order.
		customer.msgMarketCustomerDoYouWantPartialOrder("Burger",1);
		
		assertTrue("Customer's scheduler should have returned true (tellMarketEmployeeIfPartialOrderAcceptable action should be done).", customer.pickAndExecuteAction());
		assertTrue("Customer's state should now equal none after leaving", customer.state==MarketCustomerRole.MarketCustomerState.none);
	
			
	}//end one normal customer scenario
	public void testTwoCustomerScenarioWithPartialOrderInstockAndPartialOrderISAcceptable()
	{
		//setUp() runs first before this test!
		customer.willTakePartialOrder=true;
		
		
		assertEquals(
				"customer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertTrue("Customer's scheduler should have returned true (goToMarketEmployeeToOrder action should be done).", customer.pickAndExecuteAction());
		assertTrue("customer should have logged \"Recieved Entered Market" +"\" but didn't. His log reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Entered Market"));
		assertTrue("Employee should have logged \"Recieved msgMarketEmployeetTellMeWhenICanGiveOrder" +"\" but didn't. His log reads instead: " 
				+ employee.log.getLastLoggedEvent().toString(), employee.log.containsString("msgMarketEmployeetTellMeWhenICanGiveOrder"));
		customer.msgMarketCustomerReadyToTakeOrder();
		assertTrue("Customer's scheduler should have returned true (giveEmployeeOrder action should be done).", customer.pickAndExecuteAction());
		assertTrue("Employee should have logged \"Recieved msgMarketEmployeeOrder" +"\" but didn't. His log reads instead: " 
				+ employee.log.getLastLoggedEvent().toString(), employee.log.containsString("msgMarketEmployeeOrder"));
		//at this point the employee has recieved an order.
		customer.msgMarketCustomerDoYouWantPartialOrder("Burger",1);
		
		assertTrue("Customer's scheduler should have returned true (tellMarketEmployeeIfPartialOrderAcceptable action should be done).", customer.pickAndExecuteAction());
		assertTrue("Employee should have logged \"Recieved msgMarketEmployeeConfirmPartialOrder" +"\" but didn't. His log reads instead: " 
				+ employee.log.getLastLoggedEvent().toString(), employee.log.containsString("msgMarketEmployeeConfirmPartialOrder"));
		customer.msgMarketCustomerHereIsOrder("Burger",1);
		assertTrue("Customer's scheduler should have returned true (payAndLeaveMarket action should be done).", customer.pickAndExecuteAction());
		assertTrue("Customer's state should now equal none after leaving", customer.state==MarketCustomerRole.MarketCustomerState.none);
		assertTrue("Employee should have logged \"Recieved msgMarketEmployeePayment" +"\" but didn't. His log reads instead: " 
				+ employee.log.getLastLoggedEvent().toString(), employee.log.containsString("msgMarketEmployeePayment"));
		
			
	}//end one normal customer scenario
	
	
	
}
