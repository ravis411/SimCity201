package interfaces;

import MarketEmployee.MarketCustomerRole;

public interface MarketEmployee {


	public abstract void msgMarketEmployeeOrder(String foodType, int FoodTypeAmount, MarketCustomerRole customer, String string);

	public abstract void msgMarketEmployeetTellMeWhenICanGiveOrder(MarketCustomerRole marketCustomer);

	public abstract void msgMarketEmployeeAtCounter();

	public abstract void msgMarketEmployeeAtFood1();

	public abstract void msgMarketEmployeeAtFood2();

	public abstract void msgMarketEmployeeAtFood3();

	public abstract void msgMarketEmployeeConfirmPartialOrder(
			boolean willTakePartialOrder, MarketCustomerRole marketCustomerRole);

	public abstract void msgMarketEmployeeYourCounterNumber(int i);

	public abstract void msgMarketEmployeeAtManager();
	public abstract void msgMarketEmployeeAttemptToFillOrder(String foodType, int amount, int orderNumber);

	public abstract void msgMarketEmployeeAtManagerRelease();

	public abstract void msgMarketEmployeePayment(int i);
}
