package kushrestaurant;

import kushrestaurant.gui.CustomerGui;
import kushrestaurant.WaiterAgent.CustomerState;
import kushrestaurant.WaiterAgent.Menu;
import kushrestaurant.gui.RestaurantGui;
import kushrestaurant.interfaces.Customer;
import kushrestaurant.interfaces.Waiter;
import agent.Agent;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private int tableNumber=1;
  public String choice= new String();
  public Semaphore wait= new Semaphore(0,true);
   private Menu Menu;
   private int i=0;
	// agent correspondents
   double money=0;
   double check=0;
   private boolean badCustomer=false;
   public boolean orderAgain=false;
	private HostAgent host;
    private Waiter waiter;
    private CashierAgent cashier;
	//    private boolean isHungry = false; //hack for gui
    public enum AgentState
	{DoingNothing, WaitingInRestaurant, WaitingToSeat, Seated, ReadyToOrder, Ordering, Ordered, 
		Served, Eating, DoneEating,WaitingForCheck,ReceivedCheck,MadePayment, Leaving,WashingDishes};

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, doneEating, doneLeaving};
	AgentEvent event = AgentEvent.none;
	public AgentState state = AgentState.DoingNothing;
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name,int id){
		super();
		this.name = name;
		i=id;
		if(this.name.equals("badCustomer"))
		{
			money=4;
			badCustomer=true;
		}
		else if(this.name.equals("hasMinimumMoney"))
		 {money= 5.99;}
		else if(this.name.equals("noMoneyLeaves")){
			money=5;
		}
		else if(this.name.equals("Chicken")){
			money=15;
		}
		else 
			money=new Random().nextInt(20);
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}
   public void setWaiter(Waiter waiter){
	   this.waiter=waiter;
   }
   public void setCashier(CashierAgent cashier){
	   this.cashier=cashier;
   }
   public void setTableNumber(int t)
   {
	   tableNumber=t;
   }
	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry()  {//from animation
		print("I'm hungry and I have " + money + " dollars");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	public void msgwaitRelease(){
		wait.release();
	}
    public void msgWait(){
    	if(this.name.equals("DoesntWait"))
    	{
    		state=AgentState.Leaving;
    		stateChanged();
    	}
    }
	public void msgSitAtTable() {
		print("Received msgSitAtTable");
		state = AgentState.WaitingToSeat;
		event = AgentEvent.followHost;
		stateChanged();
	}
	public void msgTellMeOrder(Menu m)
	{ 
		if(money==5.99 && orderAgain){
			print("Leaving cause I have 5.99 only and they dont have salad");
			state=AgentState.Leaving;
			stateChanged();
			orderAgain=false;
			return;
		}
		state=AgentState.Ordering;
		Menu=m;
		stateChanged();
	}
	public void msgReceivedFood()
	{
		state = AgentState.Eating;
		stateChanged();
	}
	public int getTable(){
		return tableNumber;
	}
	
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	public void msgReceivedCheck(double check){
		this.check=check;
		this.state=AgentState.ReceivedCheck;
		stateChanged();
		
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		//Seating Animation
		if (state == AgentState.WaitingToSeat && event == AgentEvent.followHost ){
			state = AgentState.Seated;
			SitDown();
			return true;
		}
		//if current state is seated then change state to ReadyToOrder
		if(state == AgentState.Seated && event == AgentEvent.seated)
		{
			state = AgentState.ReadyToOrder;
			ReadyToOrder();
			return true;
		}
		
		if(state == AgentState.Ordering)
		{
			state = AgentState.Ordered;
			OrderAction();
			return true;
		}
		
		if(state == AgentState.Eating)
		{
			state = AgentState.DoneEating;
			EatFood();
		}
		if(state == AgentState.DoneEating && event == AgentEvent.doneEating)
		{
			state = AgentState.WaitingForCheck;
			IWantCheck();
		}
		if(state==AgentState.ReceivedCheck){
			state= AgentState.MadePayment;
			MakePayment();
		}
		if (state == AgentState.WashingDishes) 
            {
                washDishesAndLeave();
                state = AgentState.DoingNothing;
                return true;
            }
        

		if(state==AgentState.Leaving){
			leaveTable();
		}
		
		
		return false;
	}
	

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		try {
			wait.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Do("Being seated. Going to table");
		
		customerGui.DoGoToSeat(tableNumber);
		stateChanged();
	}
  private void ReadyToOrder(){
		int decision = (new Random()).nextInt(4)+1;
		final CustomerAgent temp = this;
		timer.schedule(new TimerTask() {
			public void run() 
			{
				print("I'm ready to order");
				waiter.msgReadytoOrder(temp);//msg to waiter
				stateChanged();
			}
		},
		decision*1000);
		
	}
  private void OrderAction()
  {    //int i=new Random().nextInt(20); 
		//Randomly Choose a Food
	  if(Menu.menu.size()>=1){
		if(money==5.99){
			choice = "Salad";
		}
		else{
			if(this.name.equals("Chicken") && !orderAgain)
			{
				choice= "Chicken";
				waiter.msgHereisChoice(this, choice);
				customerGui.orderedFood();
				
				stateChanged();
				return;
				
			}
			else 
		    {if (i%2==0){choice = "Pizza";}
		      else {choice="Steak";}
		    
		}
		print("I want " + choice);
		if(money>=Menu.getPrice(choice) && !badCustomer){
		waiter.msgHereisChoice(this, choice);
		customerGui.orderedFood();
		stateChanged();}
		else if(badCustomer)
		{
			print("Dont have enough money, still ordering the food as I am a bad customer");
			waiter.msgHereisChoice(this, choice);
			customerGui.orderedFood();
			stateChanged();
		}
		else{
			print("But dont have enough money so leaving");
			//isHungry=false;
		    state=AgentState.Leaving;
		    stateChanged();
			//leaveTable();
		}
		
	  }}
	  else leaveTable();
	}
	private void EatFood() {
		Do("Eating Food");
		customerGui.eatingFood(choice);
//ated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating");
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		hungerLevel*1000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	// (4) Customer Leaves Restaurant, Sends M
	public void IWantCheck(){
		print("I want check");
		waiter.msgIWantCheck(this);
	}
 private void MakePayment(){
	 print("Making Payment");
	 cashier.msgHereIsPayment(this,check,money);
 }
 private void washDishesAndLeave() {

     print("Washing dishes");
     //final CustomerAgent thisObject = this;

     timer.schedule(new TimerTask() {
         public void run() {
         
         print("Leaving the restaurant after washing dishes");

         leaveTable();
         }},
         5000);//how long to wait before running task
     
     

     

     stateChanged();
 }
 public void msgHereIsYourChange(double change){
	 state=AgentState.Leaving;
	 stateChanged();
 }
 public void msgWashDishes() {
     state=AgentState.WashingDishes;
     stateChanged();

 }
  public void setOrderAgain(){
	  orderAgain=true;
  }
	private void leaveTable() 
		{
			Do("Leaving.");
			
			if(!name.equals("DoesntWait"))
					{waiter.msgDoneEating(this);
			customerGui.DoExitRestaurant();}
			state = AgentState.DoingNothing;
		}
	

	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	

	
}

