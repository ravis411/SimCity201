package kushrestaurant.test.mock;

import kushrestaurant.CashierAgent;
import kushrestaurant.WaiterAgent.Menu;
import kushrestaurant.interfaces.Cashier;
import kushrestaurant.interfaces.Customer;
import kushrestaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public Cashier cashier;
        public EventLog log= new EventLog();

        public MockCustomer(String name, Cashier cashier) {
                super(name);
                this.cashier=cashier;

        }

        @Override
        public void msgReceivedCheck(double total) {
                log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));
                cashier.msgHereIsPayment(this, total,100);
                System.out.println(this.name+": Received check of " + total + " from waiter");
                        //test the normative scenario
                        //cashier.HereIsMyPayment(this, total);
                
        }

        @Override
        public void msgHereIsYourChange(double total) {
                log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
                System.out.println(this.name+": Received change of " + total + " from cashier");
        }

        @Override
        public void msgWashDishes() {
                log.add(new LoggedEvent("Received washDishes message"));
                System.out.println("Washing dishes");
        }

		@Override
		public void setTableNumber(int n) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getCustomerName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void msgWait() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgSitAtTable() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgTellMeOrder(Menu m) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgReceivedFood() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getTable() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void msgAnimationFinishedGoToSeat() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAnimationFinishedLeaveRestaurant() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setOrderAgain() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setWaiter(Waiter w) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgwaitRelease() {
			// TODO Auto-generated method stub
			
		}

}