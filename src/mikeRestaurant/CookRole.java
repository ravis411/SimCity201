package mikeRestaurant;

import interfaces.MarketManager;
import interfaces.Person;
//import building.Market;
import interfaces.generic_interfaces.GenericCook;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;








import building.BuildingList;
import mikeRestaurant.gui.CookGui;
import mikeRestaurant.interfaces.Cashier;
import mikeRestaurant.interfaces.Cook;
import building.Market;
import mikeRestaurant.interfaces.Waiter;
import MarketEmployee.MarketManagerRole;
import Person.Role.Role;
import Person.Role.ShiftTime;
import agent.Agent;

/**
 * Restaurant Cook Agent
 *
 */
public class CookRole extends GenericCook implements Cook{

	//list of orders that the Cook is handling
	private List<Order> orders;
	private static int MAX_FOOD = 5;
	private static int THRESHOLD = 2;
	private static int CAPACITY = 5;
	private Map<String, Food> foods;
	
	private final static int NUM_MARKETS = 2;

	private List<MarketManagerRole> markets;
	private Queue<String> reorders;
	private int marketIndex;
	
	private RevolvingStand revolvingStand;
	
	private Semaphore atCookingLocation = new Semaphore(0, true);
	private Semaphore atPickupLocation = new Semaphore(0, true);
	private Semaphore atFridge = new Semaphore(0, true);
	
	private CashierRole cashier;
	
	private CookGui cookGui;
	
	private List<Grill> grills;
	private Queue<Integer> grillPickups;
	
	//maps a choice to a list of markets out of that food
	private Map<String, Set<MarketRole>> choicesExpended;
	
	public enum cookState {idle,goingGroceryShopping};
	private cookState state;
	

	
	@Override
	public void setPerson(Person customerPerson) {
		// TODO Auto-generated method stub
		super.setPerson(customerPerson);
		for(String s : foods.keySet()){
			if(foods.get(s).quantity < foods.get(s).threshold){
				reorders.add(s);
				stateChanged();
			}
		}
	}

	/**
	 * Basic CookAgent constructor
	 */
	public CookRole(String workLocation){
		super(workLocation);
		this.cashier = (CashierRole)cashier;
		orders = new ArrayList<Order>();
		foods = initFoods();
		markets = new ArrayList<MarketManagerRole>();
		
		
		
		
		
		reorders = new ArrayDeque<String>();
		marketIndex = 0;
		grills = new ArrayList<Grill>();
		choicesExpended = initChoicesExpended();
		grillPickups = new ArrayDeque<Integer>();
		//restock the CookAgent upon creation if possible
		
		revolvingStand = new RevolvingStand();
		state=cookState.idle;
		
		Timer checkRevolvingStand = new Timer(15000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub	
				stateChanged();
			}
			
		});
		
		checkRevolvingStand.start();
	}
	
	public void setGui(CookGui gui){
		this.cookGui = gui;
	}
	
	private Map<String, Set<MarketRole>> initChoicesExpended(){
		Map<String, Set<MarketRole>> temp = new HashMap<String, Set<MarketRole>>();
		for(String s : WaiterRole.MENU().keySet()){
			temp.put(s, new HashSet<MarketRole>());
		}
		return temp;
	}
	
	/**
	 * Sets up the list of markets available
	 * @return List of Markets responding to the cook
	 */
//	private List<MarketAgent> initMarkets(){
//		ArrayList<MarketAgent> temp = new ArrayList<MarketAgent>();
//		for(int i = 0; i < NUM_MARKETS; i++){
//			//there is just one cook in this sim
//			MarketAgent market = new MarketAgent(this);
//			market.startThread();
//			temp.add(market);
//		}
//		return temp;
//	}
	
