package byronRestaurant;

import Person.Role.Role;
import Person.Role.ShiftTime;
import Person.Role.Employee.WorkState;
import building.BuildingList;
import building.Restaurant;
import byronRestaurant.gui.WaiterGui;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
/**
 * Restaurant Waiter Agent
 */
public abstract class WaiterRole extends GenericWaiter {
	public enum CustomerState{noAction,notSeated, readyToOrder, orderPending, orderReady, doneEating, gotCheck, isDone, orderNotAvailable};
	Timer timer;
	public List<String>menu = new ArrayList<String>();{
		menu.add("Steak");
		menu.add("Chicken");
		menu.add("Burger");
	}
	protected class MyCustomer{
		public CustomerState state;
		public CustomerRole cust;
		public String choice;
		public int table;
		public double cost;

		MyCustomer(CustomerState s, CustomerRole c, int t){
			state = s;
			cust = c;
			table = t;
		}

		public boolean find(CustomerRole c){
			if (this.cust == c){
				return true;
			}
			return false;
		}

		public boolean findTable(int t){
			if (this.table == t){
				return true;
			}
			return false;
		}
	}
	protected static class Order{
		public WaiterRole waiter;
		public int table;
		public String choice;
		
		Order(WaiterRole waiterRole, int t, String c){
			waiter = waiterRole;
			table = t;
			choice = c;
		}
		
		public WaiterRole getWaiter(){
			return waiter;
		}
		
		public int getTable(){
			return table;
		}
		
		public String getChoice(){
			return choice;
		}
	}

	public List<MyCustomer> Customers = new ArrayList<MyCustomer>();
	protected HostRole host;
	protected CookRole cook;
	protected CashierRole cashier;
	protected String name;
	protected Semaphore atTable = new Semaphore(0,true);
	protected Semaphore atKitchen = new Semaphore(0, true);
	protected Semaphore atLobby = new Semaphore(0,true);
	public WaiterGui waiterGui = null;

	public WaiterRole(String location) {
		super(location);
	}

	public void setCook(GenericCook cook) {
		this.cook = (CookRole) cook;
	}

	public void setHost(GenericHost host) {
		this.host = (HostRole) host;
	}

	public void setCashier(GenericCashier cashier) {
		this.cashier = (CashierRole) cashier;
	}


	// Messages
	public void msgSitCustomerAtTable(CustomerRole cust, int tableNum){
		Customers.add(new MyCustomer(CustomerState.notSeated, cust, tableNum)); 
		stateChanged();
	}

	public void msgReadyToOrder(CustomerRole cust){
		for (MyCustomer customer : Customers){
			if (customer.find(cust)){
				customer.state = CustomerState.readyToOrder;
			}
		}
		stateChanged();
	}

	public void msgHereIsMyChoice(CustomerRole cust, String choice){
		for (MyCustomer customer : Customers){
			if (customer.find(cust)){
				customer.choice = choice;
				customer.state = CustomerState.orderPending;
			}
		}
		stateChanged();		
	}

	public void msgOrderIsReady(int t, String choice){
		for (MyCustomer customer : Customers){
			if (customer.findTable(t)){
				customer.state = CustomerState.orderReady;
			}
		}
		stateChanged();
	}

	public void msgDoneEating(CustomerRole cust){
		for (MyCustomer customer : Customers){
			if (customer.find(cust)){
				customer.state = CustomerState.doneEating;
			}
		}
		stateChanged();
	}
	public void msgHereIsCheck(double a, int i){
		for (MyCustomer customer : Customers){
			if (customer.findTable(i)){
				customer.state = CustomerState.gotCheck;
				customer.cost = a;
			}
		}
		stateChanged();

	}
	public void msgLeaving(CustomerRole cust){
		for (MyCustomer customer : Customers){
			if (customer.find(cust)){
				customer.state = CustomerState.isDone;
			}
		}
		stateChanged();
	}

	//non-normative messages
	public void msgOutOfStock(String o, int tableNum){
		for (MyCustomer customer : Customers){
			if (customer.findTable(tableNum)){
				customer.state = CustomerState.orderNotAvailable;
			}
		}
		stateChanged();
	}

	public void msgRestockedItem(String o){
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Recieved order from cook that " + o + " is restocked");
		menu.add(o);
		stateChanged();
	}

