package restaurant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import restaurant.gui.HostGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;
import agent.Agent;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent implements Host{
	private static int NTABLES = 4; //num tables

	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	
	private class MyWaiter {
		WaiterAgent waiter;
		WaiterState state;
		
		public MyWaiter(WaiterAgent waiter){
			this.waiter = waiter;
			state = WaiterState.Working;
		}
	}
	
	public enum WaiterState {Working, RequestedBreak, OnBreak}
	//list of available waiters
	private List<MyWaiter> waiters;
	//list of waiting customers
	private List<WaitingCustomer> waitingCustomers;
	//list of all tables
	private List<Table> tables;

	public HostGui hostGui = null;

	/**
	 * Constructor for HostAgent
	 * @param name name of host
	 */
	public HostAgent(String name) {
		super();

		this.name = name;

		tables = new ArrayList<Table>();
		waiters = new ArrayList<MyWaiter>();
		waitingCustomers = new ArrayList<WaitingCustomer>();
		
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}
	
	/**
	 * Accessor for name of Host
	 * @return name of host
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Accessor functino for the list of waiting customers
	 * @return the list of waiting customers
	 */
	public List<CustomerAgent> getWaitingCustomers() {
		ArrayList<CustomerAgent> agents = new ArrayList<CustomerAgent>();
		for(WaitingCustomer cust : waitingCustomers){
			agents.add(cust.cust);
		}
		return agents;
	}

	/**
	 * Accessor function for the collection of tables
	 * @return the collection of tables
	 */
	public Collection getTables() {
		return tables;
	}
	
	//------------------MESSAGES--------------------//
	
	/**
	 * Message sent to the host by the gui panel to add a new table
	 */
	public void msgAddTable(){
		if(NTABLES != 8){
			tables.add(new Table(tables.size()+1));
			NTABLES++;
		}else{
			print("Restaurant FULL - No more tables allowed");
		}
		
	}
	
	/**
	 * Message sent to the host from the creation panel to add a waiter
	 * @param waiter the waiter to add
	 */
	public void msgAddWaiter(Waiter waiter){
		waiters.add(new MyWaiter((WaiterAgent)waiter));
		stateChanged();
	}
	
	/**
	 * Message sent to the host by the gui panel when a customer is initially hungry
	 * @param cust the customer who is hungry
	 */
	public void msgIWantToEat(Customer cust) {
		waitingCustomers.add(new WaitingCustomer((CustomerAgent)cust));
		stateChanged();
	}

	/**
	 * Message sent to the host by the waiter agent when a table is free
	 * @param table the table now free
	 */
	public void msgTableIsFree(Table table){
		for(Table t : tables){
			if(t == table){
				t.setUnoccupied();
				stateChanged();
			}
		}
	}
	
	/**
	 * Mesage sent by the Waiter when he wants to go on break
	 * @param wtr
	 */
	public void msgWantToGoOnBreak(Waiter wtr){
		for(MyWaiter waiter : waiters){
			if((WaiterAgent)wtr == waiter.waiter){
				waiter.state = WaiterState.RequestedBreak;
				stateChanged();
				return;
			}
		}
	}
	
	/**
	 * Message sent by the Waiter when he comes off break
	 * @param wtr
	 */
	public void msgOffBreak(Waiter wtr){
		for(MyWaiter waiter : waiters){
			if((WaiterAgent)wtr == waiter.waiter){
				if(waiters.size() != 0){
					print(wtr.getName() + " is now off break");
					waiter.state = WaiterState.Working;
					stateChanged();
					return;
				}
			}
		}
	}
	
	/**
	 * Message sent by a customer when he is waiting in line
	 * at a full restaurant and would prefer to leave
	 * @param sender the Customer who is leaving
	 */
	public void msgIWontWait(Customer sender){
		for(WaitingCustomer cust : waitingCustomers){
			if(cust.cust == (CustomerAgent)sender){
				waitingCustomers.remove(cust);
				return;
			}
		}
	}
	
	//---------------SCHEDULER------------------//
	
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		try {
			for(MyWaiter wtr : waiters){
				if(wtr.state == WaiterState.RequestedBreak){
					replyToWaiterBreakRequest(wtr);
					return true;
				}
			}
			
			for(WaitingCustomer customer : waitingCustomers){
				for(Table table : tables){
					//if there is an open table and a waiter to sit the customer, then do so
					if(!table.isOccupied() && !waiters.isEmpty()){
						//waiter assignment
						//WaiterAgent assignedWaiter = waiters.get(0).waiter;
						//assign the least busy waiter to the customer
						int min = Integer.MAX_VALUE;
						WaiterAgent assignedWaiter = null;
						for(int i = 0; i < waiters.size(); i++){
							MyWaiter waiter = waiters.get(i);
							//since the number of customers is always less than the initial min, 
							//assignedWaiter is never null after the loop
							if(waiter.state != WaiterState.OnBreak && waiter.waiter.getNumCustomers() < min){
								assignedWaiter = waiter.waiter;
								min = waiter.waiter.getNumCustomers();
							}
						}
						assignCustomerToWaiter(customer, assignedWaiter, table);
						return true;
					}
				}
			}
			
			//if this rule is reached, a customer is waiting to sit b/c the dining room is full
			if(!waitingCustomers.isEmpty()){
				for(WaitingCustomer cust : waitingCustomers){
					if(!cust.chosen){
						notifyCustomer(cust);
						return true;
					}
					
				}
			}
			
			return false;
			//we have tried all our rules and found
			//nothing to do. So return false to main loop of abstract agent
			//and wait.
			
		} catch (ConcurrentModificationException e) {
			// TODO Auto-generated catch block
			return false;
		}

		
	}

	//--------------------ACTIONS---------------------//
	
	/**
	 * Private method called by the scheduler to respond
	 * to a waiter if he/she has asked to go on break
	 * @param wtr the WaiterAgent to respond to
	 */
	private void replyToWaiterBreakRequest(MyWaiter wtr){
		int numWaitersWorking = 0;
		//find the number of working waiters
		for(MyWaiter waiter : waiters){
			if(waiter.state != WaiterState.OnBreak){
				numWaitersWorking++;
			}
		}
		//if there are more than one, then wtr can go on break
		if(numWaitersWorking > 1){
			wtr.state = WaiterState.OnBreak;
		}else{ //otherwise refuse the offer
			wtr.state = WaiterState.Working;
			wtr.waiter.getGui().cannotGoOnBreak();
		}
		DoReplyToWaiterBreakRequest(wtr);
		//message the wtr
		wtr.waiter.msgBreakReply(wtr.state == WaiterState.OnBreak);
		
	}
	
	/**
	 * Private method called by the scheduler for the host to assign a customer and
	 * waiter to a table.
	 * @param cust Customer to be assigned
	 * @param wtr Waiter to assign the customer to
	 * @param tbl Table to assign the both of them to
	 */
	private void assignCustomerToWaiter(WaitingCustomer cust, WaiterAgent wtr, Table tbl){
		DoAssignCustomerToWaiter(cust, wtr, tbl);
		tbl.setOccupant(cust.cust);
		waitingCustomers.remove(cust);
		wtr.msgSitAtTable(cust.cust, tbl);

	}
	
	private void notifyCustomer(WaitingCustomer cust){
		DoNotifyCustomer(cust);
		cust.cust.msgNotifyRestIsFull();
		cust.chosen = true;
	}
	
	//----------------------DO XYZ---------------------//
	
	private void DoAssignCustomerToWaiter(WaitingCustomer cust, WaiterAgent wtr, Table tbl){
		print("set "+cust.cust.getName()+ " for seating at table "+tbl.tableNumber);
	}
	
	private void DoNotifyCustomer(WaitingCustomer cust){
		print("notifying "+cust.cust.getName()+ " that the restaurant is full");
	}
	
	private void DoReplyToWaiterBreakRequest(MyWaiter wtr){
		if(wtr.state == WaiterState.OnBreak){
			print(wtr.waiter.getName()+ " can go on break");
		}else{
			print(wtr.waiter.getName()+" cannot go on break");
		}
	}

	//----------------------UTILITIES--------------------//

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	public static int getNumTables(){
		return NTABLES;
	}
	
	private class WaitingCustomer {
		CustomerAgent cust;
		boolean chosen;
		
		public WaitingCustomer(CustomerAgent cust){
			this.cust = cust;
			chosen = false;
		}
	}

}

