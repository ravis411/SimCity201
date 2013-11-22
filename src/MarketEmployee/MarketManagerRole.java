package MarketEmployee;


import market.gui.MarketManagerGui;
import market.interfaces.MarketManager;
import Person.Role.Role;

/**
 * MarketCustomer Role
 */
//MarketCustomer Agent
public class MarketManagerRole extends Role implements MarketManager{
	
	MarketManagerGui gui;
	/**
	 * Constructor for MarketManager Role
	 *
	 */
	public MarketManagerRole(){
		activate();
		}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}


	
	// Messages
	/*
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
*/

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
	/*	if (!myOrders.isEmpty)
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
	
*/
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
		 
		 
	}

	// Actions
		/*
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
	*/
	public void setGui(MarketManagerGui gui) {
		this.gui = gui;
	} 
	
/*
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
*/
}
	
