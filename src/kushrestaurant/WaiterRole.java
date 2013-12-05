package kushrestaurant;

import Person.Role.Role;
import agent.Agent;
import kushrestaurant.interfaces.*;

import java.util.Timer;
import java.util.TimerTask;


//import kushrestaurant.gui.RestaurantGui;
import kushrestaurant.gui.WaiterGui;
import kushrestaurant.interfaces.Customer;
import kushrestaurant.HostRole.Table;
import kushrestaurant.CookRole.Order;
import kushrestaurant.CookRole.orderstate;
import kushrestaurant.CookRole.state;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Random;


public class WaiterRole extends Role implements Waiter{
	
	public List<MyCustomer> customers=new ArrayList<MyCustomer>();
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
    private HostRole host;
    private Cook cook;
    private Cashier cashier;
    private String notAvailable;
    private Timer timer= new Timer();
  //  private RestaurantGui gui;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atDefault= new Semaphore(0,true);
	private Semaphore atCook= new Semaphore(0,true);
	private Semaphore atPlate= new Semaphore(0,true);
	private Semaphore atCashier= new Semaphore(0,true);
	private Semaphore atWait= new Semaphore(0,true);
	private Semaphore atTable2 = new Semaphore(0,true);
	private Semaphore atTable3 = new Semaphore(0,true);
	//private boolean test=false;
	 
