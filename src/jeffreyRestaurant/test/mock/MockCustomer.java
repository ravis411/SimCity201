package jeffreyRestaurant.test.mock;


import java.util.ArrayList;
import java.util.List;

import jeffreyRestaurant.interfaces.Cashier;
import jeffreyRestaurant.interfaces.Customer;

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
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	public MockCustomer(String name) {
		super(name);

	} 
	@Override
	public void msgHereIsCheck(Double total) {
		//Test non-norms then test norms
		//Then, send cashier interface messages
	}

	@Override
	public void msgFreeToGo() {
		log.add(new LoggedEvent("Received msgFreeToGo from cashier."));
	}

	@Override
	public void msgPayNextTime() {
		log.add(new LoggedEvent("Received msgPayNextTime from cashier."));
	}

}
