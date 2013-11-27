package ryansRestaurant;

import agent.Agent;

import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

import ryansRestaurant.MarketAgent;
import ryansRestaurant.MarketAgent.Food;
import ryansRestaurant.MarketAgent.MarketOrder;
import ryansRestaurant.gui.CookGui;
import ryansRestaurant.gui.Gui;
import ryansRestaurant.interfaces.Cashier;

/**
 * Restaurant Cook Agent
 */

public class CookAgent extends Agent {
	
	
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	//private List<Order> orders = (new ArrayList<Order>());
	private List<MarketAgent> markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
	//private List<MarketAgent> markets = (new ArrayList<MarketAgent>());
	private int currentMarket = -1;
	private Cashier cashier;
	
	private String name;
	private enum OrderState {pending, cooking, doneCooking, finished, ordered, none};
	private Timer timer = new Timer();
	private Semaphore doneCooking = new Semaphore(0,true);
	
	private Map<String, Food> inventory = (new HashMap<String, Food>());
	private boolean newInventory = false;
	private boolean orderFailed = false;//= true; //setting this to true makes cook check inventory
	
	//Grills
	private int numGrills = 5;
	private List<Grill> grills = new ArrayList<Grill>();	
	
	
	//Gui
	CookGui agentGui = null;
	public String activity = new String();	
	
	
	 public final static List<String> Defaultmenu = new ArrayList<String>()
			 {{
				add("Cookie");
				add("Steak");
				add("Chicken");
				add("Salad");
				add("Pizza");
			 }};
	
	
	// menu to give to customers/waiters		 
	//public static List<String> menu = Collections.synchronizedList(new ArrayList<String>());
	public static List<String> menu = (new ArrayList<String>());
	
	
	public CookAgent(String name, Cashier cashier) {
		super();
		this.name = name;
		this.cashier = cashier;
		
		inventory.put("Cookie", new Food("Cookie", 1000, 1, 3, 40) );
		inventory.put("Steak", new Food("Steak", 10000, 3, 5, 11) );
		inventory.put("Chicken" , new Food("Chicken", 9000, 9, 5, 10 ));
		inventory.put("Salad", new Food("Salad", 3000, 4, 3, 10 ));
		inventory.put("Pizza", new Food("Pizza", 5000, 3, 3, 10));
		
		updateMenu();
		
		for(int i = 1; i <= numGrills; i++) {
			grills.add(new Grill(i));
		}
		
	}


	public String getName() {
		return name;
	}

	// Messages

	public void msgHereIsOrder(WaiterAgent waiter, String choice, int tableNumber) {
		orders.add(new Order(waiter, choice, tableNumber));
		stateChanged();
		print("New order " + waiter + " " + choice + " Table: " + tableNumber);
	}

	
	
	public void msgOrderCannotBeFulfilled(List<MarketOrder> orders) {
		Food f;

		for(MarketOrder o:orders) {
			f = inventory.get(o.type);
			f.cancelOrder(o.quantity);
			print("" + o.quantity + " " + o.type + " cannot be delivered.");
			
		}
		orderFailed = true;
		stateChanged();
		
	}
	
	
	/**
	 * Called from a MarketAgent when an order is finished
	 * @param orders	contains the type of food and how many were fulfilled
	 */
	public void msgOrderFulfilled(List<MarketOrder> orders) {
		Food f;
		
		for(MarketOrder o : orders) {
			f = inventory.get(o.type);
			f.addToInventoryFromOrder(o.quantity);
			print("Adding " + o.quantity + " " + o.type + " to inventory.");
		}
		newInventory = true;
		stateChanged();
	}
	
	
	public void addMarket(MarketAgent market) {
		markets.add(market);
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try {
			if(newInventory) {
				updateMenu();
				return true;
			}

			synchronized (orders) 
			{
				for(Order o : orders) {
					if(o.state == OrderState.doneCooking)
					{
						plateIt(o);
						return true;
					}
				}
			}
			
			synchronized (orders) 
			{
				for(Order o : orders) {
					if(o.state == OrderState.pending)
					{
						for(Grill g : grills) {
							if(g.state == GrillState.ready)
							{
								TryToCookIt(o, g);
								return true;
							}
						}
					}
				}
			}

			if(orderFailed) {
				OrderFoodThatIsLow();
				return true;
			}
		} catch (Exception e) {
			print("Exception Caught in scheduler.");
		}

		return false;
	}
	
	
	

