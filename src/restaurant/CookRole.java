package restaurant;

import interfaces.Customer;
import interfaces.MarketManager;
import interfaces.Waiter;
import interfaces.generic_interfaces.GenericCook;

import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import restaurant.Order.orderStatus;
import restaurant.gui.CookGui;
import trace.AlertLog;
import trace.AlertTag;
import MarketEmployee.MarketManagerRole;
import Person.Role.Role;
import Person.Role.RoleState;
import Person.Role.ShiftTime;
import agent.Constants;
import building.BuildingList;
//import restaurant.gui.CookGui;

/**
 * Restaurant Cook Agent
 */

public class CookRole extends GenericCook {
	public List<Order> orders = new ArrayList<Order>();
	public List<Food> inventory = new ArrayList<Food>();
	public List<MarketManagerRole> markets = new ArrayList<MarketManagerRole>();

	private RevolvingStand revolvingStand = new RevolvingStand();
	Timer checkRevolvingStand;
	
	private Menu menu = new Menu();
	java.util.Timer timer = new java.util.Timer();
	Order currentOrder;
	public CookGui cookGui = null;
	
	public enum AgentState
	{DoingNothing, CookingFood, StockingUp};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent
	{none, placedOrder, reOrder, orderPartiallyFulfilled, orderFulfilled};
	AgentEvent event = AgentEvent.none;

	public CookRole(String workLocation) {
		super(workLocation);
		
		for(int i=0; i<3; i++) {
			inventory.add(new Food(menu.getDishName(i), 5000, 3));
		}
		
		javax.swing.Timer checkRevolvingStand = new javax.swing.Timer(8000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub	
				stateChanged();
			}
			
		});
		
		checkRevolvingStand.start();	
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
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Received order from " + waiter.getName());
		orders.add(new Order(waiter, choice, table, customer));
		stateChanged();
	}
	/**
	 * 
	 * @param ingredientNum if (ingredientNum==0) foodType=Steak; if (ingredientNum==1) foodType=Chicken; if (ingredientNum==2) foodType=Burger;
	 * @param quantity amount of ingredient being delivered
	 */
	public void msgOrderFilled(int ingredientNum, int quantity) {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, getNameOfRole(), "Recieved shipment of " + quantity + " " + inventory.get(ingredientNum).getName() + "s.");
		inventory.get(ingredientNum).addToInventory(quantity);
		event = AgentEvent.orderFulfilled;
		stateChanged();
	}
	/**
	 * 
	 * @param ingredientNum if (ingredientNum==0) foodType=Steak; if (ingredientNum==1) foodType=Chicken; if (ingredientNum==2) foodType=Burger;
	 * @param quantity amount of ingredient being delivered
	 * @param quantityOfOrderThatMarketDoesntHave Amount of the order that the Market Could send
	 */
	public void msgOrderPartiallyFilled(int ingredientNum, int quantity, int quantityOfOrderThatMarketDoesntHave) {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, getNameOfRole(), "Recieved shipment of " + quantity + " " + inventory.get(ingredientNum).getName() + "s.");
		inventory.get(ingredientNum).addToInventory(quantity);
		event = AgentEvent.orderPartiallyFulfilled;
		stateChanged();
	}
	/**
	 * 
	 * @param ingredientNum if (ingredientNum==0) foodType=Steak; if (ingredientNum==1) foodType=Chicken; if (ingredientNum==2) foodType=Burger;
	 */
	public void msgOrderNotFilled(int ingredientNum) {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, getNameOfRole(), "Market could not provide " + inventory.get(ingredientNum).getName() + "s. Will order from different market.");
		event = AgentEvent.reOrder;
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if(roleState == RoleState.Deactivating && orders.size() == 0) {
			kill();
			return true;
		}
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
	             //get the order from the stand
				Order order = revolvingStand.getLastOrder();
	             //structure the order data to fit in with my old cooking routine
				Order newOrder = new Order(order.waiter, order.choice, order.table, order.customer);
				newOrder.status = orderStatus.pending;
				orders.add(newOrder);
	             //cook the order in the same way
				cookOrder(newOrder);
				return true;
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
		3000);
		//currentOrderDone();
		//o.setCooked();
		//o.getWaiter().msgBringFoodToTable(o);
		//print("Food is ready for " + o.getWaiter().getName() + " to take to table " + o.getTableNum());
	}
	
	private void cookCurrentOrder() {
		currentOrder.setCooked();
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, getNameOfRole(), "Food is ready for " + currentOrder.getWaiter().getName() + " to take to table " + (currentOrder.getTableNum()+1));
		currentOrder.getWaiter().msgBringFoodToTable(currentOrder);
	}
	
	private void outOfFood(Order o) {
		o.getWaiter().msgOutOfFood(o.choice, o.getCustomer());
	}
	
	private void goToMarket(int ingredientNum) {
		//print("Num of markets: " + markets.size());
		List<Role> inhabitants;
		boolean tempCheck=false;
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, getNameOfRole(), "Looking Up Market 1's manager to call in order");
		do{
		inhabitants=BuildingList.findBuildingWithName("Market 1").getInhabitants();
			try {
				Thread.sleep(1 * Constants.SECOND);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Role r : inhabitants) {
				if (r instanceof MarketManager){
					tempCheck=true;
					AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, getNameOfRole(), "Cook has found a market manager to call");
					MarketManagerRole mr = (MarketManagerRole) r;
					addMarket(mr);
					break;
				}
			}
		}
		while(!tempCheck);
			

		event = AgentEvent.placedOrder;
		int marketChoice;
		Random randNum = new Random();
		marketChoice = randNum.nextInt(markets.size());

		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, getNameOfRole(), "Low on " + inventory.get(ingredientNum).getName() + ". Getting more from Market 1.");
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
		return Role.RESTAURANT_COOK_ROLE;
	}

	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
	}

	public RevolvingStand getRevolvingStand() {
		return revolvingStand;
	}

}

