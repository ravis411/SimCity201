package market.interfaces;

import MarketEmployee.MarketCustomerRole;

public interface MarketEmployee {

	public abstract void msgMarketEmployeeOrder(String foodType, int FoodTypeAmount, MarketCustomerRole customer, String string);

	public abstract void msgMarketEmployeetTellMeWhenICanGiveOrder(MarketCustomerRole marketCustomer);

	public abstract void msgMarketEmployeeAtCounter();

	public abstract void msgMarketEmployeeAtFood1();

	public abstract void msgMarketEmployeeAtFood2();

	public abstract void msgMarketEmployeeAtFood3();
}
