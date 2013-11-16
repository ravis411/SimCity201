package MarketEmployee;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Market;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import agent.Agent;
import agent.Constants;

/**
 * MarketCustomer Agent
 */
//MarketCustomer Agent
public class MarketEmployee extends Agent{
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public MarketEmployee(String name){
		super();
		this.name = name;
		}
	
	public String getName(){
		return name;
	}


	
	// Messages

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

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	
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
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
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


	
