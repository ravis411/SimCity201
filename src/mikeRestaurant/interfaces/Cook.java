package mikeRestaurant.interfaces;

import mikeRestaurant.MarketRole;
import mikeRestaurant.Table;
import mikeRestaurant.WaiterRole;

public interface Cook extends AgentInterface {

	/**
	 * Message sent by the WaiterAgent to give the Cook an order
	 * @param order the order to begin cooking
	 */
	public void msgHereIsAnOrder(Waiter waiter, String choice, Table table);
	
	
	/**
	 * Message sent by the MarketAgent when he is responding to an order for 
	 * more food. Because the orders are sent out per individual food item, we don't
	 * need a list of choices and quantities
	 * @param choice name of the food
	 * @param quantity amount of the food
	 * @param stillHasStock true if the market still has food (of any kind) left, false otherwise
	 * @param market the marketAgent that is sending food
	 */
	public void msgHereIsFoodFromMarket(String choice, int quantity, boolean stillHasStock, Market market);
	
	/**
	 * Message sent by the Market when an order will not be fulfilled
	 * @param choice the choice that couldn't be fulfilled
	 * @param mkt the market that cannot fill the order
	 */
	public void msgOrderWillNotBeFulfilled(String choice, Market mkt);
	
	public void msgAtCookingLocation();
	
	
	public void msgAtPickupLocation();
	
	public void msgAtFridge();
	
}
