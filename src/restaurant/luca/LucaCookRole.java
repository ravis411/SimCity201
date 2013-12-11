package restaurant.luca;

import interfaces.MarketManager;
import interfaces.generic_interfaces.GenericCook;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import building.Building;
import building.Market;
import building.BuildingList;
import restaurant.gui.luca.CookGui;
import restaurant.interfaces.luca.LucaCook;
import restaurant.interfaces.luca.LucaWaiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Employee;
import Person.Role.Role;
import Person.Role.ShiftTime;
import agent.Constants;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LucaCookRole extends GenericCook implements LucaCook{
	private CookGui cookGui;
	private Semaphore atRefrigerator = new Semaphore(0,false);
	private Semaphore atGrill = new Semaphore(0,false);
	private Semaphore atPlatingArea = new Semaphore(0,false);
	private Semaphore atDefaultPos = new Semaphore(0,false);
	public Collection<Order> myWaitingOrders;
	public Collection<Order> myRejectedOrders;
	private int marketCurrentlyBeingAskedForFood;
	public List<LucaWaiter> waiters
	= Collections.synchronizedList(new ArrayList<LucaWaiter>());
	public List<MarketManager> markets
	= Collections.synchronizedList(new ArrayList<MarketManager>());
	public List<Food> foodTypes
	= Collections.synchronizedList(new ArrayList<Food>());
	public EventLog log= new EventLog();
	private int howMuchFoodAgentAsksFromMarket= 2;
	private Order tempOrderToBeDeleted;
	public enum AgentEvent 
	{none, recievedOrder, foodOutOfStock, MarketAskedIfTheyHaveFoodType, orderDoneCooking, waiterHasBeenNotified};
	private AgentEvent event = AgentEvent.none;
	private RevolvingStand revolvingStand;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public LucaCookRole(String restLocation){
		super(restLocation);
		marketCurrentlyBeingAskedForFood =0;
		foodTypes.add(new Food("Steak", 7, 0));//Food type, cooktime, quantity
		foodTypes.add(new Food("Chicken", 3, 0));//Food type, cooktime, quantity
		foodTypes.add(new Food("Burger", 5, 0));//Food type, cooktime, quantity
		myWaitingOrders = Collections.synchronizedCollection(new ArrayList<Order>());
		myRejectedOrders = Collections.synchronizedCollection(new ArrayList<Order>());
		
		
		revolvingStand = new RevolvingStand();
		
		Timer checkRevolvingStand = new Timer(15000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub	
				stateChanged();
			}
			
		});
		
		checkRevolvingStand.start();
	}
	
	public String getName(){
		return myPerson.getName();
	}
	/**
	 * hack to establish connection to Host agent.
	 */


	
	// Messages

	public void msgAddMarket(MarketManager market) {
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
	public void msgOrderNotFilled(int ingredientNum){
		String Food; 
		if (ingredientNum==0) Food="Steak"; 
		else if (ingredientNum==1) Food="Chicken"; 
		else if (ingredientNum==2) Food="Burger";
		else Food="FoodDOesNtExIStttttWrongNUmber";
		AlertLog.getInstance().logMessage(AlertTag.LUCAS_RESTAURANT, getNameOfRole(), "Market could not provide " + Food + "s. Will order from different market.");
		for(int i =0; i<foodTypes.size(); i++)
			if (foodTypes.get(i).getChoice() == Food){
			foodTypes.get(i).setMoreOrderedAndOnTheWay(false);
			marketCurrentlyBeingAskedForFood++;
			}
			event =  AgentEvent.foodOutOfStock;
			stateChanged();
		
	}

	public void msgOrderPartiallyFilled(int ingredientNum, int quantity,  int quantityOfOrderThatMarketDoesntHave) {
		String Food; 
		if (ingredientNum==0) Food="Steak"; 
		else if (ingredientNum==1) Food="Chicken"; 
		else if (ingredientNum==2) Food="Burger";
		else Food="FoodDOesNtExIStttttWrongNUmber";
		AlertLog.getInstance().logMessage(AlertTag.LUCAS_RESTAURANT, getNameOfRole(), "Recieved partial shipment of " + quantity + " " + Food + "s. However didn't Recieve: " + quantityOfOrderThatMarketDoesntHave);
		marketCurrentlyBeingAskedForFood++;
		if (marketCurrentlyBeingAskedForFood==4){
			marketCurrentlyBeingAskedForFood=0;
			event = AgentEvent.none;
			stateChanged();
			return;}
		for(int i=0; i<foodTypes.size(); i++){
			foodTypes.get(i).setMoreOrderedAndOnTheWay(false);
			if (foodTypes.get(i).getChoice()==Food && !foodTypes.get(i).getMoreOrderedAndOnTheWay() && marketCurrentlyBeingAskedForFood != markets.size())
			{
				print(markets.get(marketCurrentlyBeingAskedForFood).getMarketName() + " do you Have food " + foodTypes.get(i).getChoice());
				markets.get(marketCurrentlyBeingAskedForFood).msgMarketManagerFoodOrder(foodTypes.get(i).getChoice(), quantityOfOrderThatMarketDoesntHave, this); //order (food type, amount)
				foodTypes.get(i).setMoreOrderedAndOnTheWay(true);
				
					
			}
			event = AgentEvent.none;
		}
		stateChanged();
	}


	public void msgOrderFilled(int ingredientNum, int foodAmount) {
		String Food; 
		if (ingredientNum==0) Food="Steak"; 
		else if (ingredientNum==1) Food="Chicken"; 
		else if (ingredientNum==2) Food="Burger";
		else Food="FoodDOesNtExIStttttWrongNUmber";
		AlertLog.getInstance().logMessage(AlertTag.LUCAS_RESTAURANT, getNameOfRole(), "Recieved shipment of " + foodAmount + " " + Food + "s.");
		for(int i =0; i<foodTypes.size(); i++)
			if (foodTypes.get(i).getChoice() == Food){
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
	public boolean pickAndExecuteAction() {

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

		else if(!revolvingStand.isEmpty()){
	              //get the order from the stand
			LucaWaiterRole.Order order = revolvingStand.getLastOrder();
		              //structure the order data to fit in with my old cooking routine
			Order newOrder = new Order(order.getTable(), order.getChoice(), order.getWaiter());
			event = AgentEvent.recievedOrder;
			myWaitingOrders.add(newOrder);
		              //cook the order in the same way
		return true;
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
		markets.clear();
		for( Building m : BuildingList.findBuildingsWithType("Market")){
			for (Role role :m.getInhabitants())
			{
				if (role instanceof MarketManager){
					MarketManager manager = (MarketManager) role;
					markets.add(manager);
				}
			}
		}
		if (markets.isEmpty()){
			for(int i=0; i<foodTypes.size(); i++){
				if (foodTypes.get(i).getFoodQuantity()==0 && !foodTypes.get(i).getMoreOrderedAndOnTheWay())
				{
					print(((Market)BuildingList.findBuildingWithName("Market 1")).getName() + "which is CLOSED (NONNORM) is being asked do you Have food " + foodTypes.get(i).getChoice());
					((Market)BuildingList.findBuildingWithName("Market 1")).getMarketData().msgClosedMarketAFoodOrder(foodTypes.get(i).getChoice(), howMuchFoodAgentAsksFromMarket,this); //order (food type, amount)
					foodTypes.get(i).setMoreOrderedAndOnTheWay(true);
			}
			}
		}
			
		else{
			for(int i=0; i<foodTypes.size(); i++){
				if (foodTypes.get(i).getFoodQuantity()==0 && !foodTypes.get(i).getMoreOrderedAndOnTheWay() && marketCurrentlyBeingAskedForFood != markets.size())
				{
					print(markets.get(marketCurrentlyBeingAskedForFood).getMarketName() + " do you Have food " + foodTypes.get(i).getChoice());
					markets.get(marketCurrentlyBeingAskedForFood).msgMarketManagerFoodOrder(foodTypes.get(i).getChoice(), howMuchFoodAgentAsksFromMarket,this); //order (food type, amount)
					foodTypes.get(i).setMoreOrderedAndOnTheWay(true);
					
						
				}
				
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

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return null;
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


	@Override
	public void msgOrderFilled(String foodType, int foodAmount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCookNumberThatWereOrderedButNotFullfilled(int i,
			String string) {
		// TODO Auto-generated method stub
		
	}

	public RevolvingStand getRevolvingStand() {
		return revolvingStand;
	}




	}
