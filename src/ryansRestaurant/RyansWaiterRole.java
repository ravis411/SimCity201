package ryansRestaurant;

import agent.Agent;
import ryansRestaurant.gui.WaiterGui;
import ryansRestaurant.interfaces.RyansCashier;
import ryansRestaurant.interfaces.RyansCustomer;
import ryansRestaurant.interfaces.RyansHost;
import ryansRestaurant.interfaces.RyansWaiter;

import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant RyansHost Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the RyansHostRole. A RyansHost is the manager of a ryansRestaurant who sees that all
//is proceeded as he wishes.
public class RyansWaiterRole extends Agent implements RyansWaiter {
	public List<MyCustomer> myCustomers	= new ArrayList<MyCustomer>();
	
	private String name = new String("RyansWaiter");
	private Semaphore atTable = new Semaphore(0,true);
	private RyansHost host = null;
	public RyansCookRole cook = null;
	private RyansCashier cashier = null;
	
	private enum CustomerState {waiting, seated, readyToOrder, ordering, askedToOrder, ordered, orderOut, waitingForOrder, OrderReady, eating, billReady, billed, leaving,  done};
	private enum AgentState {outSide, atTable, atCook, atCounter, atCashier, none, atHome};
	private enum AgentEvent {None, CustomerOrdering};
	private enum BreakState {none, OnBreak, requestBreak, requestedBreak, preparingForBreak, backToWork, okayToPrepareForBreak};
	private BreakState breakStatus = BreakState.none;
	private AgentEvent event = AgentEvent.None;
	private AgentState state = AgentState.outSide;
	
	public WaiterGui waiterGui = null;
	public String activity = new String();
	private Timer timer = new Timer();
	
	
	public RyansWaiterRole(String name, RyansHost host, RyansCookRole cook, RyansCashier cashier) {
		super();
		this.name = name;
		this.host = host;
		this.cook = cook;
		this.cashier = cashier;
		
		
	}

	
	public String getName() {
		return name;
	}

	// Messages

	public void msgSitAtTable(RyansCustomer cust, int tableNumber) {
		myCustomers.add(new MyCustomer(cust, tableNumber));
		stateChanged();
	}

	public void msgReadyToOrder(RyansCustomer customer) {
		for(MyCustomer c : myCustomers) {
			if(c.customer == customer)
			{
				c.s = CustomerState.readyToOrder;
				stateChanged();
			}
		}
	}
	
	public void msgHereIsMyChoice(RyansCustomer customer, String choice) {
		for(MyCustomer c : myCustomers) {
			if(c.customer == customer)
			{
				c.s = CustomerState.askedToOrder;
				c.choice = choice;
				event = AgentEvent.None;
				stateChanged();
				return;
			}
		}
	}
	
	public void msgOutOfOrder(int tableNumber, String choice) {
		for(MyCustomer c : myCustomers) {
			if(c.tableNumber == tableNumber) {
				c.s = CustomerState.orderOut;
				stateChanged();
				return;
			}
		}
	}
	
	public void msgOrderReady(int tableNumber, int grillNumber) {
		for(MyCustomer c : myCustomers) {
			if(c.tableNumber == tableNumber)
			{
				c.s = CustomerState.OrderReady;
				c.grillNumber = grillNumber;
				stateChanged();
			}
		}
	}
	
	public void msgHereIsCheck(RyansCustomer cust, double total) {
		for(MyCustomer c : myCustomers) {
			if(c.customer == cust) {
				c.bill = total;
				c.s = CustomerState.billReady;
				break;
			}
		}
		stateChanged();
	}
	
	public void msgDoneEatingLeaving(RyansCustomer customer) {
		for(MyCustomer c : myCustomers) {
			if(c.customer == customer)
			{
				c.s = CustomerState.leaving;
				stateChanged();
			}
		}
	}
	
	/**Msg from host to allow waiter to go on a break
	 * 
	 * @param b true if break is allowed, false otherwise
	 */
	public void msgFromHostGoOnBreak(boolean b) {
		if(b) {
			//breakStatus = BreakState.preparingForBreak;
			breakStatus = BreakState.okayToPrepareForBreak;
			//waiterGui.prepareForBreak();//to update gui
		}
		else {
			//breakStatus = BreakState.none;
			breakStatus = BreakState.backToWork;
			//waiterGui.backToWork();
		}
		stateChanged();
	}
	
