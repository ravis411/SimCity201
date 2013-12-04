package kushrestaurant.test;

import kushrestaurant.CashierAgent;
import kushrestaurant.CashierAgent.CState;
import kushrestaurant.interfaces.Cashier;
import kushrestaurant.interfaces.Waiter;
import kushrestaurant.test.mock.MockCustomer;
import kushrestaurant.test.mock.MockMarket;
import kushrestaurant.test.mock.MockWaiter;
import junit.framework.*;
import restaurant.WaiterAgent;
/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
        //these are instantiated for each test separately via the setUp() method.
        CashierAgent cashier;
        //MockWaiter waiter;
        MockCustomer customer;
        Waiter waiter;
        MockWaiter w;
        MockCustomer customer2;
        
        MockMarket M1;
        MockMarket M2;
        /**
         * This method is run before each test. You can use it to instantiate the class variables
         * for your agent and mocks, etc.
         */
        public void setUp() throws Exception{
                super.setUp();                
                cashier = new CashierAgent("Cashier");                
                customer = new MockCustomer("MockCustomer1",cashier);  
                customer2= new MockCustomer("MockCustomer2",cashier);
                
                M1= new MockMarket("M1",cashier);
                M2= new MockMarket("M2",cashier);
                
                waiter = new MockWaiter("mockwaiter");
        }        
        /**
         * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
         */
        public void testOneNormalCustomerScenario()
        { 
        	    System.out.println("Testing one normal customer Scenario");
        	    assertEquals("Cashier should have noone in the customers list",cashier.customers.size(),0);
                
                cashier.msgProduceCheck(customer, "Chicken",waiter);
                
                assertEquals("Bill of the customer should be 0 as cashier hasnt setCheck",cashier.customers.get(0).check, 0.0);
                assertTrue("State of customers check should be not ready",cashier.customers.get(0).state==CState.checkNotReady);
                cashier.pickAndExecuteAnAction();
                assertEquals("Bill of the customer should be 10.99 as cashier has setCheck for chicken",cashier.customers.get(0).check, 10.99);
                assertTrue("State of customers check should be ready",cashier.customers.get(0).state==CState.checkReady);
                customer.msgReceivedCheck(10.99);
                cashier.msgHereIsPayment(customer, 10.99,20);
                assertTrue("State of customers check should be not ready",cashier.customers.get(0).state==CState.ChangeReady);
                assertEquals("Change for customer should be 9.01 dollars",cashier.customers.get(0).change, 9.01);
                cashier.pickAndExecuteAnAction();
                assertTrue("State of customers check should be paid so he can leave",cashier.customers.get(0).state==CState.paid);
        }
        public void testTwoNormalCustomerScenario()
        { 
        	    System.out.println("Testing two normal customer Scenario");
        	    assertEquals("Cashier should have noone in the customers list",cashier.customers.size(),0);
                
                cashier.msgProduceCheck(customer, "Chicken",waiter);
                cashier.msgProduceCheck(customer2,"Steak",waiter);
                assertEquals("Bill of the customer should be 0 as cashier hasnt setCheck",cashier.customers.get(0).check, 0.0);
                assertTrue("State of customer's check should be not ready",cashier.customers.get(0).state==CState.checkNotReady);
                assertEquals("Bill of the customer2 should be 0 as cashier hasnt setCheck",cashier.customers.get(1).check, 0.0);
                assertTrue("State of customer2's check should be not ready",cashier.customers.get(1).state==CState.checkNotReady);
                cashier.pickAndExecuteAnAction();
                cashier.pickAndExecuteAnAction();
                assertEquals("Bill of the customer should be 10.99 as cashier has setCheck for chicken",cashier.customers.get(0).check, 10.99);
                assertTrue("State of customer's check should be ready",cashier.customers.get(0).state==CState.checkReady);
                assertEquals("Bill of the customer2's should be 15.99 as cashier has setCheck for steak",cashier.customers.get(1).check, 15.99);
                assertTrue("State of customer2's check should be ready",cashier.customers.get(1).state==CState.checkReady);
                customer.msgReceivedCheck(10.99);
                cashier.msgHereIsPayment(customer, 10.99,20);
                customer2.msgReceivedCheck(15.99);
                cashier.msgHereIsPayment(customer2,15.99,30);
                assertTrue("State of customer's check should be not ready",cashier.customers.get(0).state==CState.ChangeReady);
                assertEquals("Change for customer should be 9.01 dollars",cashier.customers.get(0).change, 9.01);
                assertTrue("State of customer2's check should be not ready",cashier.customers.get(1).state==CState.ChangeReady);
                assertEquals("Change for customer2 should be 14.01 dollars",cashier.customers.get(1).change, 14.01);
                cashier.pickAndExecuteAnAction();
                cashier.pickAndExecuteAnAction();
                assertTrue("State of customer's check should be paid so he can leave",cashier.customers.get(0).state==CState.paid);
                assertTrue("State of customer2's check should be paid so he can leave",cashier.customers.get(1).state==CState.paid);
        }
        public void testNonNormativeCustomerCantPayGetsPunishedScenario()
        { 
        	    System.out.println("Testing scenario where customer gets punished");
        	    assertEquals("Cashier should have noone in the customers list",cashier.customers.size(),0);
                
                cashier.msgProduceCheck(customer, "Chicken",waiter);
                
                assertEquals("Bill of the customer should be 0 as cashier hasnt setCheck",cashier.customers.get(0).check, 0.0);
                assertTrue("State of customers check should be not ready",cashier.customers.get(0).state==CState.checkNotReady);
                cashier.pickAndExecuteAnAction();
                assertEquals("Bill of the customer should be 10.99 as cashier has setCheck for chicken",cashier.customers.get(0).check, 10.99);
                assertTrue("State of customers check should be ready",cashier.customers.get(0).state==CState.checkReady);
                customer.msgReceivedCheck(10.99);
                cashier.msgHereIsPayment(customer, 10.99,9);
                assertTrue("State of customers check should be not ready",cashier.customers.get(0).state==CState.Punish);
               
                cashier.pickAndExecuteAnAction();
                assertTrue("State of customers check should be paid after he is punished so he can leave",cashier.customers.get(0).state==CState.paid);
        }
        public void testOneMarketFullPaymentScenario()
        {
        	System.out.println("Testing 1 Market full payment scenario");
        	assertEquals("Cashier should have 100 dollars, his intial stache", cashier.getMoney(),100.00);
        	assertEquals("Cashier should have no bills from market",cashier.getListOfBills().size(),0);
        	cashier.msgHereIsBill(M1,8);
        	assertEquals("Cashier should still have 100 dollars",cashier.getMoney(),100.00);
        	assertEquals("Cashier should have 1 bill now", cashier.getListOfBills().size(),1);
        	assertTrue("State of bill should be unpaid",cashier.getListOfBills().get(0).state==CState.Unpaid);
        	cashier.pickAndExecuteAnAction();
        	
        	assertEquals("Cashier should have 92 dollars, after paying 8", cashier.getMoney(),92.00);
        	assertTrue("State of bill should be paid",cashier.getListOfBills().get(0).state==CState.paid);
        	
        }
        public void testTwoMarketNonNormativeScenario()
        {
        	System.out.println("Testing 2 Markets non normative scenario");
        	assertEquals("Cashier should have 100 dollars, his intial stache", cashier.getMoney(),100.00);
        	assertEquals("Cashier should have no bills from market",cashier.getListOfBills().size(),0);
        	cashier.msgHereIsBill(M1,30);
        	cashier.msgHereIsBill(M2,90);
        	assertEquals("Cashier should still have 100 dollars",cashier.getMoney(),100.00);
        	assertEquals("Cashier should have 2 bills now", cashier.getListOfBills().size(),2);
        	assertTrue("State of bill 1 should be unpaid",cashier.getListOfBills().get(0).state==CState.Unpaid);
        	assertTrue("State of bill 2 should be unpaid",cashier.getListOfBills().get(1).state==CState.Unpaid);
        	cashier.pickAndExecuteAnAction();
        	cashier.pickAndExecuteAnAction();
        	
        	assertEquals("Cashier should have 0 dollars", cashier.getMoney(),0.00);
        	assertTrue("State of bill 1 should be Paid",cashier.getListOfBills().get(0).state==CState.paid);
        	assertTrue("State of bill 2 should be Madegood",cashier.getListOfBills().get(1).state==CState.MadeGood);
        	assertTrue("Cashier should have given one gift cards", cashier.givenGiftCards.size()==1);
        	assertTrue("Value of first gift card should be 20 dollars", cashier.givenGiftCards.get(1)==20);
        	
        }
        public void testTwoMarketFullPaymentScenario()
        {
        	System.out.println("Testing 2 Markets full payment scenario");
        	assertEquals("Cashier should have 100 dollars, his intial stache", cashier.getMoney(),100.00);
        	assertEquals("Cashier should have no bills from market",cashier.getListOfBills().size(),0);
        	cashier.msgHereIsBill(M1,8);
        	cashier.msgHereIsBill(M2,6);
        	assertEquals("Cashier should still have 100 dollars",cashier.getMoney(),100.00);
        	assertEquals("Cashier should have 2 bills now", cashier.getListOfBills().size(),2);
        	assertTrue("State of bill 1 should be unpaid",cashier.getListOfBills().get(0).state==CState.Unpaid);
        	assertTrue("State of bill 2 should be unpaid",cashier.getListOfBills().get(1).state==CState.Unpaid);
        	cashier.pickAndExecuteAnAction();
        	cashier.pickAndExecuteAnAction();
        	
        	assertEquals("Cashier should have 86 dollars, after paying 8+6", cashier.getMoney(),86.00);
        	assertTrue("State of bill 1 should be paid",cashier.getListOfBills().get(0).state==CState.paid);
        	assertTrue("State of bill 2 should be paid",cashier.getListOfBills().get(1).state==CState.paid);
        	
        	
        }
}
        