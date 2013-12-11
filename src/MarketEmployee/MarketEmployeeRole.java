package MarketEmployee;


import interfaces.MarketCustomer;
import interfaces.MarketEmployee;
import interfaces.MarketManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.data.MarketData;
import market.gui.MarketEmployeeGui;
import market.test.mock.EventLog;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Employee;
import Person.Role.ShiftTime;
import agent.Constants;

/**
 * MarketEmployee Role
 */
public class MarketEmployeeRole extends Employee implements MarketEmployee{
	public EventLog log= new EventLog();
	MarketEmployeeGui gui;
	String name = "Market Employee";
	private Semaphore atCounter = new Semaphore(0,false);
	private Semaphore atSteak = new Semaphore(0,false);
	private Semaphore atChicken = new Semaphore(0,false);
	private Semaphore atBurger = new Semaphore(0,false);
	private Semaphore atManager = new Semaphore(0,false);
	enum MarketEmployeeState
	{gettingCounterAssignmentFromManager,walkingToCounter, waiting, collectingOrder, waitingForReplyFromCustomerIfPartialOrderOkay};
	enum MarketEmployeeEvent
	{enteredMarket,atManager, atCounter, gotReplyFromCustomerIfPartialOrderOkay, gotCustomerOrder, getOrderForManager};
	public MarketEmployeeEvent event=MarketEmployeeEvent.enteredMarket;
	public MarketEmployeeState state=MarketEmployeeState.gettingCounterAssignmentFromManager;
	private MarketCustomer tempCustomer;
	MarketData marketData;
	private MarketManager myManager;
	
	List<Order> myOrdersFromManager	= new ArrayList<Order>();
	Order marketCustomerOrder;//since only one marketCustomer can be served at once in person
	
	/**
	 * Constructor for MarketEmployee Role
	 *
	 */
	public MarketEmployeeRole(String location){
	super(location);
	}



	public String getNameOfRole() {
		return MARKET_EMPLOYEE_ROLE;
	}






	
	// Messages
	
