package restaurant.test.mock;


import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * A sample MockWaiter built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public Cashier cashier;
        public Customer customer;

        public MockWaiter(String name) {
                super(name);

        }
        
        public EventLog log = new EventLog();
        
        @Override
        
        public void msgCheckReady(Customer customer, double amount){
        	log.add(new LoggedEvent("Received check from waiter for $" + amount + "."));
        }
}