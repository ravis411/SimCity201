package restaurant.test.mock;


import interfaces.Cashier;
import interfaces.Customer;
import interfaces.Waiter;
import interfaces.generic_interfaces.GenericWaiter;
import restaurant.Menu;
import restaurant.RestaurantCustomerRole.AgentEvent;
import restaurant.RestaurantCustomerRole.AgentState;

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
		@Override
		public void msgOrderOnItsWay() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public AgentState getState() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public AgentEvent getEvent() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void msgSitAtTable(Menu menu) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void msgWaiterReadyToTakeOrder() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void msgWaiterReadyToRetakeOrder() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public int getWaitingLocX() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void setWaitingLocX(int i) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setWaitingLocY(int i) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setTableX(int xCoor) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setTableY(int yCoor) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setTableNum(int table) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public String getCustomerName() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void msgRestaurantFull() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setWaiter(GenericWaiter waiter) {
			// TODO Auto-generated method stub
			
		}

}