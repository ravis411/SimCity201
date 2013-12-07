package byronRestaurant;

import agent.Agent;
import byronRestaurant.gui.WaiterGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
public class HostRole extends Agent {
	/*
	 * data
	 */
	private class MyWaiter{
		public WaiterRole waiter;
		public boolean onBreak;
		public boolean atLobby;

		MyWaiter(WaiterRole w){
			waiter = w;
			onBreak = false;
			atLobby = true;
		}
	}
	private class Table {
		CustomerRole occupiedBy;
		int tableNumber;
		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}
		void setOccupant(CustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerRole getOccupant() {
			return occupiedBy;
		}
		boolean isOccupied() {
			return occupiedBy != null;
		}
		public String toString() {
			return "table " + tableNumber;
		}
	}
	private int NTABLES = 3;
	//list of customers
	private List<CustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<CustomerRole>());
	//list of customers with debt
	private List<CustomerRole> redList = Collections.synchronizedList(new ArrayList<CustomerRole>());
	//list of tables
	private List<Table> tables = Collections.synchronizedList(new ArrayList<Table>());
	//List of waiters
	private List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private int nextWaiter = 0;
	private String name;

	// create an instance of HostRole
	public HostRole(String name) { 
		super();

		this.name = name;
		// make some tables
		synchronized(tables){
			for (int ix = 1; ix <= NTABLES; ix++) {
				tables.add(new Table(ix));
				System.out.println("Added table " + ix);
			}
		}
	}

	// Messages

	public void msgIWantFood(CustomerRole cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgLeavingTable(int tableNum) {
		tables.get(tableNum-1).setUnoccupied();
		stateChanged();
	}

	public void addWaiter(WaiterRole waiter){
		waiters.add(new MyWaiter(waiter));
		stateChanged();
	}
	public void msgPayUsBackLater(CustomerRole cust){
		redList.add(cust);
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try{
		for (Table table : tables){
			if (!table.isOccupied()){
				for (MyWaiter waiter : waiters){
					if (waiter.atLobby == true && waiter.onBreak == false){
						if (!waitingCustomers.isEmpty()){
							for (CustomerRole c : redList){
								for (CustomerRole c1 : waitingCustomers){
									if (c.getCustomerName() == c1.getCustomerName()){
										payBackDebt(c1);
									}
								}
							}
							sitCustomerAtTable(waitingCustomers.get(0),table.tableNumber);
							return true;
						}
					}
				}
			}
		}
		return false;
		}catch (ConcurrentModificationException e){
			return true;
		}
	}

	// Actions

	private void sitCustomerAtTable(CustomerRole c, int t){
		print("Telling " + waiters.get(nextWaiter).waiter.getName() + " to seat " + c + " at table " + (t));
		waiters.get(nextWaiter).waiter.msgSitCustomerAtTable(c, t);
		tables.get(t-1).setOccupant(c);
		waitingCustomers.remove(c);
		nextWaiter = (nextWaiter+1)%waiters.size();
	}


	private void payBackDebt(CustomerRole c){
		return;
	}

	//utilities
	public String getName(){
		return name;
	}

}