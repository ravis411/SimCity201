package mikeRestaurant.interfaces;

public interface Market extends AgentInterface {
	
	/**
	 * Message sent by the CashierAgent in response to a request for money
	 * @param name the item ordered
	 * @param quantity the quantity ordered
	 * @param bill the amount of money owed
	 * @param money the amount of money paid
	 */
	public void msgPaymentResponse(String name, int quantity, double bill, double money);
	
	/**
	 * Message sent by the CookAgent if he is ordering food
	 * @param choice
	 * @param quantity
	 */
	public void msgINeedFood(String choice, int quantity);

}
