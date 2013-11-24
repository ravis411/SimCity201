package MarketEmployee;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.data.Shelves;
import market.gui.MarketEmployeeGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import Person.Role.Role;

/**
 * MarketEmployee Role
 */
public class MarketEmployeeRole extends Role implements MarketEmployee{

	MarketEmployeeGui gui;
	String name = "Market Employee";
	private Semaphore atCounter = new Semaphore(0,false);
	private Semaphore atSteak = new Semaphore(0,false);
	private Semaphore atChicken = new Semaphore(0,false);
	private Semaphore atBurger = new Semaphore(0,false);
	enum MarketEmployeeState
	{walkingToCounter, waiting, collectingOrder, waitingForReplyFromCustomerIfPartialOrderOkay};
	enum MarketEmployeeEvent
	{enteredMarket, atCounter, gotReplyFromCustomerIfPartialOrderOkay, gotCustomerOrder};
	public MarketEmployeeEvent event=MarketEmployeeEvent.enteredMarket;
	public MarketEmployeeState state=MarketEmployeeState.walkingToCounter;
	private MarketCustomer tempCustomer;
	Shelves marketInventory;
	
	List<Order> myOrdersFromManager	= new ArrayList<Order>();
	Order marketCustomerOrder;//since only one marketCustomer can be served at once in person
	
	/**
	 * Constructor for MarketEmployee Role
	 *
	 */
	public MarketEmployeeRole(){
		role="MarketEmployeeRole";
		activate();
		
	}







	
	// Messages
	public void msgMarketEmployeeAtCounter(){
		if (state==MarketEmployeeState.walkingToCounter){
			event=MarketEmployeeEvent.atCounter;
			print("At Counter and waiting for an Order");
			stateChanged();
		}
		else if (state==MarketEmployeeState.collectingOrder){
			atCounter.release();
		}
		
	}
	public void msgMarketEmployeetTellMeWhenICanGiveOrder(MarketCustomerRole marketCustomer){
		tempCustomer= marketCustomer;
		
	}

	public void msgMarketEmployeeOrder(String foodType, int FoodTypeAmount, MarketCustomerRole customer, String name){
		marketCustomerOrder= new Order(foodType, FoodTypeAmount, customer,name);
		print("Has reieved Order from " + customer.getName()+ ": "+ name +" for "+ FoodTypeAmount+ " " + foodType );
		event=MarketEmployeeEvent.gotCustomerOrder;
		stateChanged();
	}
	public void msgMarketEmployeeAtFood1(){
		atSteak.release();
	}
	public void msgMarketEmployeeAtFood2(){
		atChicken.release();
		}
	public void msgMarketEmployeeAtFood3(){
		atBurger.release();
	}

