package kushrestaurant;

import Person.Role.Role;
import agent.Agent;
import kushrestaurant.WaiterRole.WaiterEvent;
import kushrestaurant.gui.WaiterGui;
import kushrestaurant.interfaces.Customer;
import kushrestaurant.interfaces.Host;
import kushrestaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends Role {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private List<Customer> waitingCustomers
	= new ArrayList<Customer>();
	public Collection<Table> tables;
	private int waiterCounter=-1;
	//public List<WaiterAgent> waiters = new ArrayList<WaiterAgent>();
	private List<Waiter> waiters = Collections.synchronizedList(new ArrayList<Waiter>());
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
 //  private Semaphore atTable= new Semaphore(0,true);
	private String name;
	//private Semaphore atTable = new Semaphore(0,true);

	//public HostGui hostGui = null;
   public HostRole(){
	   super();
	   tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
   }
	public HostRole(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}
   public List getWaiterList(){
	   return waiters;
   }
   public Waiter getReadyWaiter(){
	   return waiters.get(waiterCounter);
   }
   public boolean areAllTablesOccupied(){
	   boolean flag=true;;
	   for(Table t:tables){
		   if(!t.isOccupied()){
			   flag=false;
		   }
	   }
	   return flag;
   }
   public void addWaiter(Waiter waiter){
	   waiters.add(waiter);
	   stateChanged();
   }
	public Collection getTables() {
		return tables;
	}
	// Messages

	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(cust);
		
		//cust.setWaiter(waiter);
		stateChanged();
	}
    public void msgTableAvailable(Customer c){
    	 for(Table t: tables){
    		 if(t.getOccupant()==c){
    			 print(c+"leaving"+t);
    			 t.setUnoccupied();
    			 
    			 stateChanged();
    		 }
    	 }
    }
    public void seatCustomerMsg(Customer c, Table t)
	{ SeatCustomer(c,t);
	stateChanged();
	
	}
    private void SeatCustomer(Customer customer,Table t){
    	//print(waiters.size()+"Waiters");
    	
    	if(waiterCounter < waiters.size()){ waiterCounter++; }
		if(waiterCounter >= waiters.size()){ waiterCounter = 0; }
		
	    if(waiters.get(waiterCounter).isAtBreak())
	    {
	    		//print("TEST");
	    		waiterCounter++;
	    		
	    	}
	    if(waiterCounter>=waiters.size()){waiterCounter=0;}	
		//Sends Messages
		//msgSeatCustomer(customer, table);
		print("telling waiter to seat " + customer.getName());
		waiters.get(waiterCounter).msgSeatCustomer(customer, t);
		customer.setWaiter(waiters.get(waiterCounter));//hack
		 //pops first customer out of list
		
		stateChanged();
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
		//print("test");
		if(!(waiters.size() > 0))
		{
			return true; 
		}
		
		if( !areAllTablesOccupied() )
		{
		for (Table table : tables) 
		{
			if (!table.isOccupied()) //&& !areAllTablesOccupied()) 
			{
				if (!waitingCustomers.isEmpty()) 
				{
					waitingCustomers.get(0).setTableNumber(table.tableNumber);
					SeatCustomer(waitingCustomers.get(0), table);
					table.setOccupant(waitingCustomers.get(0));
					waitingCustomers.remove(0);
					//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
			
		}
		else 
			{
			if(!waitingCustomers.isEmpty())
			{waitingCustomers.get(0).msgWait();
			
				}
			}
		
		
		//else if(areAllTablesOccupied())
		//{waitingCustomers.get(0).msgWait();}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	public void msgAskForBreak(Waiter waiter){
		 int count=0;
		 for(Waiter wa: waiters){
				if(wa.isAtBreak()){count++;}}
		if(waiters.size()==1 || count==waiters.size()-1){
			print("You cant go on break");
			waiter.msgYouCantGoOnBreak();
		}
		
		else
			{ print("Go on break");
			  waiter.msgGoOnBreak();
			  for(Waiter w : waiters){
				  if(w==waiter){
					  waiter.changeBreakEvent2();
				  }
			  }
			}
	};

	// Actions
	
   public void msgBreakDone(Waiter waiter){
	   for(Waiter w : waiters){
			  if(w==waiter){
				  waiter.changeBreakEvent();
			  }
   }
   }
	/**private void seatCustomer(CustomerAgent customer, Table table) {
		customer.msgSitAtTable(table.getTablenumber());
		DoSeatCustomer(customer, table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
		hostGui.DoLeaveCustomer();
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(CustomerAgent customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		hostGui.DoBringToTable(customer); 

	}**/
	//utilities

	//public void setGui(HostGui gui) {
		//hostGui = gui;
	//}

	//public HostGui getGui() {
		//return hostGui;
	//}

	public class Table {
		Customer occupiedBy=null;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
			occupiedBy=null;
		}
       public int getTablenumber(){
    	   return tableNumber;
       }
		void setOccupant(Customer c) {
			occupiedBy = c;
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
	}
	

	

	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		// TODO Auto-generated method stub
		return "HostRole";
	}


	
	
}
