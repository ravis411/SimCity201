package MarketEmployee;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

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
	{walkingToCounter, waiting, collectingOrder};
	enum MarketEmployeeEvent
	{enteredMarket, atCounter, gotCustomerOrder};
	public MarketEmployeeEvent event=MarketEmployeeEvent.enteredMarket;
	public MarketEmployeeState state=MarketEmployeeState.walkingToCounter;
	private MarketCustomer tempCustomer;
	List<Inventory> marketInventory	= new ArrayList<Inventory>();
	
	List<Order> myOrdersFromManager	= new ArrayList<Order>();
	Order marketCustomerOrder;//since only one marketCustomer can be served at once in person
	
	/**
	 * Constructor for MarketEmployee Role
	 *
	 */
	public MarketEmployeeRole(){
		role="MarketEmployeeRole";
		activate();
		marketInventory.add(new Inventory("Steak",0));
		marketInventory.add(new Inventory("Chicken",0));
		marketInventory.add(new Inventory("Burger",0));
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
	/*
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
			checkStockAndBringAmountAvailableToCustomer(marketCustomerOrder);
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
	
	void checkStockAndBringAmountAvailableToCustomer(Order order)
	{
		gui.goCollectFoodOrder();//animation to check stock and then fetch as much as the food requested as possible
		state=MarketEmployeeState.collectingOrder;
		if (order.getFoodType() == "Steak")
		{
			try {
				atSteak.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketInventory.size(); i++){
				if (marketInventory.get(i).getFoodType() == "Steak"){
					order.setAmountAvailable(marketInventory.get(i).getAmount());
					print(marketInventory.get(i).getAmount()+ " " + marketInventory.get(i).getFoodType()+" on shelve.");
				}
			}
		}
		else if (order.getFoodType() == "Chicken")
		{
			try {
				atChicken.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketInventory.size(); i++){
				if (marketInventory.get(i).getFoodType() == "Steak"){
					order.setAmountAvailable(marketInventory.get(i).getAmount());
					print(marketInventory.get(i).getAmount()+ " " + marketInventory.get(i).getFoodType()+" on shelve.");

				}
			}
		}
		else if (order.getFoodType() == "Burger")
		{
			try {
				atBurger.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketInventory.size(); i++){
				if (marketInventory.get(i).getFoodType() == "Steak"){
					order.setAmountAvailable(marketInventory.get(i).getAmount());
					print(marketInventory.get(i).getAmount()+ " " + marketInventory.get(i).getFoodType()+" on shelve.");

				}
			}
		}
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (order.getAmountAvailable()==0){
			order.getMyCustomer().msgMarketCustomerOutofStock(order.getFoodType());
	//	marketCustomerOrder==null;
		}
	//	else if (order.getAmountAvailable()<order.getAmount()){
	//	msgMarketCustomerDoYouWantPartialOrder(String FoodType, int amount);
	//	}
		else{
	//	msgMarketCustomerHereIsOrder(String FoodType, int amount);
		marketCustomerOrder=null;
		}
	}
	/*
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
		//enum state= none, partialOrderAcceptable, partialOrderNotAcceptable;

		private int getAmountAvailable() {
			return amountAvailable;
		}

		private String getFoodType() {
			return foodType;
		}
		private void setAmountAvailable(int amount){
			amountAvailable=amount;
		}
		private MarketCustomer getMyCustomer(){
			return myCustomer;
		}
		}

	

	private class Inventory{
		String foodType;
		int amount;
		Inventory(String foodType, int amount){
			this.foodType=foodType;
			this.amount=amount;
		}
		public int getAmount() {
			return amount;
			
		}
		public String getFoodType() {
			return foodType;
		}
		}

	
}