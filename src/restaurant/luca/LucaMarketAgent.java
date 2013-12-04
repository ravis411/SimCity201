package restaurant.luca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import restaurant.interfaces.luca.LucaCashier;
import restaurant.interfaces.luca.LucaCook;
import restaurant.interfaces.luca.LucaMarket;
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
public class LucaMarketAgent extends Agent implements LucaMarket {
	private String name;
	public LucaCook cook;
	public Collection<Order> myWaitingOrders;
	public Collection<Order> myPendingDeletionOrders;
	private List<Food> foodTypes
	= Collections.synchronizedList(new ArrayList<Food>());
	public enum AgentState {nothing, outOfSteak, outOfChicken, outOfBurger, orderedFoodInStock}
	private AgentState state = AgentState.nothing;
	public LucaCashier cashier;
	public int money;
	public EventLog log= new EventLog();
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public LucaMarketAgent(String name){
		super();
		this.name = name;
		
		foodTypes.add(new Food("Steak", 1, 10));//Food type, quantity, price
		foodTypes.add(new Food("Chicken", 2,2));//Food type, quantity, price
		foodTypes.add(new Food("Burger", 1,5));//Food type, quantity, price
		myWaitingOrders = Collections.synchronizedCollection(new ArrayList<Order>());
		money=0;
		myPendingDeletionOrders = Collections.synchronizedCollection(new ArrayList<Order>());
	}
	
	public String getName(){
		return name;
	}
	/**
	 * hack to establish connection to Host agent.
	 */


	
	// Messages

	public void msgMarketOrderFood(String Food, int amount) {
		log.add(new LoggedEvent("Recieved msgMarketOrderFood. Food: " + Food + " amount: "+ amount));
		synchronized(myWaitingOrders){
		for (int i=0; i<foodTypes.size(); i++)
		{
			if (foodTypes.get(i).getChoice() == "Steak" && foodTypes.get(i).getChoice() == Food){
				if (foodTypes.get(i).getFoodQuantity() == 0){
					state = AgentState.outOfSteak;
					print(this.getName() + "Out of " + foodTypes.get(i).getChoice());
				}
				else
					myWaitingOrders.add(new Order(Food, amount, foodTypes.get(i).getPrice()));
			}
			else if (foodTypes.get(i).getChoice() == "Chicken" && foodTypes.get(i).getChoice() == Food){
				if (foodTypes.get(i).getFoodQuantity() == 0){
					state = AgentState.outOfChicken;
					print(this.getName() + "Out of " + foodTypes.get(i).getChoice());

				}
				else
					myWaitingOrders.add(new Order(Food, amount, foodTypes.get(i).getPrice()));
			}
			else if (foodTypes.get(i).getChoice() == "Burger" && foodTypes.get(i).getChoice() == Food){
				if (foodTypes.get(i).getFoodQuantity() == 0){
					state = AgentState.outOfBurger;
					print(this.getName() + "Out of " + foodTypes.get(i).getChoice());
					
				}
				else
					myWaitingOrders.add(new Order(Food, amount, foodTypes.get(i).getPrice()));
			}
		}
				
			}
			stateChanged();
		}
		


	public void msgMarketHereIsPayment(int moneyPayment) {
		log.add(new LoggedEvent("Recieved msgMarketHereIsPayment. moneyOwed: " + moneyPayment));
		money = money + moneyPayment;
		print("Was Paid $" + moneyPayment + " and now has a total of $" +money);
		stateChanged();
	}

	


	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(state == AgentState.outOfSteak){
			tellCookWeAreOut("Steak");
			return true;
		}
		if(state == AgentState.outOfChicken){
			tellCookWeAreOut("Chicken");
			return true;
		}
		if(state == AgentState.outOfBurger){
			tellCookWeAreOut("Burger");
			return true;
		}
		if (!myWaitingOrders.isEmpty()){
			shipCookFood();
			return true;
		}

		

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void tellCookWeAreOut(String FoodType) {
		cook.msgCookIDoNotHaveFoodSupplyOrdered(FoodType);
		state = AgentState.nothing;
	}


	private void shipCookFood()
	{
		Random r;
		int x;
		synchronized(myWaitingOrders){
		for (Order order : myWaitingOrders) {
		r = new Random(System.nanoTime());
		x = r.nextInt(30) ;
		print(this.getName() + " is shipping Cook " + order.getFoodAmount() +" "+  order.getFoodType() +" in " + x + " seconds.");
		try {
			Thread.sleep(x * Constants.SECOND);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for (int i=0; i<foodTypes.size(); i++)
		{
			if (foodTypes.get(i).getChoice() == order.getFoodType()){
				if (order.getFoodAmount() > foodTypes.get(i).getFoodQuantity() ){
					order.setFoodAmount(foodTypes.get(i).getFoodQuantity());// if amount of food supply requested > food in stock just send what markets has
					print("Sorry " + this.getName() + " can't fulfill full order but is shipping " + order.getFoodAmount() +" "+  order.getFoodType() + " right now.");
					cook.msgCookNumberThatWereOrderedButNotFullfilled(order.getFoodAmount()-foodTypes.get(i).getFoodQuantity(), order.getFoodType());
					cook.msgHereAreRequestedFoodSupplies(order.getFoodType(), order.getFoodAmount());
					cashier.msgCashierHereIsMarketBill(order.getItemPrice()*foodTypes.get(i).getFoodQuantity(), this);	
					foodTypes.get(i).subtractFoodQuantity(order.getFoodAmount());
				}
				else {
				foodTypes.get(i).subtractFoodQuantity(order.getFoodAmount());
				print(this.getName() + " is shipping " + order.getFoodAmount() +" "+  order.getFoodType() + " right now.");
				cook.msgHereAreRequestedFoodSupplies(order.getFoodType(), order.getFoodAmount());
				cashier.msgCashierHereIsMarketBill(order.getOrderPrice(), this);		
				}
				myPendingDeletionOrders.add(order);
			}
		}
		}
		}
		
		for (Order order1 : myPendingDeletionOrders) {
			for (Iterator<Order> ordr = myWaitingOrders.iterator(); ordr.hasNext();) {
				Order order2 = ordr.next();
			        if (order2 == order1) {
			        	ordr.remove();
			        }
			}
		}
		synchronized(this){
			myPendingDeletionOrders.clear();
		}
		
	}
			

	//utilities
	public void setCook(LucaCook cook) {
		this.cook=cook;
		
	}
	public void setCashier(LucaCashier cashier) {
		this.cashier=cashier;
		
	}

	
	private class Food {
		private String choice;
		private int amount;
		private int price;
		

		private Food(String Choice, int Amount, int price){
			choice=Choice;
			amount = Amount;
			this.price=price;
		}
		private int getPrice(){
			return price;
		}
		private String getChoice(){
			return choice;
		}
		private int getFoodQuantity(){
			return amount;
		}
		private void subtractFoodQuantity(int x){
			amount= amount - x;
		}

		
		
	}
	
	private class Order {
		String foodType;
		int foodAmount;
		boolean orderProcessed;
		int orderPrice;
		int itemprice;

		public Order(String foodType, int foodAmount, int itemprice) {
			this.foodType = foodType;
			this.foodAmount = foodAmount;
			this.orderProcessed = false;
			this.orderPrice= itemprice *foodAmount;
			this.itemprice=itemprice;

		}
		int getOrderPrice(){
			return orderPrice;
		}
		String getFoodType(){
			return foodType;
		}
		void setFoodAmount(int x){
			foodAmount = x;
		}
		int getFoodAmount(){
			return foodAmount;
		}
		int getItemPrice(){
			return itemprice;
		}
		}

}


	
