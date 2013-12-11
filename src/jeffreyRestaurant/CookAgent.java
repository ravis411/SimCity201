package jeffreyRestaurant;

import agent.Agent;
import interfaces.generic_interfaces.GenericCook;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Map;
import java.util.HashMap;

import javax.swing.Timer;

import MarketEmployee.MarketManagerRole;
import Person.Role.Role;
import Person.Role.ShiftTime;
import jeffreyRestaurant.CashierAgent.OrderState;
import jeffreyRestaurant.Gui.CookGui;
import jeffreyRestaurant.Gui.HostGui;
import jeffreyRestaurant.interfaces.Cook;


/**
 * Restaurant Cook Agent that prepares food and
 * orders from the market. 
 * @author JEFFREY
 *
 */
public class CookAgent extends GenericCook implements Cook{
	
	public void addMarket(MarketManagerRole m) {
		markets.add(new myMarket(m));
	}
	
	//Data
	private class order {
		order(WaiterAgent waiter, String c, int tableNum) {
			w = waiter; choice = c; table = tableNum;
		}
		WaiterAgent w;
		String choice;
		int table;
		state s = state.pending;
		java.util.Timer foodTimer = new java.util.Timer();
		void cookFood(Integer time, final order o) {
			//print("Time is " + time);
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
		myMarket(MarketManagerRole agent) {
			m = agent;
			marketFoods.put("Steak", m.getMarketData().getAmount(mInventoryMap.get("Steak")));
			marketFoods.put("Chicken",m.getMarketData().getAmount(mInventoryMap.get("Chicken")));
			marketFoods.put("Salad", m.getMarketData().getAmount(mInventoryMap.get("Burger")));
		}
		MarketManagerRole m;
		void updateMarket() {
			marketFoods.put("Steak", m.getMarketData().getAmount(mInventoryMap.get("Steak")));
			marketFoods.put("Chicken",m.getMarketData().getAmount(mInventoryMap.get("Chicken")));
			marketFoods.put("Salad", m.getMarketData().getAmount(mInventoryMap.get("Burger")));
		}
		Map<String, Integer> marketFoods = new HashMap<String, Integer>();
	}
	
	private Map<String, Integer> mInventoryMap = new HashMap<String,Integer>();
	
	private RevolvingStand orderStand = new RevolvingStand();
	
	
	
	
	
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
	
	public CookAgent(String location) {
		super(location);
		//Foods
		food.put("Steak", new food("Steak", 1, 6));
		food.put("Chicken", new food ("Chicken", 1, 5));
		food.put("Salad", new food("Salad", 1, 2));
		food.put("Pizza", new food("Pizza", 1, 7));
		javax.swing.Timer orderStandTime = new Timer(15000, new ActionListener(){ 
			@Override
			public void actionPerformed(ActionEvent e) {
				stateChanged();
			}
			
		});
		orderStandTime.start();
	}
	public String getName() {
		return myPerson.getName();
	}
	public RevolvingStand getStand() {
		return orderStand;
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
	
	public void msgPartialMarketOrder(String food, int quantity, MarketManagerRole mk) {
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
	
	public void msgMarketOrder(String food, int quantity, MarketManagerRole mk) {
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
	public boolean pickAndExecuteAction() {
		
		if (orderStand.isEmtpy() && workState == WorkState.ReadyToLeave) {
			//kill();
			//return true;
		}
		
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
		
		if (!orderStand.isEmtpy()) {
			RevolvingStand.order Order = orderStand.getLastOrder();
			order newOrder = new order(Order.getWaiter(), Order.getChoice(), Order.getTable());
			newOrder.s = state.pending;
			orders.add(newOrder);
			cookIt(newOrder);
			return true;
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
						m.m.msgMarketManagerFoodOrder(o.choice, ORDER_AMOUNT, this);
						food.get(o.choice).orderedFood(ORDER_AMOUNT);
						try {
							waitingForMarket.acquire();
						} catch (InterruptedException e) {
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

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_JEFFREY_COOK_ROLE;
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

