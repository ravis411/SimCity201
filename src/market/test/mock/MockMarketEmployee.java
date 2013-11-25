package market.test.mock;

import market.interfaces.MarketEmployee;
import MarketEmployee.MarketCustomerRole;

public class MockMarketEmployee extends Mock implements MarketEmployee {

	public MockMarketEmployee(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void msgMarketEmployeeOrder(String foodType, int FoodTypeAmount,
			MarketCustomerRole customer, String string) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeetTellMeWhenICanGiveOrder(
			MarketCustomerRole marketCustomer) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeeAtCounter() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeeAtFood1() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeeAtFood2() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeeAtFood3() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeeConfirmPartialOrder(
			boolean willTakePartialOrder, MarketCustomerRole marketCustomerRole) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeeYourCounterNumber(int i) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeeAtManager() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeeAttemptToFillOrder(String foodType,
			int amount, int orderNumber) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgMarketEmployeeAtManagerRelease() {
		// TODO Auto-generated method stub
		
	}

}
