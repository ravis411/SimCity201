package interfaces;

import interfaces.generic_interfaces.GenericCook;
import MarketEmployee.MarketEmployeeRole;

public interface MarketManager {

	public abstract void msgMarketManagerReportingForWork(MarketEmployeeRole marketEmployeeRole);

	public abstract void msgMarketManagerHereIsAmountWeCanFulfill(String foodType,
			int amountAvailable, int orderNumber);

	public abstract void msgMarketEmployeeAtDesk();

	public abstract void msgMarketEmployeeAtDeskRelease();

	public abstract void msgMarketEmployeeAtTruck();

	public abstract void msgDeliveryTruckAtDestination();
	
	public abstract void msgMarketManagerHereIsPayment(double moneyPayment);

	public abstract String getMarketName();

	public abstract void msgMarketManagerFoodOrder(String choice,
			int quantityOfOrderThatMarketDoesntHave, GenericCook CookRole);

	public abstract void atHome();




}