//	public void addMarket(MarketAgent m){
//		markets.add(m);
//		print("Market added");
//		return;
//	}
	
	/**
	 * Private method that sets up the foods map
	 * @return the Map of String name of food to its corresponding Food class
	 */
	private Map<String, Food> initFoods(){
		HashMap<String, Food> temp = new HashMap<String, Food>();
		for(String s : WaiterRole.MENU().keySet()){
			//temp.put(s, new Food(s, getCookTimeForChoice(s), (int)(Math.random()*MAX_FOOD), THRESHOLD, CAPACITY));
			//hacks a sufficient number of food into the cook inventory
			if(s.equals("Steak") || s.equals("Chicken")){
			temp.put(s, new Food(s, getCookTimeForChoice(s), 2, THRESHOLD, 900));
			}
			else temp.put(s, new Food(s, getCookTimeForChoice(s), 900, THRESHOLD, 900));
		}
		return temp;
	}
	
	/**
	 * Method that maps a cooking time to a menu option
	 * @param choice the choice from the menu
	 * @return time in milliseconds that the food will take to cook
	 */
	private int getCookTimeForChoice(String choice){
		int timerValue;
		switch(choice){
			case "Steak":
				timerValue = 6000;
				break;
			case "Salad":
				timerValue = 3000;
				break;
			case "Chicken":
				timerValue = 7000;
				break;
			case "Pizza":
				timerValue = 4000;
				break;
			default:
				timerValue = 5000;
		}
		
		return timerValue;
			
	}
	
	/**
	 * Accessor method for the agent's name
	 * @return the cook's name
	 */
	public String getName(){
		//the cook is a singleton - with name "Cook"
		return this.myPerson.getName();
	}
	
	//----------------MESSAGES----------------//
	
	/**
	 * Message sent by the WaiterAgent to give the Cook an order
	 * @param order the order to begin cooking
	 */
	public void msgHereIsAnOrder(Waiter waiter, String choice, Table table){
		  orders.add(new Order((WaiterRole) waiter, choice, table));
		  stateChanged();
	}
	
	/**
	 * This call from the Waiter pickup comes for an immediate gui change, 
	 * so it will be promoted to the head of the scheduler.
	 * @param grillPosition grill position fulfilled
	 */
	public void msgPickedUpFoodFromPosition(int grillPosition){
		grillPickups.add(Integer.valueOf(grillPosition));
		stateChanged();
	}

	/**
	 * Private message sent by the food-cooking timer in the CookAgent class.
	 * This message signals an order is done cooking.
	 * @param order The order that is ready
	 */
	private void msgDone(Order order){
		//DoGoToCookingLocation();
		//DoGoToPickupLocation();
		order.orderStatus = Order.OrderStatus.OrderReady;
		stateChanged();
		//order.getWaiter().msgOrderIsReady(order); //signal the order's waiter that it is ready for pickup
	}
	
	/**
	 * Message sent by the MarketAgent when he is responding to an order for 
	 * more food. Because the orders are sent out per individual food item, we don't
	 * need a list of choices and quantities
	 * @param choice name of the food
	 * @param quantity amount of the food
	 * @param stillHasStock true if the market still has food (of any kind) left, false otherwise
	 * @param market the marketAgent that is sending food
	 */
