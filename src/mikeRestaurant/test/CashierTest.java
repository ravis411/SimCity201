package mikeRestaurant.test;

import mikeRestaurant.CashierAgent;
import mikeRestaurant.test.mock.MockCustomer;
import mikeRestaurant.test.mock.MockMarket;
import mikeRestaurant.test.mock.MockWaiter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
  * 
  */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market;
	MockMarket market2;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent();		
		customer = new MockCustomer("mockcustomer");
		waiter = new MockWaiter("mockwaiter");
		market = new MockMarket("mockmarket");
		market2 = new MockMarket("mockmarket2");
	}	
	
	public static Test suite() {
	    return new TestSuite(CashierTest.class);
	}
	
	/**
	 * FIRST MANDATORY TEST
	 * 
	 * A single market requests the cashier to pay a bill, it is paid in full
	 */
	public void testOneOrderOneMarketScenario(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//check preconditions
		assertEquals("Cashier should have 0 payments in it. It doesn't.",cashier.pendingPayments.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's AskForPayment is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		assertEquals("CashierAgent should have an empty pendingPayments queue. It Doesn't.", cashier.pendingPayments.size(), 0);
		
		//make the message
		//cashier starts with 200.00 --> can pay for this
		cashier.msgAskForPayment("Steak", 3, 30.00, market);
		
		assertEquals("CashierAgent should have 1 payment in  pendingPayments queue. It Doesn't.", cashier.pendingPayments.size(), 1);
		assertEquals("CashierAgent should have an empty event log before the Cashier's scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		
		assertTrue("State Changed should have been called to allow state changed. It wasn't", cashier.stateChange.tryAcquire());
		
		//calls the scheduler
		assertTrue("Cashier scheduler should have returned true after being called once, but didn't", cashier.pickAndExecuteAnAction());
		
		//check the postconditions
		assertEquals("Cashier should have 0 payments in it after running the scheduler. It doesn't.", cashier.pendingPayments.size(), 0);
		assertTrue("Cashier should have logged an event for paying $30.00. It Didn't.", cashier.log.containsString("in amount of $30.00"));
		assertTrue("Cashier should contact mockmarket, it didn't.", cashier.log.containsString("Paying mockmarket"));
		assertTrue("Cashier should pay for 3 steaks. It didn't.", cashier.log.containsString("3xSteak"));
		
		assertEquals("Cashier should deduct its payment from its total amount of money, i.e. the new amount of cash should be $170.00", cashier.money, 170.00);
		
		//check the postconditions of messaging the market with a response
		assertTrue("MockMarket should have logged that the payment was approved. It instead reads: "+market.log.getLastLoggedEvent(), market.log.containsString("Payment Approved"));
		assertTrue("MockMarket should have logged that the payment receieved was in the amount of $30.00 for $30.00. It instead reads: "+market.log.getLastLoggedEvent(), market.log.containsString("Paid $30.00 for $30.00"));
		assertTrue("MockMarket should have logged that the order was for 3 steaks. It instead reads: "+market.log.getLastLoggedEvent(), market.log.containsString("Order: 3xSteak"));
		
		//afterwards
		assertFalse("Cashier scheduler should now return false with nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
	}
	
	/**
	 * Tests filling the Payment queue with 2 payments to be made by a Cashier to two distinct Markets.
	 * After two scheduler calls, the payments should be dealt with.
	 * 
	 * SECOND MANDATORY TEST
	 */
	public void testTwoOrderOneMarketScenario(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//check preconditions
		assertEquals("Cashier should have 0 payments in it. It doesn't.",cashier.pendingPayments.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's AskForPayment is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		assertEquals("CashierAgent should have an empty pendingPayments queue. It Doesn't.", cashier.pendingPayments.size(), 0);
		
		//make the message
		//cashier starts with 200.00 --> can pay for this
		cashier.msgAskForPayment("Steak", 3, 30.00, market);
		cashier.msgAskForPayment("Steak", 2, 20.00, market2);
		
		assertEquals("CashierAgent should have 2 payment in  pendingPayments queue. It Doesn't.", cashier.pendingPayments.size(), 2);
		assertEquals("CashierAgent should have an empty event log before the Cashier's scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		
		assertTrue("State Changed should have been called to allow state changed. It wasn't", cashier.stateChange.tryAcquire());
		
		//calls the scheduler
		assertTrue("Cashier scheduler should have returned true after being called once, but didn't", cashier.pickAndExecuteAnAction());
		
		//check the postconditions
		assertEquals("Cashier should have 1 payment in it after running the scheduler. It doesn't.", cashier.pendingPayments.size(), 1);
		assertTrue("Cashier should have logged an event for paying $30.00. It Didn't.", cashier.log.containsString("in amount of $30.00"));
		assertTrue("Cashier should contact mockmarket, it didn't.", cashier.log.containsString("Paying mockmarket"));
		assertTrue("Cashier should pay for 3 steaks. It didn't.", cashier.log.containsString("3xSteak"));
		
		assertEquals("Cashier should deduct its payment from its total amount of money, i.e. the new amount of cash should be $170.00", cashier.money, 170.00);
		
		//check the postconditions of messaging the market with a response
		assertTrue("MockMarket should have logged that the payment was approved. It instead reads: "+market.log.getLastLoggedEvent(), market.log.containsString("Payment Approved"));
		assertTrue("MockMarket should have logged that the payment receieved was in the amount of $30.00 for $30.00. It instead reads: "+market.log.getLastLoggedEvent(), market.log.containsString("Paid $30.00 for $30.00"));
		assertTrue("MockMarket should have logged that the order was for 3 steaks. It instead reads: "+market.log.getLastLoggedEvent(), market.log.containsString("Order: 3xSteak"));
		
		//preconditions for round 2
		
		assertEquals("Cashier should have 1 payments in it. It doesn't.",cashier.pendingPayments.size(), 1);		
		
		assertTrue("State Changed should have been called to allow state changed. It wasn't", cashier.stateChange.tryAcquire());
		
		//calls the scheduler
		assertTrue("Cashier scheduler should have returned true after being called a second time, but didn't", cashier.pickAndExecuteAnAction());
		
		//check the postconditions
		assertEquals("Cashier should have 0 payments in it after running the scheduler. It doesn't.", cashier.pendingPayments.size(), 0);
		assertTrue("Cashier should have logged an event for paying $20.00. It Didn't.", cashier.log.containsString("in amount of $20.00"));
		assertTrue("Cashier should contact mockmarket2, it didn't.", cashier.log.containsString("Paying mockmarket2"));
		assertTrue("Cashier should pay for 2 steaks. It didn't.", cashier.log.containsString("2xSteak"));
		
		assertEquals("Cashier should deduct its payment from its total amount of money, i.e. the new amount of cash should be $150.00", cashier.money, 150.00);
		
		//check the postconditions of messaging the market with a response
		assertTrue("MockMarket should have logged that the payment was approved. It instead reads: "+market2.log.getLastLoggedEvent(), market2.log.containsString("Payment Approved"));
		assertTrue("MockMarket should have logged that the payment receieved was in the amount of $20.00 for $20.00. It instead reads: "+market2.log.getLastLoggedEvent(), market2.log.containsString("Paid $20.00 for $20.00"));
		assertTrue("MockMarket should have logged that the order was for 2 steaks. It instead reads: "+market2.log.getLastLoggedEvent(), market2.log.containsString("Order: 2xSteak"));
		
		//afterwards
		assertFalse("Cashier scheduler should now return false with nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
	}
	
	//-------------------------------MY TESTS-----------------------------//
	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testThreeNormalCustomerScenario()
	{
		//setUp() runs first before this test!	
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//check preconditions
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.pendingTransactions.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//make the message
		cashier.msgHereIsPayment(4.52, 4.52, customer);
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 transaction in it. It doesn't.", cashier.pendingTransactions.size(), 1);
		assertTrue("State Changed should have been called to allow state changed. It wasn't", cashier.stateChange.tryAcquire());
		
		//cue scheduler
		assertTrue("Cashier scheduler should have returned true after being called once, but didn't", cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 1 / preconditions for step 1
		
		assertEquals("Cashier should have 0 transactions in it after running the scheduler. It doesn't.", cashier.pendingTransactions.size(), 0);
		
		assertTrue("Cashier should have logged \"Received Payment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received Payment"));

		assertTrue("Cashier should have logged \"Transaction Bill = $4.52\" but didn't. His log reads instead: " + cashier.log.getLastLoggedEvent(),
				cashier.log.containsString("Transaction Bill = $4.52"));
		
		assertTrue("Cashier should have logged \"Paid Amount = $4.52\" but didn't. His log reads instead: " + cashier.log.getLastLoggedEvent(),
				cashier.log.containsString("Paid Amount = $4.52"));
		
		assertTrue("Cashier should have a bill with the right customer in it. It doesn't.", 
					cashier.log.containsString("Customer = mockcustomer"));
		
		//check the customer messages
		assertTrue("MockCusomter should have logged an event for an approved transaction. It Didn't.", customer.log.containsString("Payment approved"));
		assertTrue("MockCustomer should have logged an event with $0.00 change. It logged: "+customer.log.getLastLoggedEvent(), customer.log.containsString("Change Due: $0.00"));
		
		//afterwards
		assertFalse("Cashier scheduler should now return false with nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
	
	}//end one normal customer scenario
	
	
	/**
	 * This tests the cashier: one customer is ready to pay the bill with extra money.
	 */
	public void testFourNormalCustomerScenario()
	{
		//setUp() runs first before this test!	
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//check preconditions
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.pendingTransactions.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//make the message
		cashier.msgHereIsPayment(4.52, 6.00, customer);
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 transaction in it. It doesn't.", cashier.pendingTransactions.size(), 1);
		assertTrue("State Changed should have been called to allow state changed. It wasn't", cashier.stateChange.tryAcquire());
		
		//cue scheduler
		assertTrue("Cashier scheduler should have returned true after being called once, but didn't", cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 1 / preconditions for step 1
		
		assertEquals("Cashier should have 0 transactions in it after running the scheduler. It doesn't.", cashier.pendingTransactions.size(), 0);
		
		assertTrue("Cashier should have logged \"Received Payment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received Payment"));

		assertTrue("Cashier should have logged \"Transaction Bill = $4.52\" but didn't. His log reads instead: " + cashier.log.getLastLoggedEvent(),
				cashier.log.containsString("Transaction Bill = $4.52"));
		
		assertTrue("Cashier should have logged \"Paid Amount = $6.00\" but didn't. His log reads instead: " + cashier.log.getLastLoggedEvent(),
				cashier.log.containsString("Paid Amount = $6.00"));
		
		assertTrue("Cashier should have a bill with the right customer in it. It doesn't.", 
					cashier.log.containsString("Customer = mockcustomer"));
		
		//check the customer messages
		assertTrue("MockCusomter should have logged an event for an approved transaction. It Didn't.", customer.log.containsString("Payment approved"));
		assertTrue("MockCustomer should have logged an event with $1.48 change. It logged: "+customer.log.getLastLoggedEvent(), customer.log.containsString("Change Due: $1.48"));
		
		//afterwards
		assertFalse("Cashier scheduler should now return false with nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
	
	}
	
	/**
	 * Tests a request of the Cashier to compute a bill and let the Waiter know how much it is
	 */ 
	public void testFiveNormalWaiterScenario(){
		//setUp() runs first before this test!	
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//check preconditions
		assertEquals("Cashier should have 0 bills in its cue. It doesn't.",cashier.pendingBills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's ComputeBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//make the message
		cashier.msgComputeBill("Steak", customer, waiter);
		
		assertEquals("CashierAgent should have an empty event log before the Cashier's scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("Cashier should have 1 bill in its cue. It doesn't.",cashier.pendingBills.size(), 1);		
		assertTrue("State Changed should have been called to allow state changed. It wasn't", cashier.stateChange.tryAcquire());
		
		//cue the scheduler
		assertTrue("Cashier scheduler should have returned true after being called once, but didn't", cashier.pickAndExecuteAnAction());
	
		
		//post conditions of following actions
		assertEquals("Cashier bill queue should have been emptied by the scheduler call -> pendingBills.poll() should have been called.", cashier.pendingBills.size(), 0);
		
		assertTrue("Cashier should have logged an event that we're delivering a check for mockcustomer. The log instead reads: "+cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Delivering check for mockcustomer"));
		assertTrue("Cashier should have logged an event that we're delivering a check to mockwaiter. The log instead reads: "+cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("to waiter mockwaiter"));
	
		//post conditions of further message calls to waiter
		assertTrue("MockWaiter should have logged an event that he received a check for $15.99. He didnt, his log reads instead: "+waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("$15.99"));
		assertTrue("MockWaiter should have logged an event that he received a check for mockcustomer. He didn't his log reads instead: "+waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("mockcustomer"));
		
		//afterwards
		assertFalse("Cashier scheduler should now return false with nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
	}
	
	/**
	 * Tests the ability of the scheduler to choose the right priority of tasks when dealing with a request for a bill from a waiter
	 * and a request for a payment from a market
	 */
	public void testSixNormalWaiterScenario(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//check preconditions
		assertEquals("CashierAgent should have an empty event log before the Cashier's AskForPayment is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());	
		assertEquals("Cashier should have 0 bills in its cue. It doesn't.",cashier.pendingBills.size(), 0);		
		assertEquals("CashierAgent should have an empty pendingPayments queue. It Doesn't.", cashier.pendingPayments.size(), 0);
		
		//make the message for bill request
		cashier.msgComputeBill("Chicken", customer, waiter);
		
		//priority dictates that the second message should come first -- see CashierAgent scheduler
		
		//make the message for payment request
		//cashier starts with 200.00 --> can pay for this
		cashier.msgAskForPayment("Salad", 2, 2.00, market);
		
		assertEquals("CashierAgent should have 1 payment in  pendingPayments queue. It Doesn't.", cashier.pendingPayments.size(), 1);
		assertEquals("CashierAgent should have an empty event log before the Cashier's scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("Cashier should have 1 bill in its bill queue. It doesn't.",cashier.pendingBills.size(), 1);		
		assertTrue("State Changed should have been called to allow state changed. It wasn't", cashier.stateChange.tryAcquire());
		
		//calls the scheduler
		assertTrue("Cashier scheduler should have returned true after being called once, but didn't", cashier.pickAndExecuteAnAction());
		
		//check the postconditions of round 1
		assertEquals("Cashier should have 0 payments in it after running the scheduler. It doesn't.", cashier.pendingPayments.size(), 0);
		assertTrue("Cashier should have logged an event for paying $2.00. It Didn't.", cashier.log.containsString("in amount of $2.00"));
		assertTrue("Cashier should contact mockmarket, it didn't.", cashier.log.containsString("Paying mockmarket"));
		assertTrue("Cashier should pay for 2 salads. It didn't.", cashier.log.containsString("2xSalad"));
		
		assertEquals("Cashier should deduct its payment from its total amount of money, i.e. the new amount of cash should be $198.00", cashier.money, 198.00);
		
		//check the postconditions of messaging the market with a response
		assertTrue("MockMarket should have logged that the payment was approved. It instead reads: "+market.log.getLastLoggedEvent(), market.log.containsString("Payment Approved"));
		assertTrue("MockMarket should have logged that the payment receieved was in the amount of $2.00 for $2.00. It instead reads: "+market.log.getLastLoggedEvent(), market.log.containsString("Paid $2.00 for $2.00"));
		assertTrue("MockMarket should have logged that the order was for 2 salads. It instead reads: "+market.log.getLastLoggedEvent(), market.log.containsString("Order: 2xSalad"));
		assertEquals("MockWaiter should still have an empty log because it hasn't been touched yet. It isn't empty.", waiter.log.size(), 0);
		
		//round 2
		
		//state changed was called twice, via 2 messages
		assertTrue("State Changed should have been called to allow state changed. It wasn't", cashier.stateChange.tryAcquire());
		//cue the scheduler
		assertTrue("Cashier scheduler should have returned true after being called once, but didn't", cashier.pickAndExecuteAnAction());

		//post conditions of following actions
		assertEquals("Cashier bill queue should have been emptied by the scheduler call -> pendingBills.poll() should have been called.", cashier.pendingBills.size(), 0);
		
		assertTrue("Cashier should have logged an event that we're delivering a check for mockcustomer. The log instead reads: "+cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Delivering check for mockcustomer"));
		assertTrue("Cashier should have logged an event that we're delivering a check to mockwaiter. The log instead reads: "+cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("to waiter mockwaiter"));
	
		//post conditions of further message calls to waiter
		assertTrue("MockWaiter should have logged an event that he received a check for $10.99. He didnt, his log reads instead: "+waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("$10.99"));
		assertTrue("MockWaiter should have logged an event that he received a check for mockcustomer. He didn't his log reads instead: "+waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("mockcustomer"));
		
		//afterwards
		assertFalse("Cashier scheduler should now return false with nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
	}

}