package MarketEmployee;


import market.gui.MarketCustomerGui;
import market.gui.MarketEmployeeGui;
import Person.Role.Role;

/**
 * MarketCustomer Agent
 */
//MarketCustomer Agent
public class MarketCustomer extends Role{
	MarketCustomerGui gui;

	/*String foodTypeWanted;
	int FoodTypeAmount;
	boolean willTakePartialOrder;
	enum marketCustomerState =none, waitingForMarketEmployeeToReturn, replyingToEmployee, leaving
	*/
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public MarketCustomer(){
		activate();
		}
	
	public String getName(){
		return null;
	}


	
	// Messages
/*
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

	//utilities
	public void setCook(Cook cook) {
		this.cook=cook;
		
	}
	*/
	public void setGui(MarketCustomerGui gui) {
	this.gui = gui;
} 

}

