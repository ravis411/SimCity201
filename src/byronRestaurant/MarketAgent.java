package restaurant;
import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

public class MarketAgent extends Agent{
	/**
	 * Restaurant Market Agent
	 */
	private String name;
	private CookAgent cook;
	private CashierAgent cashier;
	private double register;
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private class Order{
		public String food;   
		public int amount;
		public double price;
		Order(String f, int a){
			food = f;
			amount = a;
			price = prices.get(f)*a;
		}
	}
	private Map<String, Integer> inventory = new HashMap<String, Integer>(){{
		put("Steak", 50);
		put("Chicken",50);
		put("Salad", 50);
		put("Pizza", 50);
	}};
	private Map<String, Double> prices = new HashMap<String, Double>(){{
		put("Steak", 10.00);
		put("Chicken", 5.00);
		put("Salad", 1.00);
		put("Pizza", 7.00);
	}};

	//hacks to set other agents
	public void setCook(CookAgent c){
		cook = c;
	}
	public void setCashier(CashierAgent c){
		cashier = c;
	}

	//Initialize Market
	public MarketAgent(String name) { 
		super();
		this.name = name;
	}


	// messages
	public void msgIWantFood(String f, int a){
		orders.add(new Order(f, a));
		stateChanged();
	}

	public void msgBillPaid(double b){
		register = register + b;

	}

	// Scheduler
	protected boolean pickAndExecuteAnAction() {
		synchronized(orders){
			for (Order o : orders){
				if (o.amount <= inventory.get(o.food)){
					hereIsFood(o);
					return true;
				}
				else if (o.amount > inventory.get(o.food)){
					notEnoughFood(o);
					return true;
				}
			}
		}
		return false;
	}

	//actions
	private void hereIsFood(Order o){
		Do("Restocking byronRestaurant");
		Do("Costs " + o.price);
		cook.msgHereIsFood(o.food, o.amount);
		int temp = inventory.get(o.food) - o.amount;
		inventory.put(o.food, temp);
		cashier.msgFoodDelivered(o.price);
		orders.remove(o);

	}

	private void notEnoughFood(Order o){
		cook.msgNotEnoughFood(o.food);
	}

	//Utilities
	public String getName(){
		return name;
	}

}