package restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.Order.orderStatus;
import restaurant.gui.CookGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import MarketEmployee.MarketManagerRole;
import Person.Role.Role;
import building.BuildingList;

/**
 * Restaurant Cook Agent
 */

public class CookRole extends Role {
	public List<Order> orders = new ArrayList<Order>();
	public List<Food> inventory = new ArrayList<Food>();
	public List<MarketManagerRole> markets = new ArrayList<MarketManagerRole>();

	private RevolvingStand revolvingStand = RevolvingStand.getInstance();
	
	private Menu menu = new Menu();
	Timer timer = new Timer();
	Order currentOrder;
	public CookGui cookGui = null;
	
	public enum AgentState
	{DoingNothing, CookingFood, StockingUp};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent
	{none, placedOrder, reOrder, orderPartiallyFulfilled, orderFulfilled};
	AgentEvent event = AgentEvent.none;

	public CookRole() {
		super();
		
		for(int i=0; i<3; i++) {
			inventory.add(new Food(menu.getDishName(i), 5000, 3));
		}
		List<Role> inhabitants = BuildingList.findBuildingWithName("Market 1").getInhabitants();
		for(Role r : inhabitants) {
			if (r.getNameOfRole() == "MARKET_MANAGER_ROLE") {
				MarketManagerRole mr = (MarketManagerRole) r;
				addMarket(mr);
			}
		}
	}
	
	public void addMarket(MarketManagerRole market) {
		markets.add(market);
	}
	
	public void setGui(CookGui cg) {
		cookGui = cg;
	}
	
	public void clearInventory() {
		for(int i=0; i<3; i++) {
			inventory.get(i).inventory = 0;
		}
		print("Someone stole all the food!");
	}
	
	// Messages
	public void msgHereIsAnOrder(Waiter waiter, int choice, int table, Customer customer) {
		print("Received order from " + waiter.getName());
		orders.add(new Order(waiter, choice, table, customer));
		stateChanged();
	}
	public void msgOrderFilled(int ingredientNum, int quantity) {
		print("Recieved shipment of " + quantity + " " + inventory.get(ingredientNum).getName() + "s.");
		inventory.get(ingredientNum).addToInventory(quantity);
		event = AgentEvent.orderFulfilled;
		stateChanged();
	}
	public void msgOrderPartiallyFilled(int ingredientNum, int quantity) {
		print("Recieved shipment of " + quantity + " " + inventory.get(ingredientNum).getName() + "s.");
		inventory.get(ingredientNum).addToInventory(quantity);
		event = AgentEvent.orderPartiallyFulfilled;
		stateChanged();
	}
	public void msgOrderNotFilled(int ingredientNum) {
		print("Market could not provide " + inventory.get(ingredientNum).getName() + "s. Will order from different market.");
		event = AgentEvent.reOrder;
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		
		for(int i=0; i<inventory.size(); i++) {
			if(inventory.get(i).inventory < 1 && event != AgentEvent.placedOrder) {
				goToMarket(i);
			}
		}
		if(state == AgentState.DoingNothing) {
			for (int i=0; i < orders.size(); i++) {
				if (orders.get(i).getStatus() == orderStatus.pending) {
						if(inventory.get(orders.get(i).choice).inventory > 0) {
							cookOrder(orders.get(i));//the action
							inventory.get(orders.get(i).choice).subtractFromInventory(1);
							return true;//return true to the abstract agent to reinvoke the scheduler.
						}
						else {
							outOfFood(orders.get(i));
							orders.remove(i);
							return true;
						}
				}
			}
			
			if(!revolvingStand.isEmpty()){
				Order o = revolvingStand.popOrder();
				cookOrder(o);
				inventory.get(o.choice);
			}
			
			
		}
		return false;
	}
	

	// Actions

	private void cookOrder(Order o) {
		state = AgentState.CookingFood;
		cookGui.DoGoToFridge();
		currentOrder = o;
		timer.schedule(new TimerTask() {
			public void run() {
				cookCurrentOrder();
				state = AgentState.DoingNothing;
				cookGui.DoGoToPlatingArea();
				cookGui.setIngredients(1);
				stateChanged();
			}
		},
		5000);
		//currentOrderDone();
		//o.setCooked();
		//o.getWaiter().msgBringFoodToTable(o);
		//print("Food is ready for " + o.getWaiter().getName() + " to take to table " + o.getTableNum());
	}
	
	private void cookCurrentOrder() {
		currentOrder.setCooked();
		print("Food is ready for " + currentOrder.getWaiter().getName() + " to take to table " + (currentOrder.getTableNum()+1));
		currentOrder.getWaiter().msgBringFoodToTable(currentOrder);
	}
	
	private void outOfFood(Order o) {
		o.getWaiter().msgOutOfFood(o.choice, o.getCustomer());
	}
	
	private void goToMarket(int ingredientNum) {
		//print("Num of markets: " + markets.size());
		event = AgentEvent.placedOrder;
		int marketChoice;
		Random randNum = new Random();
		marketChoice = randNum.nextInt(markets.size());
		print("Low on " + inventory.get(ingredientNum).getName() + ". Getting more from Market 1.");
		markets.get(0).msgMarketManagerFoodOrder(inventory.get(ingredientNum).getName(), 5, this);
	}

	//utilities
	
	private class Food {
		private String name;
		private int cookTime;
		private int inventory;
		
		Food(String name, int cookTime, int inventory) {
			this.name = name;
			this.cookTime = cookTime;
			this.inventory = inventory;
		}
		
		public String getName() {
			return name;
		}
		
		public void addToInventory(int amount) {
			inventory = inventory+amount;
		}
		public void subtractFromInventory(int amount) {
			inventory = inventory-amount;
		}
	}

	@Override
	public boolean canGoGetFood() {
		return false;
	}

	@Override
	public String getNameOfRole() {
		return "CookRole";
	}

}

