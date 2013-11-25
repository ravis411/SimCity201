package market.test.mock;

import MarketEmployee.MarketEmployeeRole;
import market.interfaces.MarketManager;

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

	
}
