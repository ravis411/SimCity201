package restaurant.luca;

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


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A custome8r and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LucaWaiterRole extends Role implements LucaWaiter{
	public List<MyCustomers> myCustomers
	= Collections.synchronizedList(new ArrayList<MyCustomers>());
	private String name;
	private Semaphore atTable = new Semaphore(0,false);
	private Semaphore atCook = new Semaphore(0,false);
	private Semaphore waitForOrder = new Semaphore(0,false);
	private boolean WaiterAvailable;
	private LucaCashierRole cashier;
	private LucaCookRole cook;
	private LucaHostRole host;
	private WaiterGui waiterGui;
	private RestaurantGui restaurantGui;
	private boolean onBreak;
	private boolean requestBreak;
	private boolean tellHostIamBackFromBreak;
	public static HashMap<String, Integer> menu;
	public static Map<String, Integer> menuUnmodifiable;
	public EventLog log= new EventLog();

	
	public LucaWaiterRole(String s){
		super();
		name=s;
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
		return name;
	}
	public void tellHostIExist(){
		host.msgIAmAWaiter(this);
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

	public void msgWaiterHereIsCheck(int tab, LucaCustomer customer) {
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


	private void askHostForBreak() throws ExecutionException {
		print("Can I , " + name + ", go on Break?");
		host.msgHostCanIGoOnBreak(this);//passes the number of customers the waiter has so the host can deny his request if he currently has customers
		requestBreak =false;
	}
	private void tellHostReadyToWork() throws ExecutionException{
		print(" I , " + name + ", am back from Break");
		tellHostIamBackFromBreak= false;
		host.msgHostBackFromBreak(this);
	}
	private void seatCustomer(MyCustomers customer) throws ExecutionException{
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
	
	private void askForMyCustomerOrder(MyCustomers customer)throws ExecutionException{
		
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
	
	private void giveCookOrder(MyCustomers customer)throws ExecutionException{
		DoGiveCookOrder(customer);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.msgCookHeresAnOrder(customer.getCustomerTableNumber(), customer.getCustomerChoice(), this);
		print(customer.getCustomer() + "'s Order given to cook");
		waiterGui.showOrderInAnimation("", "");
		customer.setCookHasOrder(true);
		waiterGui.DoLeave();
	}
	
	private void giveCustomerOrder(MyCustomers customer)throws ExecutionException{
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
	private void tellCashierToComputeBill() throws ExecutionException{
		for (int i=0; i<myCustomers.size(); i++){
			if (myCustomers.get(i).isReadyForCheck()){
				cashier.msgCashierComputeBill(myCustomers.get(i).getCustomerChoice(), myCustomers.get(i).getCustomer(), this);
				myCustomers.get(i).setReadyForCheck(false);
			}
				
		}
	}
	
	private void giveCustomerCheck() throws ExecutionException{
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
	private void tellHostTableFree(MyCustomers customer) throws ExecutionException{
		host.msgHostTableFree(customer.getCustomerTableNumber());
		customer.setCustomerleaving(false);
		myCustomers.remove(customer);
		
	}
	
	// The animation DoXYZ() routines
	private void DoSeatCustomer(MyCustomers customer) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer.getCustomer() + " at table" + customer.getCustomerTableNumber());
		waiterGui.DoGoToTable(customer.getCustomerTableNumber()); 

	}
	private void DoTakeOrder(MyCustomers customer) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Taking " + customer.getCustomer() + "'s order at table" +  customer.getCustomerTableNumber());
		waiterGui.DoGoToTable( customer.getCustomerTableNumber()); 
	}

	private void DoGiveCookOrder(MyCustomers customer) {
		print("Bringing " + customer.getCustomer() + "'s order to cook from table" +  customer.getCustomerTableNumber());
		waiterGui.DoGoToCook(); 
	}
	private void doGetOrderFromCook(MyCustomers customer) {
		print("Getting " + customer.getCustomer() + "'s order from cook");
		waiterGui.DoGoToPlatingArea(); 
	}
	private void doBringOrderToCustomer(MyCustomers customer) {
		print("Bringing " + customer.getCustomer() + "'s cooked order to customer at table" +  customer.getCustomerTableNumber());
		waiterGui.DoGoToTable( customer.getCustomerTableNumber());
	}
	private void doBringCheckToCustomer(MyCustomers customer) {
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
	
	public void setCook(LucaCookRole Cook)
	{
		this.cook=Cook;
	}
	public void setHost(LucaHostRole Host)
	{
		this.host=Host;
	}
	public void setCashier(LucaCashierRole cashier)
	{
		this.cashier=cashier;
	}
	private class MyCustomers {
		LucaCustomer Customer;
		int tableNumber;
		String Choice;
		boolean menuTaken;
		boolean seated;
		boolean readyToOrder;
		private boolean orderTaken;
		private boolean cookHasOrder;
		private boolean orderReady;
		private boolean customerleaving;
		private boolean readyForCheck;
		private boolean customersCheckReady;
		private int customersTab;
		
		
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
		public void setCustomersTab(int tab) {
			customersTab= tab;			
		}
		int getCustomersTab(){
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
		// TODO Auto-generated method stub
		return null;
	}




}

