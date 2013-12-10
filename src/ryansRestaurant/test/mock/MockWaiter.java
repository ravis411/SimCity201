package ryansRestaurant.test.mock;


import java.util.List;

import ryansRestaurant.interfaces.RyansCashier;
import ryansRestaurant.interfaces.RyansCustomer;
import ryansRestaurant.interfaces.RyansWaiter;

/**
 * A sample MockCustomer built to unit test a RyansCashierRole.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements RyansWaiter {

	public EventLog log = new EventLog();
	
	
	public MockWaiter(String name) {
		super(name);
	}

	@Override
	public void msgSitAtTable(RyansCustomer cust, int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(RyansCustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(RyansCustomer customer, String choice) {
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
	public void msgHereIsCheck(RyansCustomer cust, double total) {
		log.add(new LoggedEvent("msgHereIsCheck recieved"));
		
	}

	@Override
	public void msgDoneEatingLeaving(RyansCustomer customer) {
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