	public void msgMarketEmployeeConfirmPartialOrder(boolean tf, MarketCustomerRole customer){
	marketCustomerOrder.setPartialOrderAcceptable(tf);
	event=MarketEmployeeEvent.gotReplyFromCustomerIfPartialOrderOkay;
	print(customer.getName() + " says Partial Order Okay: "+ tf);
	stateChanged();
	}
	/*
	msgMarketEmployeeAttemptToFillOrder(String foodType, int amount, int orderNumber)
	{
	myOrdersFromManager.add(new Order(foodType, amount, orderNumber);
	}
*/
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if ( state==MarketEmployeeState.walkingToCounter && event==MarketEmployeeEvent.atCounter){
			state=MarketEmployeeState.waiting;
			return true;
		}
		if (state == MarketEmployeeState.waiting && tempCustomer!=null){
		tempCustomer.msgMarketCustomerReadyToTakeOrder();
		tempCustomer=null;
		return true;
		}
		if ( state==MarketEmployeeState.waiting && event==MarketEmployeeEvent.gotCustomerOrder){
			checkStockAndBringAmountAvailableToCustomer();
		}
		if (state==MarketEmployeeState.collectingOrder && event==MarketEmployeeEvent.gotReplyFromCustomerIfPartialOrderOkay){
			partialOrder();
			return true;
		}
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
		return name;
	}



	// Actions
	/*
	void goCollectFoodOrderAndBringToMarketManager(Order order){
		gui.goCollectFoodOrder();
		state=MarketEmployeeState.collectingOrder;
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	msgMarketManagerHereIsAmountWeCanFulfill(order.getFoodType(), order.getAmountAvailable());
	//	myOrdersFromManager.remove(order);
	}
	*/
	
	synchronized void checkStockAndBringAmountAvailableToCustomer()
	{
		gui.goCollectOrRestockFoodOrder();//animation to check stock and then fetch as much as the food requested as possible
		state=MarketEmployeeState.collectingOrder;
		if (marketCustomerOrder.getFoodType() == "Steak")
		{
			try {
				atSteak.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketInventory.size(); i++){
				if (marketInventory.getFoodType(i) == "Steak"){
					if (marketInventory.getAmount(i)>=marketCustomerOrder.getAmount()){
						marketCustomerOrder.setAmountAvailable(marketCustomerOrder.getAmount());
						print(marketInventory.getAmount(i)+ " " + marketInventory.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmount() +" off shelf");
						marketInventory.decrementFoodAmount(i, marketCustomerOrder.getAmount());
					}
					else
					{
						marketCustomerOrder.setAmountAvailable(marketInventory.getAmount(i));
						print(marketInventory.getAmount(i)+ " " + marketInventory.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmountAvailable() +" off shelf");
						marketInventory.decrementFoodAmount(i,marketInventory.getAmount(i));
					}
				}
			}
		}
		else if (marketCustomerOrder.getFoodType() == "Chicken")
		{
			try {
				atSteak.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketInventory.size(); i++){
				if (marketInventory.getFoodType(i) == "Chicken"){
					if (marketInventory.getAmount(i)>=marketCustomerOrder.getAmount()){
						marketCustomerOrder.setAmountAvailable(marketCustomerOrder.getAmount());
						print(marketInventory.getAmount(i)+ " " + marketInventory.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmount() +" off shelf");
						marketInventory.decrementFoodAmount(i, marketCustomerOrder.getAmount());
					}
					else
					{
						marketCustomerOrder.setAmountAvailable(marketInventory.getAmount(i));
						print(marketInventory.getAmount(i)+ " " + marketInventory.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmountAvailable() +" off shelf");
						marketInventory.decrementFoodAmount(i,marketInventory.getAmount(i));
					}
				}
			}
		}
		else if (marketCustomerOrder.getFoodType() == "Burger")
		{
			try {
				atSteak.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketInventory.size(); i++){
				if (marketInventory.getFoodType(i) == "Burger"){
					if (marketInventory.getAmount(i)>=marketCustomerOrder.getAmount()){
						marketCustomerOrder.setAmountAvailable(marketCustomerOrder.getAmount());
						print(marketInventory.getAmount(i)+ " " + marketInventory.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmount() +" off shelf");
						marketInventory.decrementFoodAmount(i,marketCustomerOrder.getAmount());
					}
					else
					{
						marketCustomerOrder.setAmountAvailable(marketInventory.getAmount(i));
						print(marketInventory.getAmount(i)+ " " + marketInventory.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmountAvailable() +" off shelf");
						;
					}
				}
			}
		}
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (marketCustomerOrder.getAmountAvailable()==0){
			marketCustomerOrder.getMyCustomer().msgMarketCustomerOutofStock(marketCustomerOrder.getFoodType());
			marketCustomerOrder=null;
			state=MarketEmployeeState.waiting;
		}
		else if (marketCustomerOrder.getAmountAvailable()<marketCustomerOrder.getAmount()){
			print("Can only Satify Partial Order. Asking Customer if Partial Order Acceptable");
			marketCustomerOrder.getMyCustomer().msgMarketCustomerDoYouWantPartialOrder(marketCustomerOrder.getFoodType(), marketCustomerOrder.getAmountAvailable());
		}
		else{
			marketCustomerOrder.getMyCustomer().msgMarketCustomerHereIsOrder(marketCustomerOrder.getFoodType(), marketCustomerOrder.getAmountAvailable());
			marketCustomerOrder=null;
			state=MarketEmployeeState.waiting;
		}
	}
	
	private void partialOrder() {
		if (marketCustomerOrder.getPartialOrderAcceptable()){
		marketCustomerOrder.getMyCustomer().msgMarketCustomerHereIsOrder(marketCustomerOrder.getFoodType(), marketCustomerOrder.getAmountAvailable());
		marketCustomerOrder=null;
		state=MarketEmployeeState.waiting;
		}
		else{
			restockFood();
			marketCustomerOrder=null;
			state=MarketEmployeeState.waiting;
			

		}
	}


	
	private void restockFood(){
		gui.goCollectOrRestockFoodOrder();//animation to check stock and then fetch as much as the food requested as possible
		//animation to return food to shelves
		if (marketCustomerOrder.getFoodType() == "Steak")
		{
			try {
				atSteak.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketInventory.size(); i++){
				if (marketInventory.getFoodType(i) == "Steak"){
					marketInventory.restockFoodAmount(i,marketCustomerOrder.getAmountAvailable());
				}
					
	
			}
		}
		else if (marketCustomerOrder.getFoodType() == "Chicken")
		{
			try {
				atChicken.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketInventory.size(); i++){
				if (marketInventory.getFoodType(i) == "Chicken"){
					marketInventory.restockFoodAmount(i,marketCustomerOrder.getAmountAvailable());
				}
					
	
			}
		}
		else if (marketCustomerOrder.getFoodType() == "Burger")
		{
			try {
				atBurger.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketInventory.size(); i++){
				if (marketInventory.getFoodType(i) == "Burger"){
					marketInventory.restockFoodAmount(i,marketCustomerOrder.getAmountAvailable());
				}
					
	
			}
		}
	}

	//utilities
	public void setMarketInventory(Shelves marketInventory) {
		this.marketInventory=marketInventory;
	}
	public void setGui(MarketEmployeeGui gui) {
		this.gui = gui;
	} 
	/*	
	}
	
	*/
	private class Order{
		String foodType;
		int amount;
		int orderNumber;
		int amountAvailable;
		boolean partialOrderAcceptable;
		MarketCustomer myCustomer;
		String name;
		
		public Order(String foodType, int foodTypeAmount, MarketCustomer myCustomer,String name) {
			this.myCustomer=myCustomer;
			this.name=name;
			amount=foodTypeAmount;
			this.foodType=foodType;
			
		}

		private void setPartialOrderAcceptable(boolean tf) {
			partialOrderAcceptable=tf;			
		}
		private boolean getPartialOrderAcceptable(){
			return partialOrderAcceptable;
		}
		//enum state= none, partialOrderAcceptable, partialOrderNotAcceptable;
		private int getAmount() {
			return amount;
		}
		private int getAmountAvailable() {
			return amountAvailable;
		}

		private String getFoodType() {
			return foodType;
		}
		private void setAmountAvailable(int amountInInventory){
			amountAvailable=amountInInventory;
		}
		private MarketCustomer getMyCustomer(){
			return myCustomer;
		}
		}
	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}










	
}