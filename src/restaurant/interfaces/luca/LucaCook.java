package restaurant.interfaces.luca;

import restaurant.luca.LucaMarketRole;

public interface LucaCook {

	public abstract void msgAddMarket(LucaMarketRole market);

	public abstract void msgCookHeresAnOrder(int customerTableNumber, String customerChoice, LucaWaiter waiterAgent) ;
	public abstract void msgCookIDoNotHaveFoodSupplyOrdered(String Food);
	public abstract void msgHereAreRequestedFoodSupplies(String foodType, int foodAmount);

	public abstract void msgAtGrill();

	public abstract void msgAtPlatingArea();

	public abstract void msgAtDefaultPosition();

	public abstract void msgAtRefrigerator();

	public abstract void msgCookNumberThatWereOrderedButNotFullfilled(int i, String string);
}
