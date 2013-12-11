package ryansRestaurant;

import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import building.BuildingList;
import building.Restaurant;
import ryansRestaurant.interfaces.RyansCustomer;
import ryansRestaurant.interfaces.RyansHost;
import ryansRestaurant.interfaces.RyansWaiter;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;
import Person.Role.ShiftTime;
import Person.Role.Employee.WorkState;

/**
 * Restaurant RyansHost Agent
 */

public class RyansHostRole extends GenericHost implements RyansHost {
	private int NTABLES = 2;//a global for the number of tables. // there's only one host changing from static

	private List<RyansCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<RyansCustomer>());
	//private List<RyansCustomer> waitingCustomers = (new ArrayList<RyansCustomer>());
	private Collection<Table> tables;


	//private List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private List<MyWaiter> waiters = (new ArrayList<MyWaiter>());
	enum WaiterState {onBreak, requestedBreak, leaving, none};

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public RyansHostRole(String workLocation) {
		super(workLocation);

		// make some tables
		//tables = Collections.synchronizedCollection(new ArrayList<Table>(NTABLES));
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collection
		}
		tables.add(new Table(11));//Add a default table 5
	}


	public String getName() {
		return this.myPerson.getName();
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
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Concurrent Modification Exception caught.");
		}
		return nums;
	}


	// Messages

	public void msgIWantFood(RyansCustomer cust) {
		waitingCustomers.add(cust);
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "" + cust + " is in the ryansRestaurant.");
		stateChanged();
	}

	//called if a customer decides to leave before being seated.
	public void msgIWantToLeave(RyansCustomer cust) {
		synchronized (waitingCustomers) {
			for(RyansCustomer c: waitingCustomers) {
				if(c == cust) {
					AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "" + c + " is leaving.");
					waitingCustomers.remove(cust);
					return;
				}
			}
		}	
	}

	public void msgTableFree(RyansWaiter waiter, int tableNumber) {

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
					AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), table.occupiedBy + " leaving " + table);
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
	public void msgWantToGoOnBreak(RyansWaiter waiter) {
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), waiter + " wants to go on break.");
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
	public void msgBreakOver(RyansWaiter waiter) {
		//synchronized (waiters) 
		{
			for(MyWaiter w : waiters) {
				if(w.waiter == waiter) {
					w.state = WaiterState.none;
					AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), waiter + " back from break.");
					stateChanged();
					return;
				}
			}
		}

	}


	/**msgAddWaiter
	 * @param waiter the waiter to add
	 */
	public void addWaiter(GenericWaiter waiter)
	{
		waiters.add(new MyWaiter((RyansWaiter) waiter));
		stateChanged();
	}
	
	/** Removes the waiter from my list.
	 * 
	 * @param waiter
	 */
	public void msgRemoveWaiter(RyansWaiter waiter){
		for(MyWaiter w: waiters){
			if(w.waiter == waiter){
				w.state = WaiterState.leaving;
			}
		}
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
	
	public void msgWakeUp(){
		stateChanged();
		
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		try {
		
			if(workState == WorkState.ReadyToLeave){
				Restaurant rest = (Restaurant) BuildingList.findBuildingWithName(workLocation);
				if(rest.getNumCustomers() == 0){
					kill();
					AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Leaving Work.");
					return true;
				}
			}

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
					}else if(w.state == WaiterState.leaving){
						removeWaiter(w);
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
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "ConcurrentModification exception caught in hose scheduler.");
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	
	/** Removes a waiter from the restaurant
	 * 
	 * @param w
	 */
	private void removeWaiter(MyWaiter w) {
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, name, "Removing waiter from my list.");
		waiters.remove(w);
	}


	private void seatCustomer(RyansCustomer customer, Table table, MyWaiter waiter) {
		{
			customer.msgIntroduceWaiter(waiter.waiter);
			waiter.waiter.msgSitAtTable(customer, table.getTableNumber());
			waiter.numCusts++;
			table.setOccupant(customer);
			if(!waitingCustomers.remove(customer))
				AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "ERROR!!!!!! REMOVING " + customer + " from waitingCustomers.");
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Introduced " + customer + " to " + waiter.waiter);
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
				AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Telling " + waiter.waiter + " to GO ON BREAK.");
			}
			else {
				waiter.waiter.msgFromHostGoOnBreak(false);
				waiter.state = WaiterState.none;
				AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Telling " + waiter.waiter + " they CANNOT go on break.");
			}
		}
	}

	/**This function will find the waiter with the least amount of customers
	 * 
	 * @return RyansWaiter with least amount of customers
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
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Concurrent Modification Exception Caught");
		}
			return wait;
	}

	//utilities

	class MyWaiter {
		RyansWaiter waiter;
		WaiterState state;
		int numCusts = 0;

		MyWaiter(RyansWaiter waiter) {
			this.waiter = waiter;
			this.state = WaiterState.none;
		}

		MyWaiter(RyansWaiter waiter, WaiterState state) {
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
		RyansCustomer occupiedBy;

		Table(int tableNumber) {
			super(tableNumber);
		}

		Table(int tableNumber, int numSeats) {
			super(tableNumber, numSeats);
		}

		void setOccupant(RyansCustomer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		RyansCustomer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}


	}


	@Override
	public Double getSalary() {
		return 42.00;
	}


	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_RYAN_HOST_ROLE;
	}
	
	@Override
	public void deactivate() {
			super.deactivate();	
			workState = WorkState.ReadyToLeave;
	}
	
	
	@Override
	public void kill() {
		
		AlertLog.getInstance().logDebug(AlertTag.RYANS_RESTAURANT, getName(), "Leaving Work.");
		//super.kill();
	}
}

