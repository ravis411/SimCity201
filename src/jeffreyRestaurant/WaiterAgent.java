package jeffreyRestaurant;

import Person.Role.Role;
import Person.Role.ShiftTime;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import jeffreyRestaurant.Gui.HostGui;
import jeffreyRestaurant.interfaces.Customer;
import jeffreyRestaurant.interfaces.Waiter;

/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public abstract class WaiterAgent extends GenericWaiter implements Waiter{
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<myCustomer> Customers
	= new ArrayList<myCustomer>();
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
    
	protected enum CustomerState{waiting, seated, readyToOrder, AskedToOrder, OutOfOrder, Ordered, OrderReady, Eating, WaitingForCheck, Paying, GivenCheck, Leaving, Done};
	
	protected class myCustomer {
		myCustomer(CustomerAgent myCust, int tab, CustomerState state) {
			c = myCust; table = tab; s = state;
		}
		CustomerAgent c;
		int table;
		Double tab = 0.0;
		String ch;
		CustomerState s;
	}
	
	public class menu {
		menu() {
			foods = new HashMap<String, Double>();
			foods.put("Steak", 15.99);
			foods.put("Chicken", 10.99);
			foods.put("Burger", 5.99);
		}
		HashMap<String, Double> foods;
		public void removeFood(String food) {
			foods.remove(food);
		}
		public String randomFood() {
			ArrayList<String> foodSet = new ArrayList<String>(foods.keySet());
			Random rand = new Random();
			return foodSet.get(rand.nextInt(foodSet.size()));
		}
		
	}
	
	protected String name; 
	protected Semaphore atDestination = new Semaphore(0,true);
	protected boolean wantsBreak;
	protected boolean onBreak;
	
	protected HostAgent h;
	protected CookAgent ck;
	protected CashierAgent ch;
	public HostGui hostGui = null;

	protected WaiterAgent(String location) {
		super(location);

		wantsBreak = false;
		onBreak = false;
	}

	public String getMaitreDName() {
		return h.getName();
	}
//	public void setHost(HostAgent host) {
//		h = host;
//	}
//	public void setCook(CookAgent cook) {
//		ck = cook;
//	}
//	public void setCashier(CashierAgent cashier) {
//		ch = cashier;
//	}
	public String getName() {
		return myPerson.getName();
	}

	public List getCustomers() {
		return Customers;
	}
	
	public boolean isOnBreak() {
		return onBreak;
	}
	public void setOnBreak() {
		//print("I am going on break" + state);
		onBreak = true;
	}
	public void setOffBreak() {
		print("Back from break");
		onBreak = false;
	}
	public boolean doesWantBreak() {
		return wantsBreak;
	}
	public void setWantsBreak(Boolean state) {
		print("I do want break" + state);
		wantsBreak = state;
	}
	// Messages

	public void msgSeatCustomer(CustomerAgent cust, int t) {
		print ("Told to seat " + cust);
		Customers.add(new myCustomer(cust, t, CustomerState.waiting));
		stateChanged();
	}

	public void msgReadyToOrder(CustomerAgent cust) {
		//print("msgReadyToOrder received");
		for (myCustomer mc : Customers) {
			if (mc.c == cust) {
				print(cust.getName()  + "is ready to order");
				mc.s = CustomerState.readyToOrder;
				stateChanged();
			}
		}
	}
	
	public void msgHereIsChoice(String choice, CustomerAgent cust) {
		//print("msgHereIsChoice called");
		for (myCustomer mc : Customers) {
			if (mc.c == cust) {
				print("Received " + cust.getName() + "'s order of " + choice);
				mc.ch = choice;
				mc.s = CustomerState.AskedToOrder;
				stateChanged();
				//waitForOrder.release();
			}
		}
	}
	
	public void msgLeavingTable(CustomerAgent cust) {
		//print("msgLeavingTable called");
		for (myCustomer mc : Customers) {
			if (mc.c == cust) {
				mc.s = CustomerState.Leaving;
			}
		}
		stateChanged();
	}
	
	public void msgGiveFood(String Food, int myTable) {
		print ("Received " + Food + " for " + myTable + " from " + ck.getName());
		for (myCustomer mc : Customers) {
			if (mc.ch == Food) {
				if (mc.table == myTable) {
					mc.s = CustomerState.OrderReady;
					stateChanged();
				}
			}
		}
	}

	public void msgAtDestination() {//from animation
		//print("msgAtTable called");
		atDestination.release();// = true;
		stateChanged();
	}
	
	public void msgAtDesk() {
		//obsolete
		//print("msgAtDesk Called");
		atDestination.release();
		//stateChanged();
	}
	
	public void msgDone(CustomerAgent c) {
		for (myCustomer mc : Customers) {
			if (mc.c == c) {
				print(c.getName() + " is done");
				mc.s = CustomerState.Done;
				stateChanged();
			}
		}
	}
	
	public void msgOutOfFood(String food, int table) {
		for (myCustomer mc : Customers) {
			if (mc.ch == food && mc.table == table) {
				print("Told that we are out of " + food + " for " + mc.c.getName());
				mc.s = CustomerState.OutOfOrder;
				//atDestination.release();
				stateChanged();
			}
		}
	}
	
	public void msgHereIsCheck(Double tab, Customer c) {
		for (myCustomer mc : Customers) {
			if (mc.c == c) {
				print("Check received from Cashier");
				mc.tab = tab;
				mc.s = CustomerState.Paying;
				stateChanged();
			}
		}
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if (wantsBreak) {
			askForBreak();
			return true;
		}
		try {
			for (myCustomer mc : Customers) {
				//print("Dealing with " + mc.c.getName());
				if (mc.s == CustomerState.waiting) {
					seatCustomer(mc);
					return true;
				}
			}
			for (myCustomer mc : Customers) {
				//print("Dealing with " + mc.c.getName());
				if (mc.s == CustomerState.readyToOrder) {
					TakeOrder(mc);
					return true;
				}
			}
			for (myCustomer mc : Customers) {
				if (mc.s == CustomerState.AskedToOrder) {
					tellCookOrder(mc);
					return true;
				}
			}
			//print("00");
			for (myCustomer mc: Customers) {
				if (mc.s == CustomerState.OutOfOrder) {
					//print("Scheduler debug");
					pleaseOrderAgain(mc);
					return true;
				}
			}
			//print("01");
			for (myCustomer mc : Customers) {
				//print("Dealing with " + mc.c.getName());
				if (mc.s == CustomerState.OrderReady) {
					DeliverFood(mc);
					return true;
				}
			}
			for (myCustomer mc : Customers) {
				if (mc.s == CustomerState.Done){
					customerReadyToPay(mc);
					//customerLeaving(mc);
					return true;
				}
			}
			for (myCustomer mc : Customers) {
				if (mc.s == CustomerState.Paying) {
					giveCustomerCheck(mc);
					return true;
				}
			}
			
			for (myCustomer mc : Customers) {
				if (mc.s == CustomerState.Leaving) {
					customerLeaving(mc);
					return true;
				}
			}
		}
		catch (ConcurrentModificationException e) {
			return false;
		}
		
		hostGui.DoLeaveCustomer();
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	protected void seatCustomer(myCustomer myCustomer) {
		print("Seating" + myCustomer.c.getName());
		myCustomer.c.msgSitAtTable(myCustomer.table,new WaiterAgent.menu());
		DoSeatCustomer(myCustomer.c, myCustomer.table);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myCustomer.s = CustomerState.seated;
	}

	protected void TakeOrder(myCustomer myCustomer) {
		print("Taking the order of " + myCustomer.c.getName());
		//myCustomer.s = CustomerState.AskedToOrder;
		DoFindCustomer(myCustomer);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myCustomer.c.msgWhatIsOrder();
	}
	
	protected abstract void tellCookOrder(myCustomer mc);
	
	protected void pleaseOrderAgain(myCustomer mc) {
		DoFindCustomer(mc);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print ("We are out of " + mc.c.getName() + "'s order. Please choose something else");
		menu newMenu = new menu();
		newMenu.removeFood(mc.ch);
		mc.c.msgOrderAgain(newMenu);
		//mc.s = CustomerState.AskedToOrder;
		mc.s = CustomerState.readyToOrder;
		
		hostGui.DoLeaveCustomer();
	}
	
	protected void DeliverFood(myCustomer myCustomer) {
		DoGoToCook();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print ("Picking up food for " + myCustomer.c.getName());
		myCustomer.c.msgHereIsFood();
		
		DoFindCustomer(myCustomer);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Delivering food to " + myCustomer.c.getName());
		myCustomer.c.msgHereIsFood();
		myCustomer.s = CustomerState.Eating;
		hostGui.DoLeaveCustomer();
	}
	
	protected void customerReadyToPay(myCustomer mc) {
		print(mc.c.getName() + " is ready to pay");
		mc.s = CustomerState.WaitingForCheck;
		ch.msgCustomerReadyToPay(this, mc.ch, mc.c);
	}
	
	protected void giveCustomerCheck(myCustomer mc) {
		mc.c.msgHereIsCheck(mc.tab);
		mc.s = CustomerState.GivenCheck;
	}
	
	protected void customerLeaving(myCustomer mc) {
		print("Telling host that " + mc.c.getName() + " is leaving table.");
		h.msgDone(mc.c);
		h.msgLeavingTable(mc.c);
		Customers.remove(mc);
	}
	
	protected void askForBreak() {
		h.msgWantToGoOnBreak(this);
		wantsBreak = false;
	}
	
	// The animation DoXYZ() routines
	protected void DoSeatCustomer(CustomerAgent customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		hostGui.DoBringToTable(customer, table); 
	}

	protected void DoFindCustomer(myCustomer cust) {
		print ("Going to " + cust.c.getName());
		hostGui.DoGoToTable(cust.table);
	}
	
	protected void DoGoToCook() {
		print ("Going to " + ck.getName());
		hostGui.DoGoToCook();
	}
	
	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	public Integer howManyCustomers() {
		return Customers.size();
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCook(GenericCook c) {
		this.ck = (CookAgent) c;
	}

	@Override
	public void setCashier(GenericCashier c) {
		this.ch = (CashierAgent) c;
	}

	@Override
	public void setHost(GenericHost h) {
		this.h = (HostAgent) h;
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

