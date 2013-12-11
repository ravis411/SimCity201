package jeffreyRestaurant;

import Person.Role.Role;
import Person.Role.ShiftTime;
import Person.Role.Employee.WorkState;
import agent.Agent;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import jeffreyRestaurant.Gui.HostGui;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends GenericHost {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<myCustomer> Customers = Collections.synchronizedList(new ArrayList<myCustomer>());
	public Collection<Table> tables;
	public List<myWaiter> waiters = Collections.synchronizedList(new ArrayList<myWaiter>());
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	private class myWaiter {
		myWaiter(WaiterAgent waiter, WaiterState state) {
			w = waiter; s = state;
		}
		WaiterAgent w;
		WaiterState s; 
	}
	
	private class myCustomer {
		myCustomer(CustomerAgent cust, CustomerState state) {
			c = cust; s = state;
		}
		CustomerAgent c;
		CustomerState s;
	}
	
	private enum CustomerState{waiting,inLine,seated,done};
	private enum WaiterState{working,wantsToBreak,onBreak};
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private boolean atDesk;

	public HostGui hostGui = null;

	public HostAgent(String location) {
		super(location);

		// make some tables
		atDesk = true;
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public String getName() {
		return myPerson.getName();
	}

	public List getCustomers() {
		return Customers;
	}

	public Collection getTables() {
		return tables;
	}
	
	public void setAtDesk(Boolean state) {
		atDesk = state;
	}
//	public void addWaiter(WaiterAgent w) {
//		waiters.add(new myWaiter(w, WaiterState.working));
//		stateChanged();
//	}TODO
	// Messages

	public void msgIWantFood(CustomerAgent cust) {
		Customers.add(new myCustomer(cust, CustomerState.waiting));
		stateChanged();
	}

	public void msgLeavingTable(CustomerAgent cust) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}

	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgAtDesk(Boolean place) {
		atDesk = place;
		stateChanged();
	}
	
	public void msgDone(CustomerAgent c) {
		for (myCustomer mc : Customers) {
			if (mc.c == c) {
				mc.s = CustomerState.done;
			}
		}
	}
	
	public void msgWantToGoOnBreak(WaiterAgent waiter) {
		print(waiter.getName() + " wants to go on break");
		for (myWaiter w: waiters) {
			if (w.w == waiter) {
				w.s = WaiterState.wantsToBreak;
				stateChanged();
			}
		}
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		/* Think of this next rule as:
            Does there exist a table, customer, and waiter
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
            */
		if(workState == WorkState.ReadyToLeave && Customers.size() == 0){
			//kill();
			//return true;
		}
		if (waiters != null) {
			if (tables != null) {
				synchronized(Customers) {
					for (myCustomer mc : Customers) {
						for (Table t : tables) {
							if (mc.s == CustomerState.waiting) {
								WaiterAgent currentWaiter = null;
								int lowestCount = 1000;
								for (myWaiter w : waiters) {
									//Find the waiter with the fewest customers
									//Do not consider waiters on break
									if (!w.w.isOnBreak()) {
										if (w.w.howManyCustomers() < lowestCount) {
											lowestCount = w.w.howManyCustomers();
											currentWaiter = w.w;
										}
									}
								}
								if (!t.isOccupied()) {
									if (currentWaiter != null) {
										if (!currentWaiter.isOnBreak()) {
											seatCustomer(currentWaiter, mc.c, t);
											mc.s = CustomerState.seated;
											return true;
										}
									}
								}
							}
						}
						weAreFull(mc);
					}
				}
			}
		}
		synchronized(waiters) {
			for (myWaiter w : waiters) {
				if (w.s == WaiterState.wantsToBreak) {
					sendOnBreak(w);
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(WaiterAgent w, CustomerAgent customer, Table table) {
		print ("Seating " + customer);
		customer.setWaiter(w);
		w.msgSeatCustomer(customer, table.getNumber());
		table.setOccupant(customer);
		//update tables and customer 
	}

	private void sendOnBreak(myWaiter w) {
		//If this is the only waiter, they cannot go on break
		if (waiters.size() == 1) {
			print(w.w.getName() + " cannot go on break because they are the only worker");
			return;
		}
		else {
			w.s = WaiterState.onBreak;
			w.w.setOnBreak();
			//waiters.remove(w);
		}
	}
	
	private void weAreFull(myCustomer mc) {
		mc.c.msgWeAreFull();
	}
	// The animation DoXYZ() routines
	private void DoSeatCustomer(CustomerAgent customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		hostGui.DoBringToTable(customer, table.getNumber()); 

	}

	//utilities
	
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	private class Table {
		CustomerAgent occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerAgent cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerAgent getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
		public int getNumber() {
			return tableNumber;
		}
	}

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_JEFFREY_HOST_ROLE;
	}

	@Override
	public void addWaiter(GenericWaiter waiter) {
		waiters.add(new myWaiter((WaiterAgent) waiter, WaiterState.working));
		stateChanged();
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

