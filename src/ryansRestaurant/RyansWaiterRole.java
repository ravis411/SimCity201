package ryansRestaurant;

import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import ryansRestaurant.gui.WaiterGui;
import ryansRestaurant.interfaces.RyansCashier;
import ryansRestaurant.interfaces.RyansCustomer;
import ryansRestaurant.interfaces.RyansHost;
import ryansRestaurant.interfaces.RyansWaiter;
import trace.AlertLog;
import trace.AlertTag;
import Person.Role.ShiftTime;

/**
 * Restaurant RyansHost Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the RyansHostRole. A RyansHost is the manager of a ryansRestaurant who sees that all
//is proceeded as he wishes.
public abstract class RyansWaiterRole extends GenericWaiter implements RyansWaiter {
	public List<MyCustomer> myCustomers	= new ArrayList<MyCustomer>();
	

	protected String name = new String();
	protected Semaphore atTable = new Semaphore(0,true);
	protected RyansHost host = null;
	public RyansCookRole cook = null;
	protected RyansCashier cashier = null;
	
	protected enum CustomerState {waiting, seated, readyToOrder, ordering, askedToOrder, ordered, orderOut, waitingForOrder, OrderReady, eating, billReady, billed, leaving,  done};
	protected enum AgentState {outSide, atTable, atCook, atCounter, atCashier, none, atHome, leavingWork};
	protected enum AgentEvent {None, CustomerOrdering};
	protected enum BreakState {none, OnBreak, requestBreak, requestedBreak, preparingForBreak, backToWork, okayToPrepareForBreak};
	protected BreakState breakStatus = BreakState.none;
	protected AgentEvent event = AgentEvent.None;
	protected AgentState state = AgentState.outSide;
	protected AgentState shiftStatus = AgentState.none;
	
	public WaiterGui waiterGui = null;
	public String activity = new String();
	protected Timer timer = new Timer();
	
	
	protected RyansWaiterRole(String workLocation) {
		super(workLocation);

		
		
	}

	
	public String getName() {
		return myPerson.getName();
	}

	// Messages
	
	
	
	public void msgLeaveWork(){
		AlertLog.getInstance().logDebug(AlertTag.RYANS_RESTAURANT, getName(), "Preparing to leave work.");
		shiftStatus = AgentState.leavingWork;
		stateChanged();
	}
	

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
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "I'm Home!!!");
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
	public boolean pickAndExecuteAction() {
		
		try {
			if(state == AgentState.outSide )
			{
				EnterRestaurant();
				return true;
			}
			if(shiftStatus == AgentState.leavingWork && breakStatus == BreakState.none){
				AlertLog.getInstance().logDebug(AlertTag.RYANS_RESTAURANT, getName(), "Scheduler okayToPrepare for break.");
				breakStatus = BreakState.okayToPrepareForBreak;
				return true;
			}
			if(breakStatus == BreakState.OnBreak && shiftStatus == AgentState.leavingWork){
				AlertLog.getInstance().logDebug(AlertTag.RYANS_RESTAURANT, getName(), "Scheduler for called. LeaveWork should be called.");
				leaveWork();
				return true;
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
				return true;
			}
		} catch (ConcurrentModificationException e) {
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "ConcurrentModificationException caught. Returning true.");
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
	protected void EnterRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Entering Restaurant");

		try {
			waiterGui.DoEnterRestaurant();
		} catch (Exception e) {
			EnterRestaurant();
		}
		state=AgentState.atHome;
	}
	
	
	
	
	protected void seatCustomer(MyCustomer c) {
		
		
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
	
	protected void takeOrder(MyCustomer c) {
		event = AgentEvent.CustomerOrdering;
		
		activity = "Going to take " + c.customer + "'s order.";
		DoGoToTable(c.tableNumber);

		activity = "What can I get you?";
		c.customer.msgWhatWouldYouLike();
		c.s = CustomerState.ordering;
	}
	
	protected abstract void Order(MyCustomer c);
//	{
//		activity = "Going to order food for " + c.customer;
//		try {
//			waiterGui.DoGoToCook();
//		} catch (Exception e1) {
//		}
//		state=AgentState.atCook;
//		
//		activity = "" + c.customer + " would like " + c.choice;
//		
//		try {
//			Thread.sleep(2500);
//		} catch (InterruptedException e) {
//			print("EXCEPTION!!!! caught while waiting for order.");
//		}
//		
//		cook.msgHereIsOrder(this, c.choice, c.tableNumber);
//		c.s = CustomerState.ordered;
//		activity = "Ordered.";
//	}
	
	protected void BringOrderToCustomer(MyCustomer customer) {
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Getting food from cook for customer: " + customer.customer);
		activity = "Picking up food for " + customer.customer;
		DoGoToGrill(customer.grillNumber);
		

		
		activity = "Going to bring food.";
		activity += "\n\n " + customer.choice.charAt(0) + Character.toUpperCase(customer.choice.charAt(1)) + "";
        DoGoToTable(customer.tableNumber);


        AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Your food has arrived.");
		activity = "Your food has arrived.";
		activity += "\n\n " + customer.choice.charAt(0) + Character.toUpperCase(customer.choice.charAt(1));
		
		
		customer.customer.msgHereIsYourFood();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "EXCEPTION!!!!!!! caught while delivering food.");
		}
		
		
		customer.s = CustomerState.eating;
	}
	
	protected void haveCustomerReorder(MyCustomer c) {
		activity = "Going to have " + c.customer + " re-order.";
		DoGoToTable(c.tableNumber);
		activity = "We are out of " + c.choice;
		c.customer.msgOutOfChoice(cook.getMenu());
		c.s = CustomerState.seated;
	}
	
	protected void computeBill(MyCustomer cust) {
		cashier.msgComputeBill(this, cust.choice, cust.customer);
	}
	
	protected void deliverBill(MyCustomer cust) {
		//Pick up bill from cashier
		activity = "Picking up bill for " + cust.customer;
		DoGoToCashier();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "EXCEPTION!!!!! caught while speaking to cashier.");
		}
		activity = "Going to deliver bill.";
		DoGoToTable(cust.tableNumber);
		
		activity = "Here's your bill.";
		cust.s = CustomerState.billed;
		cust.customer.msgHereIsCheck(cust.bill);
		
	}
	
	
	protected void customerLeaving(MyCustomer c) {
		host.msgTableFree(this, c.tableNumber);
		c.s = CustomerState.done;
		myCustomers.remove(c);
	}
	
	
	
	protected void requestBreak() {
		breakStatus = BreakState.requestedBreak;
		host.msgWantToGoOnBreak(this);
	}
	protected void prepareForBreak() {
		breakStatus = BreakState.preparingForBreak;
		waiterGui.prepareForBreak();
	}
	protected void goOnBreak() {
		activity = "On Break.";
		breakStatus = BreakState.OnBreak;
		waiterGui.onBreak();
	}
	protected void BackToWork() {
		breakStatus = BreakState.none;
		host.msgBreakOver(this);
		waiterGui.backToWork();
		activity = "";
	}
	
	protected void leaveWork(){
		AlertLog.getInstance().logDebug(AlertTag.RYANS_RESTAURANT, getName(), "Leaving work called.");
		breakStatus = BreakState.none;
		event = AgentEvent.None;
		state = AgentState.outSide;
		shiftStatus = AgentState.none;
		activity = "Leaving Work";
		waiterGui.doLeaveWork();
		activity = "";
		host.msgRemoveWaiter(this);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				host.msgWakeUp();
				cook.msgWakeUp();
				cashier.msgWakeUp();
			}
		}, 5000);
		
		super.deactivate();
		super.kill();
		
	}
	
	
	
	// The animation DoXYZ() routines
	protected void DoGoToTable(int tableNumber) {
		try {
			waiterGui.DoGoToTable(tableNumber);
		} catch (Exception e) {
		}
		state=AgentState.atTable;
	}
	
	protected void DoGoToGrill(int grillNumber){
		try {
			waiterGui.DoGoToGrill(grillNumber);
		} catch (Exception e) {
		}
	}
	
	protected void DoLeaveCustomer() {
		activity = "Leaving area.";
		try {
			waiterGui.DoLeaveCustomer();
		} catch (Exception e) {
		}
		activity="";
		//state=AgentState.atHome;
	}
		
	protected void DoSeatCustomer(RyansCustomerRole customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		AlertLog.getInstance().logMessage(AlertTag.RYANS_RESTAURANT, getName(), "Seating " + customer + " at " + table);
		try {
			waiterGui.DoBringToTable(customer, table);
		} catch (Exception e) {
		}
		state=AgentState.atTable;

	}
	
	protected void DoGoToCashier() {
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

	
	
	
	@Override
	public void setCook(GenericCook c) {
		// TODO Auto-generated method stub
		this.cook = (RyansCookRole) c;
		this.waiterGui.setCook(this.cook.getGui());
	}


	@Override
	public void setCashier(GenericCashier c) {
		// TODO Auto-generated method stub
		this.cashier = (RyansCashierRole) c;
		if( !(cashier instanceof RyansCashierRole) || cashier == null) {
			AlertLog.getInstance().logError(AlertTag.RYANS_RESTAURANT, getName(), "ERRROROROROROROR");
		}
	}


	@Override
	public void setHost(GenericHost h) {
		// TODO Auto-generated method stub
		this.host = (RyansHostRole) h;
	}

	@Override
	public Double getSalary() {
		// TODO Auto-generated method stub
		return 42.00;
	}

	@Override
	public boolean canGoGetFood() {
		return false;
	}
	
	@Override
	public void deactivate() {
		kill();
	}
	
	@Override
	public void kill() {
		AlertLog.getInstance().logDebug(AlertTag.RYANS_RESTAURANT, getName(), "Killed called.");
		msgLeaveWork();
	}

}