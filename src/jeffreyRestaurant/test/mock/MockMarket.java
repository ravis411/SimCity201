package jeffreyRestaurant.test.mock;

import java.util.ArrayList;
import java.util.List;

import jeffreyRestaurant.interfaces.Cashier;
import jeffreyRestaurant.interfaces.Cook;
import jeffreyRestaurant.interfaces.Market;

public class MockMarket extends Mock implements Market {

	public MockMarket(String name) {
		super(name);
	}
	public Cashier cashier;
	public Cook cook;
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public void msgNeedFood(String food, int quantity) {
		log.add(new LoggedEvent("Received an order for " + quantity + " " + food + "s"));
	}
	
	public void msgOrderPayment(String food, Double price) {
		log.add(new LoggedEvent("Received payment of " + price + " for the order of " + food));
	}
}
