package restaurant.test;

import junit.framework.TestCase;
import restaurant.CashierRole;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;

/**
 * 
 * JUnit test to test CashierAgent
 *
 */
public class CashierTest extends TestCase
{
        //these are instantiated for each test separately via the setUp() method.
        CashierRole cashier;
        MockWaiter waiter;
        MockCustomer customer;
        MockMarket market1;
        MockMarket market2;
        
        EventLog log;
        
        /**
         * This method is run before each test. You can use it to instantiate the class variables
         * for your agent and mocks, etc.
         */
        public void setUp() throws Exception{
                super.setUp();                
                cashier = new CashierRole("Restaurant");
                customer = new MockCustomer("mockcustomer");                
                waiter = new MockWaiter("mockwaiter");
                market1 = new MockMarket("mockmarket 1");
                market2 = new MockMarket("mockmarket 2");
        }        
        /**
         * Cashier pays bill from one market for one order.
         * @throws InterruptedException 
         */
        public void testOneNormalMarketScenario() throws InterruptedException {
        	//set up
        	cashier.getPerson().startThread();
        	market1.cashier = cashier;
        	
        	//preconditions
        	assertEquals("CashierAgent should start with $200.00. Instead has $" + cashier.getMoney(), 200.00, cashier.getMoney());
        	assertEquals("MockMarket 1 should have an empty event log. Instead, the MockMarket's event log reads: " 
        			+ market1.log.toString(), 0, market1.log.size());
        	assertFalse("Cashier's scheduler should have returned false (no actions to do yet), but didn't.", cashier.pickAndExecuteStatus());
        	
        	//step 1
        	market1.msgNeedFood(0,1);
        	
        	//postconditions
        	Thread.sleep(1000);
        	assertTrue("MockMarket 1 should have logged Received order. Charging cashier $19.99 but last event in log reads: " 
        			+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received order. Charging cashier $19.99."));
        	assertTrue("MockMarket 1 should have logged Received payment from cashier. but last event in log reads: " 
        			+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received payment from cashier."));
        	assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                    cashier.pickAndExecuteStatus());
        	assertEquals("CashierAgent should now have $180.01. Instead has $" + cashier.getMoney(), 180.01, cashier.getMoney());

        }
        
        /**
         * Cashier pays bill from two markets for one order.
         * @throws InterruptedException 
         */
        public void testTwoMarketScenario() throws InterruptedException {
        	//set up
        	cashier.getPerson().startThread();
        	market1.cashier = cashier;
        	market2.cashier = cashier;
        	
        	//preconditions
        	assertEquals("CashierAgent should start with $200.00. Instead has $" + cashier.getMoney(), 200.00, cashier.getMoney());
        	assertEquals("MockMarket 1 should have an empty event log. Instead, the MockMarket's event log reads: " 
        			+ market1.log.toString(), 0, market1.log.size());
        	assertEquals("MockMarket 2 should have an empty event log. Instead, the MockMarket's event log reads: " 
        			+ market2.log.toString(), 0, market2.log.size());
        	assertFalse("Cashier's scheduler should have returned false (no actions to do yet), but didn't.", cashier.pickAndExecuteStatus());
        	
        	//step 1
        	market1.msgNeedFood(0,2);
        	market2.msgNeedFood(0,2);
        	
        	//postconditions
        	Thread.sleep(1000);
        	assertTrue("MockMarket 1 should have logged Received order. Charging cashier $9.99 but last event in log reads: " 
        			+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received order. Charging cashier $9.99."));
        	assertTrue("MockMarket 1 should have logged Received payment from cashier. but last event in log reads: " 
        			+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received payment from cashier."));
        	assertTrue("MockMarket 2 should have logged Received order. Charging cashier $9.99 but last event in log reads: " 
        			+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received order. Charging cashier $9.99."));
        	assertTrue("MockMarket 2 should have logged Received payment from cashier. but last event in log reads: " 
        			+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received payment from cashier."));
        	assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                    cashier.pickAndExecuteStatus());
        	assertEquals("CashierAgent should now have $180.01. Instead has $" + cashier.getMoney(), 180.02, cashier.getMoney());

        }
        
        public void testThreeNormalCustomerScenario() throws InterruptedException {
        	//set up
        	cashier.getPerson().startThread();
        	customer.cashier = cashier;
        	customer.money = 20.00;
        	
        	//preconditions
        	assertEquals("CashierAgent should start with $200.00. Instead has $" + cashier.getMoney(), 200.00, cashier.getMoney());
        	assertEquals("MockCustomer should have an empty event log. Instead, the MockCustomer's event log reads: " 
        			+ customer.log.toString(), 0, customer.log.size());
        	assertFalse("Cashier's scheduler should have returned false (no actions to do yet), but didn't.", cashier.pickAndExecuteStatus());
        	
        	//step 1
        	customer.msgCheckAtTable(15.00);
        	
        	//postconditions
        	Thread.sleep(1000);
        	assertTrue("MockCustomer should have logged Received check amount from waiter. Total = 15.00 but last event in log reads: " 
        			+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received check amount from waiter. Total = 15.0"));
        	assertTrue("MockCustomer should have logged Payed bill but last event in log reads: " 
        			+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Payed bill."));
        	assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                    cashier.pickAndExecuteStatus());
        	assertEquals("CashierAgent should now have $215.00. Instead has $" + cashier.getMoney(), 215.00, cashier.getMoney());

        }

        public void testFourCustomerCantPayScenario() throws InterruptedException {
        	cashier.getPerson().startThread();            customer.cashier = cashier;
            customer.money = 10.00;
                
            //check preconditions
            assertEquals("CashierAgent should start with $200.00. Instead has $" + cashier.getMoney(), 200.00, cashier.getMoney());
            assertEquals("MockCustomer should have an empty event log. Instead, the MockCustomer's event log reads: " 
            		+ customer.log.toString(), 0, customer.log.size());
            assertFalse("Cashier's scheduler should have returned false (no actions to do yet), but didn't.", cashier.pickAndExecuteStatus());
                
            //step 1
            customer.msgCheckAtTable(15.00);
                
            //postconditions
            Thread.sleep(1000);
            assertTrue("MockCustomer should have logged Received check amount from waiter. Total = 15.00 but last event in log reads: " 
            		+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received check amount from waiter. Total = 15.0"));
            assertTrue("MockCustomer should have logged Did not have enough money to pay bill but last event in log reads: " 
            		+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Did not have enough money to pay bill."));
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                    cashier.pickAndExecuteStatus());
            assertEquals("CashierAgent should now have $200.00. Instead has $" + cashier.getMoney(), 200.00, cashier.getMoney());
        }
        
        public void testFiveRingUpCheckScenario() throws InterruptedException {
        	cashier.getPerson().startThread();            waiter.cashier = cashier;
            
            //check preconditions
            assertEquals("MockWaiter should have an empty event log. Instead, the MockWaiter's event log reads: " 
            		+ waiter.log.toString(), 0, waiter.log.size());
            assertFalse("Cashier's scheduler should have returned false (no actions to do yet), but didn't.", 
            		cashier.pickAndExecuteStatus());
            
            //step 1
            cashier.msgPrepareCheck(waiter, customer, 0);
            
            //postconditions for step 1
            Thread.sleep(5000);
            assertTrue("MockWaiter should have logged Received check from waiter for $15.99 but last event in log reads: " 
            		+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from waiter for $15.99."));
            assertEquals("CashierAgent should have 0 checks in list but instead has " 
            		+ cashier.getNumberOfChecks(), 0, cashier.getNumberOfChecks());
            
            //step 2
            cashier.msgPrepareCheck(waiter, customer, 1);
            
            //postconditions for step 2
            Thread.sleep(5000);
            assertTrue("MockWaiter should have logged Received check from waiter for $10.99 but last event in log reads: " 
            		+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from waiter for $10.99."));
            assertEquals("CashierAgent should have 0 checks in list but instead has " 
            		+ cashier.getNumberOfChecks(), 0, cashier.getNumberOfChecks());
        }
        
        public void testSixMultiAgentScenario() throws InterruptedException {
        	cashier.getPerson().startThread();
        	waiter.cashier = cashier;
        	customer.cashier = cashier;
        	market1.cashier = cashier;
        	market2.cashier = cashier;
        	customer.money = 30.00;
        	
        	//preconditions
        	assertEquals("CashierAgent should start with $200.00. Instead has $" + cashier.getMoney(), 200.00, cashier.getMoney());
        	assertEquals("MockMarket 1 should have an empty event log. Instead, the MockMarket's event log reads: " 
        			+ market1.log.toString(), 0, market1.log.size());
        	assertEquals("MockMarket 2 should have an empty event log. Instead, the MockMarket's event log reads: " 
        			+ market2.log.toString(), 0, market2.log.size());
        	assertEquals("MockCustomer should have an empty event log. Instead, the MockCustomer's event log reads: " 
            		+ customer.log.toString(), 0, customer.log.size());
        	 assertEquals("MockWaiter should have an empty event log. Instead, the MockWaiter's event log reads: " 
             		+ waiter.log.toString(), 0, waiter.log.size());
        	assertFalse("Cashier's scheduler should have returned false (no actions to do yet), but didn't.", cashier.pickAndExecuteStatus());
        	
        	//step 1
        	market1.msgNeedFood(0,1);
        	cashier.msgPrepareCheck(waiter, customer,2);
        	
        	//postconditions for step 1
        	Thread.sleep(5000);
        	assertEquals("CashierAgent should now have $180.01. Instead has $" + cashier.getMoney(), 180.01, cashier.getMoney());
        	assertEquals("CashierAgent should have 0 checks in list but instead has " 
            		+ cashier.getNumberOfChecks(), 0, cashier.getNumberOfChecks());
        	assertTrue("MockWaiter should have logged Received check from waiter for $10.99 but last event in log reads: " 
            		+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from waiter for $5.99."));
        	
        	//step 2
        	market2.msgNeedFood(0,2);
        	customer.msgCheckAtTable(15.00);
        	
        	//postconditions for step 2
        	Thread.sleep(3000);
        	assertEquals("CashierAgent should now have $185.02. Instead has $" + cashier.getMoney(), 185.02, cashier.getMoney());
        	assertEquals("CashierAgent should have 0 paying customers in list but instead has " 
            		+ cashier.getNumberOfPayingCustomers(), 0, cashier.getNumberOfPayingCustomers());
        	assertTrue("MockCustomer should have logged Received check amount from waiter. Total = 15.00 but last event in log reads: " 
            		+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received check amount from waiter. Total = 15.0"));
        }
}
