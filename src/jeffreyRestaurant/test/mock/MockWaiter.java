package jeffreyRestaurant.test.mock;

import jeffreyRestaurant.interfaces.Customer;
import jeffreyRestaurant.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter{
	public MockWaiter(String name) {
		super(name);
	}

	@Override
	public void msgHereIsCheck(Double tab, Customer c) {
		
	}
}
