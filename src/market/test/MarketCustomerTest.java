package market.test;

import junit.framework.TestCase;
import market.test.mock.MockMarketEmployee;
import MarketEmployee.MarketCustomerRole;

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
		employee = new MockMarketEmployee();
		//customer.marketEmployee=employee;
		
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{/*
		//setUp() runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions

		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		//public void msgCashierComputeBill(String customerChoice, Customer customer, Waiter waiter) {
		String customerChoice="Steak";
		cashier.msgCashierComputeBill(customerChoice, customer, waiter);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
	

		
		assertTrue("Cashier's scheduler should have returned true (giveWaiterCheck action should be done).", cashier.pickAndExecuteAnAction());
		

		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		
		//step 2 of the test
		int money= 20;
		cashier.msgCashierPayForOrder(money,customer);
		
		//check postconditions for step 2 / preconditions for step 3
		
		assertTrue("Cashier should have logged \"Recieved msgCashierPayForOrder money: " + money + "customer: "+ customer + "\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved msgCashierPayForOrder"));

		assertTrue("CashierTab should contain a tab of price = 0. It contains something else instead: $" 
				+ cashier.Tabs.get(0).getTab(),  cashier.Tabs.get(0).getTab() == 0);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.Tabs.get(0).getCustomer() == customer);
		int bill=10;
		cashier.restaurantMoney=20;
		cashier.marketBills.add(cashier.new MarketBill(market1));	
		cashier.msgCashierHereIsMarketBill(bill,market1);
		assertTrue("Cashier should have logged \"Recieved msgCashierHereIsMarketBill orderPrice: " +bill + " market: "+market1 +"\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved msgCashierHereIsMarketBill"));
		assertTrue("Cashier should have logged \"New Event needToPayMarket\" instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("New Event needToPayMarket"));
		assertTrue("Cashier's scheduler should have returned true (giveWaiterCheck action should be done).", cashier.pickAndExecuteAnAction());
		assertTrue("Market should have logged \"Recieved msgMarketHereIsPayment. moneyOwed: " + bill+"\" but didn't. His log reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgMarketHereIsPayment"));
	*/}//end one normal customer scenario
	public void testTwoNotEnoughMoneyforCustomerToPay()
	{
		/*
		//setUp() runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions

		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		//public void msgCashierComputeBill(String customerChoice, Customer customer, Waiter waiter) {
		String customerChoice="Steak";
		cashier.msgCashierComputeBill(customerChoice, customer, waiter);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
	

		
		assertTrue("Cashier's scheduler should have returned true (giveWaiterCheck action should be done).", cashier.pickAndExecuteAnAction());
		

		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		
		//step 2 of the test
		int money= 10;
		cashier.msgCashierPayForOrder(money,customer);

		assertTrue("Cashier should have logged \"Recieved msgCashierPayForOrder money: " + money + "customer: "+ customer + "\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved msgCashierPayForOrder"));

		assertTrue("Customer Tab should contain a tab of amount = 10. It contains something else instead: $" 
				+ cashier.Tabs.get(0).getTab(),  cashier.Tabs.get(0).getTab() == 10);
		//should be ten because that is how much the customer owes next time.
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.Tabs.get(0).getCustomer() == customer);
		
	}
	public void testThreeCorrectChangeGeneratedTest()
	{
		//setUp() runs first before this test!
		
				customer.cashier = cashier;//You can do almost anything in a unit test.			
				
				//check preconditions

				assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "+ cashier.log.toString(), 0, cashier.log.size());
				
				//step 1 of the test
				String customerChoice="Steak";
				cashier.msgCashierComputeBill(customerChoice, customer, waiter);//send the message from a waiter

				//check postconditions for step 1 and preconditions for step 2
				assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
								+ waiter.log.toString(), 0, waiter.log.size());
			

				
				assertTrue("Cashier's scheduler should have returned true (giveWaiterCheck action should be done).", cashier.pickAndExecuteAnAction());
				

				assertEquals(
						"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
								+ customer.log.toString(), 0, customer.log.size());
				
				//step 2 of the test
				int money= 25;
				cashier.msgCashierPayForOrder(money,customer);

				assertTrue("Cashier should have logged \"Recieved msgCashierPayForOrder money: " + money + "customer: "+ customer + "\" but didn't. His log reads instead: " 
						+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved msgCashierPayForOrder"));

				assertTrue("Customer Change should contain a tab of amount = 10. It contains something else instead: $" 
						+ cashier.Tabs.get(0).getChange(),  cashier.Tabs.get(0).getChange() == 5);
				//should be ten because that is how much the customer owes next time.
				assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
							cashier.Tabs.get(0).getCustomer() == customer);
	*/
	}
	public void testFourWithOneOrderCashierDoesnotHaveEnoughMoney()
	{
	/*	customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.msgMarketOrderFood("Chicken", 1);
		assertTrue("Market should have logged \"Recieved msgMarketOrderFood.\" but didn't. His log reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgMarketOrderFood"));
		cashier.restaurantMoney=1;
		Vector<MarketAgent> markets = new Vector<MarketAgent>();
		markets.add(market1);
		cashier.addMarkets(markets);
		market1.pickAndExecuteAnAction();
		assertTrue("Cook should have logged \"Recieved msgHereAreRequestedFoodSupplies.\" but didn't. His log reads instead: " 
				+ cook.log.getLastLoggedEvent().toString(), cook.log.containsString("Recieved msgHereAreRequestedFoodSupplies"));
		assertFalse("Cook should NOT have logged \"Recieved msgCookNumberThatWereOrderedButNotFullfilled.\" but did.", cook.log.containsString("Recieved msgCookNumberThatWereOrderedButNotFullfilled"));
		assertTrue("Cashier should have logged \"Recieved msgCashierHereIsMarketBill.\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved msgCashierHereIsMarketBill"));
		
		assertFalse("Cashier should NOT have logged \"New Event needToPayMarket\" instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("New Event needToPayMarket"));
		assertTrue("Cashier should have logged \"New Event notEnoughMoneyToPayMarket\" instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("New Event notEnoughMoneyToPayMarket"));
		assertTrue("Cashier's scheduler should have returned true (giveWaiterCheck action should be done).", cashier.pickAndExecuteAnAction());
		assertTrue("Market should have logged \"Recieved msgMarketHereIsPayment.\" but didn't. His log reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgMarketHereIsPayment"));
	
		assertEquals("Cashier Should be in debt with negative -$1 after paying with credit but has $"
				+ cashier.restaurantMoney, -1,  cashier.restaurantMoney);
	*/
	}
	public void testFiveWithOneOrderNormalCustomerScenario()
	{
	/*	customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.msgMarketOrderFood("Chicken", 1);
		assertTrue("Market should have logged \"Recieved msgMarketOrderFood.\" but didn't. His log reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgMarketOrderFood"));
		cashier.restaurantMoney=10;
		Vector<MarketAgent> markets = new Vector<MarketAgent>();
		markets.add(market1);
		cashier.addMarkets(markets);
		market1.pickAndExecuteAnAction();
		assertTrue("Cook should have logged \"Recieved msgHereAreRequestedFoodSupplies.\" but didn't. His log reads instead: " 
				+ cook.log.getLastLoggedEvent().toString(), cook.log.containsString("Recieved msgHereAreRequestedFoodSupplies"));
		assertFalse("Cook should NOT have logged \"Recieved msgCookNumberThatWereOrderedButNotFullfilled.\" but did.", cook.log.containsString("Recieved msgCookNumberThatWereOrderedButNotFullfilled"));
		assertTrue("Cashier should have logged \"Recieved msgCashierHereIsMarketBill.\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved msgCashierHereIsMarketBill"));
		
		assertTrue("Cashier should have logged \"New Event needToPayMarket\" instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("New Event needToPayMarket"));
		assertTrue("Cashier's scheduler should have returned true (giveWaiterCheck action should be done).", cashier.pickAndExecuteAnAction());
		assertTrue("Market should have logged \"Recieved msgMarketHereIsPayment.\" but didn't. His log reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgMarketHereIsPayment"));
	
		assertEquals("Cashier's should Owe No Money. Instead, the waiter owes: "
				+ cashier.marketBills.get(0).getMoneyOwed(), 0,  cashier.marketBills.get(0).getMoneyOwed());
	*/
	}
	public void testSixWithTwoOrdersTwoBillsNormalCustomerScenario()
	{/*
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.msgMarketOrderFood("Chicken", 3);
		assertTrue("Market1 should have logged \"Recieved msgMarketOrderFood.\" but didn't. His log reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgMarketOrderFood"));
		cashier.restaurantMoney=6;
		Vector<MarketAgent> markets = new Vector<MarketAgent>();
		markets.add(market1);
		markets.add(market2);
		cashier.addMarkets(markets);
		market1.pickAndExecuteAnAction();
		assertTrue("Cook should have logged \"Recieved msgHereAreRequestedFoodSupplies.\" but didn't. His log reads instead: " 
				+ cook.log.getLastLoggedEvent().toString(), cook.log.containsString("Recieved msgHereAreRequestedFoodSupplies"));
		assertTrue("Cook should have logged \"Recieved msgCookNumberThatWereOrderedButNotFullfilled.\" and did.", cook.log.containsString("Recieved msgCookNumberThatWereOrderedButNotFullfilled"));
		assertTrue("Cashier should have logged \"Recieved msgCashierHereIsMarketBill.\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Recieved msgCashierHereIsMarketBill"));
		
		assertTrue("Cashier should have logged \"New Event needToPayMarket\" instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("New Event needToPayMarket"));
		assertTrue("Cashier's scheduler should have returned true (giveWaiterCheck action should be done).", cashier.pickAndExecuteAnAction());
		assertTrue("Market1 should have logged \"Recieved msgMarketHereIsPayment.\" but didn't. His log reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Recieved msgMarketHereIsPayment"));
		assertEquals("Market1 should have $4. But instead has: $"+ market1.money, 4,  market1.money);
		
		market2.msgMarketOrderFood("Chicken", 1);
		market2.pickAndExecuteAnAction();
		assertTrue("Market2 should have logged \"Recieved msgMarketOrderFood.\" but didn't. His log reads instead: " 
				+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Recieved msgMarketOrderFood"));
		assertTrue("Cashier's scheduler should have returned true (giveWaiterCheck action should be done).", cashier.pickAndExecuteAnAction());
		assertTrue("Market2 should have logged \"Recieved msgMarketHereIsPayment.\" but didn't. His log reads instead: " 
				+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Recieved msgMarketHereIsPayment"));
		assertEquals("Market2 should have $2. But instead has: $"
				+ market2.money, 2,  market2.money);
		assertEquals("Cashier's should Owe No Money. Instead, the waiter owes: "
				+ cashier.marketBills.get(0).getMoneyOwed(), 0,  cashier.marketBills.get(0).getMoneyOwed());
	*/
	}
	
}
