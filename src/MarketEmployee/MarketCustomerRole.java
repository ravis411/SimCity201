package MarketEmployee;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

import market.gui.MarketCustomerGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import Person.Role.Role;

/**
 * MarketCustomer Agent
 */
//MarketCustomer Agent
public class MarketCustomerRole extends Role implements MarketCustomer{
	MarketCustomerGui gui;
	String name = "Market Customer";
	String foodTypeWanted;
	int foodTypeAmount;
	boolean willTakePartialOrder;
	private Semaphore atCounter = new Semaphore(0,false);
	private Semaphore employeeReadyToTakeOrder = new Semaphore(0,false);
	enum MarketCustomerState 
	{needsToOrder, waitingForMarketEmployeeToReturn, replyingToEmployee, leaving};
	enum MarketCustomerEvent
	{none, firstInLine, employeeBackAndAskedOrderDetail, leaving};
	public MarketCustomerEvent event=MarketCustomerEvent.none;
	public MarketCustomerState state=MarketCustomerState.needsToOrder;
	public MarketEmployee marketEmployee;
	private Map<String, Integer> menu;

	
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public MarketCustomerRole(){
		activate();
		role="MarketCustomerRole";
		menu= new HashMap<String, Integer>();
		menu.put("Steak",  20);
		menu.put("Chicken", 10);
		menu.put("Burger", 15);
		}
	
	public String getName(){
		return name;
	}


	
	// Messages
	public void msgMarketCustomerAtCounter(){
		event=MarketCustomerEvent.firstInLine;
		print("At Counter and about to Order");
		atCounter.release();
	}
	public void msgMarketCustomerReadyToTakeOrder(){
		
		print("Market Employee Says he will take an Order Now");
		employeeReadyToTakeOrder.release();
	}
	

	public void msgMarketCustomerOutofStock(String foodType){
		state= MarketCustomerState.leaving;
		stateChanged();
		}
	 /*
	 *
		msgMarketCustomerDoYouWantPartialOrder(String FoodType, int amount){
		marketCustomerState= replyingToEmployee;
		}
		*/
	public void msgMarketCustomerHereIsOrder(String FoodType, int amount){
		state= MarketCustomerState.leaving;
		stateChanged();
		}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if (event == (MarketCustomerEvent.none) && state==MarketCustomerState.needsToOrder)
		{
			goToMarketEmployeeToOrder();
			return true;
		}
		if (event == (MarketCustomerEvent.employeeBackAndAskedOrderDetail) && state==MarketCustomerState.waitingForMarketEmployeeToReturn){
			return true;
		}
		if (state== MarketCustomerState.leaving)
			leaveMarket();
				
		/*
		if (marketCustomerState== replyingToEmployee)
			tellMarketEmployeeIfPartialOrderAcceptable();
		
*/	
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	void goToMarketEmployeeToOrder(){
		gui.DoGoToCounter();//walk to Counter to Order
		print("Entered Market");
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			marketEmployee.msgMarketEmployeetTellMeWhenICanGiveOrder(this);
			employeeReadyToTakeOrder.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Random random = new Random(System.nanoTime());
		List<String> keys = Collections.synchronizedList(new ArrayList<String>(menu.keySet()));
		foodTypeWanted = keys.get( random.nextInt(keys.size()) ); //selects random food type from menu
	
		foodTypeAmount =random.nextInt(3)+1;// selects amount less than 3 and greater than 1
		
		marketEmployee.msgMarketEmployeeOrder(foodTypeWanted, foodTypeAmount, this, getPerson().getName());
		state= MarketCustomerState.waitingForMarketEmployeeToReturn;

		
		}
		/*
	tellMarketEmployeeIfPartialOrderAcceptable(){
		msgMarketEmployeeConfirmPartialOrder(willTakePartialOrder, MarketCustomer customer);
		if (willTakePartialOrder == false)
		marketCustomerState= leaving;
		}*/
	void leaveMarket(){
		gui.DoLeave();//animation for CustomerRole to leave market
	
	}

	//utilities
	public void setMarketEmployee(MarketEmployeeRole marketEmployee) {
		this.marketEmployee=marketEmployee;
		
	}
	
	public void setGui(MarketCustomerGui gui) {
	this.gui = gui;
}

	@Override
	public boolean canGoGetFood() {
		return false;
	} 

}

