package restaurant.luca;

import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Person.Role.ShiftTime;
import restaurant.interfaces.luca.LucaCustomer;
import restaurant.interfaces.luca.LucaHost;
import restaurant.interfaces.luca.LucaWaiter;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LucaHostRole extends GenericHost implements LucaHost{
	static int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<LucaCustomer> waitingCustomers
	= Collections.synchronizedList(new ArrayList<LucaCustomer>());
	public List<LucaWaiter> waiters
	= Collections.synchronizedList(new ArrayList<LucaWaiter>());
	public Collection<Table> tables;
	public List<LucaWaiter> waitersRequestingBreak
	= Collections.synchronizedList(new ArrayList<LucaWaiter>());
	public List<LucaWaiter> waitersOnBreak
	=  Collections.synchronizedList(new ArrayList<LucaWaiter>());
	boolean tablesFull;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	





	public LucaHostRole(String restLocation) {
		super(restLocation);
		
		// make some tables
		tables = Collections.synchronizedCollection(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}
	public static int getNTABLES(){
		return NTABLES;
	}
	public String getMaitreDName() {
		return  getPerson().getName();
	}

	public String getName() {
		return getPerson().getName();
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages
	public void addWaiter(GenericWaiter waiter) {
		
		waiters.add((LucaWaiter)waiter);
		stateChanged();
		
	}
	public void msgIWantFood(LucaCustomer lucaCustomer) {
		waitingCustomers.add(lucaCustomer);
		stateChanged();
	}
	
	public void msgHostTableFree(int tableNum) {
		for (Table table : tables) {
			if (table.getTableNumber() == tableNum) {
				print(table.getOccupant() + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}
	
	public void msgHostCanIGoOnBreak(LucaWaiter waiter) {
		waitersRequestingBreak.add(waiter);
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
		if (!waiters.isEmpty())
		{
			tablesFull=true;
			for (Table table : tables) {
				if (!table.isOccupied())
					tablesFull=false;
			}
			if (tablesFull)
			{
				if (!waitingCustomers.isEmpty()){
				removeCustomersWhoWillNotWaitFromWaitingCustomers();
				return true;
				}
			}
			for (Table table : tables) {
				if (!table.isOccupied()) {
					if (!waitingCustomers.isEmpty()) {
						table.setOccupied(waitingCustomers.get(0));
						TellWaiterToSeatCustomer(table.getTableNumber());//the action
						//take this table of the hosts list as available
						return true;//return true to the abstract agent to reinvoke the scheduler.
						
					}
				}
			}
		}
		if (!waiters.isEmpty())
		{
			if (!waitersRequestingBreak.isEmpty())
			{
				checkIfWaiterCanBreak();
				return true;
			}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	// Actions

	
	
	private void removeCustomersWhoWillNotWaitFromWaitingCustomers() {
		for (int i = 0; i<waitingCustomers.size(); i++){
			if(waitingCustomers.get(i).getWillWaitforFood() == false){
				print(waitingCustomers.get(i).getName() + " Will not wait to be seated so he is Leaving");
				waitingCustomers.get(i).msgYouAreOffWaitList();
				waitingCustomers.remove(waitingCustomers.get(i));

			}
			/*else
				print(waitingCustomers.get(i).getCustomerName() + " Will wait to be seated so he is Staying"); //causing a console loop*/
		}	
	}
	private void TellWaiterToSeatCustomer(int tableNum) {
		boolean temp=false;
		int x=0;
		if ((waiters.size()== 1) ||(waiters.get(0).getMyCustomerSize() == 0))
			x=0;
		for (int i = 0; i<(waiters.size()-1); i++){
				for (int j = 0; j<(waiters.size()); j++){
					if (!waitersOnBreak.isEmpty()){
					for (int k = 0; k<waitersOnBreak.size(); k++){
						if (waiters.get(j)==waitersOnBreak.get(k)){
							temp=true;
							}
						}
					if (temp==false){
						if(waiters.get(i).getMyCustomerSize() > waiters.get(j).getMyCustomerSize()){
							x=j;
							break;
						}
					}
					}
					else
						if(waiters.get(i).getMyCustomerSize() > waiters.get(j).getMyCustomerSize()){
							x=j;
							break;
							}
			}
		}

		
		for (Table table : tables) {
			if (table.getTableNumber()==tableNum)
			{
				print(waiters.get(x).getName() + " seat customer at table" + tableNum);
				waiters.get(x).msgWaiterSeatCustomer(table.getOccupant(), tableNum);
				waitingCustomers.remove(waitingCustomers.get(0));//the customer may be waiting to be seated still but the host does not need to worry about this since waiter will come do it.
				break;
			}
		}
		
	}
	
	private void checkIfWaiterCanBreak() {
		for (int i =0; i<waitersRequestingBreak.size(); i++)
		{
			if (waiters.size()-waitersOnBreak.size() >1){//makes sure at least 1 waiter working
				if (waitersRequestingBreak.get(i).getMyCustomerSize() == 0){
					waitersRequestingBreak.get(i).msgWaiterYouCanBreak();
					waitersOnBreak.add(waitersRequestingBreak.get(i));//Moves the waiter on list requesting break to the list of waiters on break
					print("Yes " + waitersRequestingBreak.get(i).getName() + ", go on break");
					waitersRequestingBreak.remove(waitersRequestingBreak.get(i));
				}
			}
			else{
				waitersRequestingBreak.get(i).msgWaiterYouCanNotBreak();
				print("No " + waitersRequestingBreak.get(i).getName() + ", you can not go on break");
				waitersRequestingBreak.remove(waitersRequestingBreak.get(i));
			}
		}
		
	}
	
	public void msgHostBackFromBreak(LucaWaiter waiter) {
		waitersOnBreak.remove(waiter);
		
	}



	//utilities
	
	//private Waiter findAvailableWaiter(){
		
	//}


	private class Table {
		boolean occupied;
		int tableNumber;
		LucaCustomer occupiedBy;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupied(LucaCustomer occBy) {
			occupied = true;
			occupiedBy=occBy;
		}

		void setUnoccupied() {
			occupied = false;
			occupiedBy = null;
		}

		LucaCustomer getOccupant(){
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupied;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		public int getTableNumber(){
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
		// TODO Auto-generated method stub
		return null;
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

