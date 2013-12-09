//package ryansRestaurant.test;
//
//
//import junit.framework.TestCase;
//import ryansRestaurant.RyansCashierRole;
//import ryansRestaurant.test.mock.MockCustomer;
//import ryansRestaurant.test.mock.MockMarket;
//import ryansRestaurant.test.mock.MockWaiter;
//
//
//public class CashierTest extends TestCase
//{
//	//these are instantiated for each test separately via the setUp() method.
//	RyansCashierRole cashier;
//	MockWaiter waiter;
//	MockCustomer customer;
//	MockMarket market1;
//	MockMarket market2;
//	
//	
//	/**
//	 * This method is run before each test. You can use it to instantiate the class variables
//	 * for your agent and mocks, etc.
//	 */
//	public void setUp() throws Exception{
//		super.setUp();		
//		cashier = new RyansCashierRole("cashier", "HERE");		
//		customer = new MockCustomer("mockcustomer");		
//		waiter = new MockWaiter("mockwaiter");
//		market1 = new MockMarket("market1");
//		market2 = new MockMarket("market2");
//	}	
//	
//	
//	/**
//	 * Test cashier paying two markets. RyansCashier only has $10 to start. RyansMarket bills total $25
//	 */
//	public void testFiveNormalTwoMarketScenario()
//	{
//		//setUp() runs first before this test!
//		
//				
//		//check preconditions
//		assertEquals("RyansCashier should have 10 dollars in it. It doesn't.", cashier.getMoney(), 10.0);		
//		assertEquals("RyansCashierRole should have an empty event log before the RyansCashier's HereIsBill is called. Instead, the RyansCashier's event log reads: "
//						+ cashier.log.toString(), 0, cashier.log.size());
//		
//		cashier.msgMarketBill(market1, 20);
//		
//		assertTrue("RyansCashier should have logged \"Received msgMarketBill market1 total = 10\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgMarketBill market1 total = 20"));
//		
//		cashier.msgMarketBill(market2, 5);
//		
//		assertTrue("RyansCashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//		
//		assertTrue("RyansCashier should have logged \"Scheduler called payMarketBill market1\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called payMarketBill market1"));
//		
//		assertTrue("RyansCashier should have logged \"Bill paid in full.\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Some of bill paid."));
//		
//		
//		
//		///////////		
//		assertTrue("RyansMarket should have logged \"Recieved msgHereIsPayment total =10\" but didn't. His log reads instead: " 
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgHereIsPayment total =10.0"));
//		
//		assertEquals(0.0, cashier.getMoney());
//		
//		assertFalse(cashier.pickAndExecuteAnAction());
//		///////////
//		
//				
//		//give cashier some more money
//		cashier.setMoney(30);
//		
//		assertTrue("RyansCashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//		
//		assertTrue("RyansCashier should have logged \"Scheduler called payMarketBill market1\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called payMarketBill market1"));
//		
//		assertTrue("RyansCashier should have logged \"Bill paid in full.\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Bill paid in full."));
//		
//		///////////		
//		assertTrue("RyansMarket should have logged \"Recieved msgHereIsPayment total =10\" but didn't. His log reads instead: " 
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgHereIsPayment total =10.0"));
//		
//		assertEquals(20.0, cashier.getMoney());
//		
//		//////////
//		assertTrue("RyansCashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//		
//		assertTrue("RyansCashier should have logged \"Scheduler called payMarketBill market2\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called payMarketBill market2"));
//		
//		assertTrue("RyansCashier should have logged \"Bill paid in full.\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Bill paid in full."));
//		
//		
//		
//		assertTrue("RyansMarket should have logged \"Recieved msgHereIsPayment total =5\" but didn't. His log reads instead: " 
//				+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Recieved msgHereIsPayment total =5.0"));
//		
//		assertEquals(15., cashier.getMoney());
//		
//		assertFalse(cashier.pickAndExecuteAnAction());
//		
//		
//	}//end five two markets norm-nonnorm
//	
//	
//	/**This tests normal market cashier interactions
//	 * 		one market. RyansCashier has enough money pays market.
//	 */
//	public void testTwoNormalOneMarketScenario()
//	{
//		//setUp() runs first before this test!
//		
//				
//		//check preconditions
//		assertEquals("RyansCashier should have 10 dollars in it. It doesn't.", cashier.getMoney(), 10.0);		
//		assertEquals("RyansCashierRole should have an empty event log before the RyansCashier's HereIsBill is called. Instead, the RyansCashier's event log reads: "
//						+ cashier.log.toString(), 0, cashier.log.size());
//		
//		cashier.msgMarketBill(market1, 5);
//		
//		assertTrue("RyansCashier should have logged \"Received msgMarketBill market1 total = 5\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgMarketBill market1 total = 5"));
//		
//		
//		assertTrue("RyansCashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//		
//		assertTrue("RyansCashier should have logged \"Scheduler called payMarketBill market1\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called payMarketBill market1"));
//		
//		assertTrue("RyansCashier should have logged \"Bill paid in full.\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Bill paid in full."));
//		
//		
//		
//		assertTrue("RyansMarket should have logged \"Recieved msgHereIsPayment total =5\" but didn't. His log reads instead: " 
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgHereIsPayment total =5"));
//		
//		assertEquals(5.0, cashier.getMoney());
//		
//		assertFalse(cashier.pickAndExecuteAnAction());
//		
//		
//		assertTrue(true);
//	}//end two normal market scenario
//	
//	
//	/**This tests non-normal market cashier interactions
//	 * 		one market. RyansCashier does NOT have enough money to pay market.
//	 */
//	public void testThreeNonNormalOneMarketScenario()
//	{
//		//setUp() runs first before this test!
//		
//				
//		//check preconditions
//		assertEquals("RyansCashier should have 10 dollars in it. It doesn't.", cashier.getMoney(), 10.0);		
//		assertEquals("RyansCashierRole should have an empty event log before the RyansCashier's HereIsBill is called. Instead, the RyansCashier's event log reads: "
//						+ cashier.log.toString(), 0, cashier.log.size());
//		
//		cashier.msgMarketBill(market1, 15);
//		
//		assertTrue("RyansCashier should have logged \"Received msgMarketBill market1 total = 15\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgMarketBill market1 total = 15"));
//		
//		
//		assertTrue("RyansCashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//		
//		assertTrue("RyansCashier should have logged \"Scheduler called payMarketBill market1\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called payMarketBill market1"));
//		
//		assertTrue("RyansCashier should have logged \"Bill paid in full.\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Some of bill paid."));
//		
//		
//		
//		assertTrue("RyansMarket should have logged \"Recieved msgHereIsPayment total =10\" but didn't. His log reads instead: " 
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgHereIsPayment total =10.0"));
//		
//		assertEquals(0.0, cashier.getMoney());
//		
//		assertFalse(cashier.pickAndExecuteAnAction());
//		
//		
//		assertTrue(true);
//	}//end two normal market scenario
//	
//	
//	/**This tests non-normal market cashier interactions
//	 * 		one market. RyansCashier does NOT have any money.
//	 */
//	public void testFourNonNormalOneMarketScenario()
//	{
//		//setUp() runs first before this test!
//		
//		cashier.setMoney(0.0);
//		//check preconditions
//		assertEquals("RyansCashier should have 10 dollars in it. It doesn't.", cashier.getMoney(), 0.0);		
//		assertEquals("RyansCashierRole should have an empty event log before the RyansCashier's HereIsBill is called. Instead, the RyansCashier's event log reads: "
//						+ cashier.log.toString(), 0, cashier.log.size());
//		
//		cashier.msgMarketBill(market1, 15);
//		
//		assertTrue("RyansCashier should have logged \"Received msgMarketBill market1 total = 15\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgMarketBill market1 total = 15"));
//		
//		
//		assertFalse("RyansCashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//		
//				
//		assertEquals("RyansMarket should have no logs. His log reads instead: " 
//				+ market1.log, 0, market1.log.size());
//		
//		assertEquals(0.0, cashier.getMoney());
//		
//		assertFalse(cashier.pickAndExecuteAnAction());
//	}//end two normal market scenario
//	
//	
//	
//	/**
//	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
//	 */
//	public void testOneNormalCustomerScenario()
//	{
//		//setUp() runs first before this test!
//		
//		customer.cashier = cashier;//You can do almost anything in a unit test.			
//		
//		//check preconditions
//		assertEquals("RyansCashier should have 10 dollars in it. It doesn't.", cashier.getMoney(), 10.0);		
//		assertEquals("RyansCashierRole should have an empty event log before the RyansCashier's HereIsBill is called. Instead, the RyansCashier's event log reads: "
//						+ cashier.log.toString(), 0, cashier.log.size());
//		
//		//step 1 of the test
//		//public Bill(RyansCashier, RyansCustomer, int tableNum, double price) {
//		
//
//		//check postconditions for step 1 and preconditions for step 2
//		assertEquals("MockWaiter should have an empty event log before the RyansCashier's scheduler is called. Instead, the MockWaiter's event log reads: "
//						+ waiter.log.toString(), 0, waiter.log.size());
//		
//		cashier.msgComputeBill(waiter, "Cookie", customer);
//				
//		
//		assertTrue("RyansCashier should have logged \"Received msgComputeBill\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgComputeBill"));
//		
//		
//		assertTrue("RyansCashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//		
//		
//		assertTrue("RyansCashier should have logged \"Scheduler called computeBill\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called computeBill"));
//		
//		
//		assertTrue(
//				"MockWaiter should have an event log  msgHereIsCheck recieved after the RyansCashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
//						+ waiter.log.toString(), waiter.log.getLastLoggedEvent().toString().contains("msgHereIsCheck recieved"));
//		
//		assertEquals(
//				"MockCustomer should have an empty event log after the RyansCashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
//						+ customer.log.toString(), 0, customer.log.size());
//		
//		//step 2 of the test
//		cashier.msgHereIsPayment(4, customer);
//		
//		//check postconditions for step 2 / preconditions for step 3
//		assertTrue("RyansCashier should have logged \"Recieved msgHereIsPayment: 4\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved msgHereIsPayment: 4"));
//		
//		
//		//call scheduler
//		assertTrue("RyansCashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
//				cashier.pickAndExecuteAnAction());
//		
//		assertTrue("RyansCashier should have logged \"Scheduler called computeChange 4\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called computeChange 4"));
//		
//		assertTrue("RyansCashier should have logged \"Change = 2 Total = 0\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Change = 2"));
//		
//		assertTrue("RyansCashier should have logged \"Change = 2 Total = 0\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Total = 0"));
//	
//		
//		
//		
//		//step 3
//		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
//		
//	
//		
//		//check postconditions for step 4
//		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
//				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 2.0"));
//		
//		
//		assertTrue("RyansCashier should have $12", cashier.getMoney() == 12);
//	
//		
//		
//		
//		assertFalse("RyansCashier's scheduler should have returned false (no actions left to do), but didn't.", 
//				cashier.pickAndExecuteAnAction());
//		
//		
//		
//		assertTrue(true);
//	}//end one normal customer scenario
//	
//	
//	
//	/**
//	 * This tests the cashier under very complex terms: one customer pays, two markets get payed.
//	 */
//	public void testSixNormalCustomerMarketScenario()
//	{
//		//setUp() runs first before this test!
//		
//		customer.cashier = cashier;//You can do almost anything in a unit test.			
//		
//		//check preconditions
//		assertEquals("RyansCashier should have 10 dollars in it. It doesn't.", cashier.getMoney(), 10.0);		
//		assertEquals("RyansCashierRole should have an empty event log before the RyansCashier's HereIsBill is called. Instead, the RyansCashier's event log reads: "
//						+ cashier.log.toString(), 0, cashier.log.size());
//		
//		//step 1 of the test
//		//public Bill(RyansCashier, RyansCustomer, int tableNum, double price) {
//		
//
//		//check postconditions for step 1 and preconditions for step 2
//		assertEquals("MockWaiter should have an empty event log before the RyansCashier's scheduler is called. Instead, the MockWaiter's event log reads: "
//						+ waiter.log.toString(), 0, waiter.log.size());
//		
//		cashier.msgComputeBill(waiter, "Cookie", customer);
//		
//		///////////////////////////
//		//Send a MarketBill Message
//		cashier.msgMarketBill(market1, 10);
//		assertTrue("RyansCashier should have logged \"Received msgMarketBill market1 total = 15\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgMarketBill market1 total = 10"));
//		//////////////////////////
//		
//		assertTrue("RyansCashier should have logged \"Received msgComputeBill\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgComputeBill"));
//		
//		
//		assertTrue("RyansCashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//		
//		
//		assertTrue("RyansCashier should have logged \"Scheduler called computeBill\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called computeBill"));
//		
//		
//		assertTrue(
//				"MockWaiter should have an event log  msgHereIsCheck recieved after the RyansCashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
//						+ waiter.log.toString(), waiter.log.getLastLoggedEvent().toString().contains("msgHereIsCheck recieved"));
//		
//		assertEquals(
//				"MockCustomer should have an empty event log after the RyansCashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
//						+ customer.log.toString(), 0, customer.log.size());
//		
//		//step 2 of the test
//		cashier.msgHereIsPayment(4, customer);
//		
//		//check postconditions for step 2 / preconditions for step 3
//		assertTrue("RyansCashier should have logged \"Recieved msgHereIsPayment: 4\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved msgHereIsPayment: 4"));
//		
//		///////////////////////////
//		//Send a MarketBill Message
//		cashier.msgMarketBill(market2, 10);
//		assertTrue("RyansCashier should have logged \"Received msgMarketBill market2 total = 10\" but didn't. His log reads instead: " 
//		+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgMarketBill market2 total = 10"));
//		//////////////////////////
//		
//		
//		
//		//call scheduler
//		assertTrue("RyansCashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
//				cashier.pickAndExecuteAnAction());
//		
//		assertTrue("RyansCashier should have logged \"Scheduler called computeChange 4\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called computeChange 4"));
//		
//		assertTrue("RyansCashier should have logged \"Change = 2 Total = 0\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Change = 2"));
//		
//		assertTrue("RyansCashier should have logged \"Change = 2 Total = 0\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Total = 0"));
//	
//		
//		
//		
//		//step 3
//		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
//		
//	
//		
//		//check postconditions for step 4
//		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
//				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 2.0"));
//		
//		
//		assertTrue("RyansCashier should have $12", cashier.getMoney() == 12);
//	
//		
//		
//		
//		//step 4 pay MARKET1
//		assertTrue("RyansCashier's scheduler should have returned true (need to pay market), but didn't.", 
//				cashier.pickAndExecuteAnAction());
//		//check preconditions
//		assertTrue("RyansCashier should have logged \"Scheduler called payMarketBill market1\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called payMarketBill market1"));
//		
//		assertTrue("RyansCashier should have logged \"Bill paid in full.\" but didn't. His log reads instead: " 
//				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Bill paid in full."));
//		
//		assertTrue("RyansMarket should have logged \"Recieved msgHereIsPayment total =10\" but didn't. His log reads instead: " 
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgHereIsPayment total =10.0"));
//		
//		assertEquals(2.0, cashier.getMoney());
//		
//		//step 5 pay MARKET2
//		assertTrue("RyansCashier's scheduler should have returned true (need to pay market), but didn't.", 
//					cashier.pickAndExecuteAnAction());
//		//check preconditions
//		assertTrue("RyansCashier should have logged \"Scheduler called payMarketBill market1\" but didn't. His log reads instead: " 
//			+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Scheduler called payMarketBill market2"));
//				
//				assertTrue("RyansCashier should have logged \"Bill paid in full.\" but didn't. His log reads instead: " 
//						+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Some of bill paid."));
//				
//				assertTrue("RyansMarket should have logged \"Recieved msgHereIsPayment total =2.0\" but didn't. His log reads instead: " 
//						+ market1.log.getLastLoggedEvent().toString(), market2.log.containsString("Recieved msgHereIsPayment total =2.0"));
//				
//				assertEquals(0.0, cashier.getMoney());
//	}//end finalTest
//	
//}