	/**
	 * From animation so waiter can request a break
	 */
	public void msgRequestABreak() {
		if(breakStatus == BreakState.none)
			breakStatus = BreakState.requestBreak;
		stateChanged();
	}
	//from animation
	public void msgBackToWork() {
		breakStatus = BreakState.backToWork;
		stateChanged();
	}
	
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		state = AgentState.atTable;
		stateChanged();
	}
	
	public void msgAtCounter() {//from animation
		atTable.release();
		state = AgentState.atCounter;
		stateChanged();
	}
	
	public void msgAtCook() {
		atTable.release();
		state = AgentState.atCook;
		stateChanged();
	}
	
	public void msgAtCashier() {
		atTable.release();
		state = AgentState.atCashier;
		stateChanged();
	}
	public void msgAtHome() {
		state = AgentState.atHome;
		print("I'm Home!!!");
		activity = "";
		stateChanged();
	}
	public void msgAtDestination() {
		atTable.release();
		state = AgentState.none;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		try {
			if(state == AgentState.outSide )
			{
				EnterRestaurant();
			}
			
			
			if(breakStatus == BreakState.requestBreak) {
				requestBreak();
				return true;
			}
			else if(breakStatus == BreakState.backToWork) {
				BackToWork();
				return true;
			}
			else if(breakStatus == BreakState.okayToPrepareForBreak){
				prepareForBreak();
				return true;
			}
			
			if(!myCustomers.isEmpty())
			{
				if(event == AgentEvent.CustomerOrdering) {
						//In the middle of taking an order..there's nothing to do.
						//this could have been if(c.s == ordering)
					//and return false because stateChanged() will be called
						return false;
				}
				
				for(MyCustomer c : myCustomers) {
					
					if(c.s == CustomerState.waiting) {
						seatCustomer(c);
						return true;
					}
					
					else if(c.s == CustomerState.leaving) {
						customerLeaving(c);
						return true;
					}
					
					else if(c.s == CustomerState.orderOut) {
						haveCustomerReorder(c);
						return true;
					}
					
					else if(c.s == CustomerState.billReady) {
						deliverBill(c);
						return true;
					}
				}
				
				for(MyCustomer c : myCustomers) {
					if(c.s == CustomerState.readyToOrder)
					{
						takeOrder(c);
						return true;
					}
				}
					
				for(MyCustomer c : myCustomers) {
					if(c.s == CustomerState.askedToOrder)
					{
						Order(c);
						return true;
					}
					
					else if(c.s == CustomerState.OrderReady) {
						BringOrderToCustomer(c);
						computeBill(c);
						return true;
					}
				}
				
							
				
				if(event == AgentEvent.None && state == AgentState.atTable || state == AgentState.atCook)
				{
					DoLeaveCustomer();
					return true;
				}
			}
			else if(breakStatus == BreakState.preparingForBreak){
				goOnBreak();
			}
		} catch (ConcurrentModificationException e) {
			print("ConcurrentModificationException caught. Returning true.");
			return true;
		}
		//catch(Exception e) {
		////	print("Exception Caught in Scheduler!!!!!!!!!!!!!!!!!" + e);
	//		return true;
	//}
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	
	
	// Actions
	private void EnterRestaurant() {
		print("Entering Restaurant");
		try {
			waiterGui.DoEnterRestaurant();
		} catch (Exception e) {
			EnterRestaurant();
		}
		state=AgentState.atHome;
	}
	
	
	
	
	private void seatCustomer(MyCustomer c) {
		
		
		//waiterGui.DoGoToCounter();
		
		activity = "Going to sit " + c.customer;
		try {
			waiterGui.DoGoToCustomer(c.customer);
		} catch (Exception e) {
		}
		
		
		activity = "Seating " + c.customer;
		c.customer.msgSitAtTable(cook.getMenu(), cashier);
		DoSeatCustomer((RyansCustomerRole) c.customer, c.tableNumber);
		
		c.s = CustomerState.seated;
		activity = "RyansCustomer seated.";
	}
	
	private void takeOrder(MyCustomer c) {
		event = AgentEvent.CustomerOrdering;
		
		activity = "Going to take " + c.customer + "'s order.";
		DoGoToTable(c.tableNumber);

		activity = "What can I get you?";
		c.customer.msgWhatWouldYouLike();
		c.s = CustomerState.ordering;
	}
	
	private void Order(MyCustomer c) {
		activity = "Going to order food for " + c.customer;
		try {
			waiterGui.DoGoToCook();
		} catch (Exception e1) {
		}
		state=AgentState.atCook;
		
		activity = "" + c.customer + " would like " + c.choice;
		
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			print("EXCEPTION!!!! caught while waiting for order.");
		}
		
		cook.msgHereIsOrder(this, c.choice, c.tableNumber);
		c.s = CustomerState.ordered;
		activity = "Ordered.";
	}
	
	private void BringOrderToCustomer(MyCustomer customer) {
		print("Getting food from cook for customer: " + customer.customer);
		activity = "Picking up food for " + customer.customer;
		DoGoToGrill(customer.grillNumber);
		

		
		activity = "Going to bring food.";
		activity += "\n\n " + customer.choice.charAt(0) + Character.toUpperCase(customer.choice.charAt(1)) + "";
        DoGoToTable(customer.tableNumber);


		print("Your food has arrived.");
		activity = "Your food has arrived.";
		activity += "\n\n " + customer.choice.charAt(0) + Character.toUpperCase(customer.choice.charAt(1));
		
		
		customer.customer.msgHereIsYourFood();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			print("EXCEPTION!!!!!!! caught while delivering food.");
		}
		
		
		customer.s = CustomerState.eating;
	}
	
	private void haveCustomerReorder(MyCustomer c) {
		activity = "Going to have " + c.customer + " re-order.";
		DoGoToTable(c.tableNumber);
		activity = "We are out of " + c.choice;
		c.customer.msgOutOfChoice(cook.getMenu());
		c.s = CustomerState.seated;
	}
	
	private void computeBill(MyCustomer cust) {
		cashier.msgComputeBill(this, cust.choice, cust.customer);
	}
	
	private void deliverBill(MyCustomer cust) {
		//Pick up bill from cashier
		activity = "Picking up bill for " + cust.customer;
		DoGoToCashier();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			print("EXCEPTION!!!!! caught while speaking to cashier.");
		}
		activity = "Going to deliver bill.";
		DoGoToTable(cust.tableNumber);
		
		activity = "Here's your bill.";
		cust.s = CustomerState.billed;
		cust.customer.msgHereIsCheck(cust.bill);
		
	}
	
	
	private void customerLeaving(MyCustomer c) {
		host.msgTableFree(this, c.tableNumber);
		c.s = CustomerState.done;
		myCustomers.remove(c);
	}
	
	
	
	private void requestBreak() {
		breakStatus = BreakState.requestedBreak;
		host.msgWantToGoOnBreak(this);
	}
	private void prepareForBreak() {
		breakStatus = BreakState.preparingForBreak;
		waiterGui.prepareForBreak();
	}
	private void goOnBreak() {
		activity = "On Break.";
		breakStatus = BreakState.OnBreak;
		waiterGui.onBreak();
	}
	private void BackToWork() {
		breakStatus = BreakState.none;
		host.msgBreakOver(this);
		waiterGui.backToWork();
		activity = "";
	}
	
	
	
	// The animation DoXYZ() routines
	private void DoGoToTable(int tableNumber) {
		try {
			waiterGui.DoGoToTable(tableNumber);
		} catch (Exception e) {
		}
		state=AgentState.atTable;
	}
	
	private void DoGoToGrill(int grillNumber){
		try {
			waiterGui.DoGoToGrill(grillNumber);
		} catch (Exception e) {
		}
	}
	
	private void DoLeaveCustomer() {
		activity = "Leaving area.";
		try {
			waiterGui.DoLeaveCustomer();
		} catch (Exception e) {
		}
		activity="";
		//state=AgentState.atHome;
	}
		
	private void DoSeatCustomer(RyansCustomerRole customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		try {
			waiterGui.DoBringToTable(customer, table);
		} catch (Exception e) {
		}
		state=AgentState.atTable;

	}
	
	private void DoGoToCashier() {
		try {
			waiterGui.DoGoToCashier();
		} catch (Exception e) {
		}
	}

	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public void setHost(RyansHostRole host) {
		this.host = host;
	}
	
	public String toString() {
		return " " + name;
	}
	
//	public int getNumActiveCustomers() {
//		return myCustomers.size();
//	}

	/**MyCustomer class for the waiter
	 * 
	 */
	class MyCustomer {
		public int grillNumber;
		RyansCustomer customer = null;
		int tableNumber = 0;
		String choice = null;
		CustomerState s = null;
		double bill = 0;
		
		MyCustomer(RyansCustomer customer) {
		this.customer = customer;
		}

		MyCustomer(RyansCustomer cust, int tableNumber) {
			this.customer = cust;
			this.tableNumber = tableNumber;
			this.s = CustomerState.waiting;
		}
	}
	
	
	
}

