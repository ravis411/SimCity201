package restaurant.interfaces.luca;

import interfaces.MarketManager;
import MarketEmployee.MarketEmployeeRole;


public interface LucaCook {

	public abstract void msgAddMarket(MarketManager market);

	public abstract void msgCookHeresAnOrder(int customerTableNumber, String customerChoice, LucaWaiter waiterAgent) ;
	public abstract void msgOrderNotFilled(int ingredientNum);
	public abstract void msgOrderFilled(String foodType, int foodAmount);

	public abstract void msgAtGrill();

	public abstract void msgAtPlatingArea();

	public abstract void msgAtDefaultPosition();

	public abstract void msgAtRefrigerator();

	public abstract void msgCookNumberThatWereOrderedButNotFullfilled(int i, String string);
}
