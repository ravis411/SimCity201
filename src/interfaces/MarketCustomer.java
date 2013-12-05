package interfaces;



public interface MarketCustomer {
	public abstract void msgMarketCustomerAtCounter();

		
	public abstract void msgMarketCustomerReadyToTakeOrder();

	public abstract void msgMarketCustomerOutofStock(String foodType);
	public abstract void msgMarketCustomerHereIsOrder(String FoodType, int amount);

	public abstract void msgMarketCustomerDoYouWantPartialOrder(
			String foodType, int amountAvailable);
}
