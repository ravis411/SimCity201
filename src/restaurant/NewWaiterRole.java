package restaurant;

import Person.Role.Role;
import agent.Agent;
import restaurant.gui.HostGui;
import restaurant.Order;
import restaurant.Menu;
import restaurant.RestaurantCustomerRole.AgentEvent;
import restaurant.RestaurantCustomerRole.AgentState;
import restaurant.Menu.Dish;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the WaiterAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class NewWaiterRole extends Role implements Waiter {
	/*public List<CustomerAgent> myCustomers
	= new ArrayList<CustomerAgent>();*/
	
	private Vector<MyCustomer> myCustomers = new Vector<MyCustomer>();
	private Vector<Order> cookedOrders = new Vector<Order>();
	private Vector<Check> checks = new Vector<Check>();

	private String name;
	private Semaphore atWaitingArea = new Semaphore(0, true);
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atCookingArea = new Semaphore(0,true);
	private Semaphore atPlatingArea = new Semaphore(0,true);
	private Semaphore atFrontDesk = new Semaphore(0,true);
	private Semaphore onBreakSema = new Semaphore(0,true);
	private int atTableForOrder = -1; //-1 means waiter is not at a table
	private boolean requestBreak = false;
	private boolean onBreak = false;
	private Menu menu = new Menu();
	Timer timer = new Timer();
	
	private RevolvingStand revolvingStand = RevolvingStand.getInstance();
	
	public enum AgentState
	{DoingNothing, AtFrontDesk, AtTable, AtKitchen, TakingOrder, TakeOrderToKitchen};
	private AgentState state = AgentState.AtFrontDesk;//The start state

	public enum AgentEvent 
	{none, seatCustomer};
	AgentEvent event = AgentEvent.seatCustomer;
	
	public enum CustomerState
	{waiting, seated, ordered, needToReorder};
	
	private CookRole cook;
	private HostRole host;
	private CashierRole cashier;

	public WaiterGui waiterGui = null;

	public NewWaiterRole() {
		super();

	}

	public String getName() {
		return name;
	}
	
	public void setCook(CookRole cook) {
		this.cook = cook;
	}
	
	public void setHost(HostRole host) {
		this.host = host;
	}
	
	public void setCashier(CashierRole cashier) {
		this.cashier = cashier;
	}
	
	//Messages
	
	public void msgSeatCustomer(Customer cust) {
		myCustomers.add(new MyCustomer(cust));
		event = AgentEvent.seatCustomer;
		stateChanged();
	}
	
	public void msgGoTakeOrder() {
		state = AgentState.TakingOrder;
		stateChanged();
	}
	
	public void msgTakeOrder(Customer c, int choice) {
		for(int i=0; i<myCustomers.size(); i++) {
			if(myCustomers.get(i).customer == c) {
				myCustomers.get(i).setMealChoice(choice);
			}
		}
		state = AgentState.TakeOrderToKitchen;
		stateChanged();
	}
	
	public void msgBringFoodToTable(Order o) {
		cookedOrders.add(o);
		stateChanged();
	}
	
	public void msgAtWaitingArea() {
		atWaitingArea.release();
		//stateChanged();
	}
	
	public void msgAtTable() {//from animation
		atTable.release();// = true;
		state = AgentState.AtTable;
		//stateChanged();
	}
	
	public void msgAtCookingArea() {
		atCookingArea.release();
		state = AgentState.AtTable;
		//stateChanged();
	}
	
	public void msgAtPlatingArea() {
		atPlatingArea.release();
		state = AgentState.AtKitchen;
		//stateChanged();
	}
	
	public void msgAtFrontDesk() {
		atFrontDesk.release();
		state = AgentState.AtFrontDesk;
		//stateChanged();
	}
	
	public void msgLeavingTable(Customer cust) {
		for(int i=0; i<myCustomers.size(); i++) {
			if (cust.getTableNum() == myCustomers.get(i).customer.getTableNum()) {
				print(cust + " leaving table " + (cust.getTableNum()+1));
			}
		}
		stateChanged();
	}
	
	public void msgOutOfFood(int menuItem, Customer c) {
		print("We're out of " + menu.getDishName(menuItem) + "!");
		for(int i=0; i<myCustomers.size(); i++) {
			if(myCustomers.get(i).customer == c) {
				myCustomers.get(i).state = CustomerState.needToReorder;
			}
		}
		stateChanged();
	}
	
	public void msgBreakReply(boolean reply) {
		if(reply == true) {
			onBreak = true;
		}
		else {
			onBreak = false;
			waiterGui.DoBackFromBreak();
		}
		stateChanged();
	}
	
	public void msgCheckReady(Customer customer, double amount) {
		checks.add(new Check(customer,amount));
		stateChanged();
	}
	
	public void msgRequestBreak() {
		requestBreak = true;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if(requestBreak == true) {
			requestBreak();
			return true;
		}
		if(onBreak == true && myCustomers.isEmpty()) {
			goOnBreak();
			return true;
		}
			if(!checks.isEmpty()){
				BringCheckToTable(checks.get(0));
				checks.remove(0);
				return true;
			}
			if(!cookedOrders.isEmpty() && state != AgentState.AtKitchen) {
				GetFoodFromKitchen(cookedOrders.get(0));
				return true;
			}
			if(!cookedOrders.isEmpty() && state == AgentState.AtKitchen) {
				TakeFoodToTable(cookedOrders.get(0));
				return true;
			}
			for(int i=0; i<myCustomers.size(); i++) {
				if(myCustomers.get(i).customer.getState() == RestaurantCustomerRole.AgentState.LeavingEarly) {
					CustomerLeaving(myCustomers.get(i));
					return true;
				}
				if(myCustomers.get(i).customer.getState() == RestaurantCustomerRole.AgentState.Paying) {
					CustomerLeaving(myCustomers.get(i));
					return true;
				}
				if(myCustomers.get(i).state == CustomerState.needToReorder) {
					GoRetakeOrder(myCustomers.get(i));
					return true;
				}
				if(myCustomers.get(i).customer.getState() == RestaurantCustomerRole.AgentState.WaitingInRestaurant && state != AgentState.AtFrontDesk && event == AgentEvent.seatCustomer) {
					goToFrontDesk();
					return true;
				}
				if(myCustomers.get(i).customer.getState() == RestaurantCustomerRole.AgentState.WaitingInRestaurant && state == AgentState.AtFrontDesk) {
					seatCustomer(myCustomers.get(i));
					return true;
				}
				if(myCustomers.get(i).customer.getState() == RestaurantCustomerRole.AgentState.WaitingToOrder && myCustomers.get(i).customer.getEvent() == RestaurantCustomerRole.AgentEvent.readyToOrder) {
					GoTakeOrder(myCustomers.get(i));
					return true;
				}
				if(myCustomers.get(i).customer.getState() == RestaurantCustomerRole.AgentState.WaitingForOrder && state == AgentState.TakeOrderToKitchen && myCustomers.get(i).customer.getEvent() == RestaurantCustomerRole.AgentEvent.order){
					if(!revolvingStand.isFull()){
						TakeOrderToCook(myCustomers.get(i));
						return true;
					}
				}
				if(state == AgentState.AtTable) {
					goToIdle();
					return true;
				}
			}
			//return false;
		return false;
	}

	// Actions

	private void goToIdle() {
		//waiterGui.DoGoToIdle();
		waiterGui.DoLeaveCustomer();
	}
	
	private void goToFrontDesk() {
		waiterGui.DoLeaveCustomer();
		try {
			atFrontDesk.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void seatCustomer(MyCustomer c) {
		DoGoToWaitingArea();
		try {
			atWaitingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = AgentState.AtTable;
		c.customer.msgSitAtTable(menu);
		DoSeatCustomer(c, c.customer.getTableNum());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();
		//state = AgentState.DoingNothing;
	}
	
	private void DoGoToWaitingArea() {
		waiterGui.DoGoGetCustomer();
	}
	
	private void DoSeatCustomer(MyCustomer c, int tableNum) {
		print("Seating " + c.customer.getName() + " at table " + (tableNum+1));
		waiterGui.DoBringToTable(c.customer);
	}
	
	private void GoTakeOrder(MyCustomer c) {
		waiterGui.DoGoTakeOrder(c.customer);
		atTableForOrder = c.customer.getTableNum();
		print("Going to table " + (c.customer.getTableNum()+1) + " to take order.");
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TakeOrder(c);
	}
	
	private void GoRetakeOrder(MyCustomer c) {
		waiterGui.DoGoTakeOrder(c.customer);
		atTableForOrder = c.customer.getTableNum();
		print("Going to table " + (c.customer.getTableNum()+1) + " to retake order.");
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RetakeOrder(c);
	}
	
	private void TakeOrder(MyCustomer c) {
		print("Taking " + c.customer.getName() + "'s order.");
		c.customer.msgWaiterReadyToTakeOrder();
	}
	
	private void RetakeOrder(MyCustomer c) {
		print("Retaking " + c.customer.getName() + "'s order.");
		c.state = CustomerState.ordered;
		c.customer.msgWaiterReadyToRetakeOrder();
	}
	
	private void TakeOrderToCook(MyCustomer c) {
		print("Taking " + c.customer.getName() + "'s order to cook.");
		waiterGui.DoGoToCookingArea();
		try {
			atCookingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//cook.orders.add(new Order(this, c.mealChoice, c.customer.getTableNum(), c.customer));
		revolvingStand.addIncomingOrder(this, c.customer.getTableNum(), c.mealChoice, c.customer);
		c.customer.msgOrderOnItsWay();
	}
	
	private void GetFoodFromKitchen(Order o) {
		print("Getting " + o.getCustomer().getName() + "'s food from kitchen");
		waiterGui.DoGoToPlatingArea();
		try {
			atPlatingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void TakeFoodToTable(Order o) {
		print("Taking " + o.getCustomer().getName() + "'s food to table " + (o.getCustomer().getTableNum()+1));
		cook.cookGui.setPlate(0);
		waiterGui.DoTakeFoodToTable(o.customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		o.getCustomer().msgFoodAtTable();
		cashier.msgPrepareCheck(this, o.getCustomer(), o.choice);
		cookedOrders.remove(0);
	}
	
	private void BringCheckToTable(Check check) {
		print("Taking check to table " + (check.customer.getTableNum()+1));
		waiterGui.DoTakeCheckToTable(check.customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		check.customer.msgCheckAtTable(check.amount);
	}
	
	private void CustomerLeaving(MyCustomer c) {
		host.msgLeavingTable(c.customer);
		for(int i=0; i<myCustomers.size(); i++) {
			if(myCustomers.get(i).customer == c.customer) {
				myCustomers.remove(i);
			}
		}
		myCustomers.remove(c);
		waiterGui.DoGoToIdle();
	}
	
	public void requestBreak() {
		print("Can I go on break please?");
		host.msgWaiterGoOnBreak(this);
		requestBreak = false;
	}
	
	public void goOnBreak() {
		print("I'm going on break!");
		waiterGui.DoGoOnBreak();
		timer.schedule(new TimerTask() {
			public void run() {
				print("Off break!");
				onBreak = false;
				waiterGui.DoBackFromBreak();
				onBreakSema.release();
				stateChanged();
			}
		},
		5000);
		try {
			onBreakSema.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public void setAtTableForOrder(int tableNum) {
		atTableForOrder = tableNum;
	}
	
	public int getAtTableForOrder() {
		return atTableForOrder;
	}
	
	public int getNumberOfCustomers() {
		return myCustomers.size();
	}
	
	public boolean getBreakStatus() {
		return onBreak;
	}

	private class MyCustomer {
		public Customer customer;
		private int mealChoice = -1;
		private boolean orderTaken = false;
		
		private CustomerState state = CustomerState.seated;
		
		MyCustomer(Customer c) {
			customer = c;
		}
		
		void setMealChoice(int mc) {
			mealChoice = mc;
		}
	}
	
	private class Check {
		Customer customer;
		double amount;
	
		Check(Customer customer, double amount) {
			this.customer = customer;
			this.amount = amount;
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
		return "NewWaiterRole";
	}
}

