package jeffreyRestaurant;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Map;
import java.util.HashMap;

import jeffreyRestaurant.Gui.CookGui;
import jeffreyRestaurant.Gui.HostGui;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CookAgent extends Agent {
	
	public void addMarket(MarketAgent m) {
		markets.add(new myMarket(m));
	}
	
	//Data
	public CookAgent() {//Default, never called
		//Foods
		food.put("Steak", new food("Steak", 1, 6));
		print("Just created Steak for " + food.get("Steak"));
		food.put("Chicken", new food ("Chicken", 1, 5));
		print("Just created Chicken for " + food.get("Steak"));
		food.put("Salad", new food("Salad", 1, 2));
		print("Just created salad for " + food.get("Salad"));
		food.put("Pizza", new food("Pizza", 1, 7));
		print("Just created Pizza for " + food.get("Pizza"));
	}
	private class order {
		order(WaiterAgent waiter, String c, int tableNum) {
			w = waiter; choice = c; table = tableNum;
		}
		WaiterAgent w;
		String choice;
		int table;
		state s = state.pending;
		Timer foodTimer = new Timer();
		void cookFood(Integer time, final order o) {
			print("Time is " + time);
			foodTimer.schedule(new TimerTask() {
				public void run() {
					foodDone(o);
				}
			}, time*1000);
		}
	}
	private class food {
		food(String name, int q, int t) {
			type = name;
			quantity = q;
			time = t;
		}
		String type;
		int quantity;
		int quantityOrdered = 0;
		boolean isLow() {
			return (quantityOrdered+quantity) < LOW_FOOD;
		}
		void orderedFood(int quantity){
			this.quantityOrdered += quantity;	
		}
		int time;
	}
	private class myMarket {
		myMarket(MarketAgent agent) {
			m = agent;
			marketFoods.put("Steak", m.getFoodNum("Steak"));
			marketFoods.put("Chicken",m.getFoodNum("Chicken"));
			marketFoods.put("Salad", m.getFoodNum("Salad"));
			marketFoods.put("Pizza",m.getFoodNum("Pizza"));
		}
		MarketAgent m;
		void updateMarket() {
			marketFoods.put("Steak", m.getFoodNum("Steak"));
			marketFoods.put("Chicken",m.getFoodNum("Chicken"));
			marketFoods.put("Salad", m.getFoodNum("Salad"));
			marketFoods.put("Pizza",m.getFoodNum("Pizza"));
		}
		Map<String, Integer> marketFoods = new HashMap<String, Integer>();
	}
	
	
	private String name;
	private Semaphore waitingForMarket = new Semaphore(0, true);
	private Semaphore animation = new Semaphore(0, true);
	private List<order> orders = Collections.synchronizedList(new ArrayList<order>());
	private List<myMarket> markets = Collections.synchronizedList(new ArrayList<myMarket>());
	enum state{pending,cooking,done};
	private final int LOW_FOOD = 4;
	private final int ORDER_AMOUNT = 10;
	
	public CookGui gui = null;
	//private Timer timer;
	
	private Map<String, food> food = new HashMap<String, food>();
	//Fill map in class constructor
	
	public CookAgent(String Name) {
		name = Name;
		//Foods
		food.put("Steak", new food("Steak", 1, 6));
		food.put("Chicken", new food ("Chicken", 1, 5));
		food.put("Salad", new food("Salad", 1, 2));
		food.put("Pizza", new food("Pizza", 1, 7));
	}
	public String getName() {
		return name;
	}
	
	//Messages
	public void msgHereIsOrder(WaiterAgent w, String choice, int table) {
		orders.add(new order(w,choice,table));
		stateChanged();
	}
	
	public void foodDone(order o) {
		print(o.choice + " is done");
		o.s = state.done;
		stateChanged();
	}
	
	public void msgPartialMarketOrder(String food, int quantity, MarketAgent mk) {
		print("Received " + quantity + " of " + food + " from market");
		this.food.get(food).quantity += quantity;
		this.food.get(food).quantityOrdered -= quantity;
		for (myMarket m : markets) {
			if (m.m == mk) {
				m.updateMarket();
			}
		}
		waitingForMarket.release();
	}
	
	public void msgMarketOrder(String food, int quantity, MarketAgent mk) {
		print("Received " + quantity + " of " + food + " from market");
		this.food.get(food).quantity += quantity;
		this.food.get(food).quantityOrdered -= quantity;
		for (myMarket m : markets) {
			if (m.m == mk) {
				m.updateMarket();
			}
		}
		waitingForMarket.release();
	}
	
	
	public void msgAnimationDone() {
		animation.release();
		stateChanged();
	}
	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		if (orders != null) {
			synchronized(orders) {
				for (order o : orders) {
					if (o.s == state.pending) {
						cookIt(o);
						return true;
					}
					if (o.s == state.done){
						plateFood(o);
						return true;
					}
				}
			}
		}
		DoGoHome();
		return false;
	}
	
	//Actions
	private void cookIt(order o) {
		synchronized(orders) {
			if (food.get(o.choice).isLow()) {
				print("We are low in " + o.choice);
				for (myMarket m : markets) {
					if (m.marketFoods.get(o.choice) > 0) {
						m.m.msgNeedFood(o.choice, ORDER_AMOUNT);
						food.get(o.choice).orderedFood(ORDER_AMOUNT);
						try {
							waitingForMarket.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (food.get(o.choice).quantity > LOW_FOOD) {
						print("We're good now with " + food.get(o.choice).quantity + " of " + food.get(o.choice).type);
						break;
					} 
				}
			}
			if (food.get(o.choice).quantity == 0) {
				print("We are out of " + o.choice);
				o.w.msgOutOfFood(o.choice, o.table);
				orders.remove(o);
				//Add market code
			}
			else {
				print("Cooking " + o.choice + " for " + food.get(o.choice).time + " seconds");
				o.s = state.cooking;
				DoCookFood();
				try {
					animation.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				o.cookFood(food.get(o.choice).time, o);
				food.get(o.choice).quantity--;
				print("There is now " + food.get(o.choice).quantity + " of " + food.get(o.choice).type);
				DoGoHome();
			}
		}
	}
	private void plateFood(order o) {
		synchronized(orders) {
			print("Giving waiter " + o.choice);
			DoPlateFood();
			try {
				animation.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			o.w.msgGiveFood(o.choice, o.table);
			orders.remove(o);
			DoGoHome();
		}
	}
	
	//Animation calls
	private void DoCookFood() {
		gui.DoCookFood();
	}
	private void DoPlateFood() {
		gui.DoPlateFood();
	}
	private void DoGoHome() {
		gui.goHome();
	}
	
	public void setGui(CookGui gui) {
		this.gui = gui;
	}
	
}

