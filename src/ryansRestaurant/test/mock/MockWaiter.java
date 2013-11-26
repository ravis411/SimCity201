package ryansRestaurant.test.mock;


import java.util.List;

import ryansRestaurant.interfaces.Cashier;
import ryansRestaurant.interfaces.Customer;
import ryansRestaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter {

	public EventLog log = new EventLog();
	
	
	public MockWaiter(String name) {
		super(name);
	}

	@Override
	public void msgSitAtTable(Customer cust, int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(Customer customer, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfOrder(int tableNumber, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderReady(int tableNumber, int grillNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Customer cust, double total) {
		log.add(new LoggedEvent("msgHereIsCheck recieved"));
		
	}

	@Override
	public void msgDoneEatingLeaving(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFromHostGoOnBreak(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRequestABreak() {
		// TODO Auto-generated method stub
		
	}

	

}
