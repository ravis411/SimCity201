package restaurant.luca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import restaurant.gui.luca.CookGui;
import restaurant.interfaces.luca.LucaCook;
import restaurant.interfaces.luca.LucaWaiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import agent.Agent;
import agent.Constants;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LucaCookAgent extends Agent implements LucaCook{
	private CookGui cookGui;
	private Semaphore atRefrigerator = new Semaphore(0,false);
	private Semaphore atGrill = new Semaphore(0,false);
	private Semaphore atPlatingArea = new Semaphore(0,false);
	private Semaphore atDefaultPos = new Semaphore(0,false);
	public Collection<Order> myWaitingOrders;
	public Collection<Order> myRejectedOrders;
	private String name;
	private int marketCurrentlyBeingAskedForFood;
	public List<LucaWaiter> waiters
	= Collections.synchronizedList(new ArrayList<LucaWaiter>());
	public List<LucaMarketAgent> markets
	= Collections.synchronizedList(new ArrayList<LucaMarketAgent>());
	public List<Food> foodTypes
	= Collections.synchronizedList(new ArrayList<Food>());
	public EventLog log= new EventLog();
	private int howMuchFoodAgentAsksFromMarket= 2;
	private Order tempOrderToBeDeleted;
	public enum AgentEvent 
	{none, recievedOrder, foodOutOfStock, MarketAskedIfTheyHaveFoodType, orderDoneCooking, waiterHasBeenNotified};
	private AgentEvent event = AgentEvent.none;
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public LucaCookAgent(String name){
		super();
		this.name = name;
		marketCurrentlyBeingAskedForFood =0;
		foodTypes.add(new Food("Steak", 7, 1));//Food type, cooktime, quantity
		foodTypes.add(new Food("Chicken", 3, 1));//Food type, cooktime, quantity
		foodTypes.add(new Food("Burger", 5, 1));//Food type, cooktime, quantity
		myWaitingOrders = Collections.synchronizedCollection(new ArrayList<Order>());
		myRejectedOrders = Collections.synchronizedCollection(new ArrayList<Order>());
	}
	
	public String getName(){
		return name;
	}
	/**
	 * hack to establish connection to Host agent.
	 */


	
	// Messages

	public void msgAddMarket(LucaMarketAgent market) {
		markets.add(market);
	}

	public void msgCookHeresAnOrder(int customerTableNumber, String customerChoice, LucaWaiter waiterAgent) {
		 log.add(new LoggedEvent("Recieved msgCookHeresAnOrder. customerTableNumber: " + 
					customerTableNumber + " customerChoice: " + customerChoice + "waiterAgent: " + waiterAgent));
		waiters.add(waiterAgent);
		myWaitingOrders.add(new Order(customerTableNumber, customerChoice, waiterAgent));
		event = AgentEvent.recievedOrder;
		stateChanged();
		
	}
	public void msgCookIDoNotHaveFoodSupplyOrdered(String Food){
		for(int i =0; i<foodTypes.size(); i++)
			if (foodTypes.get(i).getChoice() == Food){
			foodTypes.get(i).setMoreOrderedAndOnTheWay(false);
			marketCurrentlyBeingAskedForFood++;
			}
			event =  AgentEvent.foodOutOfStock;
			stateChanged();
		
	}

	public void msgCookNumberThatWereOrderedButNotFullfilled(int num, String Type) {
		marketCurrentlyBeingAskedForFood++;
		if (marketCurrentlyBeingAskedForFood==4){
			marketCurrentlyBeingAskedForFood=0;
			event = AgentEvent.none;
			stateChanged();
			return;}
		for(int i=0; i<foodTypes.size(); i++){
			foodTypes.get(i).setMoreOrderedAndOnTheWay(false);
			if (foodTypes.get(i).getChoice()==Type && !foodTypes.get(i).getMoreOrderedAndOnTheWay() && marketCurrentlyBeingAskedForFood != markets.size())
			{
				print(markets.get(marketCurrentlyBeingAskedForFood).getName() + " do you Have food " + foodTypes.get(i).getChoice());
				markets.get(marketCurrentlyBeingAskedForFood).msgMarketOrderFood(foodTypes.get(i).getChoice(), num); //order (food type, amount)
				foodTypes.get(i).setMoreOrderedAndOnTheWay(true);
				
					
			}
			event = AgentEvent.none;
		}
		stateChanged();
	}


	public void msgHereAreRequestedFoodSupplies(String foodType, int foodAmount) {
		for(int i =0; i<foodTypes.size(); i++)
			if (foodTypes.get(i).getChoice() == foodType){
				foodTypes.get(i).addToFoodQuantity(foodAmount);
				foodTypes.get(i).setMoreOrderedAndOnTheWay(false);
			}
		
		event =  AgentEvent.none;
		stateChanged();
	}

	public void msgAtRefrigerator() {
		atRefrigerator.release();// = true;		
	}

	public void msgAtGrill() {//from animation
		//	print("msgAtTable() called");
			atGrill.release();// = true;
			//stateChanged();
		}
	public void msgAtPlatingArea() {//from animation
		//	print("msgAtTable() called");
		atPlatingArea.release();// = true;
			//stateChanged();
		}
	public void msgAtDefaultPosition() {//from animation
		//	print("msgAtTable() called");
		atDefaultPos.release();// = true;
			//stateChanged();
		}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

		if (event == AgentEvent.recievedOrder ){
			checkFoodStockAndUpdate();
			return true;
		}
		if (event == AgentEvent.foodOutOfStock){
			AskMarketsIfTheyHaveFoodSupplies();
			return true;
		}
		if (!myRejectedOrders.isEmpty()){
			
			for (Order order : myRejectedOrders) {
				tempOrderToBeDeleted = order;
				break;
			}
				tellWaiterOrderOutOfStock(tempOrderToBeDeleted);
				return true;
		}
		if (!myWaitingOrders.isEmpty()){
			for (Order order : myWaitingOrders) {
				if (order.isOrderCooked()==true && !order.isWaiterMessagedFoodDone()) {
					orderIsReady(order);
					return true;
				}
				else if (order.isOrderCooked()==false){
					cookOrder(order);
					return true;
				}
					
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	


	// Actions
	private void checkFoodStockAndUpdate() {
		event = AgentEvent.none;
		for (Order order : myWaitingOrders) {
			for(int i=0; i<foodTypes.size(); i++)
				if (order.getOrderChoice()== foodTypes.get(i).getChoice() && order.isOrderCooked()==false)
				{
					if (foodTypes.get(i).getFoodQuantity() >=1)
					{
						if (foodTypes.get(i).getFoodQuantity() ==1)
						{event =  AgentEvent.foodOutOfStock;
						print("That was the last of all food!");}//if after decrementing the item is out of stock event food out of stock should be called so it can be ordered from market
						marketCurrentlyBeingAskedForFood = 0; // makes fist market on list be checked first
						foodTypes.get(i).decrementFoodQuantity();
						order.passedStockCheck(true);
						
					}
					else if (order.isOrderCooked()==false && foodTypes.get(i).getFoodQuantity() <1 && !order.getCheckedStock() )
					{	
						if (foodTypes.get(i).getFoodQuantity() ==0)
						print("We have no Food!");
						{event =  AgentEvent.foodOutOfStock;}
						marketCurrentlyBeingAskedForFood = 0; // makes fist market on list be checked first
						myRejectedOrders.add(order);
						tempOrderToBeDeleted = order;
						print("Out of " + foodTypes.get(i).getChoice());
					}
				}
		}
		myWaitingOrders.remove(tempOrderToBeDeleted);
	}
	
	private void AskMarketsIfTheyHaveFoodSupplies() {
		for(int i=0; i<foodTypes.size(); i++){
			if (foodTypes.get(i).getFoodQuantity()==0 && !foodTypes.get(i).getMoreOrderedAndOnTheWay() && marketCurrentlyBeingAskedForFood != markets.size())
			{
				print(markets.get(marketCurrentlyBeingAskedForFood).getName() + " do you Have food " + foodTypes.get(i).getChoice());
				markets.get(marketCurrentlyBeingAskedForFood).msgMarketOrderFood(foodTypes.get(i).getChoice(), howMuchFoodAgentAsksFromMarket); //order (food type, amount)
				foodTypes.get(i).setMoreOrderedAndOnTheWay(true);
				
					
			}
			
		}
		event = AgentEvent.none;
	}
	private void tellWaiterOrderOutOfStock(Order order) {
		order.getWaiter().msgWaiterOrderOutOfStock(order.getTableOriginNumber(),order.getOrderChoice());
		myRejectedOrders.remove(order);
		
	}
	private void orderIsReady(Order order) {
		
		order.getWaiter().msgWaiterOrderIsReady(order.getTableOriginNumber(),order.getOrderChoice() );
		print("order ready");
		myWaitingOrders.remove(order);
		order.setWaiterMessagedFoodDone(true);
	}


	private void cookOrder( Order order)
	{
		cookGui.DoGoToRefrigerator();
		try {
			atRefrigerator.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cookGui.DoGoToGrill();
			try {
				atGrill.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		cookGui.showOrderInAnimation(order.getOrderChoice(), "");
			try {
				Thread.sleep(5 * Constants.SECOND);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		order.setOrderCooked();
		cookGui.DoGoToPlatingArea();
			try {
				atPlatingArea.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		cookGui.showOrderInAnimation("", "");
		cookGui.DoGoToDefault();
			try {
				atDefaultPos.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			

	//utilities
	
	public void setGui(CookGui gui) {
		cookGui = gui;
	} 
	private class Food {
		private String choice;
		private int cookTime;
		private int amount;
		boolean moreOrderedAndOnTheWay;
		
		private Food(String Choice, int CookTime, int Amount){
			choice=Choice;
			cookTime = CookTime;
			amount = Amount;
			moreOrderedAndOnTheWay=false;
		}
		private String getChoice(){
			return choice;
		}
		private int getCookTime() {
			return cookTime;
		}
		private int getFoodQuantity(){
			return amount;
		}
		private void addToFoodQuantity(int x){
			amount = amount + x;
		}
		private void decrementFoodQuantity(){
			amount--;
		}
		private void setMoreOrderedAndOnTheWay(boolean tf)
		{
			moreOrderedAndOnTheWay = tf;
		}
		private boolean getMoreOrderedAndOnTheWay(){
			return moreOrderedAndOnTheWay;
		}
		
	}
	
	private class Order {
		int tableOriginNumber;
		LucaWaiter waiter;
		boolean foodCooked;
		String orderChoice;
		private boolean waiterMessagedFoodDone;
		boolean stockCheck;

		public Order(int customerTableNumber, String customerChoice, LucaWaiter waiterAgent) {
			tableOriginNumber = customerTableNumber;
			orderChoice = customerChoice;
			waiterMessagedFoodDone = false;
			waiter=waiterAgent;
			stockCheck = false;
		}
		private void passedStockCheck(boolean x){
			stockCheck = x;
		}
		private boolean getCheckedStock(){
			return stockCheck;
		}
		String getOrderChoice(){
			return orderChoice;
		}
		void setTableOriginNumber(int tableNum)
		{
			tableOriginNumber=tableNum;
		}
		void setWaiter(LucaWaiter w)
		{
			waiter=w;
		}
		void addFood(String f){
			orderChoice=f;
		}
		int getTableOriginNumber(){
			return tableOriginNumber;
		}
		LucaWaiter getWaiter(){
			return waiter;
		}
		void setOrderCooked(){
			foodCooked=true;
		}
		boolean isOrderCooked(){
			return foodCooked;
		}
		boolean isWaiterMessagedFoodDone() {
			return waiterMessagedFoodDone;
		}
		void setWaiterMessagedFoodDone(boolean waiterMessagedFoodDone) {
			this.waiterMessagedFoodDone = waiterMessagedFoodDone;
		}
		}




	}
