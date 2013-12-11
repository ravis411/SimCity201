package byronRestaurant;

import Person.Role.Role;
import agent.Agent;
import byronRestaurant.WaiterRole;
import byronRestaurant.gui.CustomerGui;
import byronRestaurant.interfaces.Customer;
import interfaces.generic_interfaces.GenericCashier;
import interfaces.generic_interfaces.GenericCustomer;
import interfaces.generic_interfaces.GenericHost;
import interfaces.generic_interfaces.GenericWaiter;

import java.util.*;

import trace.AlertLog;
import trace.AlertTag;

/**
 * Restaurant customer agent.
 */
public class CustomerRole extends GenericCustomer {
	private String name;
	private double money = (Math.random() * 50);
	private double cost = 0;
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private List<String> menu = new ArrayList<String>();
	// agent correspondents
	private WaiterRole waiter;
	private HostRole host;
	private CashierRole cashier;
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, EnteringRestaurant, WaitingInRestaurant, Seated, WaiterCalled, WaitingForFood, Eating, requestingCheck, GoingToCashier, Paying};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, atWaitingArea, followingWaiter, beingSeated, decidedChoice, takeOrder, foodDelivered, doneEating, gettingCheck, atCashier, paid};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerRole class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole(){
		super();
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setWaiter(GenericWaiter waiter) {
		this.waiter = (WaiterRole)waiter;
	}
	public void setCashier(GenericCashier c) {
		this.cashier = (CashierRole) c;	
	}

	public void setHost(GenericHost h) {
		this.host = (HostRole) h;
	}

	
	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(), "I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgFollowMeToTable(WaiterRole w, int n, List<String> m){
		setWaiter(w);
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Going to seat");
		event = AgentEvent.followingWaiter;
		customerGui.DoGoToSeat(n);//hack; only one table
		for (String m1 : m){
			menu.add(m1);
		}
		stateChanged();
	}
	public void msgAtWaitingArea(){
		event = AgentEvent.atWaitingArea;
		stateChanged();
	}
	
	public void msgAtTable(){
		event = AgentEvent.beingSeated;
		stateChanged();
	}

	public void msgAtCashier(){
		event = AgentEvent.atCashier;
		stateChanged();
	}
	
	public void msgDecided(){
		event = AgentEvent.decidedChoice;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(){
		event = AgentEvent.takeOrder;
		stateChanged();
	}
	
	public void msgHereIsYourFood(){
		event = AgentEvent.foodDelivered;
		stateChanged();
	}
	
	public void msgDoneEating(){
		event = AgentEvent.doneEating;
		menu.clear();
		stateChanged();
	}
	
	public void msgHereIsCheck(double w){
		event = AgentEvent.gettingCheck;
		cost = w;
		stateChanged();
	}
	
	public void msgHereIsChange(double w){
		event = AgentEvent.paid;
		money = w;
		stateChanged();
		
	}
	
	// Non-normative messages
	
	public void msgNoMoreChoice(List<String> m){
		menu.clear();
		for (String m1 : m){
			menu.add(m1);
		}
		orderNewFood();		
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAction() {
		//	CustomerRole is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.EnteringRestaurant;
			enterRestaurant();
			return true;
		}
		if (state==AgentState.EnteringRestaurant && event == AgentEvent.atWaitingArea){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.beingSeated){
			state = AgentState.Seated;
			makeChoice();
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.decidedChoice){
			state = AgentState.WaiterCalled;
			callWaiter();
			return true;
		}

		if (state == AgentState.WaiterCalled && event == AgentEvent.takeOrder){
			state = AgentState.WaitingForFood;
			orderFood();
			return true;
		}
		if (state == AgentState.WaitingForFood && event == AgentEvent.foodDelivered){
			state = AgentState.Eating;
			eatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.requestingCheck;
			doneEating();
			return true;
		}
		if (state == AgentState.requestingCheck && event == AgentEvent.gettingCheck){
			state = AgentState.GoingToCashier;
			goToCashier();
			return true;
		}
		if (state == AgentState.GoingToCashier && event == AgentEvent.atCashier){
			state = AgentState.Paying;
			payForFood();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.paid){
			state = AgentState.DoingNothing;
			leaveRestaurant();
			return true;
		}
		return false;
	}

	// Actions
	private void enterRestaurant(){
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_CUSTOMER_ROLE, myPerson.getName(),"Entering byronRestaurant");
		customerGui.DoGoToWaitingArea();		
	}
	
	private void goToRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_CUSTOMER_ROLE, myPerson.getName(),"Going to byronRestaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}
 
	private void makeChoice(){
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"Deciding menu choice");
		timer.schedule(new TimerTask() {
			public void run() {
				msgDecided();
			}}, 
			3000);
	}
	
	private void callWaiter(){
		AlertLog.getInstance().logMessage(AlertTag.BYRONS_RESTAURANT, myPerson.getName(),"I decided!");
		waiter.msgReadyToOrder(this);
	}
	
	private void orderFood(){
		String choice = menu.get((int)(Math.random()*(menu.size())));
		if (myPerson.getName().equals("Steak")){
			System.out.println("Exception: Mandatory Steak Order");
			choice = menu.get(0);
		}
		print ("Ordering the " + choice);
		waiter.msgHereIsMyChoice(this, choice);
	}
	
	private void orderNewFood(){
		String choice = menu.get((int)(Math.random()*(menu.size())));
		print ("Ordering the " + choice);
		waiter.msgHereIsMyChoice(this, choice);
	}
	
	private void eatFood() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_CUSTOMER_ROLE, myPerson.getName(),"Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				msgDoneEating();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void doneEating(){
		waiter.msgDoneEating(this);
	}
	
	private void goToCashier(){
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_CUSTOMER_ROLE, myPerson.getName(),"Going to cashier.");
		waiter.msgLeaving(this);
		customerGui.DoGoToLobby();
	}
	
	private void payForFood(){
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_CUSTOMER_ROLE, myPerson.getName(),"Paying for food.");
		cashier.msgPayForFood(cost, money, this);
		
	}
	
	private void leaveRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANT_CUSTOMER_ROLE, myPerson.getName(),"Leaving.");
		cost = 0;
		customerGui.DoExitRestaurant();
		deactivate();
	}

	// Accessors, etc.

	public String getName() {
		return myPerson.getName();
	}
	
	@Override
	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}




	@Override
	public boolean canGoGetFood() {
		return false;
	}
	
	@Override
	public String getNameOfRole() {
		return Role.RESTAURANT_BYRON_CUSTOMER_ROLE;
	}
}

