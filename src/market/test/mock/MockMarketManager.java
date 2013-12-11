package market.test.mock;

import interfaces.MarketManager;
import interfaces.generic_interfaces.GenericCook;
import MarketEmployee.MarketEmployeeRole;

public class MockMarketManager extends Mock implements MarketManager {

	public MockMarketManager(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgMarketManagerReportingForWork(
			MarketEmployeeRole marketEmployeeRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketManagerHereIsAmountWeCanFulfill(String foodType,
			int amountAvailable, int orderNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketEmployeeAtDesk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketEmployeeAtDeskRelease() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketEmployeeAtTruck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeliveryTruckAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketManagerHereIsPayment(double moneyPayment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMarketName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgMarketManagerFoodOrder(String choice,
			int quantityOfOrderThatMarketDoesntHave, GenericCook CookRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atHome() {
		// TODO Auto-generated method stub
		
	}




	
}