	// Actions
	
	
	private void TryToCookIt(Order o, Grill g) {
		print("Trying to cook " + o.choice + " for table " + o.tableNumber +" at grill " + g.grillNumber);
		Food f = inventory.get(o.choice);
		
		if(f.amount == 0) {
			updateMenu();
			o.waiter.msgOutOfOrder(o.tableNumber, o.choice);
			orders.remove(o);
			if(f.isLow())
				OrderFoodThatIsLow();
			return;
		}
		else{
		f.amount--;
		
		activity = "Going to Fridge.";
		agentGui.DoGoToFridge();
		
		activity = "Going to Grill " + g.grillNumber + "\n\n"+o.choice.charAt(0) + o.choice.charAt(1);
		agentGui.DoGoToGrill(g.grillNumber);
		activity = "Arrived.";
		o.cookingTime = (int)(f.cookingTime * 3);
		o.grill = g;
		activity = "Grilling.";
		g.grillOrder(o);
		
		if(f.isLow())
			OrderFoodThatIsLow();
		
		}
		activity = "";
	}
	
	
	
	private void plateIt(Order o) {
		activity = "Plating.";
		agentGui.DoGoToGrill(o.grill.grillNumber);
		
		o.waiter.msgOrderReady(o.tableNumber, o.grill.grillNumber);
		agentGui.grillActive(o.grill.grillNumber, false, null);
		o.state = OrderState.finished;
		o.grill.state = GrillState.ready;
		print("" + o.choice + "Plated");
		orders.remove(o);
		activity = "";
	}

	/** Updates the public menu by scanning inventory for food that is in stock.
	 * 
	 */
	private void updateMenu() {
		Food f;
		menu.clear();
		for(String s : Defaultmenu) {
			f = inventory.get(s);
			if(f.amount > 0) {
				menu.add(s);
			}
		}
		newInventory = false;
	}
	
	/**
	 * Scans the inventory for foods that are low, then orders that food from a market
	 */
	private void OrderFoodThatIsLow() {
		print("Checking Inventory...");
		orderFailed = false;
		List<MarketOrder> order = new ArrayList<MarketOrder>();
		int quantity;
		
		for(String food:Defaultmenu) {
			Food f = inventory.get(food);
			
			
			if(f.isLow()) {
				quantity = f.ordering();
				if(quantity > 0) {
					order.add( new MarketOrder(f.type, quantity) );
					print("Need to order " + quantity +" " + f.type);
				}
			}
		}
		
		
		
		if(order.isEmpty()) {
			print("Nothing to order. Inventory fine.");
			return;
		}
		MarketAgent m = chooseAMarket();
		
		if(m != null) {
			m.msgOrder(order, this, cashier);
			print("Ordering from " + m);
			return;
		}
		else {
			print("Can't Order!!!!! THERE ARE NO MARKETS!!!!");
		}
	}
	
	private MarketAgent chooseAMarket() {
		if(!markets.isEmpty()) {
			currentMarket = (currentMarket + 1) % markets.size();
			return markets.get(currentMarket);
		}
		return null;
	}
	
	//utilities

	public List getMenu() {
		return new ArrayList<String>(menu);
	}

	
	public int getInventory(String InventoryItem, String slot) {
		if(inventory.containsKey(InventoryItem)) {
			
			switch (slot) {
			case "AMOUNT":
				return inventory.get(InventoryItem).amount;
			case "LOW" :
				return inventory.get(InventoryItem).low;
			case "CAP" :
				return inventory.get(InventoryItem).capacity;
			case "ORDERED" :
				return inventory.get(InventoryItem).ordered;
			default:
				break;
			}
			
			
			
		}
		return -1;
	}

