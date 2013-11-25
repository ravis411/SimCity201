package market.interfaces;

import MarketEmployee.MarketEmployeeRole;

public interface MarketManager {

	void msgMarketManagerReportingForWork(MarketEmployeeRole marketEmployeeRole);

	void msgMarketManagerHereIsAmountWeCanFulfill(String foodType,
			int amountAvailable, int orderNumber);

	void msgMarketEmployeeAtDesk();

}
