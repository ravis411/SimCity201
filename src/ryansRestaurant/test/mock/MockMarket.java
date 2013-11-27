package ryansRestaurant.test.mock;


import java.util.List;

import ryansRestaurant.interfaces.Cashier;
import ryansRestaurant.interfaces.Customer;
import ryansRestaurant.interfaces.Market;
import ryansRestaurant.interfaces.Waiter;


public class MockMarket extends Mock implements Market {

	public EventLog log = new EventLog();
	
	
	public MockMarket(String name) {
		super(name);
	}


	public void msgHereIsPayment(Cashier cashier, double total){
		log.add(new LoggedEvent("Recieved msgHereIsPayment total =" + total));
	}
	
	public String toString(){
		return "" + name;
	}

	
}
