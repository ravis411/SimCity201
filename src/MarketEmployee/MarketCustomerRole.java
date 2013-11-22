package MarketEmployee;


import market.gui.MarketCustomerGui;
import market.interfaces.MarketCustomer;
import Person.Role.Role;

/**
 * MarketCustomer Agent
 */
//MarketCustomer Agent
public class MarketCustomerRole extends Role implements MarketCustomer{
	MarketCustomerGui gui;
	String name = "Market Customer";
	String foodTypeWanted;
	int FoodTypeAmount;
	boolean willTakePartialOrder;
	enum MarketCustomerState 
	{none, waitingForMarketEmployeeToReturn, replyingToEmployee, leaving};
	enum marketCustomerEvent
	{none, firstInLine, replyingToEmployee, leaving};
	public marketCustomerEvent event;
	public MarketEmployeeRole marketEmployee;
	
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public MarketCustomerRole(){
		activate();
		event=marketCustomerEvent.none;
		role="MarketCustomerRole";
		}
	
	public String getName(){
		return name;
	}


	
	// Messages
	public void msgMarketCustomerAtCounter(){
		event=marketCustomerEvent.firstInLine;
		print("At Counter and about to Order");
		stateChanged();
	}
 /*
 *
	msgMarketCustomerOutofStock(String foodType){
		marketCustomerState= leaving;
		}
		
		msgMarketCustomerDoYouWantPartialOrder(String FoodType, int amount){
		marketCustomerState= replyingToEmployee;
		}
		
		msgMarketCustomerHereIsOrder(String FoodType, int amount){
		marketCustomerState= leaving;
		}
*/
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if (event == (marketCustomerEvent.firstInLine))
				return true;
		/*if (marketCustomerState== none)
			goToMarketEmployeeToOrder();
		if (marketCustomerState== replyingToEmployee)
			tellMarketEmployeeIfPartialOrderAcceptable();
		if (marketCustomerState== leaving)
			leaveMarket();
*/	
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	/*
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
*/
	//utilities
	public void setMarketEmployee(MarketEmployeeRole marketEmployee) {
		this.marketEmployee=marketEmployee;
		
	}
	
	public void setGui(MarketCustomerGui gui) {
	this.gui = gui;
} 

}

