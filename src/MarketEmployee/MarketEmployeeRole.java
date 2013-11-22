package MarketEmployee;


import market.gui.MarketEmployeeGui;
import market.interfaces.MarketEmployee;
import Person.Role.Role;

/**
 * MarketEmployee Role
 */
//MarketCustomer Agent
public class MarketEmployeeRole extends Role implements MarketEmployee{
	
	MarketEmployeeGui gui;

	/**
	 * Constructor for MarketEmployee Role
	 *
	 */
	public MarketEmployeeRole(){
			
		activate();
	}







	
	// Messages
/*
	msgMarketEmployeeOrder(String foodType, int FoodTypeAmount, MarketCustomer customer){
	marketCustomerOrder= new Order(foodType, FoodTypeAmount, customer);
	}
	
	msgMarketEmployeeConfirmPartialOrder(boolean tf, MarketCustomer customer){
	marketCustomerOrder.setSartialOrderAcceptable(tf);
	}

	msgMarketEmployeeAttemptToFillOrder(String foodType, int amount, int orderNumber)
	{
	myOrdersFromManager.add(new Order(foodType, amount, orderNumber);
	}
*/
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
	/*
		if (!myOrders.isEmpty){
			for all orders in MyOrder
			{
				goCollectFoodOrderAndBringToMarketManager(order);
				break;
			}
			if (marketCustomerOrder != null && marketCustomerOrder.getState()==none)
			{
				checkStockAndBringAmountAvailableToCustomer(marketCustomerOrder);
			}
			if (marketCustomerOrder != null && marketCustomerOrder.getState()==partialOrderAcceptable)
			{
				marketCustomerOrder.msgMarketCustomerHereIsOrder(marketCustomerOrder.getFoodType(), marketCustomerOrder.getamountAvailable());
				marketCustomerOrder==null;
			}
			if (marketCustomerOrder != null && marketCustomerOrder.getState()==partialOrderNotAcceptable)
			{
				restockFood(marketCustomerOrder);
			}
		}
		*/
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}




	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}



	// Actions
	/*
	goCollectFoodOrderAndBringToMarketManager(order){
		//animation to go pickup food from shelves
		msgMarketManagerHereIsAmountWeCanFulfill(order.getFoodType(), order.getAmountAvailable());
		myOrdersFromManager.remove(order);
	}

	checkStockAndBringAmountAvailableToCustomer(Order order)
	{
		//animation to check stock and then fetch as much as the food requested as possible
		if (order.getAmountAvailable()==0){
		msgMarketCustomerOutofStock(String foodType);
		marketCustomerOrder==null;
		}
		else if (order.getAmountAvailable()<order.getAmount()){
		msgMarketCustomerDoYouWantPartialOrder(String FoodType, int amount);
		}
		else{
		msgMarketCustomerHereIsOrder(String FoodType, int amount);
		marketCustomerOrder==null;

		}
	}
	restockFood(Order order){
	//animation to return food to shelves
	for all foodtype in storeInventory{
		if foodtype == order.getfood()
		add order.getAmount() to inventory of Foodtype
		}
	marketCustomerOrder==null;
	}

	//utilities
	public void setCook(Cook cook) {
		this.cook=cook;
		*/
		public void setGui(MarketEmployeeGui gui) {
		this.gui = gui;
	} 
	/*	
	}
	
	
	Class Order{
		enum state= none, partialOrderAcceptable, partialOrderNotAcceptable;
		String foodType;
		int amount;
		int orderNumber;
		int amountAvailable;
		boolean partialOrderAcceptable;
		}

		Class Inventory{
		String foodType;
		int amount;
		}
		ArrayList<Inventory> storeInventory;
		ArrayList<Order> myOrdersFromManager;
		Order marketCustomerOrder;//since only one marketCustomer can be served at once in person

}
*/

	
}