	public void msgMarketEmployeeYourCounterNumber(int i) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Manager Assigned Me Counter #" +(i+1) );
		gui.setCounter(i);
		stateChanged();
	}


	public void msgMarketEmployeeAtCounter(){
		if (state==MarketEmployeeState.walkingToCounter){
			event=MarketEmployeeEvent.atCounter;
			AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "At Counter and waiting for an Order");
			stateChanged();
		}
		else if (state==MarketEmployeeState.collectingOrder){
			atCounter.release();
		}
		
	}
	public void msgMarketEmployeetTellMeWhenICanGiveOrder(MarketCustomerRole marketCustomer){
		tempCustomer= marketCustomer;
		stateChanged();
	}

	public void msgMarketEmployeeOrder(String foodType, int FoodTypeAmount, MarketCustomerRole customer, String name){
		marketCustomerOrder= new Order(foodType, FoodTypeAmount, customer,name);
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Has reieved Order from " + customer.getNameOfRole()+ ": "+ name +" for "+ FoodTypeAmount+ " " + foodType );
		event=MarketEmployeeEvent.gotCustomerOrder;
		stateChanged();
	}
	public void msgMarketEmployeeAtManager(){
		event=MarketEmployeeEvent.atManager;
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
	public void msgMarketEmployeeAtManagerRelease() {
		atManager.release();
		
	}

	public void msgMarketEmployeeConfirmPartialOrder(boolean tf, MarketCustomerRole customer){
	marketCustomerOrder.setPartialOrderAcceptable(tf);
	event=MarketEmployeeEvent.gotReplyFromCustomerIfPartialOrderOkay;
	AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), customer.getNameOfRole() + " says Partial Order Okay: "+ tf);
	stateChanged();
	}
	
	public void msgMarketEmployeeAttemptToFillOrder(String foodType, int amount, int orderNumber)
	{
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Has reieved Order number: " + orderNumber+ " from Market Manager for "+ amount+ " " + foodType );
		myOrdersFromManager.add(new Order(foodType, amount, orderNumber));
		event=MarketEmployeeEvent.getOrderForManager;
		stateChanged();
	}

	
	public void msgMarketEmployeePayment(int moneyAmount) {
		marketData.giveMarketMoney(moneyAmount);
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Recieved money from customer and Added it to the Markets Money. Market Now has $" + marketData.getMarketMoney());
		
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
	
		if ( state==MarketEmployeeState.gettingCounterAssignmentFromManager && event==MarketEmployeeEvent.atManager){
			
			while (marketData.getMarketManager() == null){
				try {
					Thread.sleep(1 * Constants.SECOND);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			myManager=marketData.getMarketManager();
		
			myManager.msgMarketManagerReportingForWork(this);

			state=MarketEmployeeState.walkingToCounter;
			return true;
		}
		if ( state==MarketEmployeeState.walkingToCounter && event==MarketEmployeeEvent.atCounter){
			state=MarketEmployeeState.waiting;
			return true;
		}
		if (state == MarketEmployeeState.waiting && tempCustomer!=null){
		tempCustomer.msgMarketCustomerReadyToTakeOrder();
		tempCustomer=null;
		return true;
		}
		if ( state==MarketEmployeeState.waiting && event==MarketEmployeeEvent.gotCustomerOrder && marketCustomerOrder!=null){
			checkStockAndBringAmountAvailableToCustomer();
			return true;
		}
		if (state==MarketEmployeeState.collectingOrder && event==MarketEmployeeEvent.gotReplyFromCustomerIfPartialOrderOkay){
			partialOrder();
			return true;
		}
		
		if (!myOrdersFromManager.isEmpty() && marketCustomerOrder==null && event==MarketEmployeeEvent.getOrderForManager){
			
			goCollectFoodOrderAndBringToMarketManager(myOrdersFromManager.get(0));
			
			return true;
			}
		if(workState == WorkState.ReadyToLeave)
		{
			kill();
			return true;
		}
		
			
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	// Actions
	
	synchronized void goCollectFoodOrderAndBringToMarketManager(Order order){
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Gettting Order for Manager");
		state=MarketEmployeeState.collectingOrder;
		gui.goCollectFoodOrderForManager();
		if (order.getFoodType() == "Steak")
		{
			try {
				atSteak.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketData.size(); i++){
				if (marketData.getFoodType(i) == "Steak"){
					if (marketData.getAmount(i)>=order.getAmount()){
						order.setAmountAvailable(order.getAmount());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +order.getAmount() +" off shelf");
						marketData.decrementFoodAmount(i, order.getAmount());
					}
					else
					{
						order.setAmountAvailable(marketData.getAmount(i));
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +order.getAmountAvailable() +" off shelf");
						marketData.decrementFoodAmount(i,marketData.getAmount(i));
					}
				}
			}
		}
		else if (order.getFoodType() == "Chicken")
		{
			try {
				atSteak.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketData.size(); i++){
				if (marketData.getFoodType(i) == "Chicken"){
					if (marketData.getAmount(i)>=order.getAmount()){
						order.setAmountAvailable(order.getAmount());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +order.getAmount() +" off shelf");
						marketData.decrementFoodAmount(i, order.getAmount());
					}
					else
					{
						order.setAmountAvailable(marketData.getAmount(i));
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +order.getAmountAvailable() +" off shelf");
						marketData.decrementFoodAmount(i,marketData.getAmount(i));
					}
				}
			}
		}
		else if (order.getFoodType() == "Burger")
		{
			try {
				atSteak.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i =0; i<marketData.size(); i++){
				if (marketData.getFoodType(i) == "Burger"){
					if (marketData.getAmount(i)>=order.getAmount()){
						order.setAmountAvailable(order.getAmount());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +order.getAmount() +" off shelf");
						marketData.decrementFoodAmount(i,order.getAmount());
					}
					else
					{
						order.setAmountAvailable(marketData.getAmount(i));
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +order.getAmountAvailable() +" off shelf");
						
					}
				}
			}
		}
		AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Bringing Manager Order #" +order.getOrderNumber());
		try {
			atManager.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		myManager.msgMarketManagerHereIsAmountWeCanFulfill(order.getFoodType(), order.getAmountAvailable(), order.getOrderNumber());
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = MarketEmployeeState.waiting;
	

		//myManager.msgMarketManagerHereIsAmountWeCanFulfill(order.getFoodType(), order.getAmountAvailable(), order.getOrderNumber());
		myOrdersFromManager.remove(0);
		}
	
	
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
			for (int i =0; i<marketData.size(); i++){
				if (marketData.getFoodType(i) == "Steak"){
					if (marketData.getAmount(i)>=marketCustomerOrder.getAmount()){
						marketCustomerOrder.setAmountAvailable(marketCustomerOrder.getAmount());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmount() +" off shelf");
						marketData.decrementFoodAmount(i, marketCustomerOrder.getAmount());
					}
					else
					{
						marketCustomerOrder.setAmountAvailable(marketData.getAmount(i));
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmountAvailable() +" off shelf");
						marketData.decrementFoodAmount(i,marketData.getAmount(i));
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
			for (int i =0; i<marketData.size(); i++){
				if (marketData.getFoodType(i) == "Chicken"){
					if (marketData.getAmount(i)>=marketCustomerOrder.getAmount()){
						marketCustomerOrder.setAmountAvailable(marketCustomerOrder.getAmount());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmount() +" off shelf");
						marketData.decrementFoodAmount(i, marketCustomerOrder.getAmount());
					}
					else
					{
						marketCustomerOrder.setAmountAvailable(marketData.getAmount(i));
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmountAvailable() +" off shelf");
						marketData.decrementFoodAmount(i,marketData.getAmount(i));
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
			for (int i =0; i<marketData.size(); i++){
				if (marketData.getFoodType(i) == "Burger"){
					if (marketData.getAmount(i)>=marketCustomerOrder.getAmount()){
						marketCustomerOrder.setAmountAvailable(marketCustomerOrder.getAmount());
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmount() +" off shelf");
						marketData.decrementFoodAmount(i,marketCustomerOrder.getAmount());
					}
					else
					{
						marketCustomerOrder.setAmountAvailable(marketData.getAmount(i));
						AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), marketData.getAmount(i)+ " " + marketData.getFoodType(i)+" on shelve and took " +marketCustomerOrder.getAmountAvailable() +" off shelf");
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
			AlertLog.getInstance().logMessage(AlertTag.MARKET, getNameOfRole(), "Can only Satify Partial Order. Asking Customer if Partial Order Acceptable");
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
			for (int i =0; i<marketData.size(); i++){
				if (marketData.getFoodType(i) == "Steak"){
					marketData.restockFoodAmount(i,marketCustomerOrder.getAmountAvailable());
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
			for (int i =0; i<marketData.size(); i++){
				if (marketData.getFoodType(i) == "Chicken"){
					marketData.restockFoodAmount(i,marketCustomerOrder.getAmountAvailable());
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
			for (int i =0; i<marketData.size(); i++){
				if (marketData.getFoodType(i) == "Burger"){
					marketData.restockFoodAmount(i,marketCustomerOrder.getAmountAvailable());
				}
					
	
			}
		}
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deactivate(){
		super.deactivate();
//		kill();
		

	}
	
	public void kill(){
		super.kill();
		event=MarketEmployeeEvent.enteredMarket;
		state=MarketEmployeeState.gettingCounterAssignmentFromManager;
	}

	//utilities
	public void setMarketData(MarketData marketInventory) {
		this.marketData=marketInventory;
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

	

		public Order(String foodType, int foodTypeAmount, int orderNumber) {
			amount=foodTypeAmount;
			this.foodType=foodType;
			this.orderNumber=orderNumber;
			
		}
		private int getOrderNumber() {

			return orderNumber;
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



	@Override
	public ShiftTime getShift() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
	}


































	
}