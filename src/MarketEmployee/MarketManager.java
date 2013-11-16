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
public class MarketManager extends Agent{
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public MarketManager(String name){
		super();
		this.name = name;
		}
	
	public String getName(){
		return name;
	}


	
	// Messages
	msgMarketManagerFoodOrder(String foodType, int amount, BankManager bankManager)
	{
		myOrders.add(new Order(foodType, amount, bankManager);
	}
	msgMarketManagerFoodOrder(String foodType, int amount, Cook cook)
	{
		myOrders.add(new Order(foodType, amount, cook);
	}
	
	msgMarketManagerFoodOrder(String foodType, int amount, HomeRole homePerson)
	{
		myOrders.add(new Order(foodType, amount, homePerson);
	}
	msgMarketManagerHereIsPayment(int moneyPayment)
	{
		marketMoney= marketMoney+moneyPayment;
	}
	msgMarketManagerHereIsAmountWeCanFulfull(String foodType, int FoodTypeAmount, int orderNumber){
		for all orders in myOrders
			if orderNumber == an OrderNumber in myOrders
			order.setOrderState(Processed);
			order.setAmountReadyToBeShipped(FoodTypeAmount);
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (!myOrders.isEmpty)
			for all orders in MyOrder
			{
			if (order state == none)
				{
					giveOrderToMarketEmployee(Order order);
					break;
				}
			}
			for all orders in MyOrder
			{
				if (order state == none)
				{
					giveOrderToMarketEmployee(Order order);
					break;
				}
				if (order state == Processed)
				shipAndOrNotifyCustomerOfOrderProblems(Order order);
				{
			}
	

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
			giveOrderToMarketEmployee()
			{
			msgMarketEmployeeAttemptToFillOrder(String foodType, int amount, int orderNumber)
			order.setOrderState(waitingForOrder);
			}
			
			shipAndOrNotifyCustomerOfOrderProblems(Order order){
			
				if (order.getRole == cook)
				{
					if (order.getamountReadyToBeShipped()==0){
						msgCookIDoNotHaveFoodSupplyOrdered(order.foodType);
						}
					else if (order.getamountReadyToBeShipped()<order.getAmount()){
						msgCookNumberThatWereOrderedButNotFullfilled((order.getAmount()-order.getamountReadyToBeShipped()), order.getFoodType)
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
					}
					else
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
				}
				
				if (order.getRole == homeRole)
				{
					if (order.getamountReadyToBeShipped()==0){
						msgPersonIDoNotHaveFoodSupplyOrdered(order.foodType);
						}
					else if (order.getamountReadyToBeShipped()<order.getAmount()){
						msgPersonNumberThatWereOrderedButNotFullfilled((order.getAmount()-order.getamountReadyToBeShipped()), order.getFoodType)
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
					}
					else
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
				}

				
				if (order.getRole == BankManager)
				{
					if (order.getamountReadyToBeShipped()==0){
						msgBankManagerIDoNotHaveFoodSupplyOrdered(order.foodType);
						}
					else if (order.getamountReadyToBeShipped()<order.getAmount()){
						msgBankManagerNumberThatWereOrderedButNotFullfilled((order.getAmount()-order.getamountReadyToBeShipped()), order.getFoodType)
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
					}
					else
						msgTruckDeliverOrder(order.foodType, order.getamountReadyToBeShipped(), order.getRole());
				}
				
				remove order from myOrders
			}
			


	//utilities
	public void setCook(Cook cook) {
		this.cook=cook;
		
	}
	

	Class Order{
		enum state= none, waitingForOrder, orderProcessed
		String foodType;
		int amount;
		AgentRole role;
		int orderNumber;
		int amountReadyToBeShipped;
		}
		ArrayList myOrders;
		int marketMoney;
}


	
