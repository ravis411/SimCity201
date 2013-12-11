package restaurant.interfaces.luca;

import interfaces.MarketManager;

public interface LucaCashier {
	
	public abstract void msgCashierComputeBill(String customerChoice, LucaCustomer customer, LucaWaiter waiter);
	
	public abstract void msgCashierPayForOrder(double money, LucaCustomer customer);

	public abstract void msgCashierHereIsMarketBill(double orderPrice, MarketManager market);

}
