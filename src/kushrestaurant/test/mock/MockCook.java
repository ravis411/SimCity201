package kushrestaurant.test.mock;

import kushrestaurant.HostAgent.Table;
import kushrestaurant.MarketAgent.Order2;
import kushrestaurant.interfaces.Cook;
import kushrestaurant.interfaces.Customer;
import kushrestaurant.interfaces.Waiter;

public class MockCook implements Cook {

	@Override
	public void MsgHereisTheOrder(Waiter w, Customer c, Table t, String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketReStock(Order2 o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCantRestock() {
		// TODO Auto-generated method stub
		
	}


	public void takenFood(String x) {
		// TODO Auto-generated method stub
		
	}

}
