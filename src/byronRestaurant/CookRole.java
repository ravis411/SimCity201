package byronRestaurant;


import agent.Agent;
import byronRestaurant.gui.CookGui;
import byronRestaurant.gui.WaiterGui;

import java.util.*;
import java.util.concurrent.Semaphore;

import restaurant.WaiterAgent;

/**
 * Restaurant Cook Agent
 * make sure to change when cook makes an order to market (threshold point)
 * 
 * 
 */
public class CookRole extends Agent {
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private MarketAgent market;
	public enum cookStatus {pending, cooking, done};
	private String name;
	private int foodThreshold = 3;
	private int foodMaximum = 50;
	private CookGui cookGui;
	private Semaphore atPlatingArea = new Semaphore(0,true);
	private Semaphore atKitchen = new Semaphore(0,true);
	private Semaphore atDefault = new Semaphore(0,true);
	private class Order{
		public WaiterAgent waiter;
		public int table;
		public String choice;
		public cookStatus status;
		
		Order(WaiterAgent w, int t, String c, cookStatus s){
			waiter = w;
			table = t;
			choice = c;
			status = s;
		}
	}
	Timer timer2 = new Timer();
	private class Food{
		public String type;
		public double cookTime;
		public int amount;

		Food(String t, double c, int a){
			type = t;
			cookTime = c;
			amount = a;
		}
	}
	private Map<String, Food> inventory = Collections.synchronizedMap(new HashMap<String, Food>() {{
		put("Steak", new Food("Steak", 6000,5));
		put("Chicken", new Food("Chicken", 5000, 5));
		put("Salad", new Food("Salad", 1000, 5));
		put("Pizza", new Food("Pizza", 8000, 5));
	}});


	//Initialize Cook
	public CookRole(String name) { 
		super();

		this.name = name;
	}

	public void AddMarket(MarketAgent m){
		market = m;
	}

	// messages
	public void msgHereIsAnOrder(WaiterAgent waiter, int table, String choice){
		orders.add(new Order(waiter, table, choice, cookStatus.pending));
		print("Receiving new order from " + waiter.getName());
		stateChanged();
	}
	public void msgfoodDone(Order o){
		o.status = cookStatus.done;
		stateChanged();
	}

	public void msgHereIsFood(String s, int a){
		int temp = inventory.get(s).amount + a;
		Food tempFood = new Food(s, inventory.get(s).cookTime,temp);
		inventory.put(s,tempFood);
		synchronized(orders){
			for (Order o : orders){
				o.waiter.msgRestockedItem(s);
			}
		}
		stateChanged();
	}

	public void msgNotEnoughFood(String s){
		Do("Market has run out, keep " + s + " off the menu.");
	}
	public void msgTakingFood(){
		cookGui.setOnPlate(false);
	}
	public void msgAtPlatingArea(){
		atPlatingArea.release();
		stateChanged();
	}
	public void msgAtKitchen(){
		atKitchen.release();
		stateChanged();
	}
	public void msgAtDefault(){
		atDefault.release();
		stateChanged();
	}
	
	// Scheduler
	protected boolean pickAndExecuteAnAction() {
		synchronized(orders){
			for (Order o : orders){
				if (o.status == cookStatus.done){
					placeOrder(o);
					return true;
				}
				if (o.status == cookStatus.pending){
					cookOrder(o);
					return true;
				}
			}
		}
		return false;
	}

	//Actions
	private void placeOrder(Order o){
		cookGui.doGoToPlatingArea();
		try {
			atPlatingArea.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.setOnPlate(true);
		o.waiter.msgOrderIsReady(o.table, o.choice);
		print("Order is ready");
		orders.remove(o);
		cookGui.doGoToDefault();
		try {
			atDefault.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	private void cookOrder(final Order o){
			if (inventory.get(o.choice).amount == foodThreshold){
//				o.waiter.msgOutOfStock(o.choice, o.table);
				market.msgIWantFood(o.choice, (foodMaximum-inventory.get(o.choice).amount));
//				orders.remove(o);
			}else if (inventory.get(o.choice).amount == 0){
				o.waiter.msgOutOfStock(o.choice, o.table);
				orders.remove(o);
			}
			else{	
				Food food = inventory.get(o.choice);
				cookGui.doGoToKitchen();
				try {
					atKitchen.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cookGui.setIsCooking(true);
				o.status = cookStatus.cooking;
				timer2.schedule(new TimerTask(){
					public void run(){
						msgfoodDone(o);
						cookGui.setIsCooking(false);
					}	
				}, (long)food.cookTime);
				inventory.get(o.choice).amount --;
				print ("Cooking order of " + o.choice + ". " + inventory.get(o.choice).amount + " remaining.");
		}
	}

	//Utilities
	public String getName(){
		return name;
	}
	
	public void setGui(CookGui gui) {
		cookGui = gui;
	}

	public CookGui getGui() {
		return cookGui;
	}

}
