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
public class MarketCustomer extends Agent{
	String foodTypeWanted;
	int FoodTypeAmount;
	boolean willTakePartialOrder;
	enum marketCustomerState =none, waitingForMarketEmployeeToReturn, replyingToEmployee, leaving
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public MarketCustomer(String name){
		super();
		this.name = name;
		}
	
	public String getName(){
		return name;
	}


	
	// Messages

	msgMarketCustomerOutofStock(String foodType){
		marketCustomerState= leaving;
		}
		
		msgMarketCustomerDoYouWantPartialOrder(String FoodType, int amount){
		marketCustomerState= replyingToEmployee;
		}
		
		msgMarketCustomerHereIsOrder(String FoodType, int amount){
		marketCustomerState= leaving;
		}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (marketCustomerState== none)
			goToMarketEmployeeToOrder();
		if (marketCustomerState== replyingToEmployee)
			tellMarketEmployeeIfPartialOrderAcceptable();
		if (marketCustomerState== leaving)
			leaveMarket();

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	goToMarketEmployeeToOrder(){
		//walk to Order Window if line wait in line and when customer first in line
		msgMarketEmployeeOrder(foodTypeWanted, FoodTypeAmount, this)
		marketCustomerState= waitingForMarketEmployeeToReturn;
		}
		
	tellMarketEmployeeIfPartialOrderAcceptable(){
		msgMarketEmployeeConfirmPartialOrder(willTakePartialOrder, MarketCustomer customer);
		if (willTakePartialOrder == false)
		marketCustomerState= leaving;
		}
	leaveMarket(){
	//animation for CustomerRole to leave market
	}

	//utilities
	public void setCook(Cook cook) {
		this.cook=cook;
		
	}
	

}


	
