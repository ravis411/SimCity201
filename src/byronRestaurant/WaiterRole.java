package byronRestaurant;

import agent.Agent;
import byronRestaurant.gui.WaiterGui;

import java.util.*;
import java.util.concurrent.Semaphore;
/**
 * Restaurant Waiter Agent
 */
public class WaiterRole extends Agent {
	public enum CustomerState{noAction,notSeated, readyToOrder, orderPending, orderReady, doneEating, gotCheck, isDone, orderNotAvailable};
	Timer timer;
	public List<String>menu = new ArrayList<String>();{
		menu.add("Steak");
		menu.add("Chicken");
		menu.add("Salad");
		menu.add("Pizza");
	}
	private class MyCustomer{
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
	public List<MyCustomer> Customers = new ArrayList<MyCustomer>();
	private HostRole host;
	private CookRole cook;
	private CashierRole cashier;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atKitchen = new Semaphore(0, true);
	private Semaphore atLobby = new Semaphore(0,true);
	public WaiterGui waiterGui = null;

	public WaiterRole(String name) {
		super();

		this.name = name;
	}

	public void setCook(CookRole c){
		this.cook = c;
	}

	public void setHost(HostRole h){
		this.host = h;
	}

	public void setCashier(CashierRole c){
		this.cashier = c;
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
		Do("Recieved order from cook that " + o + " is restocked");
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
	protected boolean pickAndExecuteAnAction() {
		try {
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

	private void outOfStock(MyCustomer customer){
		for (String m : menu){
			if (m == customer.choice){
				menu.remove(customer.choice);
				for (MyCustomer c : Customers){
					if (c.table == customer.table){
						print("Food " + c.choice + " is out of stock. Informing customer.");
						c.cust.msgNoMoreChoice(menu);
					}
				}
			} 
		}
	}
	
	private void seatCustomer(MyCustomer customer){
		customer.cust.msgFollowMeToTable(this, customer.table, menu);
		DoSeatCustomer(customer.cust, customer.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		waiterGui.DoLeaveCustomer();
	}

	private void takeOrder(MyCustomer customer){
		DoGoToCustomer(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		customer.cust.msgWhatWouldYouLike();
	}

	private void giveOrderToCook(MyCustomer customer){
		Do("Giving " + customer.cust + "'s choice of " + customer.choice + " to cook");
		customer.state = CustomerState.noAction;
		cook.msgHereIsAnOrder(this, customer.table, customer.choice);
		cashier.msgHereIsOrder(customer.table, customer.choice, this);
		waiterGui.DoLeaveCustomer();
	}

	private void giveFoodToCustomer(MyCustomer customer){
		DoGoToKitchen();
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.msgTakingFood();
		DoGiveFoodToCustomer(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		customer.cust.msgHereIsYourFood();
		waiterGui.DoLeaveCustomer();
	}

	private void getCheck(MyCustomer customer){
		DoGoToLobby();
		try {
			atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		cashier.msgINeedCheck(customer.table);
	}

	private void hereIsCheck(MyCustomer customer){
		DoGoToCustomer(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.state = CustomerState.noAction;
		customer.cust.msgHereIsCheck(customer.cost);
	}
	private void clearTable(MyCustomer customer){
		DoClearingTable(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		host.msgLeavingTable(customer.table);
		Customers.remove(customer);
		customer.state = CustomerState.noAction;
		waiterGui.DoLeaveCustomer();
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(CustomerRole customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at table " + table);
		waiterGui.DoBringToTable(customer, table); 

	}
	private void DoGoToCustomer(MyCustomer customer){
		print("Going to " + customer.cust.getName() + " at table " + customer.table);
		waiterGui.doGoToTable(customer.table);

	}
	private void DoGiveFoodToCustomer(MyCustomer customer){
		print("Bringing food to " + customer.cust.getName());
		waiterGui.doGoToTable(customer.table);
	}
	private void DoGoToLobby(){
		print("Getting check");
		waiterGui.doGoToLobby();
	}
	private void DoGoToKitchen(){
		print("Grabbing food from kitchen");
		waiterGui.doGoToKitchen();
	}

	private void DoClearingTable(MyCustomer customer){
		waiterGui.doGoToTable(customer.table);
		print("Clearing table");
	}


	//utilities
	public String getName(){
		return name;
	}

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}

}

