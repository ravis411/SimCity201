package restaurant.luca;

import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCook;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import restaurant.gui.luca.RestaurantGui;
import restaurant.gui.luca.WaiterGui;
import restaurant.interfaces.luca.LucaCustomer;
import restaurant.interfaces.luca.LucaWaiter;
import restaurant.test.mock.EventLog;
import Person.Role.Role;
import Person.Role.ShiftTime;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A custome8r and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public abstract class LucaWaiterRole extends GenericWaiter implements LucaWaiter{
	public List<MyCustomers> myCustomers
	= Collections.synchronizedList(new ArrayList<MyCustomers>());
	protected Semaphore atTable = new Semaphore(0,false);
	protected Semaphore atCook = new Semaphore(0,false);
	protected Semaphore waitForOrder = new Semaphore(0,false);
	protected boolean WaiterAvailable;
	protected LucaCashierRole cashier;
	protected LucaCookRole cook;
	protected LucaHostRole host;
	protected WaiterGui waiterGui;
	protected RestaurantGui restaurantGui;
	protected boolean onBreak;
	protected boolean requestBreak;
	protected boolean tellHostIamBackFromBreak;
	public static HashMap<String, Integer> menu;
	public static Map<String, Integer> menuUnmodifiable;
	public EventLog log= new EventLog();

	
	public LucaWaiterRole(String restLocation){
		super(restLocation);
		onBreak = false;
		requestBreak=false;
		tellHostIamBackFromBreak=false;
		menu= new HashMap<String, Integer>();
		
		menu.put("Steak",  20);
		menu.put("Chicken", 10);
		menu.put("Burger", 15);
		menuUnmodifiable = Collections.unmodifiableMap(menu);
	}

	public String getName() {
		return myPerson.getName();
	}
	public void tellHostIExist(){
		host.addWaiter(this);
	}

	public boolean isOnBreak() {
		return onBreak;
	}


	// Messages

	public void msgWaiterSeatCustomer(LucaCustomer c, int Tablenum)
	{
		myCustomers.add(new MyCustomers(c,Tablenum));
		stateChanged();
	}
	
	public void msgReadyToOrder(LucaCustomer customer){
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).getCustomer() == customer){
				myCustomers.get(i).setCustomerReadyToOrder(true);
				this.stateChanged();
				break;
			}
		}
	}
	
	public void msgHereIsMyChoice(LucaCustomer customer, String choice){
		if (choice =="nothing"){
			waitForOrder.release();
		}
		else{
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).getCustomer() == customer){
				myCustomers.get(i).setCustomerChoice(choice);
				myCustomers.get(i).setCustomerOrderTaken(true);
				waitForOrder.release();
				print("got Choice");
				break;
			}
		}
		}
		stateChanged();
	}
	public void msgWaiterOrderOutOfStock(int tableOriginNumber,	String orderChoice) {
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).getCustomerTableNumber() == tableOriginNumber){
				myCustomers.get(i).setCustomerOrderTaken(false);
				myCustomers.get(i).setCustomerReadyToOrder(true);
				myCustomers.get(i).setCookHasOrder(false);
				stateChanged();
				break;
			}
		}
	}
		
		

	public void msgWaiterOrderIsReady(int tableOriginNumber, String orderChoice) {
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).getCustomerTableNumber() == tableOriginNumber){
				myCustomers.get(i).setCustomerOrderReady(true);
				break;
			}
		}
		stateChanged();
	}
	public void msgWaiterReadyForCheck(LucaCustomer customer) {
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).getCustomer() == customer){
				myCustomers.get(i).setReadyForCheck(true);
				break;
			}
		}
		stateChanged();
	}

	public void msgWaiterHereIsCheck(double tab, LucaCustomer customer) {
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).getCustomer() == customer){
				myCustomers.get(i).setCustomersCheckReady(true);
				myCustomers.get(i).setCustomersTab(tab);
				break;
			}
		}
		stateChanged();
	}

	public void msgDoneLeavingTable(LucaCustomer customer) {
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).getCustomer() == customer){
				myCustomers.get(i).setCustomerleaving(true);
				break;
			}
		}
		stateChanged();
	}
	public void msgAtEntrance(){
		stateChanged();
	}
	public void msgWaiterReadytoSeat(boolean x){
		WaiterAvailable= x;
	}
	public void msgWaiterINeedABreak() {//from Gui info panel when checkbox is selected this is called to tell the waiter to ask the host for a break
		requestBreak =true;
		stateChanged();
		
	}
	public void msgWaiterYouCanBreak() {
		onBreak=true;
		requestBreak = false;
		restaurantGui.waiterOnBreak.release();
		stateChanged();
		
	}

	public void msgWaiterYouCanNotBreak() {
		onBreak=false;
		requestBreak = false;
		restaurantGui.waiterOnBreak.release();
		stateChanged();
		
	}
	public void msgWaiterIamBackFromBreak() {
		onBreak=false;
		requestBreak = false;
		tellHostIamBackFromBreak =true;
		stateChanged();
	}

	
	public void msgAtTable() {//from animation
	//	print("msgAtTable() called");
		atTable.release();// = true;
		//stateChanged();
	}
	public void msgAtCook() {//from animation
		//print("msgAtCook() called");
		atCook.release();// = true;
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
		public boolean pickAndExecuteAction() {
			 try {
				 if(myCustomers.isEmpty()){
						if (requestBreak)
						{askHostForBreak();
						return true;
						}
					}
					if(tellHostIamBackFromBreak)
					{tellHostReadyToWork();
					return true;
					}
						
					if(!myCustomers.isEmpty()){
						if(WaiterAvailable){
							if (requestBreak)
							{askHostForBreak();
							}
							for (int i=0; i<myCustomers.size(); i++){
								if (!myCustomers.get(i).isCustomerSeated()){
									seatCustomer(myCustomers.get(i));//the action
									return true;//return true to the abstract agent to reinvoke the scheduler.s
								}
							}
						}
					}
					if(!myCustomers.isEmpty()){
						if(WaiterAvailable){
							if (requestBreak)
							{askHostForBreak();
							}
							for (int i=0; i<myCustomers.size(); i++){
							if (myCustomers.get(i).isCustomerReadyToOrder()){
								askForMyCustomerOrder(myCustomers.get(i));
								return true;
								}
							}
						}
					}
					if(!myCustomers.isEmpty()){
						if(WaiterAvailable){
							if (requestBreak)
							{askHostForBreak();
							}
							for (int i=0; i<myCustomers.size(); i++){	
							if (myCustomers.get(i).isCustomerOrderTaken() && !myCustomers.get(i).getCookHasOrder()){
								giveCookOrder(myCustomers.get(i));
								return true;
								}
							}
						}
					}
					if(!myCustomers.isEmpty()){
						if(WaiterAvailable){
							if (requestBreak)
							{askHostForBreak();
							}
							for (int i=0; i<myCustomers.size(); i++){		
								if (myCustomers.get(i).isCustomerOrderReady() && myCustomers.get(i).getCookHasOrder()){
									giveCustomerOrder(myCustomers.get(i));
									return true;
								}
							}
						}
					}
					if(!myCustomers.isEmpty()){
						if(WaiterAvailable){
							if (requestBreak)
							{askHostForBreak();
							}
							for (int i=0; i<myCustomers.size(); i++){
								if (myCustomers.get(i).isReadyForCheck()){
									tellCashierToComputeBill();
									return true;
								}
							}
						}
					}
					if(!myCustomers.isEmpty()){
						if(WaiterAvailable){
							if (requestBreak)
							{askHostForBreak();
							}
							for (int i=0; i<myCustomers.size(); i++){
								if (myCustomers.get(i).isCustomersCheckReady()){
									giveCustomerCheck();
									return true;
								}
							}
						}
					}
					if(!myCustomers.isEmpty()){
						if(WaiterAvailable){
							if (requestBreak)
							{askHostForBreak();
							}
							for (int i=0; i<myCustomers.size(); i++){
								if (myCustomers.get(i).isCustomerleaving()){
									tellHostTableFree(myCustomers.get(i));
									return true;
								}
							}
						}
					}
					return false;
			    } catch (ExecutionException e) {
			      if (e.getCause() instanceof ConcurrentModificationException ) {
			    	  return false;
			      }
			    }
					
			 return false;
		
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	

	// Actions


	protected void askHostForBreak() throws ExecutionException {
		print("Can I , " + getName() + ", go on Break?");
		host.msgHostCanIGoOnBreak(this);//passes the number of customers the waiter has so the host can deny his request if he currently has customers
		requestBreak =false;
	}
	protected void tellHostReadyToWork() throws ExecutionException{
		print(" I , " + getName() + ", am back from Break");
		tellHostIamBackFromBreak= false;
		host.msgHostBackFromBreak(this);
	}
	protected void seatCustomer(MyCustomers customer) throws ExecutionException{
		waiterGui.showOrderInAnimation("", "");
		DoSeatCustomer(customer);
		customer.getCustomer().msgSitAtTable(customer.getCustomerTableNumber(), this);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.getCustomer().msgHereIsAMenu(menuUnmodifiable);
		customer.setMenuTakenByCustomer();
		print(customer.getCustomer().getName() + " here's a menu");
		waiterGui.DoLeave();
		customer.setCustomerSeated();
	}
	
	protected void askForMyCustomerOrder(MyCustomers customer)throws ExecutionException{
		
		DoTakeOrder(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (customer.getCustomerChoice() != null && customer.getCustomer().getHasEnoughMoney())
		{
			print("Sorry " + customer.getCustomer() + " Your choice is out of stock. What is your second Choice?");
			customer.getCustomer().msgCustomerWhatIsYourSecondChoice(customer.getCustomerChoice()); //passes what they ordered last time to them so it can be removed from menu next time they order
			customer.setCustomerReadyToOrder(false);//set back to not ready to order since they just ordered
			try {
				waitForOrder.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			waiterGui.showOrderInAnimation(customer.Choice, "?");
			waiterGui.DoLeave();
		}
		else{
			print(customer.getCustomer() + " What would you like?");
			customer.getCustomer().msgCustomerWhatWouldYouLike();
			if (!customer.getCustomer().getHasEnoughMoney())
			{
				customer.setCustomerReadyToOrder(false);//set back to not ready to order since they just ordered
				waiterGui.DoLeave();
			}
			try {
				waitForOrder.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			waiterGui.showOrderInAnimation(customer.Choice, "?");
			customer.setCustomerReadyToOrder(false);//set back to not ready to order since they just ordered
			waiterGui.DoLeave();
			
		}
		
	}
	
	protected abstract void giveCookOrder(MyCustomers customer)throws ExecutionException;
	
	protected void giveCustomerOrder(MyCustomers customer)throws ExecutionException{
		doGetOrderFromCook(customer);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.setCookHasOrder(false);//host symbolically takes order from cook
		waiterGui.showOrderInAnimation(customer.Choice, "");
		doBringOrderToCustomer(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.getCustomer().msgCustomerHereIsYourChoice(customer.getCustomerChoice());
		customer.setCustomerOrderTaken(false);
		customer.setCustomerOrderReady(false);//at this point customer takes order to eat so it status as ready set back to false
		waiterGui.showOrderInAnimation("", "");
		waiterGui.DoLeave();
	}
	protected void tellCashierToComputeBill() throws ExecutionException{
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).isReadyForCheck()){
				cashier.msgCashierComputeBill(myCustomers.get(i).getCustomerChoice(), myCustomers.get(i).getCustomer(), this);
				myCustomers.get(i).setReadyForCheck(false);
			}
				
		}
	}
	
	protected void giveCustomerCheck() throws ExecutionException{
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).isCustomersCheckReady()){
			print("Giving customer their check for $" + myCustomers.get(i).getCustomersTab());
			myCustomers.get(i).getCustomer().msgCustomerHereIsYourCheck(myCustomers.get(i).getCustomersTab());
			doBringCheckToCustomer(	myCustomers.get(i));
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myCustomers.get(i).setCustomersCheckReady(false);
			print(myCustomers.get(i).getCustomer() + " has recieved check");
			waiterGui.DoLeave();
			}
		
		}
	}
	protected void tellHostTableFree(MyCustomers customer) throws ExecutionException{
		host.msgHostTableFree(customer.getCustomerTableNumber());
		customer.setCustomerleaving(false);
		myCustomers.remove(customer);
		
	}
	
	// The animation DoXYZ() routines
	protected void DoSeatCustomer(MyCustomers customer) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer.getCustomer() + " at table" + customer.getCustomerTableNumber());
		waiterGui.DoGoToTable(customer.getCustomerTableNumber()); 

	}
	protected void DoTakeOrder(MyCustomers customer) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Taking " + customer.getCustomer() + "'s order at table" +  customer.getCustomerTableNumber());
		waiterGui.DoGoToTable( customer.getCustomerTableNumber()); 
	}

	protected void DoGiveCookOrder(MyCustomers customer) {
		print("Bringing " + customer.getCustomer() + "'s order to cook from table" +  customer.getCustomerTableNumber());
		waiterGui.DoGoToCook(); 
	}
	protected void doGetOrderFromCook(MyCustomers customer) {
		print("Getting " + customer.getCustomer() + "'s order from cook");
		waiterGui.DoGoToPlatingArea(); 
	}
	protected void doBringOrderToCustomer(MyCustomers customer) {
		print("Bringing " + customer.getCustomer() + "'s cooked order to customer at table" +  customer.getCustomerTableNumber());
		waiterGui.DoGoToTable( customer.getCustomerTableNumber());
	}
	protected void doBringCheckToCustomer(MyCustomers customer) {
		print("Bringing " + customer.getCustomer() + "'s Check to customer at table" +  customer.getCustomerTableNumber());
		waiterGui.DoGoToTable( customer.getCustomerTableNumber());
	}
	
	
	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}
	public int getMyCustomerSize(){
		return myCustomers.size();
	}
	public WaiterGui getGui() {
		return waiterGui;
	}
	public void setGui(RestaurantGui gui) {
		restaurantGui = gui;
	}
	public RestaurantGui getRestaurantGui() {
		return restaurantGui;
	}
    protected static class Order {
		
		
		//Order meant to link the following data
		private LucaWaiterRole waiter;
	    private String choice;
	    private int table;

	    
		
	    /**
	     * Constructor
	     * @param w waiter handling order
	     * @param newChoice choice of customer for order
	     * @param table2 table to which the order corresponds
	     */
	    public Order(LucaWaiterRole w, String newChoice, int table2){
	    	waiter = w;
	    	choice = newChoice;
	    	table = table2;
	    }
	    
	    
	    //-----------------ACCESSORS-------------//
	    public String getChoice(){
	    	return choice;
	    }
	    
	    public int getTable(){
	    	return table;
	    }
	    
	    public LucaWaiterRole getWaiter(){
	    	return waiter;
	    }
	    
	    /**
	     * toString method which changes the Order to a readable string
	     */
	    public String toString(){
	    	StringBuilder builder = new StringBuilder();
	    	builder.append("Choice = "+ choice);
	    	builder.append("\tWaiter = "+ waiter.getName());
	    	builder.append("\tTable = "+ table);
	    	return builder.toString();
	    }
	}
	protected class MyCustomers {
		LucaCustomer Customer;
		int tableNumber;
		String Choice;
		boolean menuTaken;
		boolean seated;
		boolean readyToOrder;
		protected boolean orderTaken;
		protected boolean cookHasOrder;
		protected boolean orderReady;
		protected boolean customerleaving;
		protected boolean readyForCheck;
		protected boolean customersCheckReady;
		protected double customersTab;
		
		
		MyCustomers(LucaCustomer C, int tableNum) {
			tableNumber = tableNum;
			Customer = C;
			menuTaken=false;
			seated=false;
			orderTaken=false;
			cookHasOrder=false;
			orderReady=false;
			customerleaving=false;
			setReadyForCheck(false);
			customersCheckReady=false;
			customersTab = 0;
			
		}
		public void setCustomersTab(double tab) {
			customersTab= tab;			
		}
		double getCustomersTab(){
			return customersTab;
		}
		public void setCustomersCheckReady(boolean b) {
			customersCheckReady=b;
		}
		boolean isCustomersCheckReady(){
			return customersCheckReady;
		}
		LucaCustomer getCustomer(){
			return Customer;}
		
		int getCustomerTableNumber(){
			return tableNumber;
		}
		void setCustomerSeated(){
			seated=true;
		}
		boolean isCustomerSeated(){
			return seated;
		}
		void setCustomerReadyToOrder(boolean tf){
			readyToOrder=tf;
		}
		boolean isCustomerReadyToOrder(){
			return readyToOrder;
		}
		void setMenuTakenByCustomer(){
			menuTaken= true;
		}
		void setCustomerChoice(String choice2){
			Choice = choice2;
		}
		String getCustomerChoice(){
			return Choice;
		}
		boolean isCustomerOrderTaken() {
			return orderTaken;
		}
		void setCustomerOrderTaken(boolean tf) {
			orderTaken = tf;
		}
		boolean getCookHasOrder() {
			return cookHasOrder;
		}
		void setCookHasOrder(boolean cookHasOrder) {
			this.cookHasOrder = cookHasOrder;
		}
		boolean isCustomerOrderReady(){
		return orderReady;
		}
		void setCustomerOrderReady(boolean tf){
			orderReady= tf;
		}
		public boolean isCustomerleaving() {
			return customerleaving;
		}
		public void setCustomerleaving(boolean customerleaving) {
			this.customerleaving = customerleaving;
		}
		public boolean isReadyForCheck() {
			return readyForCheck;
		}
		public void setReadyForCheck(boolean readyForCheck) {
			this.readyForCheck = readyForCheck;
		}
	}
	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_LUCA_WAITER_ROLE;
	}

	@Override
	public void setCook(GenericCook c) {
		this.cook= (LucaCookRole)c;
		
	}

	@Override
	public void setCashier(GenericCashier c) {
		this.cashier= (LucaCashierRole)c;
	}

	@Override
	public void setHost(GenericHost h) {
		this.host= (LucaHostRole)h;
		
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

