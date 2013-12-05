package restaurant.interfaces.luca;

public interface LucaMarket {
	public abstract String getName();
	public abstract void msgMarketOrderFood(String Food, int amount);
	public abstract void msgMarketHereIsPayment(int moneyOwed);

}