	public enum CustomerState{Waiting, Seated, ReadyToOrder,RestFull,ReadyToOrder2, Ordering, 
		Ordered, FoodCooking, orderDone, Served,WaitingForCheck,ReceivedCheck, Leaving, Left}
	public enum WaiterEvent{none,seatingCustomer,AskingForCheck,AskedForCheck,GotCheck, goingToCustomer,givingCheckToCustomer,notOnBreak, onBreak,goingToCook, gettingFood, givingFood}
	public WaiterEvent event = WaiterEvent.none;
	public WaiterEvent breakevent= WaiterEvent.notOnBreak;
	private WaiterGui waiterGui= null;
	public boolean onBreak= false;
	public WaiterRole(String name, HostRole h,CookRole c) {
		super();
        host=h;
		this.name = name;
		cook=c;
	//	this.gui=g;
		
		// make some tables
		//tables = new ArrayList<Table>(NTABLES);
		//for (int ix = 1; ix <= NTABLES; ix++) {
			//tables.add(new Table(ix));//how you add to a collections
		
	}
	public WaiterRole(String name){
		super();
		this.name=name;
	}
	public WaiterRole(){
		super();
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
public void setCashier(CashierRole cashier){
	this.cashier=cashier;
}
	public List getCustomers() {
		return customers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages

	
   public boolean isAtBreak(){
	   return onBreak;
   }
  
	public void msgLeavingTable(Customer cust) {
		for (MyCustomer C : customers) {
			if (C.getCustomer() == cust){
				print(cust.getName() + " leaving " );
				C.cstate= CustomerState.Leaving;
				
				//table.setUnoccupied();
				//takingorder=false;
				stateChanged();
			}
		}
	}
    public void setHost(HostRole host){
    	this.host=host;
    }
    public void setCook(Cook cook){
    	this.cook =cook;
    }
	public void msgAtCook(){
		atCook.release();
	}
   public void msgAtPlate(){
	   atPlate.release();
   }
   
   public void msgAtCashier(){
	   
	   atCashier.release();
   }
   public void msgAtWait(){
	   atWait.release();
   }
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		// (1) Seats Waiting Customers
		       if(onBreak){
		    	   return true;
		       }
		       
		      /* if(!host.areAllTablesOccupied()){
		    	   for(MyCustomer customer:customers)
		    	   {if(customer.cstate==CustomerState.RestFull){
		    		   customer.cstate=CustomerState.Waiting;
		    		   //print("TEST");
		    		   event = WaiterEvent.seatingCustomer;
						FollowMe(customer);//the action
						customer.cstate = CustomerState.Seated;
		    		   return true;
		    	   }
		    		   
		    	   }
		       }*/
				for (MyCustomer customer : customers) 
				{
					if ( customer.cstate == CustomerState.Waiting && waiterGui.returnAtDefault()  ) 
					{   //print("TEST2");
						event = WaiterEvent.seatingCustomer;
						FollowMe(customer);//the action
						customer.cstate = CustomerState.Seated;
						
						return true;
					}
				}
				 
				// (2) Gets Order from Customer
				for (MyCustomer customer : customers)
				{
					//if there exists a customer whose state is readyToOrder
					//then take the customer's order
					if( customer.cstate == CustomerState.ReadyToOrder)
					{   
						event = WaiterEvent.goingToCustomer;
						customer.cstate = CustomerState.Ordering;
						WhatsYourChoice(customer);
						return true;
					}
				}
				for (MyCustomer customer : customers)
				{
					//if there exists a customer whose state is readyToOrder
					//then take the customer's order
					if ( customer.cstate == CustomerState.ReadyToOrder2)
					{   
						event = WaiterEvent.goingToCustomer;
						customer.cstate = CustomerState.Ordering;
						WhatsYourChoice2(customer,notAvailable);
						return true;
					}
				}
				
				// (3) Gives Order to Cook
				for (MyCustomer customer : customers)
				{
					//if there exists a customer whose state is ordered
					//give the order to the cook
					if( customer.cstate == CustomerState.Ordered)
					{
						customer.cstate = CustomerState.FoodCooking;
						HereIsOrder(customer);
						return true;
					}
				}
				
				
				
				// (4) Gives Order to Customer
				if(event == WaiterEvent.givingFood)
				{
					for (MyCustomer customer : customers)
					{
						//if there exists a customer whose state is ordered
						//give the order to the cook
						if( customer.cstate == CustomerState.FoodCooking)
						{
							customer.cstate = CustomerState.Served;
							HereIsTheFood(customer);
							return true;
						}
					}
				}
				if(event==WaiterEvent.AskingForCheck){
					//print("TEST");
					for (MyCustomer customer : customers)
					{
						
						//if there exists a customer whose state is ordered
						//give the order to the cook
						if( customer.cstate == CustomerState.WaitingForCheck)
						{
							
							ProduceCheck(customer.c,customer.choice);
							return true;
						}
					}
				}
				
				for (MyCustomer customer : customers)
				{
					//if there exists a customer whose state is ordered
					//give the order to the cook
					if( customer.cstate == CustomerState.WaitingForCheck && event==WaiterEvent.givingCheckToCustomer)
					{
						customer.cstate= CustomerState.ReceivedCheck;
						HereIsYourCheck(customer.c,customer.amount);
						return true;
					}
				}
				// (5) Frees Table when customer leaves
				for (MyCustomer customer : customers)
				{
					//if there exists a customer whose state is leaving
					//then send message to host about free table
					if( customer.cstate == CustomerState.Leaving)
					{
						customer.cstate = CustomerState.Left;
						
						
						host.msgTableAvailable(customer.c);
						return true;
					}
				}
				
				
				/*
				// (6) Doesn't Worry about Customers that left
						for (MyCustomer customer : customers)
						{
							//if there exists a customer whose state is leaving
							//then send message to host about free table
							if( customer.state == CustomerState.Left)
							{
								customers.remove(customer);
							}
						}
				*/
				return false;
			}
	

	// Actions
    
    public void msgSeatCustomer(Customer customer, Table table)
	{
		print("Received msg SeatCustomer");
		/*if(host.areAllTablesOccupied()){
			print("you have to wait");
			customer.msgWait();
			if(!customer.getName().equals("DoesntWait")){
			customers.add(new MyCustomer(customer,table));}
			stateChanged();
			return;
		}*/
		//table.setOccupant(customer);
		//else 
		//{
			//print("TEST");
			customers.add(new MyCustomer(customer, table));
			stateChanged();
	}
	
	//utilities
    public void ProduceCheck(Customer c,String Choice){
    	print("Cashier produce check for "+c.getName());
    	waiterGui.goToCashier();
    	try {atCashier.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
    	cashier.msgProduceCheck(c,Choice,this);
    	event=WaiterEvent.AskedForCheck;
    }
   public void msgReadytoOrder(Customer c)
	{
		for(MyCustomer customer: customers)
		{
			if(customer.c == c)
			{
				customer.cstate = CustomerState.ReadyToOrder; 
			}
		}
		stateChanged();
	}
   public void msgHereisChoice(Customer c, String choice)
	{
		for(MyCustomer customer: customers)
		{
			if(customer.c == c)
			{
				customer.choice = choice;
				
				customer.cstate = CustomerState.Ordered;
				break;
			}
		}
		stateChanged();
	}
   public void msgGetFoodFromCook()
	{
		event = WaiterEvent.givingFood;
		/*
		for(MyCustomer c : customers)
		{
			if( c.c == o.customer)
			{
				c.state = CustomerState.orderDone;
				break;
			}
		}
		*/
		stateChanged();
	}
   public void msgDoneEating(Customer c)
	{
		for(MyCustomer customer : customers)
		{
			if( customer.c == c )
			{
				customer.cstate = CustomerState.Leaving;
				break;
			}
		}
		stateChanged();
	}
   
   public void msgDontHaveThis(String food,Customer c){
	   for(MyCustomer customer : customers)
		{
			if( customer.c == c )
			{
				customer.cstate = CustomerState.ReadyToOrder2;
				notAvailable=food;
				break;
			}
		}
		stateChanged();
	   
	   
   }
   public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		
		atTable.release();// = true;
		
		stateChanged();
	}
   public void msgAtTable2() {//from animation
 		//print("msgAtTable() called");
 		
 		atTable2.release();// = true;
 		
 		stateChanged();
 	}
   public void msgAtTable3() {//from animation
 		//print("msgAtTable() called");
 		
 		atTable3.release();// = true;
 		
 		stateChanged();
 	}
   public void msgAtDefault(){
	   atDefault.release();
	   stateChanged();
   }
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
//Actions
	public void FollowMe(MyCustomer customer)
	{
		waiterGui.goToWaitingArea();
		try {atWait.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
		print("Seating Customer " + customer.c.getName());
		customer.c.msgwaitRelease();
		customer.c.msgSitAtTable();
		customer.table.setOccupant(customer.c);
		seatCustomerAnimation(customer.c, customer.table.tableNumber);
		//Waits for semaphore
		if(customer.table.tableNumber==1){
		try {atTable.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
		waiterGui.DoLeaveCustomer();
		stateChanged();}
		if(customer.table.tableNumber==2){
			try {atTable2.acquire();} 
			catch (InterruptedException e) { e.printStackTrace();}
			waiterGui.DoLeaveCustomer();
			stateChanged();}
		if(customer.table.tableNumber==3){
			try {atTable3.acquire();} 
			catch (InterruptedException e) { e.printStackTrace();}
			waiterGui.DoLeaveCustomer();
			stateChanged();}
	}
	
	public void WhatsYourChoice(MyCustomer c)
	{
		goToCustomerAnimation(c.table.tableNumber);
		//Waits for semaphore
		if(c.table.tableNumber==1){
		try {atTable.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
		print("taking " + c.c.getName() +"'s order" );
		c.c.msgTellMeOrder(new Menu());
		
		waiterGui.DoLeaveCustomer();
		stateChanged();}
		if(c.table.tableNumber==2){
			try {atTable2.acquire();} 
			catch (InterruptedException e) { e.printStackTrace();}
			print("taking " + c.c.getName() +"'s order" );
			c.c.msgTellMeOrder(new Menu());
			
			waiterGui.DoLeaveCustomer();
			stateChanged();}
		if(c.table.tableNumber==3){
			try {atTable3.acquire();} 
			catch (InterruptedException e) { e.printStackTrace();}
			print("taking " + c.c.getName() +"'s order" );
			c.c.msgTellMeOrder(new Menu());
			
			waiterGui.DoLeaveCustomer();
			stateChanged();}
	}
	public void changeBreakEvent(){
		breakevent=WaiterEvent.notOnBreak;
	}
	public void changeBreakEvent2(){
		breakevent=WaiterEvent.onBreak;
	}
	public void WhatsYourChoice2(MyCustomer c,String notavailable)
	{
		c.c.setOrderAgain();;
		goToCustomerAnimation(c.table.tableNumber);
		//Waits for semaphore
		if(c.table.tableNumber==1){
		try {atTable.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
		print("taking " + c.c.getName() +"'s order" );
		c.c.msgTellMeOrder(new Menu(notavailable));
		
		waiterGui.DoLeaveCustomer();
		stateChanged();}
		if(c.table.tableNumber==2){
			try {atTable2.acquire();} 
			catch (InterruptedException e) { e.printStackTrace();}
			print("taking " + c.c.getName() +"'s order" );
			c.c.msgTellMeOrder(new Menu(notavailable));
			
			waiterGui.DoLeaveCustomer();
			stateChanged();}
		if(c.table.tableNumber==3){
			try {atTable3.acquire();} 
			catch (InterruptedException e) { e.printStackTrace();}
			print("taking " + c.c.getName() +"'s order" );
			c.c.msgTellMeOrder(new Menu(notavailable));
			
			waiterGui.DoLeaveCustomer();
			stateChanged();}
	}
	public void WantToGoOnBreak(){
		print("I want to go on a break");
		host.msgAskForBreak(this);
	};
	public void msgYouCantGoOnBreak(){
		onBreak=false;
	}
	public void msgGoOnBreak(){
		print("Going on a break");
		onBreak=true;
		OnBreak();
	}
	public void msgIWantCheck(Customer c){
		for(MyCustomer customer : customers)
		{
			if( customer.c == c )
			{
				//print("TEST");
				customer.cstate=CustomerState.WaitingForCheck;
				event= WaiterEvent.AskingForCheck;
				
				break;
			}
			
		}
		stateChanged();
	}
	public void HereIsYourCheck(Customer c,double check){
		
		waiterGui.goToTable(c.getTable());
		try {atTable.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
		print(c.getName()+" here is your check");
		c.msgReceivedCheck(check);
		waiterGui.DoLeaveCustomer();
		try {atDefault.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
		
		
		stateChanged();
		
	}
	public void msgHereIsCheck(Customer c,double check){
		for(MyCustomer customer : customers)
		{
			if( customer.c == c )
			{
				customer.amount=check;
				
				event=WaiterEvent.givingCheckToCustomer;
				//customer.cstate=CustomerState.ReceivedCheck;
				break;
			}
			
		}
		stateChanged();
	}
	public void OnBreak(){
		timer.schedule(new TimerTask() {
			
			public void run() {
		    print("Back from break");
			sendBreakDoneMsgToHost();
			
			onBreak=false;
				
			}
		},
		15000);
		
	}
	public void sendBreakDoneMsgToHost(){
		host.msgBreakDone(this);
	}
	public void AnimationLeaveCustomer(){
		
		waiterGui.DoLeaveCustomer();
		stateChanged();
	}
	public void HereIsOrder(MyCustomer c)
	{
		print("giving order to cook");
		
		//animation details
		waiterGui.goToCook();
		try {atCook.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
		
		//sends msg to cook
		cook.MsgHereisTheOrder(this, c.c, c.table, c.choice);
		stateChanged();
	}
	public void HereIsTheFood(MyCustomer c)
	{
		print("Getting Table " + c.table.tableNumber +" order");
		waiterGui.goToPlatingArea();
		try {atPlate.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
		cook.takenFood(c.choice);
		print("giving food to table: " + c.table.tableNumber);
		goToCustomerAnimation(c.table.tableNumber);
		//Waits for semaphore
		if(c.table.tableNumber==1){
		try {atTable.acquire();} 
		catch (InterruptedException e) { e.printStackTrace();}
	
		c.c.msgReceivedFood();
		waiterGui.DoLeaveCustomer();
		stateChanged();}
		if(c.table.tableNumber==2){
			try {atTable2.acquire();} 
			catch (InterruptedException e) { e.printStackTrace();}
		
			c.c.msgReceivedFood();
			waiterGui.DoLeaveCustomer();
			stateChanged();}
		if(c.table.tableNumber==3){
			try {atTable3.acquire();} 
			catch (InterruptedException e) { e.printStackTrace();}
		
			c.c.msgReceivedFood();
			waiterGui.DoLeaveCustomer();
			stateChanged();}
	}
	private void seatCustomerAnimation(Customer c, int tableNumber)
	{
		waiterGui.DoBringToTable(c,tableNumber);
	}
	private void goToCustomerAnimation(int tableNumber)
	{
		//print("GOING TO TABLE");
		waiterGui.goToTable(tableNumber);
	}
	public class MyCustomer{
		Customer c;
		Table table;
		//Order order;
		CustomerState cstate;
		String choice;
		double amount;
		MyCustomer(Customer customer, Table t)
		{ c=customer;
		  table=t;
		  cstate = CustomerState.Waiting;
		  amount=0;
		}
		MyCustomer(CustomerRole cust, Table t,int g)
		{ c=cust;
		  table=t;
		  cstate = CustomerState.RestFull;
		  amount=0;
		}
		private Menu menu;
		public Customer getCustomer()
		{return c;}
		public Table getTable()
		{return table;}
	}
	public class Menu{
		public double getPrice(String choice){
			switch(choice){
			case "Chicken": return 10.99;
			case "Steak": return 15.99;
			case "Salad" : return 5.99;
			case "Pizza" : return 8.99;
			default: return 1;
			}
		}
			public ArrayList<String> menu= new ArrayList<String>();
		Menu(){
			menu.add("Chicken");
			menu.add("Steak");
			menu.add("Salad");
			menu.add("Pizza");
		}
		Menu(String notavailable){
			menu.add("Chicken");
			menu.add("Steak");
			menu.add("Salad");
			menu.add("Pizza");
			menu.remove(notavailable);
		}
		public String createChoice(){
			return menu.get(new Random().nextInt(menu.size()));
		}
		public void removeItem(String choice){
			menu.remove(choice);
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
		return "WaiterRole";
	}
	
	
	
	
}


