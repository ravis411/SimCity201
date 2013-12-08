package ryansRestaurant;

import agent.Agent;

import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

import ryansRestaurant.interfaces.RyansCashier;
import ryansRestaurant.interfaces.RyansMarket;

/**
 * Restaurant RyansMarket Agent
 */

public class RyansMarketRole extends Agent implements RyansMarket{
	
	private List<MyOrder> orders = (new ArrayList<MyOrder>());
	
	private String name;
	private enum OrderState {newOrder, pending, preparing, ready, finished, none};
	private Timer timer = new Timer();
	private double secondsToFullfillOrder = 120;
	private Semaphore donePreparing = new Semaphore(0,true);
	
	private Map<String, Food> inventory = (new HashMap<String, Food>());
	
	
	public RyansMarketRole(String name) {
		super();

		this.name = name;
	}


	public String getName() {
		return name;
	}

	// Messages

	
	
	//from cashier agent
	//markets are rich...we dont care about money...but thanks
	public void msgHereIsPayment(RyansCashier cashier, double total) {
		print("" + cashier + "paid " + total);	
	}
	
	public void msgOrder(List<MarketOrder> order, RyansCookRole cook, RyansCashier cashier) {
		MyOrder o = new MyOrder();
		
		o.cook = cook;
		o.cashier = cashier;
		o.order = order;
		o.state = OrderState.newOrder;
		
		for(MarketOrder o1 : order) {
			if(o1.quantity == 0) {
				order.remove(o1);
			}
		}
		if(order.isEmpty()) {
			print("The cook tried to give me an order...but he didn't want anything.");
			return;
		}
		print("New order.");
		
		orders.add(o);
		
		stateChanged();
	}
	
	
	public void addToInventory(String type, int quantity) {
		Food f;
		//synchronized (inventory)
		{
			
		
		if(inventory.containsKey(type)) {
			f = inventory.get(type);
			f.amount += quantity;
		}
		else {
			f = new Food(type, quantity, 100);
			inventory.put(type, f);
		}
		}
		stateChanged();
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {

		try {
			//synchronized (orders) 
			{
			if(!orders.isEmpty()) {

				for(MyOrder o : orders) {
					if(o.state == OrderState.newOrder)
					{
						prepareOrder(o);
						return true;
					}
				}
			}
			}
		} catch (ConcurrentModificationException e) {
			print("Concurrent Modification Exception caught.");
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void prepareOrder(MyOrder order) {
		print("Attempting to prepare order...");
		order.state = OrderState.preparing;
		Food f;
		List<MarketOrder> successful = new ArrayList<MarketOrder>();
		List<MarketOrder> unsuccessful = new ArrayList<MarketOrder>();
		
		for( MarketOrder o : order.order) {
			f = inventory.get(o.type);
			if(f == null){
				print("" + o.type + " is not in my inventory.");
				unsuccessful.add(o);
			}
			else if(f.fulfill((o.quantity))) {
				successful.add(new MarketOrder(o.type, o.quantity));
			}
			else
			{
				int q = f.fulfillAsMuchAsPossible(o.quantity);
				successful.add(new MarketOrder(o.type, q));
				unsuccessful.add(new MarketOrder(o.type, o.quantity - q));
			}
		}
		
		if(!unsuccessful.isEmpty()) {
			//Send a message to the cook, this part of order cannot be fulfilled
			order.cook.msgOrderCannotBeFulfilled(unsuccessful);
		}

		if(!successful.isEmpty()) {
			double defaultSecs = secondsToFullfillOrder;
			
			while(secondsToFullfillOrder > 0) {
				
				//Start a timer, when timers are done send the cook his order
				timer.schedule(new TimerTask() {
					public void run() {
						donePreparing.release();
					}
				}, 1000);


				try {
					donePreparing.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				secondsToFullfillOrder -= 1;
			}
			secondsToFullfillOrder = defaultSecs;

			//determine the total//all food costs $1 // what a nice market 
			// also no need to keep track of the bill, we trust the consumer.
			double total = 0;
			for(MarketOrder o : successful) {
				total += o.quantity;
			}
			order.cook.msgOrderFulfilled(successful);
			order.cashier.msgMarketBill(this, total);
		}
		else {
			print("Unable to supply any food.");
		}
		
		orders.remove(order);
	}
	

	

	
	//utilities
	
	public String toString() {
		return "" + name;
	}
	
	public double getSecondsToFullfillOrder() {
		return secondsToFullfillOrder;
	}
	public void setSecondsToFullfillOrder(double seconds) {
		secondsToFullfillOrder = seconds;
	}
	
	public int getInventory(String InventoryItem) {
		if(inventory.containsKey(InventoryItem)) {
			return inventory.get(InventoryItem).amount;
		}
		return 0;
	}

	public void setInventory(String InventoryItem, int amount) {
		Food f;
		if(inventory.containsKey(InventoryItem)) {
			f = inventory.get(InventoryItem);
			f.amount = amount;
		}
		else {
			f = new Food(InventoryItem, amount, 100);
			inventory.put(InventoryItem, f);
		}
	}

	/**A class to hold an Order from a cook
	 * 
	 */
	class MyOrder {
		RyansCookRole cook;
		RyansCashier cashier;
		List<MarketOrder> order;
		OrderState state;
	}
	
	public static class MarketOrder {
		String type;
		int quantity;
		
		public MarketOrder() {
			type = "";
			quantity = 0;
		}
		
		public MarketOrder(String type, int quantity) {
			this.type = type;
			this.quantity = quantity;
		}
	}
	
	class Food {
		String type;
		int amount;
		int capacity;
		
		/**
		 *  Constructor for Food class
		 * @param type			the type of food
		 * @param cookingTime	the time it takes to cook
		 * @param amount		the amount of food currently in stock
		 * @param low			the amount to be considered low
		 * @param capacity		the theoretical maximum amount that can be stored //can have more than capacity
		 */
		Food(String type, int amount, int capacity)
		{
			this.type = type; this.amount = amount; this.capacity = capacity;
		}
		
		boolean fulfillable(int amount) {
			if(amount == 0)
				return false;
			return this.amount >= amount;
		}
		
		/**
		 * 
		 * @param amount the amount of food to remove from inventory
		 * @return true if the order was fulfilled false otherwise
		 */
		boolean fulfill(int amount) {
			if(fulfillable(amount)) {
				this.amount -= amount;
				return true;
			}
			else
				return false;
		}
		
		int fulfillAsMuchAsPossible(int quantity) {
			
			if(fulfill(quantity)) {
				return quantity;
			}
			else if(quantity > amount) {
				int q;
				q = amount;
				fulfill(q);
				return q;
			}
			
			return 0;
		}
		
	}

	
	
}