public void msgOrderFilled(int ingredientNum, int quantity){
        
        switch(ingredientNum){
           case 0: foods.get("Steak").quantity+= quantity;
           		   print("Received complete delivery from market have "+ quantity + " steak");
           case 1: foods.get("Chicken").quantity+= quantity;
                   print("Received complete delivery from market have "+ quantity + " chicken");
        }
        
        state=cookState.idle;
        stateChanged();
}
public void msgOrderPartiallyFilled(int ingredientNum,int quantity, int quanityOfOrderMarketDoesntHave){
	switch(ingredientNum){
    case 0: foods.get("Steak").quantity+= quantity;
    		   print("Received partial delivery from market have "+ quantity + " steak");
    case 1: foods.get("Chicken").quantity+= quantity;
            print("Received partial delivery from market have "+ quantity + " chicken");
 }
        state=cookState.goingGroceryShopping;
        stateChanged();
}
public void msgOrderNotFilled(int ingredientNum){
	print("Did not receive anything from market");
	state=cookState.goingGroceryShopping;
	stateChanged();
	  
}

	/**
	 * Message sent by the CookGui that the gui has arrived at the cooking location (grills)
	 */
	public void msgAtCookingLocation(){
		atCookingLocation.release();
	}
	
	/**
	 * Message sent by the CookGui that the gui has arrived at the pickup location
	 */
	public void msgAtPickupLocation(){
		atPickupLocation.release();
	}
	
	/**
	 * Message sent by the CookGui that the gui has arrived at the fridge location
	 */
	public void msgAtFridge(){
		atFridge.release();
	}
	
	//---------------SCHEDULER---------------//


	@Override
	public boolean pickAndExecuteAction() {
		// TODO Auto-generated method stub
		
		try {
			
			if(orders.isEmpty() && grillPickups.isEmpty() && workState == WorkState.ReadyToLeave){
				kill();
				return true;
			}
			
			if(!grillPickups.isEmpty()){
				dealWithGrillPosition(grillPickups.poll().intValue());
				return true;
			}
			
			for(Order order : orders){
				if(order.orderStatus == Order.OrderStatus.OrderReady){
					setOrderForPickup(order);
					return true;
				}
			}
			
//			if(!reorders.isEmpty()){
//				String s = reorders.poll();
//				placeOrderForFood(s, foods.get(s).capacity-foods.get(s).quantity);
//				return true;
//			}
			if(state==cookState.goingGroceryShopping){
				this.OrderFromMarket();
				state=cookState.idle;
			}
			
			if(!revolvingStand.isEmpty()){
				WaiterRole.Order order = revolvingStand.getLastOrder();
				Order newOrder = new Order(order.getWaiter(), order.getChoice(), order.getTable());
				newOrder.orderStatus = Order.OrderStatus.OrderPending;
				orders.add(newOrder);
				cookOrder(newOrder);
				return true;
			}
			
			//cook the ready orders
			for(Order order : orders){
				if(order.orderStatus == Order.OrderStatus.OrderPending){
					cookOrder(order);
					return true;
				}
			}
			
			return false;
		} catch (ConcurrentModificationException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	//----------------ACTIONS----------------//
	
	/**
	 * Private action called by the scheduler that tells the gui
	 * to remove some food from a specific grill position
	 */
	private void dealWithGrillPosition(int grillPosition){
		DoDealWithGrillPosition(grillPosition);
		cookGui.foodPickedUp(grillPosition);
	}
	
	/**
	 * Private action called by the scheduler that sets an order to 
	 * be picked up by its corresponding waiter.
	 * @param order the order to be picked up
	 */
	private void setOrderForPickup(Order order){
		DoGoToCookingLocation();
		cookGui.foodInTransit(order.grillPosition);
		for(Grill g : grills){
			if(g.grillPosition == order.grillPosition){
				g.inUse = false;
			}
		}
		DoGoToPickupLocation();
		cookGui.foodPrepared(order.grillPosition);
		
		print("Order Is Ready!");
		order.waiter.msgOrderIsReady(order.waiter, order.choice, order.table, order.grillPosition);
		orders.remove(order);
	}
	
	/**
	 * Private method called by the scheduler to cook an order
	 * @param order the order to cook
	 */
	private void cookOrder(Order order){
		Food food = foods.get(order.choice);
		if(food.quantity == 0){
			//out of this type of food
			print("Out of Food: "+order.choice);
			OrderFromMarket();
			//placeOrderForFood(food.choice, food.capacity-food.quantity);
			//msg waiter for this
			Set<String> availableFoods = new HashSet<String>();
			for(String s : foods.keySet()){
				if(foods.get(s).quantity != 0)
					availableFoods.add(s);
			}
			order.waiter.msgOutOfFoodForOrder(order.waiter, order.choice, order.table, availableFoods);
			orders.remove(order);
			return;
		}
		//otherwise
		DoCookOrder(order);
		//decrement the food
		food.decrementFood();
		if(food.quantity <= food.threshold){
			//order CAPACITY amount of food by contacting the market
			//placeOrderForFood(food.choice, food.capacity-food.quantity);
		}
		order.orderStatus = Order.OrderStatus.OrderCooking;
		TimerListener listener = new TimerListener(order);
		Timer timer = new Timer(foods.get(order.choice).cookingTime, listener);
		timer.start();
	}
public void OrderFromMarket(){
	    markets.add((MarketManagerRole)((Market) BuildingList.findBuildingWithName("Market 1")).getManager());
        HashMap<String,Integer> order = new HashMap<String,Integer>();
        if(marketIndex==1){marketIndex=0;}
        print("ordering from "+ markets.get(0).getNameOfRole());
        
        

        for(Food f : foods.values()){
			
                if(f.threshold>=f.quantity && (f.choice=="Steak" || f.choice=="Chicken")){
                
                        markets.get(marketIndex).msgMarketManagerFoodOrder(f.choice,10, this);
                        //System.err.println(f.choice);
                        
                }
                
                stateChanged();
                
                
        }
        marketIndex++;
}
        //Order2 o= new Order2(order,this);
        
        

	
//	/**
//	 * Action called by cookOrder action if and only if more of a particular
//	 * type of food needs to be ordered
//	 * @param name the name of the food
//	 * @param quantity the amount of the food to order
//	 */
//	private void placeOrderForFood(String name, int quantity){
//		if(!markets.isEmpty()){
//			int n = markets.size();
//			int initialIndex = marketIndex;
//			MarketAgent marketToOrderFrom = null;
//			//make sure we don't continue checking markets forever
//			while(marketIndex - initialIndex < n){
//				MarketAgent temp = markets.get(marketIndex % markets.size());
//				marketIndex++;
//				//if this market has this type of food
//				if(!choicesExpended.get(name).contains(temp)){
//					marketToOrderFrom = temp;
//					break;
//				}
//			}
//			
//			//if no markets are available for this order
//			if(marketToOrderFrom == null){
//				print("All Markets out of "+name + " -- cannot order");
//				return;
//			}
//			
//			//otherwise make the order
//			DoPlaceOrderForFood(name, quantity, marketToOrderFrom.getID());
//			marketToOrderFrom.msgINeedFood(name, quantity);
//		}else{
//			print("All Markets out of food altogether");			
//		}
//	}
	
	//---------------DO XYZ---------------//
	
	private void DoDealWithGrillPosition(int value){
		print("Removing food in from grill position "+value);
	}
	
	private void DoCookOrder(Order order){
		cookGui.DoGoToRefrigerator();
		try {
			atFridge.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cookGui.DoGoToCookingLocation();
		try {
			atCookingLocation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean emptyGrill = false;
		for(Grill g : grills){
			if(g.inUse == false){
				emptyGrill = true;
				g.inUse = true;
				cookGui.addFood(order.choice, g.grillPosition);
				order.grillPosition = g.grillPosition;
				break;
			}
		}
		
		if(!emptyGrill){
			Grill g = new Grill(grills.size());
			g.inUse = true;
			cookGui.addFood(order.choice, grills.size());
			order.grillPosition = grills.size();
			grills.add(g);
		}
		
		print("Cooking " + order.choice);
	}
	
	private void DoGoToCookingLocation(){
		cookGui.DoGoToCookingLocation();
		try {
			atCookingLocation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void DoGoToPickupLocation(){
		cookGui.DoGoToPickupLocation();
		try {
			atPickupLocation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void DoPlaceOrderForFood(String name, int quantity, int marketID){
		print("Ordering "+quantity+"x"+name+" from Market #"+marketID);
	}
	
	/**
	 * Hack that clears the food stores in the CookAgent
	 */
	public void clearFoodStores(){
		for(String s : foods.keySet()){
			foods.get(s).quantity = 0;
		}
		print("Food stores cleared");
	}
	
	public RevolvingStand getRevolvingStand(){
		return revolvingStand;
	}
	
//	/**
//	 * Hack that clears all inventories of the markets held by this CookAgent
//	 */
//	public void clearMarkets(){
//		for(MarketAgent m : markets){
//			m.clearInventory();
//		}
//	}
	
	/**
	 * Private class meant only to listen to cooking timers
	 */
	private class TimerListener implements ActionListener {

		private Order order;
		
		public TimerListener(Order o){
			order = o;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			((Timer) e.getSource()).stop();
			msgDone(order); //msg the cook that this is done
		}
		
	}
	
	/**
	 * A static class to simulate an order
	 *
	 */
    private static class Order {
		   
		public enum OrderStatus {NewOrder, OrderPending, OrderCooking, OrderReady, OrderDelivered};
		
		//Order meant to link the following data
		private WaiterRole waiter;
	    private String choice;
	    private Table table;
	    public OrderStatus orderStatus;
	    
	    int grillPosition;
		
	    /**
	     * Constructor
	     * @param newWaiter waiter handling order
	     * @param newChoice choice of customer for order
	     * @param newTable table to which the order corresponds
	     */
	    public Order(WaiterRole newWaiter, String newChoice, Table newTable){
	    	waiter = newWaiter;
	    	choice = newChoice;
	    	table = newTable;
	    	orderStatus = OrderStatus.OrderPending;
	    }
	    
	    
	    //-----------------ACCESSORS-------------//
	    public String getChoice(){
	    	return choice;
	    }
	    
	    public Table getTable(){
	    	return table;
	    }
	    
	    public WaiterRole getWaiter(){
	    	return waiter;
	    }
	    
	    /**
	     * toString method which changes the Order to a readable string
	     */
	    public String toString(){
	    	StringBuilder builder = new StringBuilder();
	    	builder.append("Choice = "+ choice);
	    	builder.append("\tWaiter = "+ waiter.getName());
	    	builder.append("\tTable = "+ table);
	    	return builder.toString();
	    }
	}
    
    /**
     * Class that aims to contain the important and relevant (to the cook)
     *  information about food
     * @author MSILKJR
     *
     */
    private class Food {
    	String choice; //type of food
    	int cookingTime; //time in milliseconds for how long it takes to cook
    	int quantity; //number of current foods
    	int threshold; //how many left when we should order more
    	int capacity; //as much as we can hold of this food
    	
    	public Food(String choice, int cookingTime, int quantity, int threshold, int capacity){
    		this.choice = choice;
    		this.cookingTime = cookingTime;
    		this.quantity = quantity;
    		this.threshold = threshold;
    		this.capacity = capacity;
    	}
    	
    	public void decrementFood(){
    		quantity--;
    	}
    }
    
	
//	@Override
//	public synchronized void pauseThread() {
//		// TODO Auto-generated method stub
//		super.pauseThread();
//		
//		//pause the markets as well
//		for(MarketAgent market: markets){
//			market.pauseThread();
//		}
//	}
	
	

//	@Override
//	public synchronized void resumeThread() {
//		// TODO Auto-generated method stub
//		super.resumeThread();
//		
//		for(MarketAgent market: markets){
//			market.resumeThread();
//		}
//	}
	
	private class Grill {
		boolean inUse;
		int grillPosition;
		public Grill(int grillPosition){
			inUse = false;
			this.grillPosition = grillPosition;
		}
	}

	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return "MikeCookRole";
	}

	

	@Override
	public void msgHereIsFoodFromMarket(String choice, int quantity,
			boolean stillHasStock, mikeRestaurant.interfaces.Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderWillNotBeFulfilled(String choice,
			mikeRestaurant.interfaces.Market mkt) {
		// TODO Auto-generated method stub
		
	}

}
