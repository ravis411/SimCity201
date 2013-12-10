package ryansRestaurant.test.mock;


import java.util.List;

import ryansRestaurant.interfaces.RyansCashier;
import ryansRestaurant.interfaces.RyansCustomer;
import ryansRestaurant.interfaces.RyansMarket;
import ryansRestaurant.interfaces.RyansWaiter;


public class MockMarket extends Mock implements RyansMarket {

	public EventLog log = new EventLog();
	
	
	public MockMarket(String name) {
		super(name);
	}


	public void msgHereIsPayment(RyansCashier cashier, double total){
		log.add(new LoggedEvent("Recieved msgHereIsPayment total =" + total));
	}
	
	public String toString(){
		return "" + name;
	}

	
}
