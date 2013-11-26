package restaurant.test.mock;


import restaurant.Order;
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

		@Override
		public void msgBringFoodToTable(Order o) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgOutOfFood(int choice, Customer customer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgBreakReply(boolean b) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAtFrontDesk() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAtWaitingArea() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAtPlatingArea() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAtCookingArea() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAtTable() {
			// TODO Auto-generated method stub
			
		}
}