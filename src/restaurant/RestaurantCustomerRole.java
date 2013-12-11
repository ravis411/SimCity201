package restaurant;

import interfaces.Cashier;
import interfaces.Customer;
import interfaces.Waiter;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.gui.CustomerGui;
import trace.AlertLog;
import trace.AlertTag;
import Person.PersonAgent;
import Person.Role.Role;

/**
 * Restaurant customer agent.
 */
public class RestaurantCustomerRole extends GenericCustomer implements Customer {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	public CustomerGui customerGui;
	private int tableNum = -1;
	private int tableX = -1;
	private int tableY = -1;
	private int mealChoice = -1;
	private Menu menu;
	private double amountSpent;
	DecimalFormat moneyForm = new DecimalFormat("#.##");
	int stayLeave = -1;
	private int waitingLocX = -1;
	private int waitingLocY = -1;

	// agent correspondents
	private HostRole host;
	private Waiter waiter;
	private Cashier cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, WaitingToOrder, WaitingForOrder, Ordering, Eating, DoneEating, Paying, Leaving, LeavingEarly};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, readyToOrder, order, reorder, ordered, startEating, doneEating, goingToCashier, atRegister, gaveCashierMoney, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public RestaurantCustomerRole(){
		super();

        //money = Double.valueOf(moneyForm.format(20.00));
	}
	
	public void setPerson(PersonAgent person) {
		super.setPerson(person);
	}
	
	public void setHost(GenericHost host) {
		this.host = (HostRole) host;
	}
	
	public void setWaiter(GenericWaiter waiter2) {
		this.waiter = (Waiter) waiter2;
	}
	
	public void setCashier(GenericCashier cashier) {
		this.cashier = (CashierRole) cashier;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgRestaurantFull() {
		//Random randNum = new Random();
		//stayLeave = randNum.nextInt(2);
		stateChanged();
	}

	public void msgSitAtTable(Menu m) {
		event = AgentEvent.followWaiter;
		menu = m;
		stateChanged();
	}
	
	public void msgWaiterReadyToTakeOrder() {
		event = AgentEvent.order;
		stateChanged();
	}
	
	public void msgWaiterReadyToRetakeOrder() {
		event = AgentEvent.reorder;
		stateChanged();
	}
	
	public void msgOrderOnItsWay() {
		event = AgentEvent.ordered;
		stateChanged();
	}
	
	public void msgFoodAtTable() {
		event = AgentEvent.startEating;
		stateChanged();
	}
	
	public void msgCheckAtTable(double amount) {
		amountSpent = amount;
		state = AgentState.Paying;
		stateChanged();
	}
	
	public void msgCheckPayed() {
		myPerson.setMoney(myPerson.getMoney() - amountSpent);
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Spent $" + amountSpent + ". I have $" + myPerson.getMoney() + " left.");
		event = AgentEvent.donePaying;
		stateChanged();
	}
	
	public void msgCheckNotPayed() {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "I'll pay in full next time. Sorry!");
		event = AgentEvent.donePaying;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedPaying() {
		//from animation
		event = AgentEvent.atRegister;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		if (stayLeave == 1) {
			LeaveBeforeSeated();
		}
		
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Seated;
			LookAtMenu();
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.readyToOrder){
			state = AgentState.WaitingToOrder;
			ReadyToOrder();
			return true;
		}
		if (state == AgentState.WaitingToOrder && event == AgentEvent.order){
			state = AgentState.WaitingForOrder;
			Order();
			return true;
		}
		if (state == AgentState.WaitingForOrder && event == AgentEvent.reorder) {
			Reorder();
			return true;
		}
		if (state == AgentState.WaitingForOrder && event == AgentEvent.startEating){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.doneEating){
			GoPayCheck();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.atRegister){
			PayCheck();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.donePaying){
			LeaveRestaurant();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			kill();
			return true;
		}
		if (state == AgentState.LeavingEarly && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			kill();
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		//Do("Waiting for host");
		//if (host.atFrontDesk == true) {
			AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Being seated. Going to table");
			customerGui.DoGoToSeat(tableNum);
		//}
	}
	
	private void LookAtMenu() {
		if(myPerson.getMoney() >= 5.99) {
			AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Looking at menu");
			timer.schedule(new TimerTask() {
				public void run() {
					AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Done looking at menu");
					event = AgentEvent.readyToOrder;
					stateChanged();
				}
			},
			5000);
		}
		else {
			AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "I can't afford anything on this menu!");
			LeaveTableEarly();
		}
	}
	
	private void ReadyToOrder() {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Ready to order");
		waiter.msgGoTakeOrder();
	}
	
	private void Order() {
		Random randNum = new Random();
		mealChoice = randNum.nextInt(2);
		//mealChoice = 0; //hack to test food inventory
		waiter.msgTakeOrder(this, mealChoice);
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "I want the " + menu.getDishName(mealChoice));
	}
	
	private void Reorder() {
		int oldChoice = mealChoice;
		while (oldChoice == mealChoice) {
			Random randNum = new Random();
			mealChoice = randNum.nextInt(3);
		}
		event = AgentEvent.order;
		waiter.msgTakeOrder(this, mealChoice);
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "I want the " + menu.getDishName(mealChoice));
	}

	private void EatFood() {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Done eating " + menu.getDishName(mealChoice));
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		4000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void GoPayCheck() {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Leaving table.");
		waiter.msgLeavingTable(this);
		customerGui.DoGoPay();
		event = AgentEvent.goingToCashier;
	}
	
	private void PayCheck() {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Paying check.");
		cashier.msgPayingCheck(this, amountSpent);
		event = AgentEvent.gaveCashierMoney;
	}

	private void LeaveTableEarly() {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Leaving.");
		customerGui.DoExitRestaurant();
		waiter.msgLeavingTable(this);
		state = AgentState.LeavingEarly;
		deactivate();
	}
	
	private void LeaveRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.DYLANS_RESTAURANT, myPerson.getName(), "Leaving.");
		customerGui.DoExitRestaurant();
		state = AgentState.Leaving;
		deactivate();
	}
	
	private void LeaveBeforeSeated() {
		customerGui.DoExitRestaurant();
		state = AgentState.Leaving;
		host.msgRemoveFromWaitlist(this);
		stayLeave = -1;
		deactivate();
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
		return "customer " + getNameOfRole();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
	
	public HostRole getHost() {
		return host;
	}
	
	public int getTableNum() {
		return tableNum;
	}
	
	public void setTableNum(int t) {
		tableNum = t;
	}
	
	public int getTableX() {
		return tableX;
	}
	
	public int getTableY() {
		return tableY;
	}
	
	public void setTableX(int x) {
		tableX = x;
	}
	
	public void setTableY(int y) {
		tableY = y;
	}
	
	public AgentState getState() {
		return state;
	}
	
	public AgentEvent getEvent() {
		return event;
	}
	
	public int getWaitingLocX() {
		return waitingLocX;
	}
	
	public int getWaitingLocY() {
		return waitingLocY;
	}
	
	public void setWaitingLocX(int x) {
		waitingLocX = x;
		customerGui.waitingAreaPositionX(x);
	}
	
	public void setWaitingLocY(int y) {
		waitingLocY = y;
		customerGui.waitingAreaPositionY(y);
	}

	@Override
	public boolean canGoGetFood() {
		return false;
	}

	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_CUSTOMER_ROLE;
	}

	@Override
	public double getMoney() {
		return myPerson.getMoney();
	}
}

