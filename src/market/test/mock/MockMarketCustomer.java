package market.test.mock;

import interfaces.MarketCustomer;

public class MockMarketCustomer extends Mock implements MarketCustomer {

	public MockMarketCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgMarketCustomerAtCounter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketCustomerReadyToTakeOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketCustomerOutofStock(String foodType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketCustomerHereIsOrder(String FoodType, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketCustomerDoYouWantPartialOrder(String foodType,
			int amountAvailable) {
		// TODO Auto-generated method stub
		
	}

}
