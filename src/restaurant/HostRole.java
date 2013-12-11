package restaurant;

import interfaces.Customer;
import interfaces.Waiter;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import restaurant.gui.HostGui;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.Role;
import Person.Role.RoleState;
import Person.Role.ShiftTime;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends GenericHost {
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	/*public List<CustomerAgent> waitingCustomers
	= new ArrayList<CustomerAgent>();
	public List<WaiterAgent> waiters
	= new ArrayList<WaiterAgent>();*/
	private Vector<Customer> waitingCustomers = new Vector<Customer>();
	private Vector<Waiter> waiters = new Vector<Waiter>();
	public Collection<Table> tables;
	int waitingCust = 0;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private Semaphore atTable = new Semaphore(0,true);
	private Waiter requestedBreak = null;

	public HostGui hostGui = null;

	public HostRole(String workLocation) {
		super(workLocation);

		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix,ix*65 + 70,200));//how you add to a collections
		}
	}
	
	public int getTableX(int tableNum) {
		return ((ArrayList<Table>) tables).get(tableNum).getX();
	}
	
	public int getTableY(int tableNum) {
		return ((ArrayList<Table>) tables).get(tableNum).getY();
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages

	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(cust);
		waitingCust++;
		stateChanged();
	}

	public void msgLeavingTable(Customer customer) {
		for (Table table : tables) {
			if (table.getOccupant() == customer) {
				AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), customer.getName() + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}
	
	public void msgWaiterGoOnBreak(Waiter w) {
		requestedBreak = w;
		stateChanged();
	}

	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgRemoveFromWaitlist(RestaurantCustomerRole c) {
		for(int i=0; i<waitingCustomers.size(); i++) {
			if(waitingCustomers.get(i) == c) {
				waitingCustomers.remove(i);
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if(roleState == RoleState.Deactivating && waitingCustomers.size() == 0) {
			kill();
			return true;
		}
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

	private void getWaiter(Customer customer, int table, int xCoor, int yCoor) {
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
		customer.setWaiter((GenericWaiter)waiters.get(assignedWaiter));
		customer.setTableX(xCoor);
		customer.setTableY(yCoor);
		customer.setTableNum(table);
		waiters.get(assignedWaiter).msgSeatCustomer(customer);
		((ArrayList<Table>) tables).get(table).setOccupant(customer);
		waitingCustomers.remove(customer);
	}
	
	private void analyzeBreak() {
		if(waiters.size() > 1) {
			AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), requestedBreak.getName() + " can go on break after current customers leave.");
			requestedBreak.msgBreakReply(true);
		}
		else {
			AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), requestedBreak.getName() + " cannot go on break.");
			requestedBreak.msgBreakReply(false);		
		}
		requestedBreak = null;
	}
	
	private void notifyCustomerOfWait(Customer customer) {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), customer.getCustomerName() + ", the restaurant is full. There's a wait to be seated.");
		customer.msgRestaurantFull();
	}
	
	//utilities

	/*public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}*/
	
	public void addWaiter(GenericWaiter w) {
		waiters.add((Waiter) w);
	}

	private class Table {
		Customer occupiedBy;
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

		void setOccupant(Customer customer) {
			occupiedBy = customer;
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

	@Override
	public boolean canGoGetFood() {
		return false;
	}

	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_HOST_ROLE;
	}

	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*public boolean setFrontDesk() {
		atFrontDesk = false;
		if(hostGui.getXPos() < 0) {
			atFrontDesk = true;
		}
		return atFrontDesk;
	}*/

}

