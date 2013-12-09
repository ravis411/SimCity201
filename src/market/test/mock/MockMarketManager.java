package market.test.mock;

import interfaces.MarketManager;
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
	public void msgDeliveryTruckBackAtMarket() {
		// TODO Auto-generated method stub
		
	}



	
}
