package restaurant.test.mock;


import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;

/**
 * MockCustomer built to unit test a CashierAgent
 */
public class MockCustomer extends Mock implements Customer {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public Cashier cashier;
        
        public double money;

        public MockCustomer(String name) {
                super(name);

        }
        
        public EventLog log = new EventLog();

        @Override
        public void msgCheckAtTable(double amount){
        	log.add(new LoggedEvent("Received check amount from waiter. Total = "+ amount));
        	cashier.msgPayingCheck(this, amount);
        }
    	public void msgCheckPayed(){
    		log.add(new LoggedEvent("Payed bill."));
    	}
    	public void msgCheckNotPayed(){
    		log.add(new LoggedEvent("Did not have enough money to pay bill."));
    	}
       
		@Override
		public int getTableX() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public int getTableY() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public int getTableNum() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public double getMoney() {
			// TODO Auto-generated method stub
			return money;
		}
		@Override
		public void msgFoodAtTable() {
			// TODO Auto-generated method stub
			
		}

}