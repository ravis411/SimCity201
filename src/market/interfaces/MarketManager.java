package market.interfaces;

import MarketEmployee.MarketEmployeeRole;

public interface MarketManager {

	public abstract void msgMarketManagerReportingForWork(MarketEmployeeRole marketEmployeeRole);

	public abstract void msgMarketManagerHereIsAmountWeCanFulfill(String foodType,
			int amountAvailable, int orderNumber);

	public abstract void msgMarketEmployeeAtDesk();

	public abstract void msgMarketEmployeeAtDeskRelease();

	public abstract void msgMarketEmployeeAtTruck();


}