	public void setInventory(String InventoryItem, int amount, String slot) {
		Food f;
		if(inventory.containsKey(InventoryItem)) {
			f = inventory.get(InventoryItem);
		}
		else return;
		
		switch (slot) {
		case "AMOUNT":
			f.amount = amount;
			break;
		case "LOW" :
			f.low = amount;
			break;
		case "CAP" :
			f.capacity = amount;
			break;
		default:
			break;
		}
	}
	
	public void checkInventory() {
		newInventory = true;
		orderFailed = true;
		stateChanged();
	}
	
	
	/**A class to hold an Order from a customer
	 * 
	 */
	class Order {
		WaiterAgent waiter;
		String choice;
		int tableNumber;
		int cookingTime = -1;
		OrderState state;
		Grill grill = null;
		
		Order(WaiterAgent waiter, String choice, int tableNumber) {
			this.waiter = waiter;
			this.choice = choice;
			this.tableNumber = tableNumber;
			this.state = OrderState.pending;
		}
	}
	
	
	
	class Food {
		String type;
		int cookingTime;
		int amount;
		int low;
		int capacity;
		int ordered;
		OrderState state;
		
		/**
		 *  Constructor for Food class
		 * @param type			the type of food
		 * @param cookingTime	the time it takes to cook
		 * @param amount		the amount of food currently in stock
		 * @param low			the amount to be considered low
		 * @param capacity		the theoretical maximum amount that can be stored //can have more than capacity
		 */
		Food(String type, int cookingTime, int amount, int low, int capacity)
		{
			this.type = type; this.cookingTime = cookingTime; this.amount = amount; this.low = low; this.capacity = capacity;
			this.state = OrderState.none;
			ordered = 0;
		}
		
		boolean isLow() {
			return amount <= low;
		}
		
		/**
		 *  Called when this food will be ordered.
		 *  	Sets state to ordered
		 *  	Increases ordered
		 * @return	an integer that is the amount that should be ordered.
		 */
		int ordering() {
			int amount = 0;
			
			amount = ( capacity - this.amount - ordered );
			
			if(amount > 0) {
				state = OrderState.ordered;
				ordered += amount;
			}
			return amount;
		}
		
		/**
		 * 
		 * @param amount the amount to decrement from amount ordered
		 */
		void cancelOrder(int amount) {
			ordered -= amount;
			if(ordered == 0) {
				state = OrderState.none;
			}
		}
		
		void addToInventoryFromOrder(int amount) {
			this.amount += amount;
			ordered -= amount;
			if(ordered == 0){
				state = OrderState.none;
			}
		}
	}

	
	enum GrillState {ready, cooking, done, burnt};
	private class Grill{
		int grillNumber = 0;
		Order order = null;
		GrillState state = GrillState.ready;
		Grill grill = this;
		
		Grill(int grillNumber) {
			this.grillNumber = grillNumber;
		}
		
		void grillOrder(Order order) {
			print("grilling " + order.choice + " for table " + order.tableNumber);
			agentGui.grillActive(grillNumber, true, order.choice);
			this.order = order;
			state = GrillState.cooking;
			order.state = OrderState.cooking;
			
			timer.schedule(new TimerTask() {
				public void run() {
					print("Grill " + grillNumber + "" + grill.order.choice +" done cooking.");
					grill.order.state = OrderState.doneCooking;
					agentGui.grillActive(grillNumber, false, grill.order.choice);
					stateChanged();
				}
			}, (long)order.cookingTime);
		}
		
				
	}
	

	//GetterSetter For agent gui
	public void setGui(CookGui cookGui) {
		this.agentGui = cookGui;		
	}
	public CookGui getGui() {
		return agentGui;
	}
}