	//semaphore handling
	public void msgAtTable() {//from animation
		atTable.release();// = true;
		stateChanged();
	}

	public void msgAtKitchen(){
		atKitchen.release();
		stateChanged();
	}

	public void msgAtLobby(){
		atLobby.release();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */

	public boolean pickAndExecuteAction() {
		try {
			if(workState == WorkState.ReadyToLeave){
				Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
				if(rest.getNumCustomers() == 0 && Customers.size() == 0){
					kill();
					return true;
				}
			}
			for (MyCustomer customer : Customers){
				if (customer.state == CustomerState.orderNotAvailable){
					outOfStock(customer);
					return true;
				}
				if (customer.state == CustomerState.orderReady){
					giveFoodToCustomer(customer);
					return true;
				}
				if (customer.state == CustomerState.doneEating){
					getCheck(customer);
					return true;
				}
				if (customer.state == CustomerState.notSeated){
					seatCustomer(customer);
					return true;
				}
				if (customer.state == CustomerState.orderPending){
					giveOrderToCook(customer);
					return true;
				}
				if (customer.state == CustomerState.readyToOrder){
					takeOrder(customer);
					return true;
				}
				if (customer.state == CustomerState.gotCheck){
					hereIsCheck(customer);
					return true;
				}
				if (customer.state == CustomerState.isDone){
					clearTable(customer);
					return true;
				}

			}

			return false;
		} catch (ConcurrentModificationException e){
			return true;
		}
	}


	// Actions

	protected void outOfStock(MyCustomer customer){
		for (String m : menu){
			if (m == customer.choice){
				menu.remove(customer.choice);
				for (MyCustomer c : Customers){
					if (c.table == customer.table){
						AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Food " + c.choice + " is out of stock. Informing customer.");
						c.cust.msgNoMoreChoice(menu);
					}
				}
			} 
		}
	}

	protected void seatCustomer(MyCustomer customer){
		customer.cust.msgFollowMeToTable(this, customer.table, menu);
		DoSeatCustomer(customer.cust, customer.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		waiterGui.DoLeaveCustomer();
	}

	protected void takeOrder(MyCustomer customer){
		DoGoToCustomer(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		customer.cust.msgWhatWouldYouLike();
	}

	protected abstract void giveOrderToCook(MyCustomer customer);

	protected void giveFoodToCustomer(MyCustomer customer){
		DoGoToKitchen();
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cook.msgTakingFood();
		DoGiveFoodToCustomer(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		customer.cust.msgHereIsYourFood();
		waiterGui.DoLeaveCustomer();
	}

	protected void getCheck(MyCustomer customer){
		DoGoToLobby();
		try {
			atLobby.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		cashier.msgINeedCheck(customer.table);
	}

	protected void hereIsCheck(MyCustomer customer){
		DoGoToCustomer(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		customer.cust.msgHereIsCheck(customer.cost);
	}
	protected void clearTable(MyCustomer customer){
		DoClearingTable(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		host.msgLeavingTable(customer.table);
		Customers.remove(customer);
		customer.state = CustomerState.noAction;
		waiterGui.DoLeaveCustomer();
	}

	// The animation DoXYZ() routines
	protected void DoSeatCustomer(CustomerRole customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Seating " + customer + " at table " + table);
		waiterGui.DoBringToTable(customer, table); 

	}
	protected void DoGoToCustomer(MyCustomer customer){
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Going to " + customer.cust.getName() + " at table " + customer.table);
		waiterGui.doGoToTable(customer.table);

	}
	protected void DoGiveFoodToCustomer(MyCustomer customer){
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Bringing food to " + customer.cust.getName());
		waiterGui.doGoToTable(customer.table);
	}
	protected void DoGoToLobby(){
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Getting check");
		waiterGui.doGoToLobby();
	}
	protected void DoGoToKitchen(){
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Grabbing food from kitchen");
		waiterGui.doGoToKitchen();
	}

	protected void DoClearingTable(MyCustomer customer){
		waiterGui.doGoToTable(customer.table);
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Clearing table");
	}


	//utilities
	public String getName(){
		return myPerson.getName();
	}

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}

	public ShiftTime getShift() {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getNameOfRole() {
		return Role.RESTAURANT_BYRON_WAITER_ROLE;
	}

}

