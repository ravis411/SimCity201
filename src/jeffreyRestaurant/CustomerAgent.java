package jeffreyRestaurant;

import Person.Role.Role;
import agent.Agent;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import jeffreyRestaurant.Gui.CustomerGui;
import jeffreyRestaurant.Gui.RestaurantGui;
import jeffreyRestaurant.interfaces.Customer;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends GenericCustomer implements Customer{
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private int tableNum;

	// agent correspondents
	private HostAgent host;
	private WaiterAgent waiter;
	private CashierAgent cashier;

	//    private boolean isHungry = false; //hack for gui
	
	private Semaphore inProgress = new Semaphore(0, true);
	private jeffreyRestaurant.WaiterAgent.menu menu;
	private String choice;
	private boolean debug;
	private Double tab = 0.0;
	private Double money = 100.0;
	
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, ReadyToOrder, Ordering, Ordered, Eating, DoneEating, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, askedToOrder, orderRejected, served, doneEating, receivedCheck, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(){
		super();
		tableNum = 1;
		debug = false;
		Random rand = new Random();
		money = (double)rand.nextInt(20);
		//print("I have $" + money);
		//Debug cases
//		if (this.name.equals("Pizza")) {
//			print("Debug case");
//			choice = "Pizza";
//			money = 10.00;
//			debug = true;
//		} else if (this.name.equals("Salad")) {
//			print("Debug case");
//			choice = "Salad";
//			money = 6.00;
//			debug = true;
//		} else if (this.name.equals("Steak")) {
//			print("Debug case");
//			choice = "Steak";
//			money = 16.00;
//			debug = true;
//		} else if (this.name.equals("Chicken")) {
//			print("Debug case");
//			choice = "Chicken";
//			money = 11.00;
//			debug = true;
//		} else if (this.name.equals("Leaver")) {
//			print("Debug case");
//			debug = true;
//		} else if (this.name.equals("Flake")) {
//			print("Debug case");
//			choice = "Chicken";
//			money = 4.00;
//			debug = true;
//		}
	}
	public void setWaiter(WaiterAgent waiter) {
		this.waiter = waiter;
	}
	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		if (money <= 0.00) {
			money += 50.00;
			print("Received more money from dad. I now have " + money);
		}
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	public void msgWeAreFull() {
		if (this.getName().equals("Leaver") && debug) {
			print("Restaurant Full, I'm Leaving");
			event = AgentEvent.donePaying;
			state = AgentState.Leaving;
			stateChanged();
		}
		else {
			
		}
	}
	//FollowMe()
	public void msgSitAtTable(int number, jeffreyRestaurant.WaiterAgent.menu menu) {
		this.menu = menu;
		print("Received msgSitAtTable");
		event = AgentEvent.followHost;
		tableNum = number;
		if (!debug) {
			this.choice = menu.randomFood();
		}
		stateChanged();
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
	
	public void msgWhatIsOrder() {
		print("Asked to order");
		event = AgentEvent.askedToOrder;
		stateChanged();
	}
	public void msgHereIsFood() {
		event = AgentEvent.served;
		stateChanged();
	}
	public void msgOrderAgain(jeffreyRestaurant.WaiterAgent.menu menu) {
		print("Told to order again");
		choice = menu.randomFood();
		state = AgentState.ReadyToOrder;
		event = AgentEvent.orderRejected;
		stateChanged();
	}
	public void msgHereIsCheck(Double tab) {
		//When customer pays, he must tell waiter
		print("Received check from waiter");
		this.tab = tab;
		event = AgentEvent.receivedCheck;
		stateChanged();
	}
	public void msgDoneAnimation() {
		//From GUI
		inProgress.release();
	}
	public void msgFreeToGo() {
		event = AgentEvent.donePaying;
		stateChanged();
	}
	public void msgPayNextTime() {
		event = AgentEvent.donePaying;
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		//Need to change this to sync up with new event and state
		//May be unnecessary 
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.ReadyToOrder;
			callWaiter(waiter);
			return true;
		}
		if (state == AgentState.ReadyToOrder && event == AgentEvent.askedToOrder) {
			state = AgentState.Ordered;
			giveOrder(choice);
			return true;
		}
		if (state == AgentState.ReadyToOrder && event == AgentEvent.orderRejected) {
			callWaiter(waiter);
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.served) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Leaving;
			goPay();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.receivedCheck) {
			payCheck();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.donePaying) {
			print("I'm done. Leaving");
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		//Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		//Do("Being seated. Going to table");
		customerGui.DoGoToSeat(tableNum);
	}
	
	private void callWaiter(WaiterAgent w) {
		//Do("Calling the waiter over");
		w.msgReadyToOrder(this);
		//choice = menu.randomFood();
	}
	
	private void giveOrder(String choice) {
		print("Giving waiter choice of " + choice);
		waiter.msgHereIsChoice(choice, this);
		//hack. need to implement menu and foods. 
	}
	
	private void EatFood() {
		//Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
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
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		10000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	private void goPay() {
		//Do("Paying");
		waiter.msgDone(this);
	}
	private void payCheck() {
		//Do("Going to pay " + tab + " for my " + choice);
		customerGui.DoGoPay();
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Double amountPayed = 0.0;
		if (this.money < this.tab) {
			print("I don't have enough money");
			amountPayed = money;
		}
		else if (this.money >= this.tab){
			print("I have enough money");
			amountPayed = tab;
		}
		cashier.msgCustomerPayment(this, amountPayed);
		this.money -= amountPayed;
		event = AgentEvent.donePaying;
		//print("Done paying");
		//stateChanged();
	}
	private void leaveTable() {
		//Do("Leaving.");
		waiter.msgLeavingTable(this);
		event = AgentEvent.doneLeaving;
		//stateChanged();
		customerGui.DoExitRestaurant();
		//kill();
	}

	// Accessors, etc.

	public String getName() {
		return myPerson.getName();
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

	public void setGui(jeffreyRestaurant.Gui.CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
	@Override
	public boolean canGoGetFood() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_JEFFREY_CUSTOMER_ROLE;
	}
	@Override
	public void setCashier(GenericCashier c) {
		this.cashier = (CashierAgent) c;
		
	}
	@Override
	public void setHost(GenericHost h) {
		this.host = (HostAgent) h;
		
	}
}

