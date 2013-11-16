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
public class MarketDeliveryTruck extends Agent{
	
	enum marketDeliveryTruckState = waitingForSomethingToDeliver, delivering, returning;
	int amountbeingDelivered;;
	String foodType;
	Role person;
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public MarketDeliveryTruck(String name){
		super();
		this.name = name;
		}
	
	public String getName(){
		return name;
	}


	
	// Messages

	msgTruckDeliverOrder(String foodType, int amount, Role person)
	{
	amountbeingDelivered=amount;
	foodType=foodType;
	person=person;
	marketDeliveryTruckState=delivering;
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
		if (marketDeliveryTruckState==delivering)
			deliverFoodOrder();
			if (marketDeliveryTruckState==returning)
			returnToMarket();
	

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	deliverFoodOrder(){

		if (Role == cook)
			msgCookHereIsFoodSupplyOrder(String foodType, int amount)
		if (Role == homeRole)
			msgPersonHereIsFoodSupplyOrder(String foodType, int amount)
		if (Role == BankManager)
			msgBankManagertHereIsFoodSupplyOrder(String foodType, int amount)
			
		marketDeliveryTruckState=returning;
		}
		returnToMarket(){
		//gui returns truck to market
		marketDeliveryTruckState=waitingForSomethingToDeliver;
		}


	//utilities
	public void setCook(Cook cook) {
		this.cook=cook;
		
	}
	

}


	
