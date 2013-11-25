package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;
import restaurant.interfaces.Customer;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent {
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	/*public List<CustomerAgent> waitingCustomers
	= new ArrayList<CustomerAgent>();
	public List<WaiterAgent> waiters
	= new ArrayList<WaiterAgent>();*/
	private Vector<CustomerAgent> waitingCustomers = new Vector<CustomerAgent>();
	private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
	public Collection<Table> tables;
	int waitingCust = 0;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private WaiterAgent requestedBreak = null;

	public HostGui hostGui = null;

	public HostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix,ix*60 + 70,ix*60));//how you add to a collections
		}
	}
	
	public int getTableX(int tableNum) {
		return ((ArrayList<Table>) tables).get(tableNum).getX();
	}
	
	public int getTableY(int tableNum) {
		return ((ArrayList<Table>) tables).get(tableNum).getY();
	}


	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages

	public void msgIWantFood(CustomerAgent cust) {
		waitingCustomers.add(cust);
		waitingCust++;
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
	
	public void msgWaiterGoOnBreak(WaiterAgent w) {
		requestedBreak = w;
		stateChanged();
	}

	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgRemoveFromWaitlist(CustomerAgent c) {
		for(int i=0; i<waitingCustomers.size(); i++) {
			if(waitingCustomers.get(i) == c) {
				waitingCustomers.remove(i);
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if (!waitingCustomers.isEmpty()){
			for(int w=0; w<waitingCustomers.size(); w++){
				if(waitingCust>3) {
					waitingCust = 1;
				}
				if(waitingCustomers.get(w).getWaitingLocX() == -1) {
					waitingCustomers.get(w).setWaitingLocX((waitingCust)*40+40);
					waitingCustomers.get(w).setWaitingLocY((waitingCust)*25+15);
				}
			}
		}
		if(requestedBreak != null) {
			analyzeBreak();
		}	
		int i = 0;
		for (Table table : tables) {
			i = i+1;
				if (!table.isOccupied() && i<4) {
					if (!waitingCustomers.isEmpty()) {
						getWaiter(waitingCustomers.get(0), i-1, ((ArrayList<Table>) tables).get(i).getX(),((ArrayList<Table>) tables).get(i).getY());//the action
						//waitingCustomers.remove(0);
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
				else if (i >= 4 && !waitingCustomers.isEmpty()){
					notifyCustomerOfWait(waitingCustomers.lastElement());
				}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void getWaiter(CustomerAgent customer, int table, int xCoor, int yCoor) {
		int assignedWaiter = 0;
		for(int i=0; i<waiters.size(); i++) {
			if(waiters.get(i).getBreakStatus() == false) {
				assignedWaiter = i;
				break;
			}
		}
		for(int i=0; i<waiters.size(); i++) {
			if(waiters.get(i).getNumberOfCustomers() < waiters.get(assignedWaiter).getNumberOfCustomers() && waiters.get(i).getBreakStatus() == false) {
				assignedWaiter = i;
			}
		}
		customer.setWaiter(waiters.get(assignedWaiter));
		customer.setTableX(xCoor);
		customer.setTableY(yCoor);
		customer.setTableNum(table);
		waiters.get(assignedWaiter).msgSeatCustomer(customer);
		((ArrayList<Table>) tables).get(table).setOccupant(customer);
		waitingCustomers.remove(customer);
	}
	
	private void analyzeBreak() {
		if(waiters.size() > 1) {
			print(requestedBreak.getName() + " can go on break after current customers leave.");
			requestedBreak.msgBreakReply(true);
		}
		else {
			print(requestedBreak.getName() + " cannot go on break.");
			requestedBreak.msgBreakReply(false);		
		}
		requestedBreak = null;
	}
	
	private void notifyCustomerOfWait(CustomerAgent cust) {
		print(cust.getCustomerName() + ", the restaurant is full. There's a wait to be seated.");
		cust.msgRestaurantFull();
	}
	
	//utilities

	/*public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}*/
	
	public void addWaiter(WaiterAgent w) {
		waiters.add(w);
	}

	private class Table {
		CustomerAgent occupiedBy;
		int tableNumber;
		int xCoor;
		int yCoor;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}
		
		Table(int tableNumber, int xCoor, int yCoor) {
			this.tableNumber = tableNumber;
			this.xCoor = xCoor;
			this.yCoor = yCoor;
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
		
		public int getX() {
			return xCoor;
		}
		
		public int getY() {
			return yCoor;
		}
	}
	
	/*public boolean setFrontDesk() {
		atFrontDesk = false;
		if(hostGui.getXPos() < 0) {
			atFrontDesk = true;
		}
		return atFrontDesk;
	}*/

}

