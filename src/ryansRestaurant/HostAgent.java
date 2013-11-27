package ryansRestaurant;

import agent.Agent;

import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

import ryansRestaurant.interfaces.Customer;
import ryansRestaurant.interfaces.Host;
import ryansRestaurant.interfaces.Waiter;

/**
 * Restaurant Host Agent
 */

public class HostAgent extends Agent implements Host {
	private int NTABLES = 2;//a global for the number of tables. // there's only one host changing from static

	private List<Customer> waitingCustomers = Collections.synchronizedList(new ArrayList<Customer>());
	//private List<Customer> waitingCustomers = (new ArrayList<Customer>());
	private Collection<Table> tables;


	//private List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private List<MyWaiter> waiters = (new ArrayList<MyWaiter>());
	enum WaiterState {onBreak, requestedBreak, none};

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public HostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		//tables = Collections.synchronizedCollection(new ArrayList<Table>(NTABLES));
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collection
		}
		tables.add(new Table(11));//Add a default table 5
	}


	public String getName() {
		return name;
	}

//	private List getWaitingCustomers() {
//		return waitingCustomers;
//	}

//	public Collection getTables() {
//		return tables;
//	}
	/**returns a list of all the aTables numbers so the gui can paint them
	 * 
	 * @return ArrayList of aTables
	 */
	public List<aTable> getTableNumbers() {
		List<aTable> nums = new ArrayList<aTable>();

		try {
			for(Table t : tables) {
				nums.add(new aTable(t.tableNumber, t.numSeats));
			}
		}
		catch( ConcurrentModificationException e)
		{
			print("Concurrent Modification Exception caught.");
		}
		return nums;
	}


	// Messages

	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(cust);
		print("" + cust + " is in the ryansRestaurant.");
		stateChanged();
	}

	//called if a customer decides to leave before being seated.
	public void msgIWantToLeave(Customer cust) {
		synchronized (waitingCustomers) {
			for(Customer c: waitingCustomers) {
				if(c == cust) {
					print("" + c + " is leaving.");
					waitingCustomers.remove(cust);
					return;
				}
			}
		}	
	}

	public void msgTableFree(Waiter waiter, int tableNumber) {

		//synchronized (waiters)
		{
			//Find waiter to decrement numCusts
			for(MyWaiter w : waiters) {
				if(w.waiter == waiter) {
					w.numCusts--;
					break;
				}
			}
		}
		//synchronized (tables)
		{
			for (Table table : tables) {
				if (table.getTableNumber() == tableNumber) {
					print(table.occupiedBy + " leaving " + table);
					table.setUnoccupied();
					stateChanged();
					break;
				}
			}
		}
	}

	/** A message for the waiter to go on break
	 * 
	 * @param waiter The waiter
	 */
	public void msgWantToGoOnBreak(Waiter waiter) {
		print(waiter + " wants to go on break.");
		//synchronized (waiters)
		{
			for(MyWaiter w : waiters) {
				if(w.waiter == waiter) {
					w.state = WaiterState.requestedBreak;
					stateChanged();
					return;
				}
			}
		}
	}

	/** A waiter calls this function when they are done with their break
	 * 
	 * @param waiter The waiter
	 */
	public void msgBreakOver(Waiter waiter) {
		//synchronized (waiters) 
		{
			for(MyWaiter w : waiters) {
				if(w.waiter == waiter) {
					w.state = WaiterState.none;
					print(waiter + " back from break.");
					stateChanged();
					return;
				}
			}
		}

	}


	/**msgAddWaiter
	 * @param waiter the waiter to add
	 */
	public void msgAddWaiter(Waiter waiter)
	{
		waiters.add(new MyWaiter(waiter));
		stateChanged();
	}


	/**msgAddTable sent from GUI when a table is added.
	 * 
	 * @param tableNumber must be a tableNumber that does not currently exist or the function will terminate
	 * @param numSeats currently a value 1-4 for the number of seats
	 */
	public void msgAddTable(int tableNumber, int numSeats)
	{
		boolean exists = false;

		//First see if the tableNumber already exists
		//synchronized (tables)
		{
			for(Table temp : tables)
			{
				if(temp.getTableNumber() == tableNumber)
				{
					exists = true;
					break;
				}
			}
		}

		//if it doesn't add the table
		if(!exists)
		{
			NTABLES++;
			tables.add(new Table(tableNumber, numSeats));
		}
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		//synchronized (waiters)
			{
			if(!waiters.isEmpty()) {
				for(MyWaiter w : waiters) {
					if(w.state == WaiterState.requestedBreak) {
						determineAndNotifyWaiterAboutTheirBreak(w);
						return true;
					}
				} }
		}

		//synchronized (tables)
			{
			//synchronized (waiters)
				{

				for (Table table : tables) {
					if (!table.isOccupied()) {

						//synchronized (waitingCustomers)
						{

							if (!waitingCustomers.isEmpty()) {
								//Now pick a waiter
								if(!waiters.isEmpty()) {
									seatCustomer(waitingCustomers.get(0), table, pickAWaiter());//the action
									return true;//return true to the abstract agent to reinvoke the scheduler.
								}
							}
						}
					}
				}
			}
		}
		}catch(ConcurrentModificationException e) {
			print("ConcurrentModification exception caught in hose scheduler.");
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(Customer customer, Table table, MyWaiter waiter) {
		{
			customer.msgIntroduceWaiter(waiter.waiter);
			waiter.waiter.msgSitAtTable(customer, table.getTableNumber());
			waiter.numCusts++;
			table.setOccupant(customer);
			if(!waitingCustomers.remove(customer))
				print("ERROR!!!!!! REMOVING " + customer + " from waitingCustomers.");
			print("Introduced " + customer + " to " + waiter.waiter);
		}	
	}

	private void determineAndNotifyWaiterAboutTheirBreak(MyWaiter waiter) {
		boolean availableWaiter = false;
		//synchronized (waiters) 
		{
			for(MyWaiter w : waiters) {
				if(w.state == WaiterState.none) {
					availableWaiter = true;
					break;
				}
			}
			if(availableWaiter) {
				waiter.state = WaiterState.onBreak;
				waiter.waiter.msgFromHostGoOnBreak(true);
				print("Telling " + waiter.waiter + " to GO ON BREAK.");
			}
			else {
				waiter.waiter.msgFromHostGoOnBreak(false);
				waiter.state = WaiterState.none;
				print("Telling " + waiter.waiter + " they CANNOT go on break.");
			}
		}
	}

	/**This function will find the waiter with the least amount of customers
	 * 
	 * @return Waiter with least amount of customers
	 */
	private MyWaiter pickAWaiter() {
		int numCusts = 0;
		MyWaiter wait = null;

		try{
			if(!waiters.isEmpty()) {

				//find first waiter that isn't on break
				for(MyWaiter w : waiters) {
					if(w.state == WaiterState.none) {
						numCusts = w.numCusts;
						wait = w;
						break;
					}
				}
				//Now find the waiter with least number of customers that isn't on break
				for(MyWaiter w : waiters) {
					if(w.state == WaiterState.none && w.numCusts < numCusts) {
						numCusts = w.numCusts;
						wait = w;
					}
				}
			}
			
		}catch(ConcurrentModificationException e) {
			print("Concurrent Modification Exception Caught");
		}
			return wait;
	}

	//utilities

	class MyWaiter {
		Waiter waiter;
		WaiterState state;
		int numCusts = 0;

		MyWaiter(Waiter waiter) {
			this.waiter = waiter;
			this.state = WaiterState.none;
		}

		MyWaiter(Waiter waiter, WaiterState state) {
			this.waiter = waiter;
			this.state = state;
		}
	}

	public class aTable {
		int tableNumber = 0;
		int numSeats = 4;

		public aTable(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		aTable(int tableNumber, int numSeats) {
			this.tableNumber = tableNumber;
			this.numSeats = numSeats;
		}

		public String toString() {
			return "table " + tableNumber;
		}

		public int getTableNumber() {
			return tableNumber;
		}

		public int getNumSeats() {
			return numSeats;
		}
	}

	class Table extends aTable {
		Customer occupiedBy;

		Table(int tableNumber) {
			super(tableNumber);
		}

		Table(int tableNumber, int numSeats) {
			super(tableNumber, numSeats);
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}


	}
}

